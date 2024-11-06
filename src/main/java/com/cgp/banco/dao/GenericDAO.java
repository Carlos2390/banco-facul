package com.cgp.banco.dao;

import java.util.List;

public interface GenericDAO<T, ID> {
    // Salva uma nova entidade no banco de dados
    void salvar(T entity);

    // Atualiza uma entidade existente no banco de dados
    void atualizar(T entity);

    // Busca uma entidade pelo seu ID
    T buscarPorId(ID id);

    // Deleta uma entidade pelo seu ID
    void deletar(ID id);

    // Busca todas as entidades no banco de dados
    List<T> buscarTodos();
}