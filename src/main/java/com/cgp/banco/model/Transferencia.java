package com.cgp.banco.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "transferencia")
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_transferencia", nullable = false)
    private LocalDateTime dataTransferencia;

    @Column(nullable = false)
    private Double valor;

    @Column(name = "id_conta_origem", nullable = false)
    private Long idContaOrigem;

    @JoinColumn(name = "id_conta_origem", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private Conta contaOrigem;

    @Column(name = "id_conta_destino", nullable = false)
    private Long idContaDestino;

    @JoinColumn(name = "id_conta_destino", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private Conta contaDestino;
}
