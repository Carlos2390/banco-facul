package com.cgp.banco.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
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
}
