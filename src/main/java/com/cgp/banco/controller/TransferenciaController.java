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
        transferenciaDAO.salvar(transferencia);
        return ResponseEntity.ok("Transferência criada com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarTransferencia(@PathVariable Long id, @RequestBody Transferencia transferencia) {
        Transferencia transferenciaExistente = transferenciaDAO.buscarPorId(id);
        if (transferenciaExistente == null) {
            return ResponseEntity.notFound().build();
        }
        transferencia.setId(id);
        transferenciaDAO.atualizar(transferencia);
        return ResponseEntity.ok("Transferência atualizada com sucesso.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transferencia> buscarTransferenciaPorId(@PathVariable Long id) {
        Transferencia transferencia = transferenciaDAO.buscarPorId(id);
        if (transferencia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transferencia);
    }

    @GetMapping("/buscarTransferenciasPorNumeroContaOrigem")
    public ResponseEntity<List<Transferencia>> buscarTransferenciasPorNumeroContaOrigem(@RequestParam Integer numeroContaOrigem) {
        List<Transferencia> transferencias = transferenciaDAO.buscarTransferenciasPorNumeroContaOrigem(numeroContaOrigem);
        return ResponseEntity.ok(transferencias);
    }

    @GetMapping("/buscarTransferenciasPorNumeroContaDestino")
    public ResponseEntity<List<Transferencia>> buscarTransferenciasPorNumeroContaDestino(@RequestParam Integer numeroContaDestino) {
        List<Transferencia> transferencias = transferenciaDAO.buscarTransferenciasPorNumeroContaDestino(numeroContaDestino);
        return ResponseEntity.ok(transferencias);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarTransferencia(@PathVariable Long id) {
        Transferencia transferencia = transferenciaDAO.buscarPorId(id);
        if (transferencia == null) {
            return ResponseEntity.notFound().build();
        }
        transferenciaDAO.deletar(transferencia);
        return ResponseEntity.ok("Transferência deletada com sucesso.");
    }

    @GetMapping
    public ResponseEntity<List<Transferencia>> buscarTodasTransferencias() {
        List<Transferencia> transferencias = transferenciaDAO.buscarTodas();
        return ResponseEntity.ok(transferencias);
    }
}
