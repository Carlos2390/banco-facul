-- Alterar a coluna numero de Endereco para VARCHAR
ALTER TABLE endereco ALTER COLUMN numero TYPE VARCHAR(255);

-- Alterar a coluna cep de Endereco para INT
ALTER TABLE endereco
ALTER COLUMN cep TYPE INT USING cep::integer;

-- Alterar a coluna numero de Conta para VARCHAR
ALTER TABLE conta ALTER COLUMN numero TYPE VARCHAR(255);