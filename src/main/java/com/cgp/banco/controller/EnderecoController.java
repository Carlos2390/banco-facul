package com.cgp.banco.controller;

import com.cgp.banco.dao.EnderecoDAO;
import com.cgp.banco.model.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoDAO enderecoDAO;

    @Autowired
    private HttpSession session;

    @PostMapping
    public ResponseEntity<String> criarEndereco(@RequestBody Endereco endereco) {
        try {
            setUserIdInDAO();
            // Salva o endereço no banco de dados
            enderecoDAO.salvar(endereco);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Endereço criado com sucesso.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar o endereço: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarEndereco(@PathVariable Long id, @RequestBody Endereco endereco) {
        try {
            setUserIdInDAO();
            // Busca o endereço existente pelo ID
            Endereco enderecoExistente = enderecoDAO.buscarPorId(id);
            // Verifica se o endereço existe
            if (enderecoExistente == null) {
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            // Define o ID do endereço
            endereco.setId(id);
            // Atualiza o endereço no banco de dados
            enderecoDAO.atualizar(endereco);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Endereço atualizado com sucesso.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o endereço: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarEnderecoPorId(@PathVariable Long id) {
        try{
            setUserIdInDAO();
            // Busca o endereço pelo ID
            Endereco endereco = enderecoDAO.buscarPorId(id);
            // Verifica se o endereço existe
            if (endereco == null) {
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            // Retorna o endereço encontrado
            return ResponseEntity.ok(endereco);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscarEnderecosPorCpfCliente")
    public ResponseEntity<List<Endereco>> buscarEnderecosPorCpfCliente(@RequestParam String cpf) {
        try{
            setUserIdInDAO();
            // Busca os endereços pelo CPF do cliente
            List<Endereco> enderecos = enderecoDAO.buscarEnderecosPorCpfCliente(cpf);
            // Retorna a lista de endereços encontrados
            return ResponseEntity.ok(enderecos);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarEndereco(@PathVariable Long id) {
        try{
            setUserIdInDAO();
            // Busca o endereço pelo ID
            Endereco endereco = enderecoDAO.buscarPorId(id);
            // Verifica se o endereço existe
            if (endereco == null) {
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            // Deleta o endereço do banco de dados
            enderecoDAO.deletar(id);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Endereço deletado com sucesso.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar o endereço: " + e.getMessage());
        }
    }

    @DeleteMapping("/deletarEnderecosPorCpfCliente")
    public ResponseEntity<String> deletarEnderecosPorCpfCliente(@RequestParam String cpf) {
        try{
            setUserIdInDAO();
            // Deleta os endereços pelo CPF do cliente
            enderecoDAO.deletarEnderecosPorCpfCliente(cpf);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Endereços deletados com sucesso.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar os endereços: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> buscarTodosEnderecos() {
        try{
            setUserIdInDAO();
            // Busca todos os endereços
            List<Endereco> enderecos = enderecoDAO.buscarTodos();
            // Retorna a lista de endereços
            return ResponseEntity.ok(enderecos);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    private void setUserIdInDAO() {
        Integer userId = Optional.ofNullable((Integer) session.getAttribute("currentUserId")).orElse(1);
        enderecoDAO.setUserId(userId);
    }
}
