package com.cgp.banco.controller;

import com.cgp.banco.model.Transacao;
import com.cgp.banco.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping("/realizar")
    public ResponseEntity<String> realizarTransacao(
            @RequestParam Date data,
            @RequestParam double valor,
            @RequestParam String tipo,
            @RequestParam String contaOrigem,
            @RequestParam String contaDestino,
            @RequestParam String descricao) {
        transacaoService.realizarTransacao(data, valor, tipo, contaOrigem, contaDestino, descricao);
        return ResponseEntity.ok("Transação realizada com sucesso.");
    }

    @PostMapping("/estornar")
    public ResponseEntity<String> estornarTransacao(
            @RequestParam Date data,
            @RequestParam double valor,
            @RequestParam String contaOrigem,
            @RequestParam String contaDestino,
            @RequestParam String descricao) {
        transacaoService.estornarTransacao(data, valor, contaOrigem, contaDestino, descricao);
        return ResponseEntity.ok("Transação estornada com sucesso.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transacao> obterDetalhes(@PathVariable Long id) {
        Transacao transacao = transacaoService.obterDetalhes(id);
        if (transacao != null) {
            return ResponseEntity.ok(transacao);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
