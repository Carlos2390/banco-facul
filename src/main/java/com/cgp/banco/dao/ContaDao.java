package com.cgp.banco.dao;

import com.cgp.banco.model.Conta;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ContaDao extends CrudRepository<Conta, String> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE Conta SET saldo = saldo + :valor WHERE NumeroConta = :numeroConta", nativeQuery = true)
    void depositar(@Param("numeroConta") String numeroConta, @Param("valor") double valor);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Conta SET saldo = saldo - :valor WHERE NumeroConta = :numeroConta AND saldo >= :valor", nativeQuery = true)
    void sacar(@Param("numeroConta") String numeroConta, @Param("valor") double valor);

    @Modifying
    @Transactional
    default void transferir(String contaOrigem, String contaDestino, double valor) {
        sacar(contaOrigem, valor);
        depositar(contaDestino, valor);
    }

    @Modifying
    @Transactional
    @Query(value = "UPDATE Conta SET StatusConta = 'Encerrada' WHERE NumeroConta = :numeroConta", nativeQuery = true)
    void encerrarConta(@Param("numeroConta") String numeroConta);

    @Query(value = "SELECT saldo FROM Conta WHERE NumeroConta = :numeroConta", nativeQuery = true)
    double verificarSaldo(@Param("numeroConta") String numeroConta);
}
