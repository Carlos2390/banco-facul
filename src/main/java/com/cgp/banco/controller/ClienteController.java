package com.cgp.banco.controller;

import com.cgp.banco.model.Cliente;
import com.cgp.banco.model.Log;
import com.cgp.banco.model.Response;
import com.cgp.banco.repository.ClienteRepository;
import com.cgp.banco.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Response criarCliente(@RequestBody Cliente cliente, @RequestParam Long userId) {
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
            return new Response("Cliente criado com sucesso.", HttpStatus.OK.value(), cliente);
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
            return new Response("Erro ao criar cliente: " + e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        }

    }

    @PutMapping("/{id}")
    public Response atualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente, @RequestParam Long userId) {
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
            return new Response("Cliente não encontrado para atualização.", HttpStatus.NOT_FOUND.value(), null);
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
            return new Response("Cliente atualizado com sucesso.", HttpStatus.OK.value(), cliente);
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
            return new Response("Erro ao atualizar cliente: " + e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        }

    }

    @GetMapping("/{id}")
    public Response buscarClientePorId(@PathVariable Long id, @RequestParam Long userId) {
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
            return new Response("Cliente não encontrado.", HttpStatus.NOT_FOUND.value(), null);
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
        return new Response("Cliente encontrado.", HttpStatus.OK.value(), cliente);
    }

    @GetMapping("/buscarClientePorCpf")
    public Response buscarClientePorCpf(@RequestParam String cpf, @RequestParam Long userId) {
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
            return new Response("Cliente não encontrado.", HttpStatus.NOT_FOUND.value(), null);
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
        return new Response("Cliente encontrado.", HttpStatus.OK.value(), cliente);
    }

    @DeleteMapping("/{id}")
    public Response deletarCliente(@PathVariable Long id, @RequestParam Long userId) {
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
            return new Response("Cliente não encontrado para deleção.", HttpStatus.NOT_FOUND.value(), null);
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
        return new Response("Cliente deletado com sucesso.", HttpStatus.OK.value(), null);
    }

    @DeleteMapping("/deletarClientePorCpf")
    public Response deletarClientePorCpf(@RequestParam String cpf, @RequestParam Long userId) {
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
            return new Response("Cliente não encontrado para deleção.", HttpStatus.NOT_FOUND.value(), null);
        }
        // Retorna uma resposta de sucesso
        return new Response("Cliente deletado com sucesso.", HttpStatus.OK.value(), null);
    }

    @GetMapping("/buscarClientePorIdUsuario")
    public Response buscarClientePorIdUsuario(@RequestParam Long idUsuario) {
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
            return new Response("Cliente não encontrado.", HttpStatus.NOT_FOUND.value(), null);
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
        return new Response("Cliente encontrado.", HttpStatus.OK.value(), cliente);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> buscarTodosClientes() {
        // Busca todos os clientes
        List<Cliente> clientes = clienteRepository.findAll();
        // Retorna a lista de clientes
        return ResponseEntity.ok(clientes);
    }
}