CREATE TABLE Log (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT,
    acao VARCHAR(255),
    tabela_afetada VARCHAR(255),
    id_registro_afetado INT,
    descricao_mudanca TEXT,
    dados_antigos JSON,
    dados_novos JSON
);