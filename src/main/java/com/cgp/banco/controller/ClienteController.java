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
        clienteDAO.salvar(cliente);
        return ResponseEntity.ok("Cliente criado com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente clienteExistente = clienteDAO.buscarPorId(id);
        if (clienteExistente == null) {
            return ResponseEntity.notFound().build();
        }
        cliente.setId(id);
        clienteDAO.atualizar(cliente);
        return ResponseEntity.ok("Cliente atualizado com sucesso.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteDAO.buscarPorId(id);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/buscarClientePorCpf")
    public ResponseEntity<Cliente> buscarClientePorCpf(@RequestParam String cpf) {
        Cliente cliente = clienteDAO.buscarPorCpf(cpf);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarCliente(@PathVariable Long id) {
        Cliente cliente = clienteDAO.buscarPorId(id);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        clienteDAO.deletar(id);
        return ResponseEntity.ok("Cliente deletado com sucesso.");
    }

    @DeleteMapping("/deletarClientePorCpf")
    public ResponseEntity<String> deletarClientePorCpf(@RequestParam String cpf) {
        clienteDAO.deletarClientePorCpf(cpf);
        return ResponseEntity.ok("Cliente deletado com sucesso.");
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> buscarTodosClientes() {
        List<Cliente> clientes = clienteDAO.buscarTodos();
        return ResponseEntity.ok(clientes);
    }
}
