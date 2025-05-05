package com.cgp.banco.controller;

import com.cgp.banco.dao.GenericDAO;
import com.cgp.banco.dao.LogDAO;
import com.cgp.banco.dao.UsuarioDAO;
import com.cgp.banco.model.Log;
import com.cgp.banco.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private LogDAO logDAO;

    @Autowired
    private GenericDAO genericDAO;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        try {
            if (usuario == null || usuario.getUsername() == null || usuario.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credenciais inválidas");
            }

            Usuario user = usuarioDAO.findByUsername(usuario.getUsername());

            if (user != null && user.getPassword().equals(usuario.getPassword())) {
                // Definir o ID do usuário na sessão (neste exemplo, diretamente no DAO)
                genericDAO.setUserId(user.getId());

                return ResponseEntity.ok("Login feito com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
            }
        } catch (Exception e) {
             logError("Erro durante o login", null, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario, @RequestHeader(value = "currentUserId", required = false) Integer currentUserId) {
        try {
            if(currentUserId == null){
                currentUserId = 1;
            }
            genericDAO.setUserId(currentUserId);
            usuarioDAO.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (Exception e) {
            logError("Erro ao criar o usuário", null, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar o usuário");
        }
    }
    
    private void logError(String message, Object entity, Exception e) {
        try {
            Log log = new Log();
            log.setIdUsuario(genericDAO.getUserId());
            log.setAcao("Erro");
            log.setTabelaAfetada(entity != null ? entity.getClass().getSimpleName() : null);
            log.setDescricaoMudanca(message + ": " + e.getMessage());

            // Convertendo o objeto para JSON (se disponível)
            if (entity != null) {
                log.setDadosNovos(objectMapper.writeValueAsString(entity));
            }

            logDAO.save(log);
        } catch (Exception logException) {
            System.err.println("Erro ao registrar log de erro: " + logException.getMessage());
            logException.printStackTrace();
        }
    }
}
