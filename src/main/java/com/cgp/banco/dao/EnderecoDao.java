package com.cgp.banco.dao;

import com.cgp.banco.model.Endereco;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EnderecoDao extends CrudRepository<Endereco, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Endereco (logradouro, numero, complemento, bairro, cidade, estado, cep) " +
            "VALUES (:logradouro, :numero, :complemento, :bairro, :cidade, :estado, :cep)", nativeQuery = true)
    void cadastrarEndereco(
            @Param("logradouro") String logradouro,
            @Param("numero") String numero,
            @Param("complemento") String complemento,
            @Param("bairro") String bairro,
            @Param("cidade") String cidade,
            @Param("estado") String estado,
            @Param("cep") String cep
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE Endereco SET logradouro = :logradouro, numero = :numero, complemento = :complemento, " +
            "bairro = :bairro, cidade = :cidade, estado = :estado, cep = :cep WHERE id_endereco = :id_endereco", nativeQuery = true)
    void atualizarEndereco(
            @Param("id_endereco") Long id_endereco,
            @Param("logradouro") String logradouro,
            @Param("numero") String numero,
            @Param("complemento") String complemento,
            @Param("bairro") String bairro,
            @Param("cidade") String cidade,
            @Param("estado") String estado,
            @Param("cep") String cep
    );

    @Query(value = "SELECT * FROM Endereco WHERE id_endereco = :id_endereco", nativeQuery = true)
    Endereco visualizarEndereco(@Param("id_endereco") Long id_endereco);
}
