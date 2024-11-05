package com.cgp.banco.dao;

import com.cgp.banco.model.Transferencia;
import java.util.List;

public interface TransferenciaDAO {
    // Salva uma nova transferência no banco de dados
    void salvar(Transferencia transferencia);

    // Atualiza uma transferência existente no banco de dados
    void atualizar(Transferencia transferencia);

    // Busca uma transferência pelo seu ID
    Transferencia buscarPorId(Long id);

    // Deleta uma transferência
    void deletar(Transferencia transferencia);

    // Deleta transferências pelo ID da conta
    void deletarTransferenciasPorIdConta(Long idConta);

    // Deleta transferências pelo número da conta
    void deletarTransferenciasPorNumeroConta(Integer numeroConta);

    // Deleta transferências pelo CPF do cliente
    void deletarTransferenciasPorCpfCliente(String cpf);

    // Deleta transferências pelo ID do cliente
    void deletarTransferenciasPorIdCliente(Long id);

    // Busca todas as transferências no banco de dados
    List<Transferencia> buscarTodas();

    // Busca transferências pelo número da conta de origem
    List<Transferencia> buscarTransferenciasPorNumeroContaOrigem(Integer numeroContaOrigem);

    // Busca transferências pelo número da conta de destino
    List<Transferencia> buscarTransferenciasPorNumeroContaDestino(Integer numeroContaDestino);
}