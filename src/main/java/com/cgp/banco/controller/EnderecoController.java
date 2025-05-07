package com.cgp.banco.controller;

import com.cgp.banco.repository.EnderecoRepository;
import com.cgp.banco.model.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired // Injeta o repositório de Endereço
    private EnderecoRepository enderecoRepository;

    @PostMapping
    public ResponseEntity<String> criarEndereco(@RequestBody Endereco endereco) {
        try {

            // Usa o método save do EnderecoRepository para salvar o endereço
            enderecoRepository.save(endereco);

            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Endereço criado com sucesso.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar o endereço: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarEndereco(@PathVariable Long id, @RequestBody Endereco endereco) {
        try {
            // Busca o endereço existente pelo ID utilizando o método findById
            Optional<Endereco> enderecoExistenteOptional = enderecoRepository.findById(id);
            // Verifica se o endereço existe
            if (enderecoExistenteOptional.isEmpty()) {
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }
            Endereco enderecoExistente = enderecoExistenteOptional.get();
            // Define o ID do endereço
            endereco.setId(id);

            // Salva a entidade atualizada no banco de dados
            enderecoRepository.save(endereco);


            return ResponseEntity.ok("Endereço atualizado com sucesso.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o endereço: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarEnderecoPorId(@PathVariable Long id) {
        try {
            // Utiliza o método findById do EnderecoRepository para buscar o endereço pelo ID
            Optional<Endereco> enderecoOptional = enderecoRepository.findById(id);

            // Extrai o endereço do Optional ou retorna not found
            Endereco endereco = enderecoOptional.orElse(null);
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
    public ResponseEntity buscarEnderecosPorCpfCliente(@RequestParam String cpf) {
        try{
            java.util.List<Endereco> enderecos = new ArrayList<>();

            enderecoRepository.findAll().forEach(endereco -> {
               if(endereco.getCliente() != null && endereco.getCliente().getCpf().equals(cpf)){
                   enderecos.add(endereco);
               }
            });

            // Retorna a lista de endereços encontrados
            return ResponseEntity.ok(enderecos);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarEndereco(@PathVariable Long id) {
        try {
            // Utiliza o método findById do EnderecoRepository para buscar o endereço pelo ID
            Optional<Endereco> enderecoOptional = enderecoRepository.findById(id);

            // Verifica se o endereço foi encontrado
            if (enderecoOptional.isEmpty()) {
                // Retorna uma resposta de não encontrado
                return ResponseEntity.notFound().build();
            }


            // Deleta o endereço do banco de dados
            enderecoRepository.deleteById(id);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Endereço deletado com sucesso.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar o endereço: " + e.getMessage());
        }
    }

    @DeleteMapping("/deletarEnderecosPorCpfCliente")
    public ResponseEntity<String> deletarEnderecosPorCpfCliente(@RequestParam String cpf) {
        try {
            enderecoRepository.findAll().forEach(endereco -> {
                if(endereco.getCliente() != null && endereco.getCliente().getCpf().equals(cpf)){
                    enderecoRepository.delete(endereco);
                }
            });

            // Retorna uma resposta de sucesso


            return ResponseEntity.ok("Endereços deletados com sucesso.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar os endereços: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity buscarTodosEnderecos() {
        try {
            // Utiliza o método findAll do EnderecoRepository para buscar todos os endereços
            java.util.List<Endereco> enderecos = enderecoRepository.findAll();
            // Retorna a lista de endereços
            return ResponseEntity.ok(enderecos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
