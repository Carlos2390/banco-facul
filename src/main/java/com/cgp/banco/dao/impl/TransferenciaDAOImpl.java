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
        gerenciadorEntidade.createNativeQuery("INSERT INTO Transferencia (data, valor, id_conta_origem, id_conta_destino) VALUES (?, ?, ?, ?)")
                .setParameter(1, transferencia.getData())
                .setParameter(2, transferencia.getValor())
                .setParameter(3, transferencia.getId_conta_origem())
                .setParameter(4, transferencia.getId_conta_destino())
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
    public List<Transferencia> buscarTodas() {
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Transferencia", Transferencia.class)
                .getResultList();
    }
}
