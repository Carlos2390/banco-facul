package com.cgp.banco.service;

import com.cgp.banco.dao.ClienteDao;
import com.cgp.banco.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ClienteService {

    @Autowired
    private ClienteDao clienteDao;

    public Cliente cadastrarCliente(Cliente cliente) {
        cliente.setData_criacao(new Date());
        clienteDao.cadastrarCliente(
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getIdEndereco(),
                cliente.getData_nascimento(),
                cliente.getData_criacao(),
                cliente.getSaldo()
        );
        return clienteDao.visualizarDados(cliente.getCpf());
    }

    public Cliente atualizarDados(String cpf, Cliente clienteAtualizado) {
        Cliente cliente = clienteDao.visualizarDados(cpf);

        if (cliente != null) {
            clienteDao.atualizarDados(
                    clienteAtualizado.getNome(),
                    clienteAtualizado.getEmail(),
                    clienteAtualizado.getIdEndereco(),
                    cpf
            );
            return clienteDao.visualizarDados(cpf);
        } else {
            throw new RuntimeException("Cliente não encontrado");
        }
    }

    public Cliente visualizarDados(String cpf) {
        Cliente cliente = clienteDao.visualizarDados(cpf);

        if (cliente != null) {
            return cliente;
        } else {
            throw new RuntimeException("Cliente não encontrado");
        }
    }

    public void depositar(String cpf, double valor) {
        clienteDao.depositar(cpf, valor);
    }

    public void sacar(String cpf, double valor) {
        Cliente cliente = visualizarDados(cpf);
        if (cliente.getSaldo() >= valor) {
            clienteDao.sacar(cpf, valor);
        } else {
            throw new RuntimeException("Saldo insuficiente para saque");
        }
    }

    public void transferir(String cpf, String contaDestino, double valor) {
        clienteDao.transferir(cpf, contaDestino, valor);
    }
}