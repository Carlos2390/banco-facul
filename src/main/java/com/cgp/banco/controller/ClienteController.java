package com.cgp.banco.controller;

import com.cgp.banco.dao.ClienteRepository;
import com.cgp.banco.dao.LogDAO;
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
    private LogDAO logDAO;

    @PostMapping
    public ResponseEntity<String> criarCliente(@RequestBody Cliente cliente, HttpSession session) {
        try {
            // Obtenha o ID do usuário da sessão
            // Salva o cliente no banco de dados
            clienteRepository.save(cliente);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Cliente criado com sucesso.");
        } catch (Exception e) {
            Log log = new Log(null, session.getAttribute("currentUserId") != null ? (Long) session.getAttribute("currentUserId") : null, "Erro ao criar cliente", "Cliente", null, e.getMessage(), null, null);
            logDAO.salvar(log);
            // Retorna uma resposta de erro
            return ResponseEntity.badRequest().body("Erro ao criar cliente: " + e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente, HttpSession session) {
        // Busca o cliente existente pelo ID
        Cliente clienteExistente = clienteRepository.findById(id).orElse(null);
        // Verifica se o cliente existe
        if (clienteExistente == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }

        try {
            // Obtenha o ID do usuário da sessão
            cliente.setId(id);
            // Atualiza o cliente no banco de dados
            clienteRepository.save(cliente);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Cliente atualizado com sucesso.");
        } catch (Exception e) {
            Log log = new Log(null, session.getAttribute("currentUserId") != null ? (Long) session.getAttribute("currentUserId") : null, "Erro ao atualizar cliente", "Cliente", cliente.getId(), e.getMessage(), null, null);
            logDAO.salvar(log);
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