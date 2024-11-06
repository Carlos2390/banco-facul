package com.cgp.banco.dao;

import com.cgp.banco.model.Transferencia;

import java.util.List;

public interface TransferenciaDAO extends GenericDAO<Transferencia, Long> {
    // Deleta transferências pelo ID da conta
    void deletarTransferenciasPorIdConta(Long idConta);

    // Deleta transferências pelo número da conta
    void deletarTransferenciasPorNumeroConta(Integer numeroConta);

    // Deleta transferências pelo CPF do cliente
    void deletarTransferenciasPorCpfCliente(String cpf);

    // Deleta transferências pelo ID do cliente
    void deletarTransferenciasPorIdCliente(Long id);

    // Busca transferências pelo número da conta de origem
    List<Transferencia> buscarTransferenciasPorNumeroContaOrigem(Integer numeroContaOrigem);

    // Busca transferências pelo número da conta de destino
    List<Transferencia> buscarTransferenciasPorNumeroContaDestino(Integer numeroContaDestino);
}