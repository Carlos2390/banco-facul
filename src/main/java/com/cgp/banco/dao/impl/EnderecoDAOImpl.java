package com.cgp.banco.dao.impl;

import com.cgp.banco.dao.EnderecoDAO;
import com.cgp.banco.model.Endereco;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnderecoDAOImpl implements EnderecoDAO {

    @PersistenceContext
    private EntityManager gerenciadorEntidade;

    @Override
    @Transactional
    public void salvar(Endereco endereco) {
        gerenciadorEntidade.createNativeQuery("INSERT INTO Endereco (rua, numero, cidade, estado, cep, id_cliente) VALUES (?, ?, ?, ?, ?, ?)")
                .setParameter(1, endereco.getRua())
                .setParameter(2, endereco.getNumero())
                .setParameter(3, endereco.getCidade())
                .setParameter(4, endereco.getEstado())
                .setParameter(5, endereco.getCep())
                .setParameter(6, endereco.getCliente().getId())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void atualizar(Endereco endereco) {
        gerenciadorEntidade.createNativeQuery("UPDATE Endereco SET rua = ?, numero = ?, cidade = ?, estado = ?, cep = ?, id_cliente = ? WHERE id_endereco = ?")
                .setParameter(1, endereco.getRua())
                .setParameter(2, endereco.getNumero())
                .setParameter(3, endereco.getCidade())
                .setParameter(4, endereco.getEstado())
                .setParameter(5, endereco.getCep())
                .setParameter(6, endereco.getCliente().getId())
                .setParameter(7, endereco.getId())
                .executeUpdate();
    }

    @Override
    public Endereco buscarPorId(Long id) {
        return (Endereco) gerenciadorEntidade.createNativeQuery("SELECT * FROM Endereco WHERE id_endereco = ?", Endereco.class)
                .setParameter(1, id)
                .getSingleResult();
    }

    @Override
    public List<Endereco> buscarTodos() {
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Endereco", Endereco.class)
                .getResultList();
    }
}
