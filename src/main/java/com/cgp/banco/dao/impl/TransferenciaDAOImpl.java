package com.cgp.banco.dao.impl;

import com.cgp.banco.dao.TransferenciaDAO;
import com.cgp.banco.model.Transferencia;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TransferenciaDAOImpl extends GenericDAOImpl<Transferencia> implements TransferenciaDAO {

    @PersistenceContext
    private EntityManager gerenciadorEntidade;

    @Override
    @Transactional
    public void salvar(Transferencia transferencia) {
        EntityTransaction transaction = gerenciadorEntidade.getTransaction();
        try {
            transaction.begin();
            // Verifica se há saldo na conta de origem
            BigDecimal saldo = (BigDecimal) gerenciadorEntidade.createNativeQuery("SELECT saldo FROM Conta WHERE id_conta = ?")
                    .setParameter(1, transferencia.getId_conta_origem()) // Define o primeiro parâmetro como o ID da conta de origem
                    .getSingleResult();
            if (saldo.doubleValue() < transferencia.getValor()) { // Verifica se há saldo suficiente na conta de origem
                throw new RuntimeException("Saldo insuficiente.");
            }
            // Insere a transferência no banco de dados
            gerenciadorEntidade.createNativeQuery("INSERT INTO Transferencia (data, valor, id_conta_origem, id_conta_destino) VALUES (?, ?, ?, ?)")
                    .setParameter(1, transferencia.getData()) // Define o primeiro parâmetro como a data da transferência
                    .setParameter(2, transferencia.getValor()) // Define o segundo parâmetro como o valor da transferência
                    .setParameter(3, transferencia.getId_conta_origem()) // Define o terceiro parâmetro como o ID da conta de origem
                    .setParameter(4, transferencia.getId_conta_destino()) // Define o quarto parâmetro como o ID da conta de destino
                    .executeUpdate();

            // Atualiza o saldo das contas envolvidas
            gerenciadorEntidade.createNativeQuery("UPDATE Conta SET saldo = saldo - ? WHERE id_conta = ?")
                    .setParameter(1, transferencia.getValor()) // Define o primeiro parâmetro como o valor da transferência
                    .setParameter(2, transferencia.getId_conta_origem()) // Define o segundo parâmetro como o ID da conta de origem
                    .executeUpdate();

            gerenciadorEntidade.createNativeQuery("UPDATE Conta SET saldo = saldo + ? WHERE id_conta = ?")
                    .setParameter(1, transferencia.getValor()) // Define o primeiro parâmetro como o valor da transferência
                    .setParameter(2, transferencia.getId_conta_destino()) // Define o segundo parâmetro como o ID da conta de destino
                    .executeUpdate();
            inserirLog(getUserId(), "INSERT", "Transferencia", null, null, new JSONObject(transferencia).toString(), String.format("Transferência inserida para a conta %d.", transferencia.getId_conta_destino()));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            inserirLog(getUserId(), "ERROR", "Transferencia", null, null, null, String.format("Erro ao inserir Transferência para a conta %d. Motivo: %s", transferencia.getId_conta_destino(), e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void atualizar(Transferencia transferencia) {
        EntityTransaction transaction = gerenciadorEntidade.getTransaction();
        Optional<Transferencia> transferenciaAntes = Optional.ofNullable(buscarPorId(transferencia.getId()));
        try {
            transaction.begin();
            // Atualiza uma transferência existente no banco de dados
            gerenciadorEntidade.createNativeQuery("UPDATE Transferencia SET data = ?, valor = ?, id_conta_origem = ?, id_conta_destino = ? WHERE id_transferencia = ?")
                    .setParameter(1, transferencia.getData()) // Define o primeiro parâmetro como a data da transferência
                    .setParameter(2, transferencia.getValor()) // Define o segundo parâmetro como o valor da transferência
                    .setParameter(3, transferencia.getId_conta_origem()) // Define o terceiro parâmetro como o ID da conta de origem
                    .setParameter(4, transferencia.getId_conta_destino()) // Define o quarto parâmetro como o ID da conta de destino
                    .setParameter(5, transferencia.getId()) // Define o quinto parâmetro como o ID da transferência
                    .executeUpdate();
            transferenciaAntes.ifPresent(transferencia1 -> inserirLog(getUserId(), "UPDATE", "Transferencia", transferencia.getId(), new JSONObject(transferencia1).toString(), new JSONObject(transferencia).toString(), String.format("Transferência %d atualizada.", transferencia.getId())));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            inserirLog(getUserId(), "ERROR", "Transferencia", transferencia.getId(), null, null, String.format("Erro ao atualizar transferência %d. Motivo: %s", transferencia.getId(), e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transferencia buscarPorId(Long id) {
        try {
            // Busca uma transferência pelo ID no banco de dados
            return (Transferencia) gerenciadorEntidade.createNativeQuery("SELECT * FROM Transferencia WHERE id_transferencia = ?", Transferencia.class)
                    .setParameter(1, id) // Define o primeiro parâmetro como o ID da transferência
                    .getSingleResult();
        } catch (Exception e) {
            inserirLog(getUserId(), "ERROR", "Transferencia", id, null, null, String.format("Erro ao buscar transferência pelo ID %d. Motivo: %s", id, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deletar(Long id_transferencia) {
        EntityTransaction transaction = gerenciadorEntidade.getTransaction();
        Optional<Transferencia> transferenciaAntes = Optional.ofNullable(buscarPorId(id_transferencia));
        try {
            transaction.begin();
            // Deleta uma transferência pelo ID no banco de dados
            gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_transferencia = ?")
                    .setParameter(1, id_transferencia) // Define o primeiro parâmetro como o ID da transferência
                    .executeUpdate();
            transferenciaAntes.ifPresent(transferencia -> inserirLog(getUserId(), "DELETE", "Transferencia", id_transferencia, new JSONObject(transferencia).toString(), null, String.format("Transferência %d deletada.", id_transferencia)));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            inserirLog(getUserId(), "ERROR", "Transferencia", id_transferencia, null, null, String.format("Erro ao deletar transferência %d. Motivo: %s", id_transferencia, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deletarTransferenciasPorIdConta(Long idConta) {
        EntityTransaction transaction = gerenciadorEntidade.getTransaction();
        try {
            transaction.begin();
            // Deleta transferências associadas a uma conta pelo ID da conta
            gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_conta_origem = ? OR id_conta_destino = ?")
                    .setParameter(1, idConta) // Define o primeiro parâmetro como o ID da conta de origem
                    .setParameter(2, idConta) // Define o segundo parâmetro como o ID da conta de destino
                    .executeUpdate();
            inserirLog(getUserId(), "DELETE", "Transferencia", null, null, null, String.format("Transferências deletadas para a conta %d.", idConta));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            inserirLog(getUserId(), "ERROR", "Transferencia", null, null, null, String.format("Erro ao deletar transferências para a conta %d. Motivo: %s", idConta, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deletarTransferenciasPorNumeroConta(Integer numeroConta) {
        EntityTransaction transaction = gerenciadorEntidade.getTransaction();
        try {
            transaction.begin();
            // Deleta transferências associadas a uma conta pelo número da conta
            gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_conta_origem IN (SELECT id_conta FROM Conta WHERE numero_conta = ?) OR id_conta_destino IN (SELECT id_conta FROM Conta WHERE numero_conta = ?)")
                    .setParameter(1, numeroConta) // Define o primeiro parâmetro como o número da conta de origem
                    .setParameter(2, numeroConta) // Define o segundo parâmetro como o número da conta de destino
                    .executeUpdate();
            inserirLog(getUserId(), "DELETE", "Transferencia", null, null, null, String.format("Transferências deletadas para a conta de número %d.", numeroConta));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            inserirLog(getUserId(), "ERROR", "Transferencia", null, null, null, String.format("Erro ao deletar transferências para a conta de número %d. Motivo: %s", numeroConta, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deletarTransferenciasPorCpfCliente(String cpf) {
        EntityTransaction transaction = gerenciadorEntidade.getTransaction();
        try {
            transaction.begin();
            // Deleta transferências associadas a um cliente pelo CPF do cliente
            gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_conta_origem IN (SELECT id_conta FROM Conta WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE cpf = ?)) OR id_conta_destino IN (SELECT id_conta FROM Conta WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE cpf = ?))")
                    .setParameter(1, cpf) // Define o primeiro parâmetro como o CPF do cliente
                    .setParameter(2, cpf) // Define o segundo parâmetro como o CPF do cliente
                    .executeUpdate();
            inserirLog(getUserId(), "DELETE", "Transferencia", null, null, null, String.format("Transferências deletadas para o cliente de CPF %s.", cpf));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            inserirLog(getUserId(), "ERROR", "Transferencia", null, null, null, String.format("Erro ao deletar transferências para o cliente de CPF %s. Motivo: %s", cpf, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deletarTransferenciasPorIdCliente(Long id) {
        EntityTransaction transaction = gerenciadorEntidade.getTransaction();
        try {
            transaction.begin();
            // Deleta transferências associadas a um cliente pelo ID do cliente
            gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_conta_origem IN (SELECT id_conta FROM Conta WHERE id_cliente = ?) OR id_conta_destino IN (SELECT id_conta FROM Conta WHERE id_cliente = ?)")
                    .setParameter(1, id) // Define o primeiro parâmetro como o ID do cliente
                    .setParameter(2, id) // Define o segundo parâmetro como o ID do cliente
                    .executeUpdate();
            inserirLog(getUserId(), "DELETE", "Transferencia", null, null, null, String.format("Transferências deletadas para o cliente de ID %d.", id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            inserirLog(getUserId(), "ERROR", "Transferencia", null, null, null, String.format("Erro ao deletar transferências para o cliente de ID %d. Motivo: %s", id, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transferencia> buscarTodos() {
        try {
            // Busca todas as transferências no banco de dados
            return gerenciadorEntidade.createNativeQuery("SELECT * FROM Transferencia", Transferencia.class)
                    .getResultList();
        } catch (Exception e) {
            inserirLog(getUserId(), "ERROR", "Transferencia", null, null, null, String.format("Erro ao buscar todas transferências. Motivo: %s", e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transferencia> buscarTransferenciasPorNumeroContaOrigem(Integer numeroContaOrigem) {
        try {
            // Busca transferências pela conta de origem no banco de dados
            return gerenciadorEntidade.createNativeQuery("SELECT * FROM Transferencia WHERE id_conta_origem = ?", Transferencia.class)
                    .setParameter(1, numeroContaOrigem) // Define o primeiro parâmetro como o número da conta de origem
                    .getResultList();
        } catch (Exception e) {
            inserirLog(getUserId(), "ERROR", "Transferencia", null, null, null, String.format("Erro ao buscar transferências pela conta de origem %d. Motivo: %s", numeroContaOrigem, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transferencia> buscarTransferenciasPorNumeroContaDestino(Integer numeroContaDestino) {
        try {
            // Busca transferências pela conta de destino no banco de dados
            return gerenciadorEntidade.createNativeQuery("SELECT * FROM Transferencia WHERE id_conta_destino = ?", Transferencia.class)
                    .setParameter(1, numeroContaDestino) // Define o primeiro parâmetro como o número da conta de destino
                    .getResultList();
        } catch (Exception e) {
            inserirLog(getUserId(), "ERROR", "Transferencia", null, null, null, String.format("Erro ao buscar transferências pela conta de destino %d. Motivo: %s", numeroContaDestino, e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    public void inserirLog(Long userId, String acao, String tabela, Long registroId, String dadosAntigos, String dadosNovos, String descricao) {
        try {
            gerenciadorEntidade.createNativeQuery("INSERT INTO Log (data_hora, id_usuario, acao, tabela_afetada, id_registro_afetado, descricao_mudanca, dados_antigos, dados_novos) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
                    .setParameter(1, LocalDateTime.now())
                    .setParameter(2, userId)
                    .setParameter(3, acao)
                    .setParameter(4, tabela)
                    .setParameter(5, registroId)
                    .setParameter(6, descricao)
                    .setParameter(7, dadosAntigos)
                    .setParameter(8, dadosNovos)
                    .executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long getUserId() {
       return super.getCurrentUserId();

    }
    public TransferenciaDAOImpl(){super(Transferencia.class);}
}