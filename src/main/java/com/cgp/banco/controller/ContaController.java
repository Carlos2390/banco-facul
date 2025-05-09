package com.cgp.banco.controller;


import com.cgp.banco.model.Conta;
import com.cgp.banco.model.Log;
import com.cgp.banco.repository.ClienteRepository;
import com.cgp.banco.repository.ContaRepository;
import com.cgp.banco.repository.LogRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping
    public ResponseEntity<?> criarConta(@RequestBody Conta conta) {
        try {
            // Salva a conta no banco de dados
            contaRepository.save(conta);
            // Cria um log de operação
            Log log = new Log();
            clienteRepository.findById(conta.getClienteId()).ifPresent(cliente -> {
                log.setUserId(cliente.getIdUsuario());
            });
            log.setTipoOperacao("CREATE");
            log.setTabela("conta");
            log.setDescricao("SUCESSO: Conta criada com sucesso: " + conta.toString());
            logRepository.save(log);

            // Retorna uma resposta de sucesso
            return ResponseEntity.ok(conta);
        } catch (Exception e) {
            Log log = new Log();
            clienteRepository.findById(conta.getClienteId()).ifPresent(cliente -> {
                log.setUserId(cliente.getIdUsuario());
            });
            log.setTipoOperacao("CREATE");
            log.setTabela("conta");
            log.setDescricao("ERRO: Erro ao criar Conta: " + e.getMessage());
            logRepository.save(log);
            return ResponseEntity.badRequest().body("Erro ao criar Conta: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarConta(@PathVariable Long id, @RequestBody Conta conta) {
        try {
            // Busca a conta existente pelo ID
            Conta contaExistente = contaRepository.findById(id).orElse(null);
            // Verifica se a conta existe
            if (contaExistente == null) {
                // Cria um log de operação
                Log log = new Log();
                clienteRepository.findById(conta.getClienteId()).ifPresent(cliente -> {
                    log.setUserId(cliente.getIdUsuario());
                });
                log.setTipoOperacao("ATUALIZAR");
                log.setTabela("conta");
                log.setIdTabela(id);
                log.setDescricao("ERRO: Erro ao atualizar Conta: Conta não encontrada");
                log.setDadosAntigos(null);
                log.setDadosNovos(conta.toString());
                logRepository.save(log);
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            conta.setClienteId(contaExistente.getClienteId());
            // Define o ID da conta
            conta.setId(id);
            // Atualiza a conta no banco de dados
            contaRepository.save(conta);
            // Cria um log de operação
            Log log = new Log();
            clienteRepository.findById(conta.getClienteId()).ifPresent(cliente -> {
                log.setUserId(cliente.getIdUsuario());
            });
            log.setTipoOperacao("ATUALIZAR");
            log.setTabela("conta");
            log.setIdTabela(id);
            log.setDescricao("SUCESSO: Conta atualizada com sucesso: " + conta.toString());
            log.setDadosAntigos(contaExistente.toString());
            log.setDadosNovos(conta.toString());
            logRepository.save(log);

            // Retorna uma resposta de sucesso
            return ResponseEntity.ok(conta);
        } catch (Exception e) {
            Log log = new Log();
            log.setUserId(conta.getCliente().getIdUsuario());
            log.setTipoOperacao("ATUALIZAR");
            log.setTabela("conta");
            log.setIdTabela(id);
            log.setDescricao("Erro ao atualizar Conta: " + e.getMessage());
            log.setDadosAntigos(null);
            log.setDadosNovos(conta.toString());
            logRepository.save(log);
            return ResponseEntity.badRequest().body("Erro ao atualizar Conta: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> buscarContaPorId(@PathVariable Long id) {
        try {
            // Busca a conta pelo ID 
            Optional<Conta> contaOptional = contaRepository.findById(id);
            // Verifica se a conta existe
            if (contaOptional.isEmpty()) {
                // Cria um log de operação
                Log log = new Log();
                log.setTipoOperacao("BUSCAR");
                log.setTabela("conta");
                log.setIdTabela(id);
                log.setDescricao("ERRO: Erro ao buscar Conta por ID: Conta não encontrada");
                logRepository.save(log);
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(contaOptional.get().getCliente().getIdUsuario());
            log.setTipoOperacao("BUSCAR");
            log.setTabela("conta");
            log.setIdTabela(id);
            log.setDescricao("SUCESSO: Conta encontrada por ID: " + contaOptional.get().toString());
            logRepository.save(log);

            // Retorna a conta encontrada
            return ResponseEntity.ok(contaOptional.get());
        } catch (Exception e) {
            Log log = new Log();
            log.setTipoOperacao("BUSCAR");
            log.setTabela("conta");
            log.setIdTabela(id);
            log.setDescricao("Erro ao buscar Conta por ID: " + e.getMessage());
            logRepository.save(log);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/buscarContasPorCpfCliente")
    public ResponseEntity<List<Conta>> buscarContasPorCpfCliente(@RequestParam String cpf, HttpSession session) {
        try {
            // Busca as contas pelo CPF do cliente
            List<Conta> contas = contaRepository.findByClienteCpf(cpf);
            // Verifica se as contas existem
            if (contas.isEmpty()) {
                // Cria um log de operação
                Log log = new Log();
                log.setTipoOperacao("BUSCAR");
                log.setTabela("conta");
                log.setDescricao("ERRO: Erro ao buscar contas por CPF: Conta não encontrada");
                logRepository.save(log);
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(contas.get(0).getCliente().getIdUsuario());
            log.setTipoOperacao("BUSCAR");
            log.setTabela("conta");
            log.setDescricao("SUCESSO: Contas encontradas por CPF: " + contas.toString());
            logRepository.save(log);

            // Retorna a lista de contas encontradas
            return ResponseEntity.ok(contas);
        } catch (Exception e) {
            Log log = new Log();
            log.setTipoOperacao("BUSCAR");
            log.setTabela("conta");
            log.setDescricao("Erro ao buscar contas por CPF: " + e.getMessage());
            logRepository.save(log);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/buscarContaPorNumero")
    public ResponseEntity<Conta> buscarContaPorNumero(@RequestParam String numeroConta) {
        try {
            // Busca a conta pelo número da conta
            Conta conta = contaRepository.findByNumeroConta(numeroConta);
            // Verifica se a conta existe
            if (conta == null) {
                // Cria um log de operação
                Log log = new Log();
                log.setTipoOperacao("BUSCAR");
                log.setTabela("conta");
                log.setDescricao("ERRO: Erro ao buscar contas por número: Conta não encontrada");
                logRepository.save(log);

                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(conta.getCliente().getIdUsuario());
            log.setTipoOperacao("BUSCAR");
            log.setTabela("conta");
            log.setDescricao("SUCESSO: Conta encontrada por número: " + conta.toString());
            logRepository.save(log);

            // Retorna a conta encontrada
            return ResponseEntity.ok(conta);
        } catch (Exception e) {
            Log log = new Log();
            log.setTipoOperacao("BUSCAR");
            log.setTabela("conta");
            log.setDescricao("Erro ao buscar contas por número: " + e.getMessage());
            logRepository.save(log);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarConta(@PathVariable Long id) {
        try {
            Optional<Conta> byId = contaRepository.findById(id);
            if (byId.isEmpty()) {
                // Cria um log de operação
                Log log = new Log();
                log.setTipoOperacao("DELETAR");
                log.setTabela("conta");
                log.setIdTabela(id);
                log.setDescricao("ERRO: Erro ao deletar Conta: Conta não encontrada");
                logRepository.save(log);

                return ResponseEntity.notFound().build();
            } else {
                contaRepository.deleteById(id);
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(byId.get().getCliente().getIdUsuario());
                log.setTipoOperacao("DELETAR");
                log.setTabela("conta");
                log.setIdTabela(id);
                log.setDescricao("SUCESSO: Conta deletada com sucesso: " + byId.get().toString());
                logRepository.save(log);

                return ResponseEntity.ok("Conta deletada com sucesso.");
            }
        } catch (EmptyResultDataAccessException e) {
            // Cria um log de operação
            Log log = new Log();
            log.setTipoOperacao("DELETAR");
            log.setTabela("conta");
            log.setIdTabela(id);
            log.setDescricao("ERRO: Erro ao deletar Conta: Conta não encontrada");
            logRepository.save(log);

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setTipoOperacao("DELETAR");
            log.setTabela("conta");
            log.setIdTabela(id);
            log.setDescricao("Erro ao deletar Conta: " + e.getMessage());
            logRepository.save(log);

            return ResponseEntity.internalServerError().body("Erro ao deletar conta.");
        }
    }

    @DeleteMapping("/deletarContaPorNumero")
    public ResponseEntity<String> deletarContaPorNumero(@RequestParam String numeroConta) {
        try {
            // Busca a conta pelo número da conta
            Conta conta = contaRepository.findByNumeroConta(numeroConta);
            // Verifica se a conta existe
            if (conta == null) {
                // Cria um log de operação
                Log log = new Log();
                log.setTipoOperacao("DELETAR");
                log.setTabela("conta");
                log.setDescricao("ERRO: Erro ao deletar contas por número: Conta não encontrada");
                logRepository.save(log);

                return ResponseEntity.notFound().build();
            } else {
                // Cria um log de operação
                Log log = new Log();
                log.setUserId(conta.getCliente().getIdUsuario());
                log.setTipoOperacao("DELETAR");
                log.setTabela("conta");
                log.setDescricao("SUCESSO: Conta deletada com sucesso: " + conta.toString());
                logRepository.save(log);

                // Deleta a conta pelo número da conta
                contaRepository.deleteByNumeroConta(numeroConta);
                // Retorna uma resposta de sucesso
                return ResponseEntity.ok("Conta deletada com sucesso.");
            }
        } catch (Exception e) {
            Log log = new Log();
            log.setTipoOperacao("DELETAR");
            log.setTabela("conta");
            log.setDescricao("ERRO: Erro ao deletar contas por número: " + e.getMessage());
            logRepository.save(log);
            return ResponseEntity.badRequest().body("Erro ao deletar contas por número: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Conta>> buscarTodasContas() {
        try {
            // Busca todas as contas
            List<Conta> contas = contaRepository.findAll();
            // Retorna a lista de contas
            return ResponseEntity.ok(contas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}