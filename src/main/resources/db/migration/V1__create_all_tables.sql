CREATE TABLE Endereco (
                          id_endereco SERIAL PRIMARY KEY,
                          logradouro VARCHAR(255) NOT NULL,
                          numero VARCHAR(10) NOT NULL,
                          complemento VARCHAR(255),
                          bairro VARCHAR(255) NOT NULL,
                          cidade VARCHAR(255) NOT NULL,
                          estado VARCHAR(2) NOT NULL,
                          cep VARCHAR(10) NOT NULL
);

CREATE TABLE Cliente (
                         id_cliente SERIAL PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         cpf VARCHAR(11) UNIQUE NOT NULL,
                         email VARCHAR(255) NOT NULL,
                         telefone VARCHAR(15),
                         id_endereco INT NOT NULL,
                         data_nascimento DATE NOT NULL,
                         data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         saldo DECIMAL(15, 2) DEFAULT 0,
                         CONSTRAINT fk_endereco FOREIGN KEY (id_endereco) REFERENCES Endereco(id_endereco)
);

CREATE TABLE Conta (
                       id_conta SERIAL PRIMARY KEY,
                       numero_conta VARCHAR(20) NOT NULL,
                       agencia VARCHAR(10) NOT NULL,
                       titular VARCHAR(255) NOT NULL,
                       cpfCnpj VARCHAR(14) NOT NULL,
                       saldo DECIMAL(15, 2) NOT NULL,
                       data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       status_conta VARCHAR(50) NOT NULL,
                       tipo_conta VARCHAR(50) NOT NULL,
                       limite_credito DECIMAL(15, 2) DEFAULT 0
);

CREATE TABLE Transacao (
                           id_transacao SERIAL PRIMARY KEY,
                           data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           valor DECIMAL(15, 2) NOT NULL,
                           tipo VARCHAR(50) NOT NULL,
                           conta_origem INT NOT NULL,
                           conta_destino INT NOT NULL,
                           descricao VARCHAR(255),
                           CONSTRAINT fk_conta_origem FOREIGN KEY (conta_origem) REFERENCES Conta(id_conta),
                           CONSTRAINT fk_conta_destino FOREIGN KEY (conta_destino) REFERENCES Conta(id_conta)
);
