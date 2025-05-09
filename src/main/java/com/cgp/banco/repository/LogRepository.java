package com.cgp.banco.repository;

import com.cgp.banco.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    // Método para buscar todos os logs ordenados pelo id
    List<Log> findAllByOrderByIdDesc();
}