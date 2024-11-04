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
        contaDAO.salvar(conta);
        return ResponseEntity.ok("Conta criada com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarConta(@PathVariable Long id, @RequestBody Conta conta) {
        Conta contaExistente = contaDAO.buscarPorId(id);
        if (contaExistente == null) {
            return ResponseEntity.notFound().build();
        }
        conta.setId(id);
        contaDAO.atualizar(conta);
        return ResponseEntity.ok("Conta atualizada com sucesso.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> buscarContaPorId(@PathVariable Long id) {
        Conta conta = contaDAO.buscarPorId(id);
        if (conta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(conta);
    }

    @GetMapping("/buscarContasPorCpfCliente")
    public ResponseEntity<List<Conta>> buscarContasPorCpfCliente(@RequestParam String cpf) {
        List<Conta> contas = contaDAO.buscarContasPorCpfCliente(cpf);
        return ResponseEntity.ok(contas);
    }

    @GetMapping("/buscarContaPorNumero")
    public ResponseEntity<Conta> buscarContaPorNumero(@RequestParam Integer numeroConta) {
        Conta conta = contaDAO.buscarContaPorNumero(numeroConta);
        if (conta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(conta);
    }

    @GetMapping
    public ResponseEntity<List<Conta>> buscarTodasContas() {
        List<Conta> contas = contaDAO.buscarTodas();
        return ResponseEntity.ok(contas);
    }
}
