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

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "contaOrigem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transferencia> transferenciasOrigem;

    @OneToMany(mappedBy = "contaDestino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transferencia> transferenciasDestino;

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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Transferencia> getTransferenciasOrigem() {
        return transferenciasOrigem;
    }

    public void setTransferenciasOrigem(List<Transferencia> transferenciasOrigem) {
        this.transferenciasOrigem = transferenciasOrigem;
    }

    public List<Transferencia> getTransferenciasDestino() {
        return transferenciasDestino;
    }

    public void setTransferenciasDestino(List<Transferencia> transferenciasDestino) {
        this.transferenciasDestino = transferenciasDestino;
    }
}
