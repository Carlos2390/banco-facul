package com.cgp.banco.controller;

import com.cgp.banco.dao.LogRepository;
import com.cgp.banco.dao.UsuarioRepository;
import com.cgp.banco.model.Log;
import com.cgp.banco.model.LogAcoes;
import com.cgp.banco.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LogRepository logRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Integer currentUserId = 1;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
            if (usuario == null || usuario.getUsername() == null || usuario.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credenciais inválidas");
            }

            Usuario user = usuarioDAO.findByUsername(usuario.getUsername());

            if (user != null && user.getPassword().equals(usuario.getPassword())) {

                this.currentUserId = user.getId();

                return ResponseEntity.ok("Login feito com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
            }
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario, @RequestHeader(value = "currentUserId", required = false) Integer currentUserId) {
        try {
            this.currentUserId = Optional.ofNullable(currentUserId).orElse(1);

            usuarioRepository.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (Exception e) {
            logError("Erro ao criar o usuário", null, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar o usuário");
        }
    }
    
    private void logError(String message, Object entity, Exception e) {
        try {
            Log log = new Log();
            log.setIdUsuario(this.currentUserId);
            log.setAcao(LogAcoes.ERRO.name());
            log.setTabelaAfetada(entity != null ? entity.getClass().getSimpleName() : null);
            log.setDescricaoMudanca(message + ": " + e.getMessage());


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
