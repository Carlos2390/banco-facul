package com.cgp.banco.service;

import com.cgp.banco.dao.EnderecoDao;
import com.cgp.banco.model.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoDao enderecoDao;

    public Endereco cadastrarEndereco(Endereco endereco) {
        enderecoDao.cadastrarEndereco(
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );
        return enderecoDao.visualizarEndereco(endereco.getId_endereco());
    }

    public Endereco atualizarEndereco(Long id, Endereco enderecoAtualizado) {
        Endereco endereco = visualizarEndereco(id);

        enderecoDao.atualizarEndereco(
                id,
                enderecoAtualizado.getLogradouro(),
                enderecoAtualizado.getNumero(),
                enderecoAtualizado.getComplemento(),
                enderecoAtualizado.getBairro(),
                enderecoAtualizado.getCidade(),
                enderecoAtualizado.getEstado(),
                enderecoAtualizado.getCep()
        );
        return enderecoDao.visualizarEndereco(id);
    }

    public Endereco visualizarEndereco(Long id) {
        Endereco endereco = enderecoDao.visualizarEndereco(id);

        if (endereco != null) {
            return endereco;
        } else {
            throw new RuntimeException("Endereço não encontrado");
        }
    }
}