-- Sistema Sagrado — script de criação das tabelas (MySQL)
-- No Aiven o banco já existe (defaultdb).
-- Padres, coroinhas, catequistas e fiéis comuns ficam na mesma tabela.
-- A coluna "tipo" guarda: PADRE, COROINHA, CATEQUISTA ou FIELCOMUM.
CREATE TABLE IF NOT EXISTS fiel (
    cpf VARCHAR(11) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    idade INT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    paroquia_nome VARCHAR(100),
    paroquia_cidade VARCHAR(100),
    padre_paroquia VARCHAR(100),
    padre_cidade VARCHAR(100),
    padre_setor VARCHAR(100),
    escala VARCHAR(100),
    tempo_pastoral VARCHAR(50),
    etapa VARCHAR(50),
    qtd_catequisandos INT
);

CREATE TABLE IF NOT EXISTS missa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data VARCHAR(20) NOT NULL,
    horario VARCHAR(10) NOT NULL,
    padre_cpf VARCHAR(11) NOT NULL,
    FOREIGN KEY (padre_cpf) REFERENCES fiel(cpf)
);

CREATE TABLE IF NOT EXISTS missa_coroinha (
    missa_id BIGINT,
    coroinha_cpf VARCHAR(11),
    PRIMARY KEY (missa_id, coroinha_cpf),
    FOREIGN KEY (missa_id) REFERENCES missa(id),
    FOREIGN KEY (coroinha_cpf) REFERENCES fiel(cpf)
);

CREATE TABLE IF NOT EXISTS sacramento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fiel_cpf VARCHAR(11) NOT NULL,
    tipo VARCHAR(50),
    data DATE,
    observacoes VARCHAR(200),
    FOREIGN KEY (fiel_cpf) REFERENCES fiel(cpf)
);

CREATE TABLE IF NOT EXISTS casamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    noivo_cpf VARCHAR(11) NOT NULL,
    noiva_cpf VARCHAR(11) NOT NULL,
    data DATE,
    horario TIME,
    local VARCHAR(100),
    FOREIGN KEY (noivo_cpf) REFERENCES fiel(cpf),
    FOREIGN KEY (noiva_cpf) REFERENCES fiel(cpf)
);

CREATE TABLE IF NOT EXISTS catequizando (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    idade INT,
    etapa VARCHAR(50),
    responsavel VARCHAR(100),
    telefone VARCHAR(20),
    catequista_cpf VARCHAR(11),
    FOREIGN KEY (catequista_cpf) REFERENCES fiel(cpf)
);

CREATE TABLE IF NOT EXISTS doacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doador_cpf VARCHAR(11),
    descricao VARCHAR(100),
    valor DECIMAL(10,2),
    data DATE,
    forma_pagamento VARCHAR(50),
    FOREIGN KEY (doador_cpf) REFERENCES fiel(cpf)
);

CREATE TABLE IF NOT EXISTS despesa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(100),
    categoria VARCHAR(50),
    valor DECIMAL(10,2),
    data DATE
);

CREATE TABLE IF NOT EXISTS evento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    descricao VARCHAR(200),
    local VARCHAR(100),
    data DATE,
    responsavel_cpf VARCHAR(11),
    FOREIGN KEY (responsavel_cpf) REFERENCES fiel(cpf)
);
