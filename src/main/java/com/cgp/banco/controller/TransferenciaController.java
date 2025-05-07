package com.cgp.banco.controller;

import com.cgp.banco.repository.ContaRepository;
import com.cgp.banco.repository.TransferenciaRepository;
import com.cgp.banco.model.Conta;
import com.cgp.banco.model.Transferencia;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/transferencias")
public class TransferenciaController {

    private final TransferenciaRepository transferenciaRepository;

    private final ContaRepository contaRepository;

    public TransferenciaController(TransferenciaRepository transferenciaRepository, ContaRepository contaRepository) {
        this.transferenciaRepository = transferenciaRepository;
        this.contaRepository = contaRepository;
    }

    @PostMapping("/porNumeroContas")
    public ResponseEntity<?> criarTransferenciaPorNumeroContas(@RequestParam Integer numeroContaOrigem, @RequestParam Integer numeroContaDestino, @RequestParam Double valor) {
        try {
            // Cria uma nova transferência
            Transferencia transferencia = new Transferencia();
            // Define o id da conta de origem
            Conta contaOrigem = contaRepository.findByNumeroConta(numeroContaOrigem);
            transferencia.setIdContaOrigem(contaOrigem.getId());
            // Define o id da conta de destino
            Conta contaDesitino = contaRepository.findByNumeroConta(numeroContaDestino);
            transferencia.setIdContaDestino(contaDesitino.getId());
            // Define o valor da transferência
            transferencia.setValor(valor);

            transferencia.setData(LocalDateTime.now());
            
            transferenciaRepository.save(transferencia);

           
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Transferência realizada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar transferencia: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> criarTransferencia(@RequestBody Transferencia transferencia) {
        try {
            // Salva a transferência no banco de dados
            transferencia.setData(LocalDateTime.now());
            transferenciaRepository.save(transferencia);
         
            return ResponseEntity.ok("Transferência criada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar transferencia: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarTransferencia(@PathVariable Long id, @RequestBody Transferencia transferencia) {
        try {
            // Verifica se a transferência existe
            if (transferencia == null) {
                return ResponseEntity.badRequest().body("Transferência não pode ser nula.");
            }
            // Busca a transferência existente no banco de dados
        
        Transferencia transferenciaExistente = transferenciaRepository.findById(id).orElse(null);
            // Verifica se a transferência existe
            if (transferenciaExistente == null) {
                return ResponseEntity.notFound().build();
            }
            
            transferencia.setId(id);
            // Atualiza a transferência no banco de dados
            transferenciaRepository.save(transferencia);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Transferência atualizada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar transferencia: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTransferenciaPorId(@PathVariable Long id) {
        try {
            Transferencia transferencia = transferenciaRepository.findById(id).orElse(null);
            // Verifica se a transferência existe
            if (transferencia == null) {
               
                return ResponseEntity.notFound().build();
            }
            // Retorna a transferência encontrada
            return ResponseEntity.ok(transferencia);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencia: " + e.getMessage());
        }
    }

    @GetMapping("/buscarTransferenciasPorNumeroContaOrigem")
    public ResponseEntity<?> buscarTransferenciasPorNumeroContaOrigem(@RequestParam Integer numeroContaOrigem) {
        try {
            Conta conta = contaRepository.findByNumeroConta(numeroContaOrigem);
            List<Transferencia> transferencias = transferenciaRepository.findByIdContaOrigem(conta.getId());

            // Retorna a lista de transferências encontradas
            return ResponseEntity.ok(transferencias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencias: " + e.getMessage());
        }
    }

    @GetMapping("/buscarTransferenciasPorNumeroContaDestino")
    public ResponseEntity<?> buscarTransferenciasPorNumeroContaDestino(@RequestParam Integer numeroContaDestino) {
        try {
            Conta conta = contaRepository.findByNumeroConta(numeroContaDestino);

            List<Transferencia> transferencias = transferenciaRepository.findByIdContaDestino(conta.getId());
            // Retorna a lista de transferências encontradas
            return ResponseEntity.ok(transferencias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencias: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarTransferencia(@PathVariable Long id) {
        try {
            Transferencia transferencia = transferenciaRepository.findById(id).orElse(null);
            // Verifica se a transferência existe
            if (transferencia == null) {
                return ResponseEntity.notFound().build();
            }
            
            transferenciaRepository.deleteById(id);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Transferência deletada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar transferencia: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> buscarTodasTransferencias() {
        try {
            List<Transferencia> transferencias = transferenciaRepository.findAll();
            // Retorna a lista de transferências
            return ResponseEntity.ok(transferencias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencias: " + e.getMessage());
        }
    }

}
