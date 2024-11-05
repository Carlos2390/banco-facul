package com.cgp.banco.dao;

import com.cgp.banco.model.Transferencia;
import java.util.List;

public interface TransferenciaDAO {
    void salvar(Transferencia transferencia);
    void atualizar(Transferencia transferencia);
    Transferencia buscarPorId(Long id);
    void deletar(Transferencia transferencia);
    void deletarTransferenciasPorIdConta(Long idConta);
    void deletarTransferenciasPorNumeroConta(Integer numeroConta);
    void deletarTransferenciasPorCpfCliente(String cpf);
    void deletarTransferenciasPorIdCliente(Long id);
    List<Transferencia> buscarTodas();
    List<Transferencia> buscarTransferenciasPorNumeroContaOrigem(Integer numeroContaOrigem);
    List<Transferencia> buscarTransferenciasPorNumeroContaDestino(Integer numeroContaDestino);
}
