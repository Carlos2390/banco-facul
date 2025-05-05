package com.cgp.banco.dao.impl;

import com.cgp.banco.dao.LogDAO;
import com.cgp.banco.dao.EnderecoDAO;
import com.cgp.banco.model.Endereco;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EnderecoDAOImpl extends GenericDAOImpl<Endereco> implements EnderecoDAO {

    @PersistenceContext
    private EntityManager gerenciadorEntidade;
    private LogDAO logDAO;

    public EnderecoDAOImpl(LogDAO logDAO) {
        super(Endereco.class);
        this.gerenciadorEntidade = gerenciadorEntidade;
        super.setGerenciadorEntidade(gerenciadorEntidade);
        this.logDAO = logDAO;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    private Long getCurrentUserId() {
        if (currentUserId == null) {
            throw new RuntimeException("User ID not set in session.");
        }
        return currentUserId;
    }

    @Override
    @Transactional
    public void salvar(Endereco endereco){
        try {
            gerenciadorEntidade.createNativeQuery("INSERT INTO Endereco (rua, numero, cidade, estado, cep, id_cliente) VALUES (?, ?, ?, ?, ?, ?)")
                    .setParameter(1, endereco.getRua())
                    .setParameter(2, endereco.getNumero())
                    .setParameter(3, endereco.getCidade())
                    .setParameter(4, endereco.getEstado())
                    .setParameter(5, endereco.getCep())
                    .setParameter(6, endereco.getId_cliente())
                    .executeUpdate();
            logDAO.registrarLog(getCurrentUserId(), "INSERT", "Endereco", null, null, endereco);
        } catch (Exception e) {
            logDAO.registrarLog(getCurrentUserId(), "ERROR", "Endereco", null, null, e.getMessage());
            throw new RuntimeException("Erro ao salvar Endereco", e);
        }
    }

    @Override
    @Transactional
    public void atualizar(Endereco endereco) {
        try {
            Endereco enderecoOld = buscarPorId(endereco.getId());
            gerenciadorEntidade.createNativeQuery("UPDATE Endereco SET rua = ?, numero = ?, cidade = ?, estado = ?, cep = ?, id_cliente = ? WHERE id_endereco = ?")
                    .setParameter(1, endereco.getRua())
                    .setParameter(2, endereco.getNumero())
                    .setParameter(3, endereco.getCidade())
                    .setParameter(4, endereco.getEstado())
                    .setParameter(5, endereco.getCep())
                    .setParameter(6, endereco.getId_cliente())
                    .setParameter(7, endereco.getId())
                    .executeUpdate();

            logDAO.registrarLog(getCurrentUserId(), "UPDATE", "Endereco", endereco.getId(), enderecoOld, endereco);
        } catch (Exception e) {
            logDAO.registrarLog(getCurrentUserId(), "ERROR", "Endereco", null, null, e.getMessage());
            throw new RuntimeException("Erro ao atualizar Endereco", e);
        }
    }

    @Override
    public Endereco buscarPorId(Long id) {
        try {
            return (Endereco) gerenciadorEntidade.createNativeQuery("SELECT * FROM Endereco WHERE id_endereco = ?", Endereco.class)
                    .setParameter(1, id)
                    .getSingleResult();
        } catch (Exception e) {
            logDAO.registrarLog(getCurrentUserId(), "ERROR", "Endereco", id, null, e.getMessage());
            throw new RuntimeException("Erro ao buscar Endereco", e);
        }
    }

    @Override
    @Transactional
    public void deletar(Long id_endereco) {
        try {
            Endereco enderecoOld = buscarPorId(id_endereco);
            gerenciadorEntidade.createNativeQuery("DELETE FROM Endereco WHERE id_endereco = ?")
                    .setParameter(1, id_endereco)
                    .executeUpdate();
            logDAO.registrarLog(getCurrentUserId(), "DELETE", "Endereco", id_endereco, enderecoOld, null);
        } catch (Exception e) {
            logDAO.registrarLog(getCurrentUserId(), "ERROR", "Endereco", id_endereco, null, e.getMessage());
            throw new RuntimeException("Erro ao deletar Endereco", e);
        }
    }

    @Override
    @Transactional
    public void deletarEnderecosPorCpfCliente(String cpf) {
        try {
            List<Endereco> enderecos = buscarEnderecosPorCpfCliente(cpf);
            gerenciadorEntidade.createNativeQuery("DELETE FROM Endereco WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE cpf = ?)")
                    .setParameter(1, cpf)
                    .executeUpdate();
            logDAO.registrarLog(getCurrentUserId(), "DELETE", "Endereco", null, enderecos, null);
        } catch (Exception e) {
            logDAO.registrarLog(getCurrentUserId(), "ERROR", "Endereco", null, null, e.getMessage());
            throw new RuntimeException("Erro ao deletar enderecos por CPF", e);
        }
    }

    @Override
    @Transactional
    public void deletarEnderecosPorIdCliente(Long id) {
        try {
            List<Endereco> enderecos = buscarEnderecosPorIdCliente(id);
            gerenciadorEntidade.createNativeQuery("DELETE FROM Endereco WHERE id_cliente = ?")
                    .setParameter(1, id)
                    .executeUpdate();
            logDAO.registrarLog(getCurrentUserId(), "DELETE", "Endereco", id, enderecos, null);
        } catch (Exception e) {
            logDAO.registrarLog(getCurrentUserId(), "ERROR", "Endereco", id, null, e.getMessage());
            throw new RuntimeException("Erro ao deletar enderecos por ID do cliente", e);
        }
    }

    @Override
    public List<Endereco> buscarTodos() {
        try {
            return gerenciadorEntidade.createNativeQuery("SELECT * FROM Endereco", Endereco.class)
                    .getResultList();
        } catch (Exception e) {
            logDAO.registrarLog(getCurrentUserId(), "ERROR", "Endereco", null, null, e.getMessage());
            throw new RuntimeException("Erro ao buscar todos os enderecos", e);
        }
    }

    @Override
    public List<Endereco> buscarEnderecosPorCpfCliente(String cpf) {
        try {
            return gerenciadorEntidade.createNativeQuery("SELECT * FROM Endereco WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE cpf = ?)", Endereco.class)
                    .setParameter(1, cpf)
                    .getResultList();
        } catch (Exception e) {
            logDAO.registrarLog(getCurrentUserId(), "ERROR", "Endereco", null, null, e.getMessage());
            throw new RuntimeException("Erro ao buscar enderecos por CPF do cliente", e);
        }
    }
    public List<Endereco> buscarEnderecosPorIdCliente(Long id) {
        try {
            return gerenciadorEntidade.createNativeQuery("SELECT * FROM Endereco WHERE id_cliente = ?", Endereco.class)
                    .setParameter(1, id)
                    .getResultList();
        } catch (Exception e) {
            logDAO.registrarLog(getCurrentUserId(), "ERROR", "Endereco", null, null, e.getMessage());
            throw new RuntimeException("Erro ao buscar enderecos por ID do cliente", e);
        }
    }
}
