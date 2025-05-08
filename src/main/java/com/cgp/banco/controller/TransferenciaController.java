package com.cgp.banco.controller;

import com.cgp.banco.repository.ContaRepository;
import com.cgp.banco.repository.TransferenciaRepository;
import com.cgp.banco.model.Conta;
import com.cgp.banco.model.Transferencia;
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
    public ResponseEntity<?> criarTransferenciaPorNumeroContas(@RequestParam String numeroContaOrigem, @RequestParam String numeroContaDestino, @RequestParam Double valor) {
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
            // Define a data da transferência
            transferencia.setDataTransferencia(LocalDateTime.now());

            // Verifica se a conta de origem e a conta de destino são diferentes
            if (contaOrigem.getId().equals(contaDesitino.getId())) {
                return ResponseEntity.badRequest().body("A conta de origem e a conta de destino não podem ser iguais.");
            }
            // Verifica se o valor da transferência é maior que 0
            if (valor <= 0) {
                return ResponseEntity.badRequest().body("O valor da transferência deve ser maior que 0.");
            }
            // Verifica se a conta de origem tem saldo suficiente
            if (contaOrigem.getSaldo() < valor) {
                return ResponseEntity.badRequest().body("Saldo insuficiente na conta de origem.");
            }
            // Atualiza o saldo da conta de origem
            contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
            // Atualiza o saldo da conta de destino
            contaDesitino.setSaldo(contaDesitino.getSaldo() + valor);
            // Salva as contas atualizadas no banco de dados
            contaRepository.save(contaOrigem);
            contaRepository.save(contaDesitino);
            // Salva a transferência no banco de dados
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
            transferencia.setDataTransferencia(LocalDateTime.now());

            Conta contaOrigem = contaRepository.findById(transferencia.getIdContaOrigem()).orElse(null);
            if (contaOrigem == null) {
                return ResponseEntity.badRequest().body("Conta de origem não encontrada.");
            }
            Conta contaDestino = contaRepository.findById(transferencia.getIdContaDestino()).orElse(null);
            if (contaDestino == null) {
                return ResponseEntity.badRequest().body("Conta de destino não encontrada.");
            }
            // Verifica se a conta de origem e a conta de destino são diferentes
            if (contaOrigem.getId().equals(contaDestino.getId())) {
                return ResponseEntity.badRequest().body("A conta de origem e a conta de destino não podem ser iguais.");
            }
            // Verifica se o valor da transferência é maior que 0
            if (transferencia.getValor() <= 0) {
                return ResponseEntity.badRequest().body("O valor da transferência deve ser maior que 0.");
            }
            // Verifica se a conta de origem tem saldo suficiente
            if (contaOrigem.getSaldo() < transferencia.getValor()) {
                return ResponseEntity.badRequest().body("Saldo insuficiente na conta de origem.");
            }

            transferenciaRepository.save(transferencia);
            //Atualizar o saldo da conta de origem
            contaOrigem.setSaldo(contaOrigem.getSaldo() - transferencia.getValor());
            contaRepository.save(contaOrigem);
            //Atualizar o saldo da conta de destino
            contaDestino.setSaldo(contaDestino.getSaldo() + transferencia.getValor());
            contaRepository.save(contaDestino);
         
            return ResponseEntity.ok(transferencia);
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
            return ResponseEntity.ok(transferencia);
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
    public ResponseEntity<?> buscarTransferenciasPorNumeroContaOrigem(@RequestParam String numeroContaOrigem) {
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
    public ResponseEntity<?> buscarTransferenciasPorNumeroContaDestino(@RequestParam String numeroContaDestino) {
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
