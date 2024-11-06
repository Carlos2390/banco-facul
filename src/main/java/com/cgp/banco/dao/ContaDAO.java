package com.cgp.banco.dao;

import com.cgp.banco.model.Conta;

import java.util.List;

public interface ContaDAO extends GenericDAO<Conta, Long> {
    // Busca contas pelo CPF do cliente
    List<Conta> buscarContasPorCpfCliente(String cpf);

    // Deleta uma conta pelo número da conta
    void deletarContaPorNumero(Integer numeroConta);

    // Deleta contas pelo CPF do cliente
    void deletarContasPorCpfCliente(String cpf);

    // Deleta contas pelo ID do cliente
    void deletarContasPorIdCliente(Long id);

    // Busca uma conta pelo número da conta
    Conta buscarContaPorNumero(Integer numeroConta);
}