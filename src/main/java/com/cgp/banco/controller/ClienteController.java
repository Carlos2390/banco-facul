package com.cgp.banco.controller;

import com.cgp.banco.model.Cliente;
import com.cgp.banco.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> cadastrarCliente(
            @RequestBody Cliente cliente) {
        Cliente novoCliente = clienteService.cadastrarCliente(cliente);
        return ResponseEntity.ok(novoCliente);
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<Cliente> atualizarDados(
            @PathVariable String cpf,
            @RequestBody Cliente clienteAtualizado) {
        Cliente cliente = clienteService.atualizarDados(cpf, clienteAtualizado);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Cliente> visualizarDados(@PathVariable String cpf) {
        Cliente cliente = clienteService.visualizarDados(cpf);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping("/{cpf}/depositar")
    public ResponseEntity<String> depositar(
            @PathVariable String cpf,
            @RequestParam double valor) {
        clienteService.depositar(cpf, valor);
        return ResponseEntity.ok("Depósito realizado com sucesso.");
    }

    @PostMapping("/{cpf}/sacar")
    public ResponseEntity<String> sacar(
            @PathVariable String cpf,
            @RequestParam double valor) {
        clienteService.sacar(cpf, valor);
        return ResponseEntity.ok("Saque realizado com sucesso.");
    }

    @PostMapping("/{cpf}/transferir")
    public ResponseEntity<String> transferir(
            @PathVariable String cpf,
            @RequestParam String contaDestino,
            @RequestParam double valor) {
        clienteService.transferir(cpf, contaDestino, valor);
        return ResponseEntity.ok("Transferência realizada com sucesso.");
    }
}
