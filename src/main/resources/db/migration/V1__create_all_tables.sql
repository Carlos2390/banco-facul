-- V1__create_all_tables.sql
-- Criação da tabela Cliente
CREATE TABLE cliente (
                         id BIGSERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         cpf VARCHAR(11) UNIQUE NOT NULL,
                         data_nascimento DATE NOT NULL
);

-- Criação da tabela Endereco - Ajuste no nome da coluna id_cliente para cliente_id
CREATE TABLE endereco (
                          id BIGSERIAL PRIMARY KEY,
                          rua VARCHAR(100) NOT NULL,
                          numero VARCHAR(255) NOT NULL,
                          cidade VARCHAR(50) NOT NULL,
                          estado VARCHAR(2) NOT NULL,
                          cep INT NOT NULL,
                          cliente_id BIGINT NOT NULL,
                          FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

-- Criação da tabela Conta
CREATE TABLE conta (
                       id BIGSERIAL PRIMARY KEY,
                       numero_conta VARCHAR(255) UNIQUE NOT NULL,
                       saldo DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
                       tipo VARCHAR(20) NOT NULL,
                       cliente_id BIGINT NOT NULL,
                       FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

-- Criação da tabela Transferencia
CREATE TABLE transferencia (
                               id BIGSERIAL PRIMARY KEY,
                               data_transferencia TIMESTAMP NOT NULL,
                               valor DECIMAL(15, 2) NOT NULL,
                               id_conta_origem INT NOT NULL,
                               id_conta_destino INT NOT NULL,
                               FOREIGN KEY (id_conta_origem) REFERENCES conta(id),
                               FOREIGN KEY (id_conta_destino) REFERENCES conta(id)
);

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

CREATE TABLE log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_hora TIMESTAMP NOT NULL,
    descricao TEXT NOT NULL,
    usuario_id INT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);