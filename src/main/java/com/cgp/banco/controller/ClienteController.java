package com.cgp.banco.controller;

import com.cgp.banco.dao.ClienteDAO;
import com.cgp.banco.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteDAO clienteDAO;

    @PostMapping
    public ResponseEntity<String> criarCliente(@RequestBody Cliente cliente) {
        // Salva o cliente no banco de dados
        clienteDAO.salvar(cliente);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Cliente criado com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        // Busca o cliente existente pelo ID
        Cliente clienteExistente = clienteDAO.buscarPorId(id);
        // Verifica se o cliente existe
        if (clienteExistente == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Define o ID do cliente
        cliente.setId(id);
        // Atualiza o cliente no banco de dados
        clienteDAO.atualizar(cliente);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Cliente atualizado com sucesso.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
        // Busca o cliente pelo ID
        Cliente cliente = clienteDAO.buscarPorId(id);
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
        Cliente cliente = clienteDAO.buscarPorCpf(cpf);
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
        Cliente cliente = clienteDAO.buscarPorId(id);
        // Verifica se o cliente existe
        if (cliente == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Deleta o cliente do banco de dados
        clienteDAO.deletar(id);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Cliente deletado com sucesso.");
    }

    @DeleteMapping("/deletarClientePorCpf")
    public ResponseEntity<String> deletarClientePorCpf(@RequestParam String cpf) {
        // Deleta o cliente pelo CPF
        clienteDAO.deletarClientePorCpf(cpf);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Cliente deletado com sucesso.");
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> buscarTodosClientes() {
        // Busca todos os clientes
        List<Cliente> clientes = clienteDAO.buscarTodos();
        // Retorna a lista de clientes
        return ResponseEntity.ok(clientes);
    }
}