package com.cgp.banco.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "tipo_operacao")
    private String tipoOperacao;
    private String tabela;
    @Column(name = "id_tabela")
    private Long idTabela;
    private String descricao;
    @Column(name = "dados_antigos")
    private String dadosAntigos;
    @Column(name = "dados_novos")
    private String dadosNovos;
}