package com.cgp.banco.dao.impl;

import com.cgp.banco.model.Log;
import com.cgp.banco.dao.UsuarioDAO;
import com.cgp.banco.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class UsuarioDAOImpl implements UsuarioDAO{

    @PersistenceContext
    private EntityManager entityManager;

    private Long currentUserId;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setUserId(Long userId) {
        this.currentUserId = userId;
    }

    @Override
    public Long getUserId() {
        return this.currentUserId;
    }

    @Override
    public Optional<Usuario> findById(Long id) {
         try {
            return Optional.ofNullable(entityManager.find(Usuario.class, id));
        } catch (Exception e) {
            logError("Erro ao buscar Usuario por ID", e);
            throw e;
        }
    }

    @Override
    public List<Usuario> findAll() {
         try {
            return entityManager.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        } catch (Exception e) {
            logError("Erro ao buscar todos os Usuarios", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
         try {
            entityManager.persist(usuario);
            logAction("INSERT", "Usuario", usuario.getId(), "Inserção de novo Usuario", null, usuario);
            return usuario;
        } catch (Exception e) {
            logError("Erro ao salvar Usuario", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Usuario update(Usuario usuario) {
         try {
            Usuario usuarioAntigo = findById(usuario.getId()).orElse(null);
            Usuario usuarioAtualizado = entityManager.merge(usuario);
            logAction("UPDATE", "Usuario", usuario.getId(), "Atualização de Usuario", usuarioAntigo, usuarioAtualizado);
            return usuarioAtualizado;
        } catch (Exception e) {
            logError("Erro ao atualizar Usuario", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(Usuario usuario) {
         try {
            Usuario usuarioToRemove = entityManager.contains(usuario) ? usuario : entityManager.merge(usuario);
            entityManager.remove(usuarioToRemove);
            logAction("DELETE", "Usuario", usuario.getId(), "Exclusão de Usuario", usuario, null);
        } catch (Exception e) {
            logError("Erro ao deletar Usuario", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
         try {
            findById(id).ifPresent(this::delete);
        } catch (Exception e) {
            logError("Erro ao deletar Usuario por ID", e);
            throw e;
        }
    }

    private void logAction(String action, String table, Long recordId, String description, Object oldData, Object newData) {
         try {
            String oldDataJson = oldData != null ? objectMapper.writeValueAsString(oldData) : null;
            String newDataJson = newData != null ? objectMapper.writeValueAsString(newData) : null;

            Log log = new Log();
            log.setDataHora(LocalDateTime.now());
            log.setIdUsuario(currentUserId);
            log.setAcao(action);
            log.setTabelaAfetada(table);
            log.setIdRegistroAfetado(recordId);
            log.setDescricaoMudanca(description);
            log.setDadosAntigos(oldDataJson);
            log.setDadosNovos(newDataJson);
            entityManager.persist(log);
        } catch (Exception e) {
            logError("Erro ao registrar log", e);
        }
    }

    private void logError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
    }
}