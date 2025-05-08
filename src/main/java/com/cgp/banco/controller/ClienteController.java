package com.cgp.banco.controller;

import com.cgp.banco.model.Usuario;
import com.cgp.banco.repository.LogRepository;
import com.cgp.banco.repository.ClienteRepository;
import com.cgp.banco.model.Cliente;
import com.cgp.banco.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private LogRepository logRepository;

    @PostMapping
    public ResponseEntity<?> criarCliente(@RequestBody Cliente cliente) {
        try {
            // Salva o cliente no banco de dados
            clienteRepository.save(cliente);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            // Retorna uma resposta de erro
            return ResponseEntity.badRequest().body("Erro ao criar cliente: " + e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        // Busca o cliente existente pelo ID
        Cliente clienteExistente = clienteRepository.findById(id).orElse(null);
        // Verifica se o cliente existe
        if (clienteExistente == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }

        try {
            cliente.setId(id);
            // Atualiza o cliente no banco de dados
            clienteRepository.save(cliente);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            // Retorna uma resposta de erro
            return ResponseEntity.badRequest().body("Erro ao atualizar cliente: " + e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
        // Busca o cliente pelo ID
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        // Verifica se o cliente existe
        if (cliente == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Retorna o cliente encontrado
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/buscarClientePorCpf")
    public ResponseEntity<Cliente> buscarClientePorCpf(@RequestParam String cpf) {
        // Busca o cliente pelo CPF
        Cliente cliente = clienteRepository.findByCpf(cpf);
        // Verifica se o cliente existe
        if (cliente == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Retorna o cliente encontrado
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarCliente(@PathVariable Long id) {
        // Busca o cliente pelo ID
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        // Verifica se o cliente existe
        if (cliente == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Deleta o cliente do banco de dados
        clienteRepository.deleteById(id);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Cliente deletado com sucesso.");
    }

    @DeleteMapping("/deletarClientePorCpf")
    public ResponseEntity<String> deletarClientePorCpf(@RequestParam String cpf) {
        // Deleta o cliente pelo CPF
        clienteRepository.deleteByCpf(cpf);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Cliente deletado com sucesso.");
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> buscarTodosClientes() {
        // Busca todos os clientes
        List<Cliente> clientes = clienteRepository.findAll();
        // Retorna a lista de clientes
        return ResponseEntity.ok(clientes);
    }
}