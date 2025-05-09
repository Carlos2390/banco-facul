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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        if (usuario == null || usuario.getUsername() == null || usuario.getPassword() == null) {
            // Cria um log de operação
            Log log = new Log();
            if (usuario != null) {
                log.setUserId(usuario.getId());
            }
            log.setTipoOperacao("LOGIN");
            log.setTabela("usuario");
            log.setDescricao("ERRO: Credenciais inválidas.");
            logRepository.save(log);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credenciais inválidas");
        }

        Usuario user = usuarioRepository.findByUsername(usuario.getUsername());

        if (user != null && user.getPassword().equals(usuario.getPassword())) {
            user.setPassword("");
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(user.getId());
            log.setTipoOperacao("LOGIN");
            log.setTabela("usuario");
            log.setDescricao("SUCESSO: Login realizado com sucesso.");
            logRepository.save(log);

            return ResponseEntity.ok(user);
        } else {
            // Cria um log de operação
            Log log = new Log();
            if (user != null) {
                log.setUserId(usuario.getId());
            }
            log.setTipoOperacao("LOGIN");
            log.setTabela("usuario");
            log.setDescricao("ERRO: Credenciais inválidas.");
            logRepository.save(log);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        try {
            usuarioRepository.save(usuario);
            usuario.setPassword("");
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(usuario.getId());
            log.setTipoOperacao("CREATE");
            log.setTabela("usuario");
            log.setDescricao("SUCESSO: Usuário criado com sucesso.");
            log.setDadosNovos(usuario.toString());
            logRepository.save(log);

            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(usuario.getId());
            log.setTipoOperacao("CREATE");
            log.setTabela("usuario");
            log.setDescricao("ERRO: Erro ao criar usuário: " + e.getMessage());
            log.setDadosNovos(usuario.toString());
            logRepository.save(log);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar o usuário, " + e.getMessage());
        }
    }
}
