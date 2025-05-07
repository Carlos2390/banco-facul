package com.cgp.banco.repository;

import com.cgp.banco.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    Conta findByNumeroConta(String numeroConta);
    void deleteByNumeroConta(String numeroConta);
    List<Conta> findByClienteCpf(String cpf);
}