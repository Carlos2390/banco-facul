package com.cgp.banco.dao;

import com.cgp.banco.model.Cliente;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface ClienteDao extends CrudRepository<Cliente, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Cliente (nome, cpf, email, id_endereco, data_nascimento, data_criacao, saldo) VALUES (:nome, :cpf, :email, :endereco, :data_nascimento, :data_criacao, :saldo)", nativeQuery = true)
    void cadastrarCliente(
            @Param("nome") String nome,
            @Param("cpf") String cpf,
            @Param("email") String email,
            @Param("endereco") Long id_endereco,
            @Param("data_nascimento") Date data_nascimento,
            @Param("data_criacao") Date data_criacao,
            @Param("saldo") double saldo
    );


    @Modifying
    @Transactional
    @Query(value = "UPDATE Cliente SET nome = :nome, email = :email, id_endereco = :endereco WHERE cpf = :cpf", nativeQuery = true)
    void atualizarDados(
            @Param("nome") String nome,
            @Param("email") String email,
            @Param("endereco") Long id_endereco,
            @Param("cpf") String cpf
    );

    @Query(value = "SELECT * FROM Cliente WHERE cpf = :cpf", nativeQuery = true)
    Cliente visualizarDados(@Param("cpf") String cpf);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Cliente SET saldo = saldo + :valor WHERE cpf = :cpf", nativeQuery = true)
    void depositar(
            @Param("cpf") String cpf,
            @Param("valor") double valor
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE Cliente SET saldo = saldo - :valor WHERE cpf = :cpf AND saldo >= :valor", nativeQuery = true)
    void sacar(
            @Param("cpf") String cpf,
            @Param("valor") double valor
    );


    @Modifying
    @Transactional
    default void transferir(
            @Param("cpfOrigem") String cpfOrigem,
            @Param("cpfDestino") String cpfDestino,
            @Param("valor") double valor
    ) {
        sacar(cpfOrigem, valor);
        depositar(cpfDestino, valor);
    }

    @Query(value = "SELECT saldo FROM Cliente WHERE cpf = :cpf", nativeQuery = true)
    double getSaldo(@Param("cpf") String cpf);


}
