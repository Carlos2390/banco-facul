package com.cgp.banco.controller;

import com.cgp.banco.dao.TransferenciaDAO;
import com.cgp.banco.model.Transferencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/transferencias")
public class TransferenciaController {

    @Autowired
    private TransferenciaDAO transferenciaDAO;

    @PostMapping
    public ResponseEntity<String> criarTransferencia(@RequestBody Transferencia transferencia) {
        // Salva a transferência no banco de dados
        transferenciaDAO.salvar(transferencia);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Transferência criada com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarTransferencia(@PathVariable Long id, @RequestBody Transferencia transferencia) {
        // Busca a transferência existente pelo ID
        Transferencia transferenciaExistente = transferenciaDAO.buscarPorId(id);
        // Verifica se a transferência existe
        if (transferenciaExistente == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Define o ID da transferência
        transferencia.setId(id);
        // Atualiza a transferência no banco de dados
        transferenciaDAO.atualizar(transferencia);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Transferência atualizada com sucesso.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transferencia> buscarTransferenciaPorId(@PathVariable Long id) {
        // Busca a transferência pelo ID
        Transferencia transferencia = transferenciaDAO.buscarPorId(id);
        // Verifica se a transferência existe
        if (transferencia == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Retorna a transferência encontrada
        return ResponseEntity.ok(transferencia);
    }

    @GetMapping("/buscarTransferenciasPorNumeroContaOrigem")
    public ResponseEntity<List<Transferencia>> buscarTransferenciasPorNumeroContaOrigem(@RequestParam Integer numeroContaOrigem) {
        // Busca as transferências pelo número da conta de origem
        List<Transferencia> transferencias = transferenciaDAO.buscarTransferenciasPorNumeroContaOrigem(numeroContaOrigem);
        // Retorna a lista de transferências encontradas
        return ResponseEntity.ok(transferencias);
    }

    @GetMapping("/buscarTransferenciasPorNumeroContaDestino")
    public ResponseEntity<List<Transferencia>> buscarTransferenciasPorNumeroContaDestino(@RequestParam Integer numeroContaDestino) {
        // Busca as transferências pelo número da conta de destino
        List<Transferencia> transferencias = transferenciaDAO.buscarTransferenciasPorNumeroContaDestino(numeroContaDestino);
        // Retorna a lista de transferências encontradas
        return ResponseEntity.ok(transferencias);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarTransferencia(@PathVariable Long id) {
        // Busca a transferência pelo ID
        Transferencia transferencia = transferenciaDAO.buscarPorId(id);
        // Verifica se a transferência existe
        if (transferencia == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Deleta a transferência do banco de dados
        transferenciaDAO.deletar(transferencia);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Transferência deletada com sucesso.");
    }

    @GetMapping
    public ResponseEntity<List<Transferencia>> buscarTodasTransferencias() {
        // Busca todas as transferências
        List<Transferencia> transferencias = transferenciaDAO.buscarTodas();
        // Retorna a lista de transferências
        return ResponseEntity.ok(transferencias);
    }
}