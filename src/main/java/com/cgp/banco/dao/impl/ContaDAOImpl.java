package com.cgp.banco.dao.impl;

import com.cgp.banco.dao.ContaDAO;
import com.cgp.banco.model.Conta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContaDAOImpl implements ContaDAO {

    @PersistenceContext
    private EntityManager gerenciadorEntidade;

    @Override
    @Transactional
    public void salvar(Conta conta) {
        gerenciadorEntidade.createNativeQuery("INSERT INTO Conta (numero_conta, saldo, tipo, id_cliente) VALUES (?, ?, ?, ?)")
                .setParameter(1, conta.getNumeroConta())
                .setParameter(2, conta.getSaldo())
                .setParameter(3, conta.getTipo())
                .setParameter(4, conta.getCliente().getId())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void atualizar(Conta conta) {
        gerenciadorEntidade.createNativeQuery("UPDATE Conta SET numero_conta = ?, saldo = ?, tipo = ?, id_cliente = ? WHERE id = ?")
                .setParameter(1, conta.getNumeroConta())
                .setParameter(2, conta.getSaldo())
                .setParameter(3, conta.getTipo())
                .setParameter(4, conta.getCliente().getId())
                .setParameter(5, conta.getId())
                .executeUpdate();
    }

    @Override
    public Conta buscarPorId(Long id) {
        return (Conta) gerenciadorEntidade.createNativeQuery("SELECT * FROM Conta WHERE id = ?", Conta.class)
                .setParameter(1, id)
                .getSingleResult();
    }

    @Override
    public List<Conta> buscarTodas() {
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Conta", Conta.class)
                .getResultList();
    }
}
