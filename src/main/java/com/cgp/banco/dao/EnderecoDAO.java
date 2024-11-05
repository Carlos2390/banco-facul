package com.cgp.banco.dao;

import com.cgp.banco.model.Endereco;
import java.util.List;

public interface EnderecoDAO {
    // Salva um novo endereço no banco de dados
    void salvar(Endereco endereco);

    // Atualiza um endereço existente no banco de dados
    void atualizar(Endereco endereco);

    // Busca um endereço pelo seu ID
    Endereco buscarPorId(Long id);

    // Deleta um endereço
    void deletar(Endereco endereco);

    // Deleta endereços pelo CPF do cliente
    void deletarEnderecosPorCpfCliente(String cpf);

    // Deleta endereços pelo ID do cliente
    void deletarEnderecosPorIdCliente(Long id);

    // Busca todos os endereços no banco de dados
    List<Endereco> buscarTodos();

    // Busca endereços pelo CPF do cliente
    List<Endereco> buscarEnderecosPorCpfCliente(String cpf);
}