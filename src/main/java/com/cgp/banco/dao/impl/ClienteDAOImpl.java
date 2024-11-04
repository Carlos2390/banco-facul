package com.cgp.banco.dao.impl;

import com.cgp.banco.dao.ClienteDAO;
import com.cgp.banco.model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClienteDAOImpl implements ClienteDAO {

    @PersistenceContext
    private EntityManager gerenciadorEntidade;

    @Override
    @Transactional
    public void salvar(Cliente cliente) {
        gerenciadorEntidade.createNativeQuery("INSERT INTO Cliente (nome, cpf, data_nascimento) VALUES (?, ?, ?)")
                .setParameter(1, cliente.getNome())
                .setParameter(2, cliente.getCpf())
                .setParameter(3, cliente.getDataNascimento())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void atualizar(Cliente cliente) {
        gerenciadorEntidade.createNativeQuery("UPDATE Cliente SET nome = ?, cpf = ?, data_nascimento = ? WHERE id_cliente = ?")
                .setParameter(1, cliente.getNome())
                .setParameter(2, cliente.getCpf())
                .setParameter(3, cliente.getDataNascimento())
                .setParameter(4, cliente.getId())
                .executeUpdate();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return (Cliente) gerenciadorEntidade.createNativeQuery("SELECT * FROM Cliente WHERE id_cliente = ?", Cliente.class)
                .setParameter(1, id)
                .getSingleResult();
    }

    @Override
    public Cliente buscarPorCpf(String cpf) {
        return (Cliente) gerenciadorEntidade.createNativeQuery("SELECT * FROM Cliente WHERE cpf = ?", Cliente.class)
                .setParameter(1, cpf)
                .getSingleResult();
    }

    @Override
    public List<Cliente> buscarTodos() {
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Cliente", Cliente.class)
                .getResultList();
    }
}
