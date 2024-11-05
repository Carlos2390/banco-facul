package com.cgp.banco.controller;

import com.cgp.banco.dao.ContaDAO;
import com.cgp.banco.model.Conta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaDAO contaDAO;

    @PostMapping
    public ResponseEntity<String> criarConta(@RequestBody Conta conta) {
        // Salva a conta no banco de dados
        contaDAO.salvar(conta);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Conta criada com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarConta(@PathVariable Long id, @RequestBody Conta conta) {
        // Busca a conta existente pelo ID
        Conta contaExistente = contaDAO.buscarPorId(id);
        // Verifica se a conta existe
        if (contaExistente == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Define o ID da conta
        conta.setId(id);
        // Atualiza a conta no banco de dados
        contaDAO.atualizar(conta);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Conta atualizada com sucesso.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> buscarContaPorId(@PathVariable Long id) {
        // Busca a conta pelo ID
        Conta conta = contaDAO.buscarPorId(id);
        // Verifica se a conta existe
        if (conta == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Retorna a conta encontrada
        return ResponseEntity.ok(conta);
    }

    @GetMapping("/buscarContasPorCpfCliente")
    public ResponseEntity<List<Conta>> buscarContasPorCpfCliente(@RequestParam String cpf) {
        // Busca as contas pelo CPF do cliente
        List<Conta> contas = contaDAO.buscarContasPorCpfCliente(cpf);
        // Retorna a lista de contas encontradas
        return ResponseEntity.ok(contas);
    }

    @GetMapping("/buscarContaPorNumero")
    public ResponseEntity<Conta> buscarContaPorNumero(@RequestParam Integer numeroConta) {
        // Busca a conta pelo número da conta
        Conta conta = contaDAO.buscarContaPorNumero(numeroConta);
        // Verifica se a conta existe
        if (conta == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Retorna a conta encontrada
        return ResponseEntity.ok(conta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarConta(@PathVariable Long id) {
        // Busca a conta pelo ID
        Conta conta = contaDAO.buscarPorId(id);
        // Verifica se a conta existe
        if (conta == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Deleta a conta do banco de dados
        contaDAO.deletar(id);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Conta deletada com sucesso.");
    }

    @DeleteMapping("/deletarContaPorNumero")
    public ResponseEntity<String> deletarContaPorNumero(@RequestParam Integer numeroConta) {
        // Deleta a conta pelo número da conta
        contaDAO.deletarContaPorNumero(numeroConta);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Conta deletada com sucesso.");
    }

    @GetMapping
    public ResponseEntity<List<Conta>> buscarTodasContas() {
        // Busca todas as contas
        List<Conta> contas = contaDAO.buscarTodas();
        // Retorna a lista de contas
        return ResponseEntity.ok(contas);
    }
}