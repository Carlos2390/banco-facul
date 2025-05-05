package com.cgp.banco.controller;


import com.cgp.banco.dao.LogDAO;
import com.cgp.banco.dao.ContaRepository;
import com.cgp.banco.model.Conta;
import com.cgp.banco.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired    
    private ContaRepository contaRepository;
    
    @Autowired
    private LogDAO logDAO;

    @PostMapping
    public ResponseEntity<String> criarConta(@RequestBody Conta conta, HttpSession session) {
        try {
           
            
            // Salva a conta no banco de dados
            contaRepository.save(conta);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Conta criada com sucesso.");
        } catch (Exception e) {
            Log log = new Log();
            log.setTipoOperacao("INSERT");
            log.setTabela("Conta");
            log.setDescricao("Erro ao criar Conta: " + e.getMessage());
            log.setUserId((Long) session.getAttribute("currentUserId"));
            logDAO.salvar(log);
            return ResponseEntity.badRequest().body("Erro ao criar Conta: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarConta(@PathVariable Long id, @RequestBody Conta conta, HttpSession session) {
        try {
            
            // Busca a conta existente pelo ID
            Conta contaExistente = contaRepository.findById(id).orElse(null);
            // Verifica se a conta existe
            if (contaExistente == null) {
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            conta.setCliente(contaExistente.getCliente());
            // Define o ID da conta
           
            conta.setId(id);
            // Atualiza a conta no banco de dados
            contaDAO.atualizar(conta);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Conta atualizada com sucesso.");
        } catch (Exception e) {
            Log log = new Log();
            log.setAcao("ATUALIZAR");
            log.setTabelaAfetada("Conta");
            log.setIdRegistroAfetado(id.intValue());
            log.setDescricaoMudanca("Erro ao atualizar Conta: " + e.getMessage());
            log.setIdUsuario((Integer) session.getAttribute("currentUserId"));
            logDAO.salvar(log);
            return ResponseEntity.badRequest().body("Erro ao atualizar Conta: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> buscarContaPorId(@PathVariable Long id, HttpSession session) {
        try {
           
            // Busca a conta pelo ID 
            Conta conta = contaDAO.buscarPorId(id);
            // Verifica se a conta existe
            if (conta == null) {
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            // Retorna a conta encontrada
            return ResponseEntity.ok(conta);
        }catch (Exception e) {
            Log log = new Log();
            log.setAcao("BUSCAR");
            log.setTabelaAfetada("Conta");
            log.setIdRegistroAfetado(id.intValue());
            log.setDescricaoMudanca("Erro ao buscar Conta por ID: " + e.getMessage());
            log.setIdUsuario((Integer) session.getAttribute("currentUserId"));
            logDAO.salvar(log);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/buscarContasPorCpfCliente")
    public ResponseEntity<List<Conta>> buscarContasPorCpfCliente(@RequestParam String cpf, HttpSession session) {
        try {
            
            // Busca as contas pelo CPF do cliente
             List<Conta> contas = contaRepository.findByClienteCpf(cpf);
            List<Conta> contas = contaDAO.buscarContasPorCpfCliente(cpf);
            // Retorna a lista de contas encontradas
            return ResponseEntity.ok(contas);
        } catch (Exception e) {
            Log log = new Log();
            log.setAcao("BUSCAR");
            log.setTabelaAfetada("Conta");
            log.setDescricaoMudanca("Erro ao buscar contas por CPF: " + e.getMessage());
            log.setIdUsuario((Integer) session.getAttribute("currentUserId"));
            logDAO.salvar(log);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/buscarContaPorNumero")
    public ResponseEntity<Conta> buscarContaPorNumero(@RequestParam Integer numeroConta, HttpSession session) {
        try {
           
            // Busca a conta pelo número da conta
             Conta conta = contaRepository.findByNumeroConta(numeroConta);
            Conta conta = contaDAO.buscarContaPorNumero(numeroConta);
            // Verifica se a conta existe
            if (conta == null) {
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            // Retorna a conta encontrada
            return ResponseEntity.ok(conta);
        }catch (Exception e) {
            Log log = new Log();
            log.setAcao("BUSCAR");
            log.setTabelaAfetada("Conta");
            log.setDescricaoMudanca("Erro ao buscar contas por número: " + e.getMessage());
            log.setIdUsuario((Integer) session.getAttribute("currentUserId"));
            logDAO.salvar(log);
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
    public ResponseEntity<String> deletarContaPorNumero(@RequestParam Integer numeroConta, HttpSession session) {
        try{
            //set user id on session
            contaDAO.setUserId((Integer) session.getAttribute("currentUserId"));
            // Deleta a conta pelo número da conta
            contaDAO.deletarContaPorNumero(numeroConta);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Conta deletada com sucesso.");
        }catch(Exception e){
            Log log = new Log();
            log.setAcao("DELETAR");
            log.setTabelaAfetada("Conta");
            log.setDescricaoMudanca("Erro ao deletar contas por número: " + e.getMessage());
            log.setIdUsuario((Integer) session.getAttribute("currentUserId"));
            logDAO.salvar(log);
            return ResponseEntity.badRequest().body("Erro ao deletar contas por número: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Conta>> buscarTodasContas(HttpSession session) {
        try {
            //set user id on session
            contaDAO.setUserId((Integer) session.getAttribute("currentUserId"));
            // Busca todas as contas
            List<Conta> contas = contaDAO.buscarTodos();
            // Retorna a lista de contas
            return ResponseEntity.ok(contas);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }
}