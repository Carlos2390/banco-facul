-- Alterar a coluna numero de Endereco para INT
ALTER TABLE Endereco
ALTER COLUMN numero TYPE INT USING numero::integer;

-- Alterar a coluna cep de Endereco para INT
ALTER TABLE Endereco
ALTER COLUMN cep TYPE INT USING cep::integer;

-- Alterar a coluna numero_conta de Conta para INT
ALTER TABLE Conta
ALTER COLUMN numero_conta TYPE INT USING numero_conta::integer;