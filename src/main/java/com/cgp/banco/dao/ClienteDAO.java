package com.cgp.banco.dao;

import com.cgp.banco.model.Cliente;

public interface ClienteDAO extends GenericDAO<Cliente, Long> {
    // Busca um cliente pelo seu CPF
    Cliente buscarPorCpf(String cpf);

    // Deleta um cliente pelo seu CPF
    void deletarClientePorCpf(String cpf);
}