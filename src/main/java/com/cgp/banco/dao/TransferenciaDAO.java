package com.cgp.banco.dao;

import com.cgp.banco.model.Transferencia;
import java.util.List;

public interface TransferenciaDAO {
    void salvar(Transferencia transferencia);
    void atualizar(Transferencia transferencia);
    Transferencia buscarPorId(Long id);
    List<Transferencia> buscarTodas();
    List<Transferencia> buscarTransferenciasPorNumeroContaOrigem(Integer numeroContaOrigem);
    List<Transferencia> buscarTransferenciasPorNumeroContaDestino(Integer numeroContaDestino);
}
