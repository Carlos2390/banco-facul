package com.cgp.banco.controller;

import com.cgp.banco.model.Cliente;
import com.cgp.banco.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping("/{numeroConta}/depositar")
    public ResponseEntity<String> depositar(@PathVariable String numeroConta, @RequestParam double valor) {
        contaService.depositar(numeroConta, valor);
        return ResponseEntity.ok("Depósito realizado com sucesso.");
    }

    @PostMapping("/{numeroConta}/sacar")
    public ResponseEntity<String> sacar(@PathVariable String numeroConta, @RequestParam double valor) {
        contaService.sacar(numeroConta, valor);
        return ResponseEntity.ok("Saque realizado com sucesso.");
    }

    @PostMapping("/transferir")
    public ResponseEntity<String> transferir(@RequestParam String contaOrigem, @RequestParam String contaDestino, @RequestParam double valor) {
        contaService.transferir(contaOrigem, contaDestino, valor);
        return ResponseEntity.ok("Transferência realizada com sucesso.");
    }

    @PostMapping("/{numeroConta}/encerrar")
    public ResponseEntity<String> encerrarConta(@PathVariable String numeroConta) {
        contaService.encerrarConta(numeroConta);
        return ResponseEntity.ok("Conta encerrada com sucesso.");
    }

    @GetMapping("/{numeroConta}/saldo")
    public ResponseEntity<Double> verificarSaldo(@PathVariable String numeroConta) {
        double saldo = contaService.verificarSaldo(numeroConta);
        return ResponseEntity.ok(saldo);
    }
}
