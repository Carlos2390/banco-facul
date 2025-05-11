package com.cgp.banco.repository;

import com.cgp.banco.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    List<Transferencia> findByIdContaDestino(Long idContaDestino);
    List<Transferencia> findByIdContaOrigem(Long idContaOrigem);
    List<Transferencia> findByNumeroContaDestinoOrNumeroContaOrigem(String numeroContaDestino, String numeroContaOrigem);
}