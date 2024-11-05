package com.cgp.banco.dao;

import com.cgp.banco.model.Cliente;

import java.util.List;

public interface ClienteDAO {
    // Salva um novo cliente no banco de dados
    public void salvar(Cliente cliente);

    // Atualiza um cliente existente no banco de dados
    public void atualizar(Cliente cliente);

    // Busca um cliente pelo seu ID
    public Cliente buscarPorId(Long id);

    // Busca um cliente pelo seu CPF
    public Cliente buscarPorCpf(String cpf);

    // Deleta um cliente pelo seu ID
    public void deletar(Long id);

    // Deleta um cliente pelo seu CPF
    public void deletarClientePorCpf(String cpf);

    // Busca todos os clientes no banco de dados
    public List<Cliente> buscarTodos();
}