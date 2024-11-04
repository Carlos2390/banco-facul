-- Criação da tabela Cliente
CREATE TABLE Cliente (
                         id_cliente SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         cpf VARCHAR(11) UNIQUE NOT NULL,
                         data_nascimento DATE NOT NULL
);

-- Criação da tabela Endereco
CREATE TABLE Endereco (
                          id_endereco SERIAL PRIMARY KEY,
                          rua VARCHAR(100) NOT NULL,
                          numero VARCHAR(10) NOT NULL,
                          cidade VARCHAR(50) NOT NULL,
                          estado VARCHAR(2) NOT NULL,
                          cep VARCHAR(8) NOT NULL,
                          id_cliente INT NOT NULL,
                          FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
);

-- Criação da tabela Conta
CREATE TABLE Conta (
                       id_conta SERIAL PRIMARY KEY,
                       numero_conta VARCHAR(20) UNIQUE NOT NULL,
                       saldo DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
                       tipo VARCHAR(20) NOT NULL,
                       id_cliente INT NOT NULL,
                       FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
);

-- Criação da tabela Transferencia
CREATE TABLE Transferencia (
                               id_transferencia SERIAL PRIMARY KEY,
                               data TIMESTAMP NOT NULL,
                               valor DECIMAL(15, 2) NOT NULL,
                               id_conta_origem INT NOT NULL,
                               id_conta_destino INT NOT NULL,
                               FOREIGN KEY (id_conta_origem) REFERENCES Conta(id_conta),
                               FOREIGN KEY (id_conta_destino) REFERENCES Conta(id_conta)
);
