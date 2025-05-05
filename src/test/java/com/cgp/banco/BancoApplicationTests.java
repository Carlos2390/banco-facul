package com.cgp.banco;

import com.cgp.banco.model.Cliente;
import com.cgp.banco.dao.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class BancoApplicationTests {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void testContextLoads() {
        assertNotNull(clienteRepository);
    }

    @Test
    void testSaveCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("Test Cliente");
        cliente.setCpf("12345678900");
        Cliente savedCliente = clienteRepository.save(cliente);
        assertNotNull(savedCliente.getId());
    }

}
