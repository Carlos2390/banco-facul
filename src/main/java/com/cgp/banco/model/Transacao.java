package com.cgp.banco.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Transacao")
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data", nullable = false)
    private Date data;

    @Column(name = "valor", nullable = false)
    private double valor;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "conta_origem_id", nullable = false)
    private Conta contaOrigem;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id", nullable = true)
    private Conta contaDestino;

    @Column(name = "descricao")
    private String descricao;

    public Transacao() {
    }

    public Transacao(Date data, double valor, String tipo, Conta contaOrigem, Conta contaDestino, String descricao) {
        this.data = data;
        this.valor = valor;
        this.tipo = tipo;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Conta getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(Conta contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(Conta contaDestino) {
        this.contaDestino = contaDestino;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void realizarTransacao() {
        if (this.valor > 0 && this.contaOrigem != null) {
            this.contaOrigem.sacar(this.valor);
            if (this.contaDestino != null) {
                this.contaDestino.depositar(this.valor);
            }
        }
    }

    public void estornarTransacao() {
        if (this.valor > 0 && this.contaOrigem != null) {
            this.contaOrigem.depositar(this.valor);
            if (this.contaDestino != null) {
                this.contaDestino.sacar(this.valor);
            }
        }
    }

    public String obterDetalhes() {
        return "Transação " + this.tipo + " no valor de " + this.valor +
                " realizada na data " + this.data + ". Descrição: " + this.descricao;
    }
}
