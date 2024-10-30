package com.cgp.banco.dao;

import com.cgp.banco.model.Transacao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface TransacaoDao extends CrudRepository<Transacao, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Transacao (data, valor, tipo, conta_origem, conta_destino, descricao) " +
            "VALUES (:data, :valor, :tipo, :contaOrigem, :contaDestino, :descricao)", nativeQuery = true)
    void realizarTransacao(
            @Param("data") Date data,
            @Param("valor") double valor,
            @Param("tipo") String tipo,
            @Param("contaOrigem") String contaOrigem,
            @Param("contaDestino") String contaDestino,
            @Param("descricao") String descricao
    );

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Transacao (data, valor, tipo, conta_origem, conta_destino, descricao) " +
            "VALUES (:data, :valor, 'ESTORNO', :contaDestino, :contaOrigem, :descricao)", nativeQuery = true)
    void estornarTransacao(
            @Param("data") Date data,
            @Param("valor") double valor,
            @Param("contaOrigem") String contaOrigem,
            @Param("contaDestino") String contaDestino,
            @Param("descricao") String descricao
    );

    @Query(value = "SELECT * FROM Transacao WHERE id = :id", nativeQuery = true)
    Transacao obterDetalhes(@Param("id") Long id);
}
