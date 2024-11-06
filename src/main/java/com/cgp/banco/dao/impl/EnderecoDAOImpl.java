package com.cgp.banco.dao.impl;

import com.cgp.banco.dao.EnderecoDAO;
import com.cgp.banco.model.Endereco;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnderecoDAOImpl implements EnderecoDAO {

    @PersistenceContext
    private EntityManager gerenciadorEntidade;

    @Override
    @Transactional
    public void salvar(Endereco endereco) {
        // Cria uma consulta nativa para inserir um novo endereço no banco de dados
        gerenciadorEntidade.createNativeQuery("INSERT INTO Endereco (rua, numero, cidade, estado, cep, id_cliente) VALUES (?, ?, ?, ?, ?, ?)")
                .setParameter(1, endereco.getRua()) // Define o primeiro parâmetro como a rua do endereço
                .setParameter(2, endereco.getNumero()) // Define o segundo parâmetro como o número do endereço
                .setParameter(3, endereco.getCidade()) // Define o terceiro parâmetro como a cidade do endereço
                .setParameter(4, endereco.getEstado()) // Define o quarto parâmetro como o estado do endereço
                .setParameter(5, endereco.getCep()) // Define o quinto parâmetro como o CEP do endereço
                .setParameter(6, endereco.getId_cliente()) // Define o sexto parâmetro como o ID do cliente
                .executeUpdate(); // Executa a consulta de atualização
    }

    @Override
    @Transactional
    public void atualizar(Endereco endereco) {
        // Cria uma consulta nativa para atualizar um endereço existente no banco de dados
        gerenciadorEntidade.createNativeQuery("UPDATE Endereco SET rua = ?, numero = ?, cidade = ?, estado = ?, cep = ?, id_cliente = ? WHERE id_endereco = ?")
                .setParameter(1, endereco.getRua()) // Define o primeiro parâmetro como a rua do endereço
                .setParameter(2, endereco.getNumero()) // Define o segundo parâmetro como o número do endereço
                .setParameter(3, endereco.getCidade()) // Define o terceiro parâmetro como a cidade do endereço
                .setParameter(4, endereco.getEstado()) // Define o quarto parâmetro como o estado do endereço
                .setParameter(5, endereco.getCep()) // Define o quinto parâmetro como o CEP do endereço
                .setParameter(6, endereco.getId_cliente()) // Define o sexto parâmetro como o ID do cliente
                .setParameter(7, endereco.getId()) // Define o sétimo parâmetro como o ID do endereço
                .executeUpdate(); // Executa a consulta de atualização
    }

    @Override
    public Endereco buscarPorId(Long id) {
        // Cria uma consulta nativa para buscar um endereço pelo ID no banco de dados
        return (Endereco) gerenciadorEntidade.createNativeQuery("SELECT * FROM Endereco WHERE id_endereco = ?", Endereco.class)
                .setParameter(1, id) // Define o primeiro parâmetro como o ID do endereço
                .getSingleResult(); // Executa a consulta e retorna um único resultado
    }

    @Override
    @Transactional
    public void deletar(Long id_endereco) {
        // Cria uma consulta nativa para deletar um endereço pelo ID no banco de dados
        gerenciadorEntidade.createNativeQuery("DELETE FROM Endereco WHERE id_endereco = ?")
                .setParameter(1, id_endereco) // Define o primeiro parâmetro como o ID do endereço
                .executeUpdate(); // Executa a consulta de deleção
    }

    @Override
    @Transactional
    public void deletarEnderecosPorCpfCliente(String cpf) {
        // Cria uma consulta nativa para deletar endereços pelo CPF do cliente no banco de dados
        gerenciadorEntidade.createNativeQuery("DELETE FROM Endereco WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE cpf = ?)")
                .setParameter(1, cpf) // Define o primeiro parâmetro como o CPF do cliente
                .executeUpdate(); // Executa a consulta de deleção
    }

    @Override
    @Transactional
    public void deletarEnderecosPorIdCliente(Long id) {
        // Cria uma consulta nativa para deletar endereços pelo ID do cliente no banco de dados
        gerenciadorEntidade.createNativeQuery("DELETE FROM Endereco WHERE id_cliente = ?")
                .setParameter(1, id) // Define o primeiro parâmetro como o ID do cliente
                .executeUpdate(); // Executa a consulta de deleção
    }

    @Override
    public List<Endereco> buscarTodos() {
        // Cria uma consulta nativa para buscar todos os endereços no banco de dados
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Endereco", Endereco.class)
                .getResultList(); // Executa a consulta e retorna uma lista de resultados
    }

    @Override
    public List<Endereco> buscarEnderecosPorCpfCliente(String cpf) {
        // Cria uma consulta nativa para buscar endereços pelo CPF do cliente no banco de dados
        return gerenciadorEntidade.createNativeQuery("SELECT * FROM Endereco WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE cpf = ?)", Endereco.class)
                .setParameter(1, cpf) // Define o primeiro parâmetro como o CPF do cliente
                .getResultList(); // Executa a consulta e retorna uma lista de resultados
    }
}