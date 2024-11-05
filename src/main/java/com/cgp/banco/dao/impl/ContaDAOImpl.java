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
        // Cria uma consulta nativa para inserir uma nova conta no banco de dados
        gerenciadorEntidade.createNativeQuery("INSERT INTO Conta (numero_conta, saldo, tipo, id_cliente) VALUES (?, ?, ?, ?)")
                .setParameter(1, conta.getNumeroConta()) // Define o primeiro parâmetro como o número da conta
                .setParameter(2, conta.getSaldo()) // Define o segundo parâmetro como o saldo da conta
                .setParameter(3, conta.getTipo()) // Define o terceiro parâmetro como o tipo da conta
                .setParameter(4, conta.getId_cliente()) // Define o quarto parâmetro como o ID do cliente
                .executeUpdate(); // Executa a consulta de atualização
    }

    @Override
    @Transactional
    public void atualizar(Conta conta) {
        // Cria uma consulta nativa para atualizar uma conta existente no banco de dados
        gerenciadorEntidade.createNativeQuery("UPDATE Conta SET numero_conta = ?, saldo = ?, tipo = ?, id_cliente = ? WHERE id_conta = ?")
                .setParameter(1, conta.getNumeroConta()) // Define o primeiro parâmetro como o número da conta
                .setParameter(2, conta.getSaldo()) // Define o segundo parâmetro como o saldo da conta
                .setParameter(3, conta.getTipo()) // Define o terceiro parâmetro como o tipo da conta
                .setParameter(4, conta.getId_cliente()) // Define o quarto parâmetro como o ID do cliente
                .setParameter(5, conta.getId()) // Define o quinto parâmetro como o ID da conta
                .executeUpdate(); // Executa a consulta de atualização
    }

    @Override
    public Conta buscarPorId(Long id) {
        // Cria uma consulta nativa para buscar uma conta pelo ID no banco de dados
        return (Conta) gerenciadorEntidade.createNativeQuery("SELECT * FROM Conta WHERE id_conta = ?", Conta.class)
                .setParameter(1, id) // Define o primeiro parâmetro como o ID da conta
                .getSingleResult(); // Executa a consulta e retorna um único resultado
    }

    @Override
    public List<Conta> buscarTodas() {
        // Cria uma consulta nativa para buscar todas as contas no banco de dados
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Conta", Conta.class)
                .getResultList(); // Executa a consulta e retorna uma lista de resultados
    }

    @Override
    public List<Conta> buscarContasPorCpfCliente(String cpf) {
        // Cria uma consulta nativa para buscar contas pelo CPF do cliente no banco de dados
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Conta WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE cpf = ?)", Conta.class)
                .setParameter(1, cpf) // Define o primeiro parâmetro como o CPF do cliente
                .getResultList(); // Executa a consulta e retorna uma lista de resultados
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        // Cria uma consulta nativa para deletar uma conta pelo ID no banco de dados
        gerenciadorEntidade.createNativeQuery("DELETE FROM Conta WHERE id_conta = ?")
                .setParameter(1, id) // Define o primeiro parâmetro como o ID da conta
                .executeUpdate(); // Executa a consulta de deleção
        // Deleta todas as transferências associadas à conta pelo ID da conta
        transferenciaDAO.deletarTransferenciasPorIdConta(id);
    }

    @Override
    @Transactional
    public void deletarContaPorNumero(Integer numeroConta) {
        // Cria uma consulta nativa para deletar uma conta pelo número da conta no banco de dados
        gerenciadorEntidade.createNativeQuery("DELETE FROM Conta WHERE numero_conta = ?")
                .setParameter(1, numeroConta) // Define o primeiro parâmetro como o número da conta
                .executeUpdate(); // Executa a consulta de deleção
        // Deleta todas as transferências associadas à conta pelo número da conta
        transferenciaDAO.deletarTransferenciasPorNumeroConta(numeroConta);
    }

    @Override
    @Transactional
    public void deletarContasPorCpfCliente(String cpf) {
        // Cria uma consulta nativa para deletar contas pelo CPF do cliente no banco de dados
        gerenciadorEntidade.createNativeQuery("DELETE FROM Conta WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE cpf = ?)")
                .setParameter(1, cpf) // Define o primeiro parâmetro como o CPF do cliente
                .executeUpdate(); // Executa a consulta de deleção
        // Deleta todas as transferências associadas às contas pelo CPF do cliente
        transferenciaDAO.deletarTransferenciasPorCpfCliente(cpf);
    }

    @Override
    @Transactional
    public void deletarContasPorIdCliente(Long id) {
        // Cria uma consulta nativa para deletar contas pelo ID do cliente no banco de dados
        gerenciadorEntidade.createNativeQuery("DELETE FROM Conta WHERE id_cliente = ?")
                .setParameter(1, id) // Define o primeiro parâmetro como o ID do cliente
                .executeUpdate(); // Executa a consulta de deleção
        // Deleta todas as transferências associadas às contas pelo ID do cliente
        transferenciaDAO.deletarTransferenciasPorIdCliente(id);
    }

    @Override
    public Conta buscarContaPorNumero(Integer numeroConta) {
        // Cria uma consulta nativa para buscar uma conta pelo número da conta no banco de dados
        return (Conta) gerenciadorEntidade.createNativeQuery("SELECT * FROM Conta WHERE numero_conta = ?", Conta.class)
                .setParameter(1, numeroConta) // Define o primeiro parâmetro como o número da conta
                .getSingleResult(); // Executa a consulta e retorna um único resultado
    }
}