package com.cgp.banco.controller;

import com.cgp.banco.dao.ContaDAO;
import com.cgp.banco.dao.TransferenciaDAO;
import com.cgp.banco.model.Conta;
import com.cgp.banco.model.Transferencia;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/transferencias")
public class TransferenciaController {

    @Autowired
    private TransferenciaDAO transferenciaDAO;

    @Autowired
    private ContaDAO contaDAO;

    @PostMapping("/porNumeroContas")
    public ResponseEntity<?> criarTransferenciaPorNumeroContas(@RequestParam Integer numeroContaOrigem, @RequestParam Integer numeroContaDestino, @RequestParam Double valor, HttpSession session) {
        try {
            transferenciaDAO.setUserId((Integer) session.getAttribute("currentUserId"));
            // Cria uma nova transferência
            Transferencia transferencia = new Transferencia();
            // Define o id da conta de origem
            Conta contaOrigem = contaDAO.buscarContaPorNumero(numeroContaOrigem);
            transferencia.setId_conta_origem(contaOrigem.getId());
            // Define o id da conta de destino
            Conta contaDesitino = contaDAO.buscarContaPorNumero(numeroContaDestino);
            transferencia.setId_conta_destino(contaDesitino.getId());
            // Define o valor da transferência
            transferencia.setValor(valor);

            transferencia.setData(LocalDateTime.now());
            // Salva a transferência no banco de dados
            transferenciaDAO.salvar(transferencia);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Transferência criada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar transferencia: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> criarTransferencia(@RequestBody Transferencia transferencia, HttpSession session) {
        try {
            transferenciaDAO.setUserId((Integer) session.getAttribute("currentUserId"));
            // Salva a transferência no banco de dados
            transferencia.setData(LocalDateTime.now());
            transferenciaDAO.salvar(transferencia);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Transferência criada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar transferencia: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarTransferencia(@PathVariable Long id, @RequestBody Transferencia transferencia, HttpSession session) {
        try {
            transferenciaDAO.setUserId((Integer) session.getAttribute("currentUserId"));
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar transferencia: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTransferenciaPorId(@PathVariable Long id, HttpSession session) {
        try {
            transferenciaDAO.setUserId((Integer) session.getAttribute("currentUserId"));
            // Busca a transferência pelo ID
            Transferencia transferencia = transferenciaDAO.buscarPorId(id);
            // Verifica se a transferência existe
            if (transferencia == null) {
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            // Retorna a transferência encontrada
            return ResponseEntity.ok(transferencia);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencia: " + e.getMessage());
        }
    }

    @GetMapping("/buscarTransferenciasPorNumeroContaOrigem")
    public ResponseEntity<?> buscarTransferenciasPorNumeroContaOrigem(@RequestParam Integer numeroContaOrigem, HttpSession session) {
        try {
            transferenciaDAO.setUserId((Integer) session.getAttribute("currentUserId"));
            // Busca as transferências pelo número da conta de origem
            List<Transferencia> transferencias = transferenciaDAO.buscarTransferenciasPorNumeroContaOrigem(numeroContaOrigem);
            // Retorna a lista de transferências encontradas
            return ResponseEntity.ok(transferencias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencias: " + e.getMessage());
        }
    }

    @GetMapping("/buscarTransferenciasPorNumeroContaDestino")
    public ResponseEntity<?> buscarTransferenciasPorNumeroContaDestino(@RequestParam Integer numeroContaDestino, HttpSession session) {
        try {
            transferenciaDAO.setUserId((Integer) session.getAttribute("currentUserId"));
            // Busca as transferências pelo número da conta de destino
            List<Transferencia> transferencias = transferenciaDAO.buscarTransferenciasPorNumeroContaDestino(numeroContaDestino);
            // Retorna a lista de transferências encontradas
            return ResponseEntity.ok(transferencias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencias: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarTransferencia(@PathVariable Long id, HttpSession session) {
        try {
            transferenciaDAO.setUserId((Integer) session.getAttribute("currentUserId"));
            // Busca a transferência pelo ID
            Transferencia transferencia = transferenciaDAO.buscarPorId(id);
            // Verifica se a transferência existe
            if (transferencia == null) {
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            // Deleta a transferência do banco de dados
            transferenciaDAO.deletar(id);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Transferência deletada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar transferencia: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> buscarTodasTransferencias(HttpSession session) {
        try {
            transferenciaDAO.setUserId((Integer) session.getAttribute("currentUserId"));
            // Busca todas as transferências
            List<Transferencia> transferencias = transferenciaDAO.buscarTodos();
            // Retorna a lista de transferências
            return ResponseEntity.ok(transferencias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencias: " + e.getMessage());
        }
    }

}
