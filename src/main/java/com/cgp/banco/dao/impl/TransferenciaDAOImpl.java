package com.cgp.banco.dao.impl;

import com.cgp.banco.dao.TransferenciaDAO;
import com.cgp.banco.model.Transferencia;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class TransferenciaDAOImpl implements TransferenciaDAO {

    @PersistenceContext
    private EntityManager gerenciadorEntidade;

    @Override
    @Transactional
    public void salvar(Transferencia transferencia) {
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
    }

    @Override
    @Transactional
    public void atualizar(Transferencia transferencia) {
        // Atualiza uma transferência existente no banco de dados
        gerenciadorEntidade.createNativeQuery("UPDATE Transferencia SET data = ?, valor = ?, id_conta_origem = ?, id_conta_destino = ? WHERE id_transferencia = ?")
                .setParameter(1, transferencia.getData()) // Define o primeiro parâmetro como a data da transferência
                .setParameter(2, transferencia.getValor()) // Define o segundo parâmetro como o valor da transferência
                .setParameter(3, transferencia.getId_conta_origem()) // Define o terceiro parâmetro como o ID da conta de origem
                .setParameter(4, transferencia.getId_conta_destino()) // Define o quarto parâmetro como o ID da conta de destino
                .setParameter(5, transferencia.getId()) // Define o quinto parâmetro como o ID da transferência
                .executeUpdate();
    }

    @Override
    public Transferencia buscarPorId(Long id) {
        // Busca uma transferência pelo ID no banco de dados
        return (Transferencia) gerenciadorEntidade.createNativeQuery("SELECT * FROM Transferencia WHERE id_transferencia = ?", Transferencia.class)
                .setParameter(1, id) // Define o primeiro parâmetro como o ID da transferência
                .getSingleResult();
    }

    @Override
    @Transactional
    public void deletar(Transferencia transferencia) {
        // Deleta uma transferência pelo ID no banco de dados
        gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_transferencia = ?")
                .setParameter(1, transferencia.getId()) // Define o primeiro parâmetro como o ID da transferência
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deletarTransferenciasPorIdConta(Long idConta) {
        // Deleta transferências associadas a uma conta pelo ID da conta
        gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_conta_origem = ? OR id_conta_destino = ?")
                .setParameter(1, idConta) // Define o primeiro parâmetro como o ID da conta de origem
                .setParameter(2, idConta) // Define o segundo parâmetro como o ID da conta de destino
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deletarTransferenciasPorNumeroConta(Integer numeroConta) {
        // Deleta transferências associadas a uma conta pelo número da conta
        gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_conta_origem IN (SELECT id_conta FROM Conta WHERE numero_conta = ?) OR id_conta_destino IN (SELECT id_conta FROM Conta WHERE numero_conta = ?)")
                .setParameter(1, numeroConta) // Define o primeiro parâmetro como o número da conta de origem
                .setParameter(2, numeroConta) // Define o segundo parâmetro como o número da conta de destino
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deletarTransferenciasPorCpfCliente(String cpf) {
        // Deleta transferências associadas a um cliente pelo CPF do cliente
        gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_conta_origem IN (SELECT id_conta FROM Conta WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE cpf = ?)) OR id_conta_destino IN (SELECT id_conta FROM Conta WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE cpf = ?))")
                .setParameter(1, cpf) // Define o primeiro parâmetro como o CPF do cliente
                .setParameter(2, cpf) // Define o segundo parâmetro como o CPF do cliente
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deletarTransferenciasPorIdCliente(Long id) {
        // Deleta transferências associadas a um cliente pelo ID do cliente
        gerenciadorEntidade.createNativeQuery("DELETE FROM Transferencia WHERE id_conta_origem IN (SELECT id_conta FROM Conta WHERE id_cliente = ?) OR id_conta_destino IN (SELECT id_conta FROM Conta WHERE id_cliente = ?)")
                .setParameter(1, id) // Define o primeiro parâmetro como o ID do cliente
                .setParameter(2, id) // Define o segundo parâmetro como o ID do cliente
                .executeUpdate();
    }

    @Override
    public List<Transferencia> buscarTodas() {
        // Busca todas as transferências no banco de dados
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Transferencia", Transferencia.class)
                .getResultList();
    }

    @Override
    public List<Transferencia> buscarTransferenciasPorNumeroContaOrigem(Integer numeroContaOrigem) {
        // Busca transferências pela conta de origem no banco de dados
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Transferencia WHERE id_conta_origem = ?", Transferencia.class)
                .setParameter(1, numeroContaOrigem) // Define o primeiro parâmetro como o número da conta de origem
                .getResultList();
    }

    @Override
    public List<Transferencia> buscarTransferenciasPorNumeroContaDestino(Integer numeroContaDestino) {
        // Busca transferências pela conta de destino no banco de dados
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Transferencia WHERE id_conta_destino = ?", Transferencia.class)
                .setParameter(1, numeroContaDestino) // Define o primeiro parâmetro como o número da conta de destino
                .getResultList();
    }
}