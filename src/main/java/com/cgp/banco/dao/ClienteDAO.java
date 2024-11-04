package com.cgp.banco.dao;

import com.cgp.banco.model.Cliente;

import java.util.List;

public interface ClienteDAO {
    public void salvar(Cliente cliente);

    public void atualizar(Cliente cliente);

    public Cliente buscarPorId(Long id);

    public List<Cliente> buscarTodos();
}

