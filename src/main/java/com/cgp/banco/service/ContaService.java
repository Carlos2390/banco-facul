package com.cgp.banco.service;

import com.cgp.banco.dao.ContaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContaService {

    @Autowired
    private ContaDao contaDao;

    public void depositar(String numeroConta, double valor) {
        contaDao.depositar(numeroConta, valor);
    }

    public void sacar(String numeroConta, double valor) {
        contaDao.sacar(numeroConta, valor);
    }

    public void transferir(String contaOrigem, String contaDestino, double valor) {
        contaDao.transferir(contaOrigem, contaDestino, valor);
    }

    public void encerrarConta(String numeroConta) {
        contaDao.encerrarConta(numeroConta);
    }

    public double verificarSaldo(String numeroConta) {
        return contaDao.verificarSaldo(numeroConta);
    }
}
