package com.cgp.banco.controller;

import com.cgp.banco.model.Conta;
import com.cgp.banco.model.Log;
import com.cgp.banco.model.Transferencia;
import com.cgp.banco.repository.ContaRepository;
import com.cgp.banco.repository.LogRepository;
import com.cgp.banco.repository.TransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/transferencias")
@RequiredArgsConstructor
public class TransferenciaController {

    private final TransferenciaRepository transferenciaRepository;

    private final ContaRepository contaRepository;

    private final LogRepository logRepository;

    @PostMapping("/porNumeroContas")
    public ResponseEntity<?> criarTransferenciaPorNumeroContas(@RequestParam String numeroContaOrigem, @RequestParam String numeroContaDestino, @RequestParam Double valor, @RequestParam Long userId) {
        try {
            // Cria uma nova transferência
            Transferencia transferencia = new Transferencia();
            Conta contaOrigem = contaRepository.findByNumeroConta(numeroContaOrigem);
            Conta contaDesitino = contaRepository.findByNumeroConta(numeroContaDestino);

            if (contaOrigem == null) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: Conta de origem não encontrada.");
                logRepository.save(log);

                return ResponseEntity.badRequest().body("Conta de origem não encontrada.");
            }
            if (contaDesitino == null) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: Conta de destino não encontrada.");
                logRepository.save(log);

                return ResponseEntity.badRequest().body("Conta de destino não encontrada.");
            }

            // Verifica se a conta de origem e a conta de destino são diferentes
            if (contaOrigem.getId().equals(contaDesitino.getId())) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: A conta de origem e a conta de destino não podem ser iguais.");
                logRepository.save(log);

