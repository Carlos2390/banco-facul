package com.cgp.banco.controller;

import com.cgp.banco.model.Log;
import com.cgp.banco.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogRepository logRepository;

    @GetMapping
    public List<Log> getAllLogs() {
        return logRepository.findAllByOrderByIdDesc();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllLogs() {
        logRepository.deleteAll(); // ou logRepository.deleteAllLogs() se tiver um método customizado
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}