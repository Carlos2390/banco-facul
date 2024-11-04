package com.cgp.banco.dao;

import com.cgp.banco.model.Conta;
import java.util.List;

public interface ContaDAO {
    void salvar(Conta conta);
    void atualizar(Conta conta);
    Conta buscarPorId(Long id);
    List<Conta> buscarTodas();
}
