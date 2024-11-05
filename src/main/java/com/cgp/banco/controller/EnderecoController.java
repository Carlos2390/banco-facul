package com.cgp.banco.controller;

import com.cgp.banco.dao.EnderecoDAO;
import com.cgp.banco.model.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoDAO enderecoDAO;

    @PostMapping
    public ResponseEntity<String> criarEndereco(@RequestBody Endereco endereco) {
        enderecoDAO.salvar(endereco);
        return ResponseEntity.ok("Endereço criado com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarEndereco(@PathVariable Long id, @RequestBody Endereco endereco) {
        Endereco enderecoExistente = enderecoDAO.buscarPorId(id);
        if (enderecoExistente == null) {
            return ResponseEntity.notFound().build();
        }
        endereco.setId(id);
        enderecoDAO.atualizar(endereco);
        return ResponseEntity.ok("Endereço atualizado com sucesso.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarEnderecoPorId(@PathVariable Long id) {
        Endereco endereco = enderecoDAO.buscarPorId(id);
        if (endereco == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(endereco);
    }

    @GetMapping("/buscarEnderecosPorCpfCliente")
    public ResponseEntity<List<Endereco>> buscarEnderecosPorCpfCliente(@RequestParam String cpf) {
        List<Endereco> enderecos = enderecoDAO.buscarEnderecosPorCpfCliente(cpf);
        return ResponseEntity.ok(enderecos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarEndereco(@PathVariable Long id) {
        Endereco endereco = enderecoDAO.buscarPorId(id);
        if (endereco == null) {
            return ResponseEntity.notFound().build();
        }
        enderecoDAO.deletar(endereco);
        return ResponseEntity.ok("Endereço deletado com sucesso.");
    }

    @DeleteMapping("/deletarEnderecosPorCpfCliente")
    public ResponseEntity<String> deletarEnderecosPorCpfCliente(@RequestParam String cpf) {
        enderecoDAO.deletarEnderecosPorCpfCliente(cpf);
        return ResponseEntity.ok("Endereços deletados com sucesso.");
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> buscarTodosEnderecos() {
        List<Endereco> enderecos = enderecoDAO.buscarTodos();
        return ResponseEntity.ok(enderecos);
    }
}
