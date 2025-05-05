CREATE TABLE log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario BIGINT,
    acao VARCHAR(255) NOT NULL,
    tabela_afetada VARCHAR(255),
    id_registro_afetado BIGINT,
    descricao_mudanca TEXT,
    dados_antigos JSON,
    dados_novos JSON
);