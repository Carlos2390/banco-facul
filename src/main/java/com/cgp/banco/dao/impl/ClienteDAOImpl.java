package com.cgp.banco.dao.impl;

import com.cgp.banco.dao.ClienteDAO;
import com.cgp.banco.dao.ContaDAO;
import com.cgp.banco.dao.EnderecoDAO;
import com.cgp.banco.model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClienteDAOImpl implements ClienteDAO {

    @PersistenceContext
    private EntityManager gerenciadorEntidade;

    @Autowired
    private EnderecoDAO enderecoDAO;

    @Autowired
    private ContaDAO contaDAO;

    @Override
    @Transactional
    public void salvar(Cliente cliente) {
        // Cria uma consulta nativa para inserir um novo cliente no banco de dados
        gerenciadorEntidade.createNativeQuery("INSERT INTO Cliente (nome, cpf, data_nascimento) VALUES (?, ?, ?)")
                .setParameter(1, cliente.getNome()) // Define o primeiro parâmetro como o nome do cliente
                .setParameter(2, cliente.getCpf()) // Define o segundo parâmetro como o CPF do cliente
                .setParameter(3, cliente.getDataNascimento()) // Define o terceiro parâmetro como a data de nascimento do cliente
                .executeUpdate(); // Executa a consulta de atualização
    }

    @Override
    @Transactional
    public void atualizar(Cliente cliente) {
        // Cria uma consulta nativa para atualizar um cliente existente no banco de dados
        gerenciadorEntidade.createNativeQuery("UPDATE Cliente SET nome = ?, cpf = ?, data_nascimento = ? WHERE id_cliente = ?")
                .setParameter(1, cliente.getNome()) // Define o primeiro parâmetro como o nome do cliente
                .setParameter(2, cliente.getCpf()) // Define o segundo parâmetro como o CPF do cliente
                .setParameter(3, cliente.getDataNascimento()) // Define o terceiro parâmetro como a data de nascimento do cliente
                .setParameter(4, cliente.getId()) // Define o quarto parâmetro como o ID do cliente
                .executeUpdate(); // Executa a consulta de atualização
    }

    @Override
    public Cliente buscarPorId(Long id) {
        // Cria uma consulta nativa para buscar um cliente pelo ID no banco de dados
        return (Cliente) gerenciadorEntidade.createNativeQuery("SELECT * FROM Cliente WHERE id_cliente = ?", Cliente.class)
                .setParameter(1, id) // Define o primeiro parâmetro como o ID do cliente
                .getSingleResult(); // Executa a consulta e retorna um único resultado
    }

    @Override
    public Cliente buscarPorCpf(String cpf) {
        // Cria uma consulta nativa para buscar um cliente pelo CPF no banco de dados
        return (Cliente) gerenciadorEntidade.createNativeQuery("SELECT * FROM Cliente WHERE cpf = ?", Cliente.class)
                .setParameter(1, cpf) // Define o primeiro parâmetro como o CPF do cliente
                .getSingleResult(); // Executa a consulta e retorna um único resultado
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        // Cria uma consulta nativa para deletar um cliente pelo ID no banco de dados
        gerenciadorEntidade.createNativeQuery("DELETE FROM Cliente WHERE id_cliente = ?")
                .setParameter(1, id) // Define o primeiro parâmetro como o ID do cliente
                .executeUpdate(); // Executa a consulta de deleção

        // Deleta todas as contas associadas ao cliente pelo ID do cliente
        contaDAO.deletarContasPorIdCliente(id);
        // Deleta todos os endereços associados ao cliente pelo ID do cliente
        enderecoDAO.deletarEnderecosPorIdCliente(id);
    }

    @Override
    @Transactional
    public void deletarClientePorCpf(String cpf) {
        // Cria uma consulta nativa para deletar um cliente pelo CPF no banco de dados
        gerenciadorEntidade.createNativeQuery("DELETE FROM Cliente WHERE cpf = ?")
                .setParameter(1, cpf) // Define o primeiro parâmetro como o CPF do cliente
                .executeUpdate(); // Executa a consulta de deleção

        // Deleta todas as contas associadas ao cliente pelo CPF do cliente
        contaDAO.deletarContasPorCpfCliente(cpf);
        // Deleta todos os endereços associados ao cliente pelo CPF do cliente
        enderecoDAO.deletarEnderecosPorCpfCliente(cpf);
    }

    @Override
    public List<Cliente> buscarTodos() {
        // Cria uma consulta nativa para buscar todos os clientes no banco de dados
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Cliente", Cliente.class)
                .getResultList(); // Executa a consulta e retorna uma lista de resultados
    }
}