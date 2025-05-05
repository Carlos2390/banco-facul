CREATE TABLE Usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Adicionar coluna id_usuario na tabela Cliente como chave estrangeira referenciando a tabela Usuario
ALTER TABLE Cliente
ADD COLUMN id_usuario INT REFERENCES Usuario(id);

-- Adicionar coluna id_usuario na tabela Conta como chave estrangeira referenciando a tabela Usuario
ALTER TABLE Conta
ADD COLUMN id_usuario INT REFERENCES Usuario(id);