package com.cgp.banco.controller;

import com.cgp.banco.model.Log;
import com.cgp.banco.model.Usuario;
import com.cgp.banco.repository.LogRepository;
import com.cgp.banco.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LogRepository logRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        if (usuario == null || usuario.getUsername() == null || usuario.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credenciais inválidas");
        }

        Usuario user = usuarioRepository.findByUsername(usuario.getUsername());

        if (user != null && user.getPassword().equals(usuario.getPassword())) {
            user.setPassword("");
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        try {

            usuarioRepository.save(usuario);
            usuario.setPassword("");
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (Exception e) {
            logError("Erro ao criar o usuário", null, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar o usuário, " + e.getMessage());
        }
    }

    private void logError(String message, Object entity, Exception e) {
        try {
            Log log = new Log();
            log.setUserId(null); // User ID is not available in this context after removing currentUserId
            log.setTipoOperacao("INSERIR");
            log.setTabela(entity != null ? entity.getClass().getSimpleName() : null);
            log.setDescricao(message + ": " + e.getMessage());


            if (entity != null) {
                log.setDadosNovos(objectMapper.writeValueAsString(entity));
            }

            logRepository.save(log);
        } catch (Exception logException) {
            System.err.println("Erro ao registrar log de erro: " + logException.getMessage());
            logException.printStackTrace();
        }
    }
}
