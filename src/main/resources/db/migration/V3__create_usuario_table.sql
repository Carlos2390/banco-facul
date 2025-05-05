CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Adicionar coluna id_usuario na tabela cliente como chave estrangeira referenciando a tabela usuario
ALTER TABLE cliente
ADD COLUMN usuario_id INT REFERENCES usuario(id);

-- Adicionar coluna id_usuario na tabela conta como chave estrangeira referenciando a tabela usuario
ALTER TABLE conta
ADD COLUMN usuario_id INT REFERENCES usuario(id);