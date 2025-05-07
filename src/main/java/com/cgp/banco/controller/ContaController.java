package com.cgp.banco.controller;


import com.cgp.banco.model.Conta;
import com.cgp.banco.model.Log;
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

    @PostMapping
    public ResponseEntity<String> criarConta(@RequestBody Conta conta) {
        try {
            // Salva a conta no banco de dados
            contaRepository.save(conta);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Conta criada com sucesso.");
        } catch (Exception e) {
            Log log = new Log();
            log.setTipoOperacao("INSERIR");
            log.setTabela("Conta");
            log.setDescricao("Erro ao criar Conta: " + e.getMessage());
            logRepository.save(log);
            return ResponseEntity.badRequest().body("Erro ao criar Conta: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarConta(@PathVariable Long id, @RequestBody Conta conta) {
        try {
            
            // Busca a conta existente pelo ID
            Conta contaExistente = contaRepository.findById(id).orElse(null);
            // Verifica se a conta existe
            if (contaExistente == null) {
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            conta.setClienteId(contaExistente.getClienteId());
            // Define o ID da conta
           
            conta.setId(id);
            // Atualiza a conta no banco de dados
            contaRepository.save(conta);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Conta atualizada com sucesso.");
        } catch (Exception e) {
            Log log = new Log();
            log.setTipoOperacao("ATUALIZAR");
            log.setTabela("Conta");
            log.setIdTabela(id);
            log.setDescricao("Erro ao atualizar Conta: " + e.getMessage());
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
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            // Retorna a conta encontrada
            return ResponseEntity.ok(contaOptional.get());
        }catch (Exception e) {
            Log log = new Log();
            log.setTipoOperacao("BUSCAR");
            log.setTabela("Conta");
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
            // Retorna a lista de contas encontradas
            return ResponseEntity.ok(contas);
        } catch (Exception e) {
            Log log = new Log();
            log.setTipoOperacao("BUSCAR");
            log.setTabela("Conta");
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
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            // Retorna a conta encontrada
            return ResponseEntity.ok(conta);
        }catch (Exception e) {
            Log log = new Log();
            log.setTipoOperacao("BUSCAR");
            log.setTabela("Conta");
            log.setDescricao("Erro ao buscar contas por número: " + e.getMessage());
            logRepository.save(log);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarConta(@PathVariable Long id) {
        try{
            contaRepository.deleteById(id);
            return ResponseEntity.ok("Conta deletada com sucesso.");
        }catch(EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body("Erro ao deletar conta.");
        }
    }

    @DeleteMapping("/deletarContaPorNumero")
    public ResponseEntity<String> deletarContaPorNumero(@RequestParam String numeroConta) {
        try{
            // Deleta a conta pelo número da conta
            contaRepository.deleteByNumeroConta(numeroConta);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Conta deletada com sucesso.");
        }catch(Exception e){
            Log log = new Log();
            log.setTipoOperacao("DELETAR");
            log.setTabela("Conta");
            log.setDescricao("Erro ao deletar contas por número: " + e.getMessage());
            logRepository.save(log);
            return ResponseEntity.badRequest().body("Erro ao deletar contas por número: " + e.getMessage());
        }
    }

//    @GetMapping
//    public ResponseEntity<List<Conta>> buscarTodasContasPor(HttpSession session) {
//        try {
//            //set user id on session
//            contaDAO.setUserId((Integer) session.getAttribute("currentUserId"));
//            // Busca todas as contas
//            List<Conta> contas = contaDAO.buscarTodos();
//            // Retorna a lista de contas
//            return ResponseEntity.ok(contas);
//        }catch(Exception e){
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
}