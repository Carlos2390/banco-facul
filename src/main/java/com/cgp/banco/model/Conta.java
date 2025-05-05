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
@Table(name = "conta")
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_conta", unique = true, nullable = false, length = 20)
    private Integer numeroConta;

    @Column(nullable = false)
    private Double saldo;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @JoinColumn(name = "id_cliente", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private Cliente cliente;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @JoinColumn(name = "id_usuario", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private Usuario usuario;
}
