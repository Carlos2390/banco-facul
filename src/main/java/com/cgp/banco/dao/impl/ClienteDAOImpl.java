package com.cgp.banco.dao.impl;

import com.cgp.banco.dao.LogDAO;
import com.cgp.banco.dao.ClienteDAO;
import com.cgp.banco.dao.ContaDAO;
import com.cgp.banco.dao.EnderecoDAO;
import com.cgp.banco.model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.json.JSONObject;
@Repository
public class ClienteDAOImpl implements ClienteDAO {
    private static final String TABLE_NAME = "Cliente";
    private Long currentUserId = null;

    @PersistenceContext
    private EntityManager gerenciadorEntidade;

    @Autowired
    private EnderecoDAO enderecoDAO;

    @Autowired
    private ContaDAO contaDAO;

    @Autowired
    private LogDAO logDAO;

    @Override
    public void setUserId(Long userId) {
        this.currentUserId = userId;
    }

    @Override
    public Long getUserId() {
        return this.currentUserId;
    }

    private Long getCurrentUserId() {
        if (this.currentUserId == null) {
            throw new IllegalStateException("O ID do usuário atual não foi definido.");
        }
        return this.currentUserId;
    }

    @Override
    @Transactional
    public void salvar(Cliente cliente) {
        try {
            // Cria uma consulta nativa para inserir um novo cliente no banco de dados
            gerenciadorEntidade.createNativeQuery("INSERT INTO Cliente (nome, cpf, data_nascimento, id_usuario) VALUES (?, ?, ?, ?)")
                    .setParameter(1, cliente.getNome()) // Define o primeiro parâmetro como o nome do cliente
                    .setParameter(2, cliente.getCpf()) // Define o segundo parâmetro como o CPF do cliente
                    .setParameter(3, cliente.getDataNascimento()) // Define o terceiro parâmetro como a data de nascimento do cliente
                    .setParameter(4, cliente.getIdUsuario()) // Define o terceiro parâmetro como a data de nascimento do cliente
                    .executeUpdate(); // Executa a consulta de atualização

            JSONObject newData = new JSONObject();
            newData.put("nome", cliente.getNome());
            newData.put("cpf", cliente.getCpf());
            newData.put("data_nascimento", cliente.getDataNascimento());
            newData.put("id_usuario", cliente.getIdUsuario());

            logDAO.salvarLog(LocalDateTime.now(), getCurrentUserId(), "INSERT", TABLE_NAME, null, "Cliente inserido", null, newData);
        } catch (Exception e) {
            logDAO.salvarLog(LocalDateTime.now(), getCurrentUserId(), "ERROR", TABLE_NAME, null, "Erro ao inserir cliente: " + e.getMessage(), null, null);
            throw e;
        }
    }

    @Override
    @Transactional
    public void atualizar(Cliente cliente) {
        try {
            Cliente clienteAntigo = buscarPorId(cliente.getId());
            JSONObject oldData = new JSONObject();
            oldData.put("nome", clienteAntigo.getNome());
            oldData.put("cpf", clienteAntigo.getCpf());
            oldData.put("data_nascimento", clienteAntigo.getDataNascimento());
            oldData.put("id_usuario", clienteAntigo.getIdUsuario());

            // Cria uma consulta nativa para atualizar um cliente existente no banco de dados
            gerenciadorEntidade.createNativeQuery("UPDATE Cliente SET nome = ?, cpf = ?, data_nascimento = ?, id_usuario = ? WHERE id_cliente = ?")
                    .setParameter(1, cliente.getNome()) // Define o primeiro parâmetro como o nome do cliente
                    .setParameter(2, cliente.getCpf()) // Define o segundo parâmetro como o CPF do cliente
                    .setParameter(3, cliente.getDataNascimento()) // Define o terceiro parâmetro como a data de nascimento do cliente
                    .setParameter(4, cliente.getIdUsuario()) // Define o terceiro parâmetro como o ID do usuário do cliente
                    .setParameter(5, cliente.getId()) // Define o quarto parâmetro como o ID do cliente
                    .executeUpdate(); // Executa a consulta de atualização

            JSONObject newData = new JSONObject();
            newData.put("nome", cliente.getNome());
            newData.put("cpf", cliente.getCpf());
            newData.put("data_nascimento", cliente.getDataNascimento());
            newData.put("id_usuario", cliente.getIdUsuario());

            logDAO.salvarLog(LocalDateTime.now(), getCurrentUserId(), "UPDATE", TABLE_NAME, cliente.getId(), "Cliente atualizado", oldData, newData);
        } catch (Exception e) {
            logDAO.salvarLog(LocalDateTime.now(), getCurrentUserId(), "ERROR", TABLE_NAME, cliente.getId(), "Erro ao atualizar cliente: " + e.getMessage(), null, null);
            throw e;
        }
    }

