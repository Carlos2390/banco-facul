package com.cgp.banco.dao;

import com.cgp.banco.model.Endereco;
import java.util.List;

public interface EnderecoDAO {
    void salvar(Endereco endereco);
    void atualizar(Endereco endereco);
    Endereco buscarPorId(Long id);
    List<Endereco> buscarTodos();
    List<Endereco> buscarEnderecosPorCpfCliente(String cpf);
}
