package com.cgp.banco.controller;

import com.cgp.banco.dao.EnderecoDAO;
import com.cgp.banco.model.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoDAO enderecoDAO;

    @PostMapping
    public ResponseEntity<String> criarEndereco(@RequestBody Endereco endereco) {
        // Salva o endereço no banco de dados
        enderecoDAO.salvar(endereco);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Endereço criado com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarEndereco(@PathVariable Long id, @RequestBody Endereco endereco) {
        // Busca o endereço existente pelo ID
        Endereco enderecoExistente = enderecoDAO.buscarPorId(id);
        // Verifica se o endereço existe
        if (enderecoExistente == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Define o ID do endereço
        endereco.setId(id);
        // Atualiza o endereço no banco de dados
        enderecoDAO.atualizar(endereco);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Endereço atualizado com sucesso.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarEnderecoPorId(@PathVariable Long id) {
        // Busca o endereço pelo ID
        Endereco endereco = enderecoDAO.buscarPorId(id);
        // Verifica se o endereço existe
        if (endereco == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Retorna o endereço encontrado
        return ResponseEntity.ok(endereco);
    }

    @GetMapping("/buscarEnderecosPorCpfCliente")
    public ResponseEntity<List<Endereco>> buscarEnderecosPorCpfCliente(@RequestParam String cpf) {
        // Busca os endereços pelo CPF do cliente
        List<Endereco> enderecos = enderecoDAO.buscarEnderecosPorCpfCliente(cpf);
        // Retorna a lista de endereços encontrados
        return ResponseEntity.ok(enderecos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarEndereco(@PathVariable Long id) {
        // Busca o endereço pelo ID
        Endereco endereco = enderecoDAO.buscarPorId(id);
        // Verifica se o endereço existe
        if (endereco == null) {
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Deleta o endereço do banco de dados
        enderecoDAO.deletar(endereco);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Endereço deletado com sucesso.");
    }

    @DeleteMapping("/deletarEnderecosPorCpfCliente")
    public ResponseEntity<String> deletarEnderecosPorCpfCliente(@RequestParam String cpf) {
        // Deleta os endereços pelo CPF do cliente
        enderecoDAO.deletarEnderecosPorCpfCliente(cpf);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Endereços deletados com sucesso.");
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> buscarTodosEnderecos() {
        // Busca todos os endereços
        List<Endereco> enderecos = enderecoDAO.buscarTodos();
        // Retorna a lista de endereços
        return ResponseEntity.ok(enderecos);
    }
}