    @Override
    public Cliente buscarPorId(Long id) {
        try {
            // Cria uma consulta nativa para buscar um cliente pelo ID no banco de dados
            Cliente cliente = (Cliente) gerenciadorEntidade.createNativeQuery("SELECT * FROM Cliente WHERE id_cliente = ?", Cliente.class)
                    .setParameter(1, id) // Define o primeiro parâmetro como o ID do cliente
                    .getSingleResult(); // Executa a consulta e retorna um único resultado
            return cliente;
        } catch (Exception e) {
            logDAO.salvarLog(LocalDateTime.now(), getCurrentUserId(), "ERROR", TABLE_NAME, id, "Erro ao buscar cliente por ID: " + e.getMessage(), null, null);
            throw e;
        }
    }

    @Override
    public Cliente buscarPorCpf(String cpf) {
        try {
            // Cria uma consulta nativa para buscar um cliente pelo CPF no banco de dados
            Cliente cliente = (Cliente) gerenciadorEntidade.createNativeQuery("SELECT * FROM Cliente WHERE cpf = ?", Cliente.class)
                    .setParameter(1, cpf) // Define o primeiro parâmetro como o CPF do cliente
                    .getSingleResult(); // Executa a consulta e retorna um único resultado
            return cliente;
        } catch (Exception e) {
            logDAO.salvarLog(LocalDateTime.now(), getCurrentUserId(), "ERROR", TABLE_NAME, null, "Erro ao buscar cliente por CPF: " + e.getMessage(), null, null);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        try {
            Cliente clienteAntigo = buscarPorId(id);
            JSONObject oldData = new JSONObject();
            oldData.put("nome", clienteAntigo.getNome());
            oldData.put("cpf", clienteAntigo.getCpf());
            oldData.put("data_nascimento", clienteAntigo.getDataNascimento());
            oldData.put("id_usuario", clienteAntigo.getIdUsuario());

            // Cria uma consulta nativa para deletar um cliente pelo ID no banco de dados
            gerenciadorEntidade.createNativeQuery("DELETE FROM Cliente WHERE id_cliente = ?")
                    .setParameter(1, id) // Define o primeiro parâmetro como o ID do cliente
                    .executeUpdate(); // Executa a consulta de deleção

            // Deleta todas as contas associadas ao cliente pelo ID do cliente
            contaDAO.deletarContasPorIdCliente(id);
            // Deleta todos os endereços associados ao cliente pelo ID do cliente
            enderecoDAO.deletarEnderecosPorIdCliente(id);
            logDAO.salvarLog(LocalDateTime.now(), getCurrentUserId(), "DELETE", TABLE_NAME, id, "Cliente deletado", oldData, null);
        } catch (Exception e) {
            logDAO.salvarLog(LocalDateTime.now(), getCurrentUserId(), "ERROR", TABLE_NAME, id, "Erro ao deletar cliente: " + e.getMessage(), null, null);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deletarClientePorCpf(String cpf) {
        try {
            Cliente clienteAntigo = buscarPorCpf(cpf);
            JSONObject oldData = new JSONObject();
            oldData.put("nome", clienteAntigo.getNome());
            oldData.put("cpf", clienteAntigo.getCpf());
            oldData.put("data_nascimento", clienteAntigo.getDataNascimento());
            oldData.put("id_usuario", clienteAntigo.getIdUsuario());

            // Cria uma consulta nativa para deletar um cliente pelo CPF no banco de dados
            gerenciadorEntidade.createNativeQuery("DELETE FROM Cliente WHERE cpf = ?")
                    .setParameter(1, cpf) // Define o primeiro parâmetro como o CPF do cliente
                    .executeUpdate(); // Executa a consulta de deleção

            // Deleta todas as contas associadas ao cliente pelo CPF do cliente
            contaDAO.deletarContasPorCpfCliente(cpf);
            // Deleta todos os endereços associados ao cliente pelo CPF do cliente
            enderecoDAO.deletarEnderecosPorCpfCliente(cpf);
            logDAO.salvarLog(LocalDateTime.now(), getCurrentUserId(), "DELETE", TABLE_NAME, null, "Cliente deletado por CPF", oldData, null);
        } catch (Exception e) {
            logDAO.salvarLog(LocalDateTime.now(), getCurrentUserId(), "ERROR", TABLE_NAME, null, "Erro ao deletar cliente por CPF: " + e.getMessage(), null, null);
            throw e;
        }
    }

    @Override
    public List<Cliente> buscarTodos() {
        try {
            // Cria uma consulta nativa para buscar todos os clientes no banco de dados
            return gerenciadorEntidade.createNativeQuery("SELECT * FROM Cliente", Cliente.class)
                    .getResultList(); // Executa a consulta e retorna uma lista de resultados
        } catch (Exception e) {
            logDAO.salvarLog(LocalDateTime.now(), getCurrentUserId(), "ERROR", TABLE_NAME, null, "Erro ao buscar todos os clientes: " + e.getMessage(), null, null);
            throw e;
        }
    }
}