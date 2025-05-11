package com.cgp.banco.controller;

import com.cgp.banco.model.Cliente;
import com.cgp.banco.model.Log;
import com.cgp.banco.repository.ClienteRepository;
import com.cgp.banco.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/clientes")

public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private LogRepository logRepository;

    @PostMapping
    public ResponseEntity<?> criarCliente(@RequestBody Cliente cliente, @RequestParam Long userId) {
        try {
            // Salva o cliente no banco de dados
            clienteRepository.save(cliente);
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("CREATE");
            log.setTabela("cliente");
            log.setIdTabela(cliente.getId());
            log.setDescricao("SUCESSO: Cliente criado com sucesso.");
            log.setDadosAntigos(null);
            log.setDadosNovos(cliente.toString());
            logRepository.save(log);
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("CREATE");
            log.setTabela("cliente");
            log.setIdTabela(cliente.getId());
            log.setDescricao("ERRO: Erro ao criar cliente: " + e.getMessage());
            log.setDadosAntigos(null);
            log.setDadosNovos(cliente.toString());
            logRepository.save(log);
            // Retorna uma resposta de erro
            return ResponseEntity.badRequest().body("Erro ao criar cliente: " + e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente, @RequestParam Long userId) {
        // Busca o cliente existente pelo ID
        Cliente clienteExistente = clienteRepository.findById(id).orElse(null);
        // Verifica se o cliente existe
        if (clienteExistente == null) {
            // Cria um log de operação
            Log log = new Log();
            log.setTipoOperacao("UPDATE");
            log.setUserId(userId);
            log.setTabela("cliente");
            log.setIdTabela(id);
            log.setDescricao("ERRO: Cliente não encontrado para atualização.");
            log.setDadosAntigos(null);
            log.setDadosNovos(cliente.toString());
            logRepository.save(log);
            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }

        try {
            cliente.setId(id);
            // Atualiza o cliente no banco de dados
            clienteRepository.save(cliente);
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("UPDATE");
            log.setTabela("cliente");
            log.setIdTabela(cliente.getId());
            log.setDescricao("SUCESSO: Cliente atualizado com sucesso.");
            log.setDadosAntigos(clienteExistente.toString());
            log.setDadosNovos(cliente.toString());
            logRepository.save(log);

            // Retorna uma resposta de sucesso
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("UPDATE");
            log.setTabela("cliente");
            log.setIdTabela(cliente.getId());
            log.setDescricao("ERRO: Erro ao atualizar cliente: " + e.getMessage());
            log.setDadosAntigos(clienteExistente.toString());
            log.setDadosNovos(cliente.toString());
            logRepository.save(log);

            // Retorna uma resposta de erro
            return ResponseEntity.badRequest().body("Erro ao atualizar cliente: " + e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id, @RequestParam Long userId) {
        // Busca o cliente pelo ID
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        // Verifica se o cliente existe
        if (cliente == null) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("BUSCAR");
            log.setTabela("cliente");
            log.setIdTabela(id);
            log.setDescricao("ERRO: Cliente não encontrado.");
            log.setDadosAntigos(null);
            log.setDadosNovos(null);
            logRepository.save(log);

            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }

        // Cria um log de operação
        Log log = new Log();
        log.setUserId(userId);
        log.setTipoOperacao("BUSCAR");
        log.setTabela("cliente");
        log.setIdTabela(cliente.getId());
        log.setDescricao("SUCESSO: Cliente encontrado.");
        logRepository.save(log);

        // Retorna o cliente encontrado
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/buscarClientePorCpf")
    public ResponseEntity<Cliente> buscarClientePorCpf(@RequestParam String cpf, @RequestParam Long userId) {
        // Busca o cliente pelo CPF
        Cliente cliente = clienteRepository.findByCpf(cpf);
        // Verifica se o cliente existe
        if (cliente == null) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("BUSCAR");
            log.setTabela("cliente");
            log.setDescricao("ERRO: Cliente não encontrado.");
            log.setDadosAntigos(null);
            log.setDadosNovos(null);
            logRepository.save(log);

            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Cria um log de operação
        Log log = new Log();
        log.setUserId(userId);
        log.setTipoOperacao("BUSCAR");
        log.setTabela("cliente");
        log.setIdTabela(cliente.getId());
        log.setDescricao("SUCESSO: Cliente encontrado.");
        logRepository.save(log);

        // Retorna o cliente encontrado
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarCliente(@PathVariable Long id, @RequestParam Long userId) {
        // Busca o cliente pelo ID
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        // Verifica se o cliente existe
        if (cliente == null) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("DELETAR");
            log.setTabela("cliente");
            log.setIdTabela(id);
            log.setDescricao("ERRO: Cliente não encontrado para deleção.");
            log.setDadosAntigos(null);
            log.setDadosNovos(null);
            logRepository.save(log);

            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Cria um log de operação
        Log log = new Log();
        log.setUserId(userId);
        log.setTipoOperacao("DELETAR");
        log.setTabela("cliente");
        log.setIdTabela(cliente.getId());
        log.setDescricao("SUCESSO: Cliente deletado com sucesso.");
        log.setDadosAntigos(cliente.toString());
        log.setDadosNovos(null);
        logRepository.save(log);

        // Deleta o cliente do banco de dados
        clienteRepository.deleteById(id);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Cliente deletado com sucesso.");
    }

    @DeleteMapping("/deletarClientePorCpf")
    public ResponseEntity<String> deletarClientePorCpf(@RequestParam String cpf, @RequestParam Long userId) {
        Cliente cliente = clienteRepository.findByCpf(cpf);
        if (cliente != null) {
            // Deleta o cliente pelo CPF
            clienteRepository.deleteByCpf(cpf);

            // Cria um log de operação
            Log log = new Log();
            log.setTipoOperacao("DELETAR");
            log.setUserId(userId);
            log.setTabela("cliente");
            log.setIdTabela(cliente.getId());
            log.setDescricao("SUCESSO: Cliente deletado com sucesso.");
            log.setDadosAntigos(cliente.toString());
            log.setDadosNovos(null);
            logRepository.save(log);
        } else {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(userId);
            log.setTipoOperacao("DELETAR");
            log.setTabela("cliente");
            log.setIdTabela(clienteRepository.findByCpf(cpf).getId());
            log.setDescricao("ERRO: Cliente não encontrado para deleção.");
            log.setDadosAntigos(null);
            log.setDadosNovos(null);
            logRepository.save(log);

            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Cliente deletado com sucesso.");
    }

    @GetMapping("/buscarClientePorIdUsuario")
    public ResponseEntity<Cliente> buscarClientePorIdUsuario(@RequestParam Long idUsuario) {
        // Busca o cliente pelo ID do usuário
        Cliente cliente = clienteRepository.findByIdUsuario(idUsuario);
        // Verifica se o cliente existe
        if (cliente == null) {
            // Cria um log de operação
            Log log = new Log();
            log.setUserId(idUsuario);
            log.setTipoOperacao("BUSCAR");
            log.setTabela("cliente");
            log.setIdTabela(idUsuario);
            log.setDescricao("ERRO: Cliente não encontrado.");
            log.setDadosAntigos(null);
            log.setDadosNovos(null);
            logRepository.save(log);

            // Retorna uma resposta de não encontrado
            return ResponseEntity.notFound().build();
        }
        // Cria um log de operação
        Log log = new Log();
        log.setUserId(idUsuario);
        log.setTipoOperacao("BUSCAR");
        log.setTabela("cliente");
        log.setIdTabela(cliente.getId());
        log.setDescricao("SUCESSO: Cliente encontrado.");
        logRepository.save(log);

        // Retorna o cliente encontrado
        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> buscarTodosClientes() {
        // Busca todos os clientes
        List<Cliente> clientes = clienteRepository.findAll();
        // Retorna a lista de clientes
        return ResponseEntity.ok(clientes);
    }
}