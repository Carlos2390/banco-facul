package com.cgp.banco.controller;

import com.cgp.banco.model.Usuario;
import com.cgp.banco.repository.LogRepository;
import com.cgp.banco.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import com.cgp.banco.model.Log;
import jakarta.servlet.http.HttpSession;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LogRepository logRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario, HttpSession session) {
        if (usuario == null || usuario.getUsername() == null || usuario.getPassword() == null) {
            // Cria um log de operação
            Log log = new Log();            if (session.getAttribute("userId") != null) {
                log.setUserId((Long) session.getAttribute("userId"));
            }
            log.setTipoOperacao("LOGIN");
            log.setTabela("usuario");
            log.setDescricao("ERRO: Credenciais inválidas.");
            logRepository.save(log);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credenciais inválidas");
        }

        Usuario user = usuarioRepository.findByUsername(usuario.getUsername());

        if (user != null && user.getPassword().equals(usuario.getPassword())) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId((Long) session.getAttribute("userId"));
            log.setTipoOperacao("LOGIN");
            log.setTabela("usuario");
            log.setDescricao("SUCESSO: Login realizado com sucesso.");
            logRepository.save(log);
            user.setPassword(""); // Limpa a senha antes de retornar
            session.setAttribute("userId", user.getId()); // Salva o ID do usuário na sessão

            return ResponseEntity.ok(user);
        } else {
            // Cria um log de operação
            Log log = new Log();
            if (session.getAttribute("userId") != null) {
                log.setUserId((Long) session.getAttribute("userId"));
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

    @PutMapping("/{id}")
    public ResponseEntity<?> alterarSenhaPorId(@PathVariable Long id, @RequestBody String senha) {
        try {
            Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
            if (usuarioExistente.isPresent()) {
                Usuario usuarioAtualizado = usuarioExistente.get();
                usuarioAtualizado.setPassword(senha);
                usuarioRepository.save(usuarioAtualizado);

                // Cria um log de operação
                Log log = new Log();
                log.setUserId(usuarioAtualizado.getId());
                log.setTipoOperacao("UPDATE");
                log.setTabela("usuario");
                log.setDescricao("SUCESSO: Senha alterada com sucesso.");
                logRepository.save(log);

                return ResponseEntity.ok(usuarioAtualizado);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao alterar a senha: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        try {
            return ResponseEntity.ok(usuarioRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar usuários: " + e.getMessage());
        }
    }
}
