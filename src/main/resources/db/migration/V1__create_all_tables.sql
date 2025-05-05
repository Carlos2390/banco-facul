-- Criação da tabela Cliente
CREATE TABLE Cliente (
                         id BIGSERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         cpf VARCHAR(11) UNIQUE NOT NULL,
                         data_nascimento DATE NOT NULL
);

-- Criação da tabela Endereco - Ajuste no nome da coluna id_cliente para cliente_id
CREATE TABLE Endereco (
                          id BIGSERIAL PRIMARY KEY,
                          rua VARCHAR(100) NOT NULL,
                          numero VARCHAR(10) NOT NULL,
                          cidade VARCHAR(50) NOT NULL,
                          estado VARCHAR(2) NOT NULL,
                          cep VARCHAR(8) NOT NULL,
                          cliente_id BIGINT NOT NULL,
                          FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
);

-- Criação da tabela Conta - Ajuste no nome da coluna id_cliente para cliente_id
CREATE TABLE Conta (
                       id BIGSERIAL PRIMARY KEY,
                       numero_conta VARCHAR(20) UNIQUE NOT NULL,
                       saldo DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
                       tipo VARCHAR(20) NOT NULL,
                       cliente_id BIGINT NOT NULL,
                       FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
);

-- Criação da tabela Transferencia
CREATE TABLE Transferencia (
                               id BIGSERIAL PRIMARY KEY,
                               data_transferencia TIMESTAMP NOT NULL,
                               valor DECIMAL(15, 2) NOT NULL,
                               id_conta_origem INT NOT NULL,
                               id_conta_destino INT NOT NULL,
                               FOREIGN KEY (id_conta_origem) REFERENCES Conta(id_conta),
                               FOREIGN KEY (id_conta_destino) REFERENCES Conta(id_conta)
);