                return ResponseEntity.badRequest().body("A conta de origem e a conta de destino não podem ser iguais.");
            }
            // Verifica se o valor da transferência é maior que 0
            if (valor <= 0) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: O valor da transferência deve ser maior que 0.");
                logRepository.save(log);

                return ResponseEntity.badRequest().body("O valor da transferência deve ser maior que 0.");
            }
            // Verifica se a conta de origem tem saldo suficiente
            if (contaOrigem.getSaldo() < valor) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: Saldo insuficiente na conta de origem.");
                logRepository.save(log);
                return ResponseEntity.badRequest().body("Saldo insuficiente na conta de origem.");
            }
            // Define o id da conta de origem
            transferencia.setIdContaOrigem(contaOrigem.getId());
            // Define o id da conta de destino
            transferencia.setIdContaDestino(contaDesitino.getId());
            // Define o valor da transferência
            transferencia.setValor(valor);
            // Define a data da transferência
            transferencia.setDataTransferencia(LocalDateTime.now());

            // Atualiza o saldo da conta de origem
            contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
            // Atualiza o saldo da conta de destino
            contaDesitino.setSaldo(contaDesitino.getSaldo() + valor);
            // Salva as contas atualizadas no banco de dados
            contaRepository.save(contaOrigem);
            contaRepository.save(contaDesitino);
            // Salva a transferência no banco de dados
            transferenciaRepository.save(transferencia);
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("CREATE");
            log.setTabela("transferencia");
            log.setIdTabela(transferencia.getId());
            log.setDescricao("SUCESSO: Transferência criada com sucesso.");
            log.setDadosAntigos(null);
            log.setDadosNovos(transferencia.toString());
            logRepository.save(log);

            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Transferência realizada com sucesso.");
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("CREATE");
            log.setTabela("transferencia");
            log.setIdTabela(null);
            log.setDescricao("ERRO: Erro ao criar transferência: " + e.getMessage());
            log.setDadosAntigos(null);
            log.setDadosNovos(null);
            logRepository.save(log);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar transferencia: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> criarTransferencia(@RequestBody Transferencia transferencia, @RequestParam Long userId) {
        try {
            // Salva a transferência no banco de dados
            transferencia.setDataTransferencia(LocalDateTime.now());

            Conta contaOrigem = contaRepository.findById(transferencia.getIdContaOrigem()).orElse(null);
            if (contaOrigem == null) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: Conta de origem não encontrada.");
                logRepository.save(log);

                return ResponseEntity.badRequest().body("Conta de origem não encontrada.");
            }
            Conta contaDestino = contaRepository.findById(transferencia.getIdContaDestino()).orElse(null);
            if (contaDestino == null) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: Conta de destino não encontrada.");
                log.setDadosNovos(transferencia.toString());
                log.setIdTabela(transferencia.getId());
                logRepository.save(log);

                return ResponseEntity.badRequest().body("Conta de destino não encontrada.");
            }
            // Verifica se a conta de origem e a conta de destino são diferentes
            if (contaOrigem.getId().equals(contaDestino.getId())) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: A conta de origem e a conta de destino não podem ser iguais.");
                log.setDadosNovos(transferencia.toString());
                log.setIdTabela(transferencia.getId());
                logRepository.save(log);

                return ResponseEntity.badRequest().body("A conta de origem e a conta de destino não podem ser iguais.");
            }
            // Verifica se o valor da transferência é maior que 0
            if (transferencia.getValor() <= 0) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: O valor da transferência deve ser maior que 0.");
                log.setDadosNovos(transferencia.toString());
                log.setIdTabela(transferencia.getId());
                logRepository.save(log);

                return ResponseEntity.badRequest().body("O valor da transferência deve ser maior que 0.");
            }
            // Verifica se a conta de origem tem saldo suficiente
            if (contaOrigem.getSaldo() < transferencia.getValor()) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: Saldo insuficiente na conta de origem.");
                log.setDadosNovos(transferencia.toString());
                log.setIdTabela(transferencia.getId());
                logRepository.save(log);

                return ResponseEntity.badRequest().body("Saldo insuficiente na conta de origem.");
            }

            transferenciaRepository.save(transferencia);
            //Atualizar o saldo da conta de origem
            contaOrigem.setSaldo(contaOrigem.getSaldo() - transferencia.getValor());
            contaRepository.save(contaOrigem);
            //Atualizar o saldo da conta de destino
            contaDestino.setSaldo(contaDestino.getSaldo() + transferencia.getValor());
            contaRepository.save(contaDestino);

            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("CREATE");
            log.setTabela("transferencia");
            log.setIdTabela(transferencia.getId());
            log.setDescricao("SUCESSO: Transferência criada com sucesso.");
            log.setDadosNovos(transferencia.toString());
            logRepository.save(log);

            return ResponseEntity.ok(transferencia);
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("CREATE");
            log.setTabela("transferencia");
            log.setDescricao("ERRO: Erro ao criar transferência: " + e.getMessage());
            logRepository.save(log);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar transferencia: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarTransferencia(@PathVariable Long id, @RequestBody Transferencia transferencia, @RequestParam Long userId) {
        try {
            // Verifica se a transferência existe
            if (transferencia == null) {
                // Cria um log de operação
                Log log = new Log();
                log.setTipoOperacao("UPDATE");
                log.setTabela("transferencia");
                log.setIdTabela(id);
                log.setDescricao("ERRO: Transferência não pode ser nula.");
                logRepository.save(log);

                return ResponseEntity.badRequest().body("Transferência não pode ser nula.");
            }
            // Busca a transferência existente no banco de dados

            Transferencia transferenciaExistente = transferenciaRepository.findById(id).orElse(null);
            // Verifica se a transferência existe
            if (transferenciaExistente == null) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("UPDATE");
                log.setTabela("transferencia");
                log.setIdTabela(id);
                log.setDescricao("ERRO: Transferência não encontrada.");
                logRepository.save(log);

                return ResponseEntity.notFound().build();
            }

            transferencia.setId(id);
            // Atualiza a transferência no banco de dados
            transferenciaRepository.save(transferencia);
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("UPDATE");
            log.setTabela("transferencia");
            log.setIdTabela(transferencia.getId());
            log.setDescricao("SUCESSO: Transferência atualizada com sucesso.");
            log.setDadosAntigos(transferenciaExistente.toString());
            log.setDadosNovos(transferencia.toString());
            logRepository.save(log);

            // Retorna uma resposta de sucesso
            return ResponseEntity.ok(transferencia);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar transferencia: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTransferenciaPorId(@PathVariable Long id, @RequestParam Long userId) {
        try {
            Transferencia transferencia = transferenciaRepository.findById(id).orElse(null);
            // Verifica se a transferência existe
            if (transferencia == null) {
                // Cria um log de operação
                Log log = new Log();
                log.setTipoOperacao("BUSCAR");
                log.setUserId(userId);
                log.setTabela("transferencia");
                log.setIdTabela(id);
                log.setDescricao("ERRO: Transferência não encontrada.");
                logRepository.save(log);

                return ResponseEntity.notFound().build();
            }
            // Cria um log de operação
            Log log = new Log();
            log.setTipoOperacao("BUSCAR");
            log.setUserId(userId);
            log.setTabela("transferencia");
            log.setIdTabela(id);
            log.setDescricao("SUCESSO: Transferência encontrada com sucesso.");
            logRepository.save(log);

            // Retorna a transferência encontrada
            return ResponseEntity.ok(transferencia);
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("BUSCAR");
            log.setTabela("transferencia");
            log.setIdTabela(id);
            log.setDescricao("ERRO: Erro ao buscar transferência: " + e.getMessage());
            logRepository.save(log);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencia: " + e.getMessage());
        }
    }

    @GetMapping("/buscarTransferenciasPorNumeroContaOrigem")
    public ResponseEntity<?> buscarTransferenciasPorNumeroContaOrigem(@RequestParam String numeroContaOrigem, @RequestParam Long userId) {
        try {
            Conta conta = contaRepository.findByNumeroConta(numeroContaOrigem);
            // Verifica se a conta existe
            if (conta == null) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("BUSCAR");
                log.setTabela("transferencia");
                log.setIdTabela(null);
                log.setDescricao("ERRO: Conta não encontrada.");
                logRepository.save(log);

                return ResponseEntity.notFound().build();
            }
            List<Transferencia> transferencias = transferenciaRepository.findByIdContaOrigem(conta.getId());
            // Cria um log de operação
            Log log = new Log();
            log.setTipoOperacao("BUSCAR");
            log.setUserId(userId);
            log.setTabela("transferencia");
            log.setIdTabela(conta.getId());
            log.setDescricao("SUCESSO: Transferências encontradas com sucesso.");
            logRepository.save(log);

            // Retorna a lista de transferências encontradas
            return ResponseEntity.ok(transferencias);
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("BUSCAR");
            log.setTabela("transferencia");
            log.setDescricao("ERRO: Erro ao buscar transferências: " + e.getMessage());
            logRepository.save(log);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencias: " + e.getMessage());
        }
    }

    @GetMapping("/buscarTransferenciasPorNumeroContaDestino")
    public ResponseEntity<?> buscarTransferenciasPorNumeroContaDestino(@RequestParam String numeroContaDestino, @RequestParam Long userId) {
        try {
            Conta conta = contaRepository.findByNumeroConta(numeroContaDestino);

            List<Transferencia> transferencias = transferenciaRepository.findByIdContaDestino(conta.getId());
            // Cria um log de operação
            // Cria um log de operação
            Log log = new Log();
            log.setTipoOperacao("BUSCAR");
            log.setTabela("transferencia");
            log.setIdTabela(conta.getId());
            log.setDescricao("SUCESSO: Transferências encontradas com sucesso.");
            logRepository.save(log);

            // Retorna a lista de transferências encontradas
            return ResponseEntity.ok(transferencias);

        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("BUSCAR");
            log.setTabela("transferencia");
            log.setDescricao("ERRO: Erro ao buscar transferências por conta de destino: " + e.getMessage());
            logRepository.save(log);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencias por conta de destino: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarTransferencia(@PathVariable Long id) {
        try {
            Transferencia transferencia = transferenciaRepository.findById(id).orElse(null);
            // Verifica se a transferência existe
            if (transferencia == null) {
                // Cria um log de operação
                Log log = new Log();
                log.setTipoOperacao("DELETAR");
                log.setTabela("transferencia");
                log.setIdTabela(id);
                log.setDescricao("ERRO: Transferência não encontrada.");
                logRepository.save(log);

                return ResponseEntity.notFound().build();
            }
            transferenciaRepository.deleteById(id);
            // Cria um log de operação
            Log log = new Log();
            log.setTipoOperacao("DELETAR");
            log.setTabela("transferencia");
            log.setIdTabela(id);
            log.setDescricao("SUCESSO: Transferência deletada com sucesso.");
            log.setDadosAntigos(transferencia.toString());
            logRepository.save(log);

            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Transferência deletada com sucesso.");
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setTipoOperacao("DELETAR");
            log.setTabela("transferencia");
            log.setIdTabela(id);
            log.setDescricao("ERRO: Erro ao deletar transferência: " + e.getMessage());
            logRepository.save(log);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar transferencia: " + e.getMessage());
        }
    }

    @PostMapping("/buscarTransferenciasPorNumerosContas")
    public ResponseEntity<?> buscarTransferenciasPorNumeroConta(@RequestBody List<String> numerosContas, @RequestParam Long userId) {
        try {
            List<Transferencia> transferencias = new ArrayList<>();
            for (String numeroConta : numerosContas) {
                List<Transferencia> byNumeroContaDestinoOrNumeroContaOrigem = transferenciaRepository.findByContaDestinoNumeroContaOrContaOrigemNumeroConta(numeroConta, numeroConta);
                transferencias.addAll(byNumeroContaDestinoOrNumeroContaOrigem);
            }
            // Cria um log de operação
            Log log = new Log();
            log.setTipoOperacao("BUSCAR");
            log.setUserId(userId);
            log.setTabela("transferencia");
            log.setDescricao("SUCESSO: Transferências encontradas com sucesso.");
            logRepository.save(log);

            // Retorna a lista de transferências encontradas
            return ResponseEntity.ok(transferencias);
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("BUSCAR");
            log.setTabela("transferencia");
            log.setDescricao("ERRO: Erro ao buscar transferências por número de conta: " + e.getMessage());
            logRepository.save(log);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transferencias por numero de conta: " + e.getMessage());
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
