package com.cgp.banco.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Conta")
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_conta", nullable = false, unique = true)
    private String numeroConta;

    @Column(name = "agencia", nullable = false)
    private String agencia;

    @Column(name = "titular", nullable = false)
    private String titular;

    @Column(name = "cpf_cnpj", nullable = false, unique = true)
    private String cpfCnpj;

    @Column(name = "saldo", nullable = false)
    private double saldo;

    @Column(name = "data_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @Column(name = "status_conta", nullable = false)
    private String statusConta;

    @Column(name = "tipo_conta", nullable = false)
    private String tipoConta;

    @Column(name = "limite_credito")
    private double limiteCredito;

    public Conta() {
    }

    public Conta(String numeroConta, String agencia, String titular, String cpfCnpj, double saldo, Date dataCriacao, String statusConta, String tipoConta, double limiteCredito) {
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.titular = titular;
        this.cpfCnpj = cpfCnpj;
        this.saldo = saldo;
        this.dataCriacao = dataCriacao;
        this.statusConta = statusConta;
        this.tipoConta = tipoConta;
        this.limiteCredito = limiteCredito;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getStatusConta() {
        return statusConta;
    }

    public void setStatusConta(String statusConta) {
        this.statusConta = statusConta;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public double getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
        }
    }

    public void sacar(double valor) {
        if (valor > 0 && valor <= this.saldo) {
            this.saldo -= valor;
        }
    }

    public void transferir(Conta destino, double valor) {
        if (valor > 0 && valor <= this.saldo) {
            this.saldo -= valor;
            destino.depositar(valor);
        }
    }

    public void encerrarConta() {
        this.statusConta = "Encerrada";
    }

    public double verificarSaldo() {
        return this.saldo;
    }
}
