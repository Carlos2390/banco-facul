package com.cgp.banco.controller;

import com.cgp.banco.model.Log;
import com.cgp.banco.model.Response;
import com.cgp.banco.model.Usuario;
import com.cgp.banco.repository.LogRepository;
import com.cgp.banco.repository.UsuarioRepository;
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
    public Response login(@RequestBody Usuario usuario) {
        if (usuario == null || usuario.getUsername() == null || usuario.getPassword() == null) {
            // Cria um log de operação
            Log log = new Log();
            log.setTipoOperacao("LOGIN");
            log.setTabela("usuario");
            log.setDescricao("ERRO: Credenciais inválidas.");
            logRepository.save(log);
            return new Response("Credenciais inválidas", HttpStatus.BAD_REQUEST.value(), null);
        }

        Usuario user = usuarioRepository.findByUsername(usuario.getUsername());

        Log log = new Log();
        if (user != null && user.getPassword().equals(usuario.getPassword())) {
            // Cria um log de operação
            log.setUserId(user.getId());
            log.setTipoOperacao("LOGIN");
            log.setTabela("usuario");
            log.setDescricao("SUCESSO: Login realizado com sucesso.");
            logRepository.save(log);
            user.setPassword(""); // Limpa a senha antes de retornar
            System.out.println("ID do usuário que será setado na sessão: " + user.getId());
            System.out.println("ID do usuário na sessão: " + user.getId());
            return new Response("Login realizado com sucesso", HttpStatus.OK.value(), user);
        } else {
            // Cria um log de operação
            log.setTipoOperacao("LOGIN");
            log.setTabela("usuario");
            if (user != null) {
                log.setUserId(user.getId());
            } else {
                log.setUserId(null);
            }
            log.setDescricao("ERRO: Credenciais inválidas.");
            logRepository.save(log);
            return new Response("Credenciais inválidas", HttpStatus.UNAUTHORIZED.value(), null);
        }
    }

    @PostMapping
    public Response criarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario existente = usuarioRepository.findByUsername(usuario.getUsername());
            if (existente != null) {
                // Cria um log de operação
                Log log = new Log();
                log.setTipoOperacao("CREATE");
                log.setTabela("usuario");
                log.setDescricao("ERRO: Usuário já existe.");
                logRepository.save(log);
                return new Response("Usuário já existe", HttpStatus.BAD_REQUEST.value(), null);
            }

            usuarioRepository.save(usuario);
//            usuario.setPassword("");
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(usuario.getId());
            log.setTipoOperacao("CREATE");
            log.setTabela("usuario");
            log.setDescricao("SUCESSO: Usuário criado com sucesso.");
            log.setDadosNovos(usuario.toString());
            logRepository.save(log);
            return new Response("Usuário criado com sucesso", HttpStatus.CREATED.value(), usuario);
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(usuario.getId());
            log.setTipoOperacao("CREATE");
            log.setTabela("usuario");
            log.setDescricao("ERRO: Erro ao criar usuário: " + e.getMessage());
            log.setDadosNovos(usuario.toString());
            logRepository.save(log);
            return new Response("Erro ao criar o usuário", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @PutMapping("/{id}")
    public Response alterarSenhaPorId(@PathVariable Long id, @RequestBody String senha) {
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
                return new Response("Senha alterada com sucesso", HttpStatus.OK.value(), usuarioAtualizado);
            } else {
                // Cria um log de operação
                Log log = new Log();
                log.setTipoOperacao("UPDATE");
                log.setTabela("usuario");
                log.setDescricao("ERRO: Usuário não encontrado.");
                logRepository.save(log);
                return new Response("Usuário não encontrado", HttpStatus.NOT_FOUND.value(), null);
            }
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setTipoOperacao("UPDATE");
            log.setTabela("usuario");
            log.setDescricao("ERRO: Erro ao alterar a senha: " + e.getMessage());
            logRepository.save(log);
            return new Response("Erro ao alterar a senha", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
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
