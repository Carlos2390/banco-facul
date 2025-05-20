package com.cgp.banco.controller;

import com.cgp.banco.model.Conta;
import com.cgp.banco.model.Log;
import com.cgp.banco.model.Response;
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
    public Response criarTransferenciaPorNumeroContas(@RequestParam String numeroContaOrigem, @RequestParam String numeroContaDestino, @RequestParam Double valor, @RequestParam Long userId) {
        try {
            // Cria uma nova transferência
            Transferencia transferencia = new Transferencia();
            Conta contaOrigem = contaRepository.findByNumeroConta(numeroContaOrigem);
            Conta contaDestino = contaRepository.findByNumeroConta(numeroContaDestino);

            if (contaOrigem == null) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: Conta de origem não encontrada.");
                log.setDadosNovos(String.format(
                        "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                        transferencia.getValor(),
                        numeroContaOrigem,
                        numeroContaDestino,
                        0.00
                ));
                logRepository.save(log);
                return new Response("Conta de origem não encontrada.", HttpStatus.BAD_REQUEST.value(), null);
            }
            if (contaDestino == null) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: Conta de destino não encontrada.");
                log.setDadosNovos(String.format(
                        "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                        transferencia.getValor(),
                        numeroContaOrigem,
                        numeroContaDestino,
                        transferencia.getContaOrigem().getSaldo()
                ));
                logRepository.save(log);
                return new Response("Conta de destino não encontrada.", HttpStatus.BAD_REQUEST.value(), null);
            }

            // Verifica se a conta de origem e a conta de destino são diferentes
            if (contaOrigem.getId().equals(contaDestino.getId())) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: A conta de origem e a conta de destino não podem ser iguais.");
                log.setDadosNovos(String.format(
                        "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                        transferencia.getValor(),
                        numeroContaOrigem,
                        numeroContaDestino,
                        transferencia.getContaOrigem().getSaldo()
                ));
                logRepository.save(log);
                return new Response("A conta de origem e a conta de destino não podem ser iguais.", HttpStatus.BAD_REQUEST.value(), null);
            }
            // Verifica se o valor da transferência é maior que 0
            if (valor <= 0) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: O valor da transferência deve ser maior que 0.");
                log.setDadosNovos(String.format(
                        "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                        transferencia.getValor(),
                        numeroContaOrigem,
                        numeroContaDestino,
                        transferencia.getContaOrigem().getSaldo()
                ));
                logRepository.save(log);
                return new Response("O valor da transferência deve ser maior que 0.", HttpStatus.BAD_REQUEST.value(), null);
            }
            // Verifica se a conta de origem tem saldo suficiente
            if (contaOrigem.getSaldo() < valor) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: Saldo insuficiente na conta de origem.");
                log.setDadosNovos(String.format(
                        "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                        transferencia.getValor(),
                        numeroContaOrigem,
                        numeroContaDestino,
                        transferencia.getContaOrigem().getSaldo()
                ));
                logRepository.save(log);
                return new Response("Saldo insuficiente na conta de origem.", HttpStatus.BAD_REQUEST.value(), null);
            }
            // Define o id da conta de origem
            transferencia.setIdContaOrigem(contaOrigem.getId());
            // Define o id da conta de destino
            transferencia.setIdContaDestino(contaDestino.getId());
            // Define o valor da transferência
            transferencia.setValor(valor);
            // Define a data da transferência
            transferencia.setDataTransferencia(LocalDateTime.now());

            // Atualiza o saldo da conta de origem
            contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
            // Atualiza o saldo da conta de destino
            contaDestino.setSaldo(contaDestino.getSaldo() + valor);
            // Salva as contas atualizadas no banco de dados
            contaRepository.save(contaOrigem);
            contaRepository.save(contaDestino);
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
            log.setDadosNovos(String.format(
                    "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                    transferencia.getValor(),
                    numeroContaOrigem,
                    numeroContaDestino,
                    transferencia.getContaOrigem().getSaldo()
            ));
            logRepository.save(log);

            // Retorna uma resposta de sucesso
            return new Response("Transferência realizada com sucesso.", HttpStatus.OK.value(), transferencia);
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
            return new Response("Erro ao criar transferência: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @PostMapping
    public Response criarTransferencia(@RequestBody Transferencia transferencia, @RequestParam Long userId) {
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
                log.setDadosNovos(String.format(
                        "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                        transferencia.getValor(),
                        transferencia.getContaOrigem().getNumeroConta(),
                        transferencia.getContaDestino().getNumeroConta(),
                        transferencia.getContaOrigem().getSaldo()
                ));
                logRepository.save(log);
                return new Response("Conta de origem não encontrada.", HttpStatus.BAD_REQUEST.value(), null);
            }
            Conta contaDestino = contaRepository.findById(transferencia.getIdContaDestino()).orElse(null);
            if (contaDestino == null) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: Conta de destino não encontrada.");
                log.setDadosNovos(String.format(
                        "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                        transferencia.getValor(),
                        transferencia.getContaOrigem().getNumeroConta(),
                        transferencia.getContaDestino().getNumeroConta(),
                        transferencia.getContaOrigem().getSaldo()
                ));
                log.setIdTabela(transferencia.getId());
                logRepository.save(log);
                return new Response("Conta de destino não encontrada.", HttpStatus.BAD_REQUEST.value(), null);
            }
            // Verifica se a conta de origem e a conta de destino são diferentes
            if (contaOrigem.getId().equals(contaDestino.getId())) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: A conta de origem e a conta de destino não podem ser iguais.");
                log.setIdTabela(transferencia.getId());
                log.setDadosNovos(String.format(
                        "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                        transferencia.getValor(),
                        transferencia.getContaOrigem().getNumeroConta(),
                        transferencia.getContaDestino().getNumeroConta(),
                        transferencia.getContaOrigem().getSaldo()
                ));
                logRepository.save(log);
                return new Response("A conta de origem e a conta de destino não podem ser iguais.", HttpStatus.BAD_REQUEST.value(), null);
            }
            // Verifica se o valor da transferência é maior que 0
            if (transferencia.getValor() <= 0) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: O valor da transferência deve ser maior que 0.");
                log.setIdTabela(transferencia.getId());
                log.setDadosNovos(String.format(
                        "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                        transferencia.getValor(),
                        transferencia.getContaOrigem().getNumeroConta(),
                        transferencia.getContaDestino().getNumeroConta(),
                        transferencia.getContaOrigem().getSaldo()
                ));
                logRepository.save(log);
                return new Response("O valor da transferência deve ser maior que 0.", HttpStatus.BAD_REQUEST.value(), null);
            }
            // Verifica se a conta de origem tem saldo suficiente
            if (contaOrigem.getSaldo() < transferencia.getValor()) {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(userId);
                log.setTipoOperacao("CREATE");
                log.setTabela("transferencia");
                log.setDescricao("ERRO: Saldo insuficiente na conta de origem.");
                log.setIdTabela(transferencia.getId());
                log.setDadosNovos(String.format(
                        "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                        transferencia.getValor(),
                        transferencia.getContaOrigem().getNumeroConta(),
                        transferencia.getContaDestino().getNumeroConta(),
                        transferencia.getContaOrigem().getSaldo()
                ));
                logRepository.save(log);
                return new Response("Saldo insuficiente na conta de origem.", HttpStatus.BAD_REQUEST.value(), null);
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
            log.setDadosNovos(String.format(
                    "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                    transferencia.getValor(),
                    transferencia.getContaOrigem().getNumeroConta(),
                    transferencia.getContaDestino().getNumeroConta(),
                    transferencia.getContaOrigem().getSaldo()
            ));
            logRepository.save(log);
            return new Response("Transferência criada com sucesso.", HttpStatus.OK.value(), transferencia);
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("CREATE");
            log.setTabela("transferencia");
            log.setDescricao("ERRO: Erro ao criar transferência: " + e.getMessage());
            log.setDadosNovos(String.format(
                    "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                    transferencia.getValor(),
                    transferencia.getContaOrigem().getNumeroConta(),
                    transferencia.getContaDestino().getNumeroConta(),
                    transferencia.getContaOrigem().getSaldo()
            ));
            logRepository.save(log);
            return new Response("Erro ao criar transferência: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @PutMapping("/{id}")
    public Response atualizarTransferencia(@PathVariable Long id, @RequestBody Transferencia transferencia, @RequestParam Long userId) {
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
                return new Response("Transferência não pode ser nula.", HttpStatus.BAD_REQUEST.value(), null);
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
                log.setDadosNovos(String.format(
                        "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                        transferencia.getValor(),
                        transferencia.getContaOrigem().getNumeroConta(),
                        transferencia.getContaDestino().getNumeroConta(),
                        transferencia.getContaOrigem().getSaldo()
                ));
                logRepository.save(log);
                return new Response("Transferência não encontrada.", HttpStatus.NOT_FOUND.value(), null);
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
            log.setDadosNovos(String.format(
                    "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                    transferencia.getValor(),
                    transferencia.getContaOrigem().getNumeroConta(),
                    transferencia.getContaDestino().getNumeroConta(),
                    transferencia.getContaOrigem().getSaldo()
            ));
            logRepository.save(log);

            // Retorna uma resposta de sucesso
            return new Response("Transferência atualizada com sucesso.", HttpStatus.OK.value(), transferencia);
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("UPDATE");
            log.setTabela("transferencia");
            log.setIdTabela(id);
            log.setDescricao("ERRO: Erro ao atualizar transferência: " + e.getMessage());
            assert transferencia != null;
            log.setDadosNovos(String.format(
                    "valor=%.2f|origem=%s|destino=%s|novo_saldo_origem=%.2f",
                    transferencia.getValor(),
                    transferencia.getContaOrigem().getNumeroConta(),
                    transferencia.getContaDestino().getNumeroConta(),
                    transferencia.getContaOrigem().getSaldo()
            ));
            logRepository.save(log);

            return new Response("Erro ao atualizar transferência: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @GetMapping("/{id}")
    public Response buscarTransferenciaPorId(@PathVariable Long id, @RequestParam Long userId) {
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
                return new Response("Transferência não encontrada.", HttpStatus.NOT_FOUND.value(), null);
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
            return new Response("Transferência encontrada com sucesso.", HttpStatus.OK.value(), transferencia);
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("BUSCAR");
            log.setTabela("transferencia");
            log.setIdTabela(id);
            log.setDescricao("ERRO: Erro ao buscar transferência: " + e.getMessage());
            logRepository.save(log);
            return new Response("Erro ao buscar transferência: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @GetMapping("/buscarTransferenciasPorNumeroContaOrigem")
    public Response buscarTransferenciasPorNumeroContaOrigem(@RequestParam String numeroContaOrigem, @RequestParam Long userId) {
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
                return new Response("Conta não encontrada.", HttpStatus.NOT_FOUND.value(), null);
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
            return new Response("Transferências encontradas com sucesso.", HttpStatus.OK.value(), transferencias);
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("BUSCAR");
            log.setTabela("transferencia");
            log.setDescricao("ERRO: Erro ao buscar transferências: " + e.getMessage());
            logRepository.save(log);
            return new Response("Erro ao buscar transferências: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @GetMapping("/buscarTransferenciasPorNumeroContaDestino")
    public Response buscarTransferenciasPorNumeroContaDestino(@RequestParam String numeroContaDestino, @RequestParam Long userId) {
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
            return new Response("Transferências encontradas com sucesso.", HttpStatus.OK.value(), transferencias);

        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("BUSCAR");
            log.setTabela("transferencia");
            log.setDescricao("ERRO: Erro ao buscar transferências por conta de destino: " + e.getMessage());
            logRepository.save(log);
            return new Response("Erro ao buscar transferências por conta de destino: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @DeleteMapping("/{id}")
    public Response deletarTransferencia(@PathVariable Long id) {
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
                return new Response("Transferência não encontrada.", HttpStatus.NOT_FOUND.value(), null);
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
            return new Response("Transferência deletada com sucesso.", HttpStatus.OK.value(), null);
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setTipoOperacao("DELETAR");
            log.setTabela("transferencia");
            log.setIdTabela(id);
            log.setDescricao("ERRO: Erro ao deletar transferência: " + e.getMessage());
            logRepository.save(log);
            return new Response("Erro ao deletar transferência: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @PostMapping("/buscarTransferenciasPorNumerosContas")
    public Response buscarTransferenciasPorNumeroConta(@RequestBody List<String> numerosContas, @RequestParam Long userId) {
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
            return new Response("Transferências encontradas com sucesso.", HttpStatus.OK.value(), transferencias);
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("BUSCAR");
            log.setTabela("transferencia");
            log.setDescricao("ERRO: Erro ao buscar transferências por número de conta: " + e.getMessage());
            logRepository.save(log);
            return new Response("Erro ao buscar transferências por número de conta: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
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
