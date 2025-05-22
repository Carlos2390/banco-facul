package com.cgp.banco.controller;


import com.cgp.banco.model.Conta;
import com.cgp.banco.model.Response;
import com.cgp.banco.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public Response criarConta(@RequestBody Conta conta, @RequestParam Long userId) {
        try {
            // Salva a conta no banco de dados
            contaRepository.save(conta);
            // Retorna uma resposta de sucesso
            return new Response("Conta criada com sucesso", HttpStatus.OK.value(), conta);
        } catch (Exception e) {
            return new Response("Erro ao criar Conta: " + e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        }
    }

    @PutMapping("/{id}")
    public Response atualizarConta(@PathVariable Long id, @RequestBody Conta conta, @RequestParam Long userId) {
        try {
            // Busca a conta existente pelo ID
            Conta contaExistente = contaRepository.findById(id).orElse(null);
            // Verifica se a conta existe
            if (contaExistente == null) {
                // Retorna uma resposta de não encontrado
                return new Response("Erro ao atualizar Conta: Conta não encontrada", HttpStatus.NOT_FOUND.value(), null);
            }
            conta.setClienteId(contaExistente.getClienteId());
            // Define o ID da conta
            conta.setId(id);
            // Atualiza a conta no banco de dados
            contaRepository.save(conta);
            // Retorna uma resposta de sucesso
            return new Response("Conta atualizada com sucesso", HttpStatus.OK.value(), conta);
        } catch (Exception e) {
            return new Response("Erro ao atualizar Conta: " + e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        }
    }

    @GetMapping("/{id}")
    public Response buscarContaPorId(@PathVariable Long id, @RequestParam Long userId) {
        try {
            // Busca a conta pelo ID 
            Optional<Conta> contaOptional = contaRepository.findById(id);
            // Verifica se a conta existe
            if (contaOptional.isEmpty()) {
                // Retorna uma resposta de não encontrado
                return new Response("Conta não encontrada", HttpStatus.NOT_FOUND.value(), null);
            }
            // Retorna a conta encontrada
            return new Response("Conta encontrada com sucesso", HttpStatus.OK.value(), contaOptional.get());
        } catch (Exception e) {
            return new Response("Erro ao buscar Conta por ID: " + e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        }
    }

    @GetMapping("/buscarContasPorCpfCliente")
    public Response buscarContasPorCpfCliente(@RequestParam String cpf, @RequestParam Long userId) {
        try {
            // Busca as contas pelo CPF do cliente
            List<Conta> contas = contaRepository.findByClienteCpf(cpf);
            // Verifica se as contas existem
            if (contas.isEmpty()) {
                // Retorna uma resposta de não encontrado
                return new Response("Conta não encontrada", HttpStatus.NOT_FOUND.value(), null);
            }
            // Retorna a lista de contas encontradas
            return new Response("Contas encontradas com sucesso", HttpStatus.OK.value(), contas);
        } catch (Exception e) {
            return new Response("Erro ao buscar contas por CPF: " + e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        }
    }

    @GetMapping("/buscarContaPorNumero")
    public Response buscarContaPorNumero(@RequestParam String numeroConta, @RequestParam Long userId) {
        try {
            // Busca a conta pelo número da conta
            Conta conta = contaRepository.findByNumeroConta(numeroConta);
            // Verifica se a conta existe
            if (conta == null) {
                // Retorna uma resposta de não encontrado
                return new Response("Conta não encontrada", HttpStatus.NOT_FOUND.value(), null);
            }
            // Retorna a conta encontrada
            return new Response("Conta encontrada com sucesso", HttpStatus.OK.value(), conta);
        } catch (Exception e) {
            return new Response("Erro ao buscar contas por número: " + e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        }
    }

    @DeleteMapping("/{id}")
    public Response deletarConta(@PathVariable Long id, @RequestParam Long userId) {
        try {
            Optional<Conta> byId = contaRepository.findById(id);
            if (byId.isEmpty()) {
                return new Response("Conta não encontrada", HttpStatus.NOT_FOUND.value(), null);
            } else {
                contaRepository.deleteById(id);
                return new Response("Conta deletada com sucesso", HttpStatus.OK.value(), null);
            }
        } catch (EmptyResultDataAccessException e) {
            return new Response("Conta não encontrada", HttpStatus.NOT_FOUND.value(), null);
        } catch (Exception e) {
            return new Response("Erro ao deletar Conta: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @DeleteMapping("/deletarContaPorNumero")
    public Response deletarContaPorNumero(@RequestParam String numeroConta, @RequestParam Long userId) {
        try {
            // Busca a conta pelo número da conta
            Conta conta = contaRepository.findByNumeroConta(numeroConta);
            // Verifica se a conta existe
            if (conta == null) {
                return new Response("Conta não encontrada", HttpStatus.NOT_FOUND.value(), null);
            } else {
                // Deleta a conta pelo número da conta
                contaRepository.deleteByNumeroConta(numeroConta);
                // Retorna uma resposta de sucesso
                return new Response("Conta deletada com sucesso", HttpStatus.OK.value(), null);
            }
        } catch (Exception e) {
            return new Response("Erro ao deletar contas por número: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
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