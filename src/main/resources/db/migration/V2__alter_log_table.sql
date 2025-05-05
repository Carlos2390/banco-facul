-- Drop the existing log table if it exists
DROP TABLE IF EXISTS log;

-- Create the new log table with the correct columns
CREATE TABLE log (
    id BIGSERIAL PRIMARY KEY,
    user_id INT,
    tipo_operacao VARCHAR(255),
    tabela VARCHAR(255),
    id_tabela BIGINT,
    descricao TEXT,
    dados_antigos TEXT,
    dados_novos TEXT,
    FOREIGN KEY (user_id) REFERENCES usuario(id)
);