package com.cgp.banco.dao.impl;

import com.cgp.banco.dao.ContaDAO;
import com.cgp.banco.dao.TransferenciaDAO;
import com.cgp.banco.model.Conta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContaDAOImpl implements ContaDAO {

    @PersistenceContext
    private EntityManager gerenciadorEntidade;

    @Autowired
    private TransferenciaDAO transferenciaDAO;

    @Override
    @Transactional
    public void salvar(Conta conta) {
        gerenciadorEntidade.createNativeQuery("INSERT INTO Conta (numero_conta, saldo, tipo, id_cliente) VALUES (?, ?, ?, ?)")
                .setParameter(1, conta.getNumeroConta())
                .setParameter(2, conta.getSaldo())
                .setParameter(3, conta.getTipo())
                .setParameter(4, conta.getId_cliente())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void atualizar(Conta conta) {
        gerenciadorEntidade.createNativeQuery("UPDATE Conta SET numero_conta = ?, saldo = ?, tipo = ?, id_cliente = ? WHERE id_conta = ?")
                .setParameter(1, conta.getNumeroConta())
                .setParameter(2, conta.getSaldo())
                .setParameter(3, conta.getTipo())
                .setParameter(4, conta.getId_cliente())
                .setParameter(5, conta.getId())
                .executeUpdate();
    }

    @Override
    public Conta buscarPorId(Long id) {
        return (Conta) gerenciadorEntidade.createNativeQuery("SELECT * FROM Conta WHERE id_conta = ?", Conta.class)
                .setParameter(1, id)
                .getSingleResult();
    }

    @Override
    public List<Conta> buscarTodas() {
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Conta", Conta.class)
                .getResultList();
    }

    @Override
    public List<Conta> buscarContasPorCpfCliente(String cpf) {
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Conta WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE cpf = ?)", Conta.class)
                .setParameter(1, cpf)
                .getResultList();
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        gerenciadorEntidade.createNativeQuery("DELETE FROM Conta WHERE id_conta = ?")
                .setParameter(1, id)
                .executeUpdate();
        transferenciaDAO.deletarTransferenciasPorIdConta(id);
    }

    @Override
    @Transactional
    public void deletarContaPorNumero(Integer numeroConta) {
        gerenciadorEntidade.createNativeQuery("DELETE FROM Conta WHERE numero_conta = ?")
                .setParameter(1, numeroConta)
                .executeUpdate();
        transferenciaDAO.deletarTransferenciasPorNumeroConta(numeroConta);
    }

    @Override
    @Transactional
    public void deletarContasPorCpfCliente(String cpf) {
        gerenciadorEntidade.createNativeQuery("DELETE FROM Conta WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE cpf = ?)")
                .setParameter(1, cpf)
                .executeUpdate();
        transferenciaDAO.deletarTransferenciasPorCpfCliente(cpf);
    }

    @Override
    @Transactional
    public void deletarContasPorIdCliente(Long id) {
        gerenciadorEntidade.createNativeQuery("DELETE FROM Conta WHERE id_cliente = ?")
                .setParameter(1, id)
                .executeUpdate();
        transferenciaDAO.deletarTransferenciasPorIdCliente(id);
    }

    @Override
    public Conta buscarContaPorNumero(Integer numeroConta) {
        return (Conta) gerenciadorEntidade.createNativeQuery("SELECT * FROM Conta WHERE numero_conta = ?", Conta.class)
                .setParameter(1, numeroConta)
                .getSingleResult();
    }
}
