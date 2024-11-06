package com.cgp.banco.dao;

import com.cgp.banco.model.Endereco;

import java.util.List;

public interface EnderecoDAO extends GenericDAO<Endereco, Long> {
    // Deleta endereços pelo CPF do cliente
    void deletarEnderecosPorCpfCliente(String cpf);

    // Deleta endereços pelo ID do cliente
    void deletarEnderecosPorIdCliente(Long id);

    // Busca endereços pelo CPF do cliente
    List<Endereco> buscarEnderecosPorCpfCliente(String cpf);
}