package com.cgp.banco.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_conta;

    @Column(name = "numero_conta", unique = true, nullable = false, length = 20)
    private Integer numeroConta;

    @Column(nullable = false)
    private Double saldo;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false)
    private Long id_cliente;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    // Getters e Setters
    public Long getId() {
        return id_conta;
    }

    public void setId(Long id) {
        this.id_conta = id;
    }

    public Integer getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(Integer numeroConta) {
        this.numeroConta = numeroConta;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public Long getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Long id_cliente) {
        this.id_cliente = id_cliente;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
