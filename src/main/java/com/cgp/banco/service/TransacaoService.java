package com.cgp.banco.service;

import com.cgp.banco.dao.TransacaoDao;
import com.cgp.banco.model.Transacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransacaoService {

    private final TransacaoDao transacaoDao;

    @Autowired
    public TransacaoService(TransacaoDao transacaoDao) {
        this.transacaoDao = transacaoDao;
    }

    public void realizarTransacao(Date data, double valor, String tipo, String contaOrigem, String contaDestino, String descricao) {
        transacaoDao.realizarTransacao(data, valor, tipo, contaOrigem, contaDestino, descricao);
    }

    public void estornarTransacao(Date data, double valor, String contaOrigem, String contaDestino, String descricao) {
        transacaoDao.estornarTransacao(data, valor, contaOrigem, contaDestino, descricao);
    }

    public Transacao obterDetalhes(Long id) {
        return transacaoDao.obterDetalhes(id);
    }
}
