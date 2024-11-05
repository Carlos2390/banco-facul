package com.cgp.banco.dao.impl;

import com.cgp.banco.dao.TransferenciaDAO;
import com.cgp.banco.model.Transferencia;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransferenciaDAOImpl implements TransferenciaDAO {

    @PersistenceContext
    private EntityManager gerenciadorEntidade;

    @Override
    @Transactional
    public void salvar(Transferencia transferencia) {
        //verificar se ha saldo na conta de origem
        Double saldo = (Double) gerenciadorEntidade.createNativeQuery("SELECT saldo FROM Conta WHERE id_conta = ?")
                .setParameter(1, transferencia.getId_conta_origem())
                .getSingleResult();
        if (saldo < transferencia.getValor()) {
            throw new RuntimeException("Saldo insuficiente.");
        }
        gerenciadorEntidade.createNativeQuery("INSERT INTO Transferencia (data, valor, id_conta_origem, id_conta_destino) VALUES (?, ?, ?, ?)")
                .setParameter(1, transferencia.getData())
                .setParameter(2, transferencia.getValor())
                .setParameter(3, transferencia.getId_conta_origem())
                .setParameter(4, transferencia.getId_conta_destino())
                .executeUpdate();

        //Ao salvar a transferência, é necessário atualizar o saldo das contas envolvidas
        gerenciadorEntidade.createNativeQuery("UPDATE Conta SET saldo = saldo - ? WHERE id_conta = ?")
                .setParameter(1, transferencia.getValor())
                .setParameter(2, transferencia.getId_conta_origem())
                .executeUpdate();

        gerenciadorEntidade.createNativeQuery("UPDATE Conta SET saldo = saldo + ? WHERE id_conta = ?")
                .setParameter(1, transferencia.getValor())
                .setParameter(2, transferencia.getId_conta_destino())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void atualizar(Transferencia transferencia) {
        gerenciadorEntidade.createNativeQuery("UPDATE Transferencia SET data = ?, valor = ?, id_conta_origem = ?, id_conta_destino = ? WHERE id_transferencia = ?")
                .setParameter(1, transferencia.getData())
                .setParameter(2, transferencia.getValor())
                .setParameter(3, transferencia.getId_conta_origem())
                .setParameter(4, transferencia.getId_conta_destino())
                .setParameter(5, transferencia.getId())
                .executeUpdate();
    }

    @Override
    public Transferencia buscarPorId(Long id) {
        return (Transferencia) gerenciadorEntidade.createNativeQuery("SELECT * FROM Transferencia WHERE id_transferencia = ?", Transferencia.class)
                .setParameter(1, id)
                .getSingleResult();
    }

    @Override
    @Transactional
    public void deletar(Transferencia transferencia) {
        gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_transferencia = ?")
                .setParameter(1, transferencia.getId())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deletarTransferenciasPorIdConta(Long idConta) {
        gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_conta_origem = ? OR id_conta_destino = ?")
                .setParameter(1, idConta)
                .setParameter(2, idConta)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deletarTransferenciasPorNumeroConta(Integer numeroConta) {
        gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_conta_origem IN (SELECT id_conta FROM Conta WHERE numero_conta = ?) OR id_conta_destino IN (SELECT id_conta FROM Conta WHERE numero_conta = ?)")
                .setParameter(1, numeroConta)
                .setParameter(2, numeroConta)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deletarTransferenciasPorCpfCliente(String cpf) {
        gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_conta_origem IN (SELECT id_conta FROM Conta WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE cpf = ?)) OR id_conta_destino IN (SELECT id_conta FROM Conta WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE cpf = ?))")
                .setParameter(1, cpf)
                .setParameter(2, cpf)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deletarTransferenciasPorIdCliente(Long id) {
        gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_conta_origem IN (SELECT id_conta FROM Conta WHERE id_cliente = ?) OR id_conta_destino IN (SELECT id_conta FROM Conta WHERE id_cliente = ?)")
                .setParameter(1, id)
                .setParameter(2, id)
                .executeUpdate();
    }

    @Override
    public List<Transferencia> buscarTodas() {
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Transferencia", Transferencia.class)
                .getResultList();
    }

    @Override
    public List<Transferencia> buscarTransferenciasPorNumeroContaOrigem(Integer numeroContaOrigem) {
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Transferencia WHERE id_conta_origem = ?", Transferencia.class)
                .setParameter(1, numeroContaOrigem)
                .getResultList();
    }

    @Override
    public List<Transferencia> buscarTransferenciasPorNumeroContaDestino(Integer numeroContaDestino) {
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Transferencia WHERE id_conta_destino = ?", Transferencia.class)
                .setParameter(1, numeroContaDestino)
                .getResultList();
    }
}
