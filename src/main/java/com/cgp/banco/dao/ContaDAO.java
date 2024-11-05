package com.cgp.banco.dao;

import com.cgp.banco.model.Conta;
import java.util.List;

public interface ContaDAO {
    // Salva uma nova conta no banco de dados
    void salvar(Conta conta);

    // Atualiza uma conta existente no banco de dados
    void atualizar(Conta conta);

    // Busca uma conta pelo seu ID
    Conta buscarPorId(Long id);

    // Busca todas as contas no banco de dados
    List<Conta> buscarTodas();

    // Busca contas pelo CPF do cliente
    List<Conta> buscarContasPorCpfCliente(String cpf);

    // Deleta uma conta pelo seu ID
    void deletar(Long id);

    // Deleta uma conta pelo número da conta
    void deletarContaPorNumero(Integer numeroConta);

    // Deleta contas pelo CPF do cliente
    void deletarContasPorCpfCliente(String cpf);

    // Deleta contas pelo ID do cliente
    void deletarContasPorIdCliente(Long id);

    // Busca uma conta pelo número da conta
    Conta buscarContaPorNumero(Integer numeroConta);
}