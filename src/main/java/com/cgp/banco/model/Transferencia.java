package com.cgp.banco.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Transferencia")
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_transferencia;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private Long id_conta_origem;

    @Column(nullable = false)
    private Long id_conta_destino;

    // Getters e Setters
    public Long getId() {
        return id_transferencia;
    }

    public void setId(Long id) {
        this.id_transferencia = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Long getId_conta_origem() {
        return id_conta_origem;
    }

    public void setId_conta_origem(Long id_conta_origem) {
        this.id_conta_origem = id_conta_origem;
    }

    public Long getId_conta_destino() {
        return id_conta_destino;
    }

    public void setId_conta_destino(Long id_conta_destino) {
        this.id_conta_destino = id_conta_destino;
    }
}
