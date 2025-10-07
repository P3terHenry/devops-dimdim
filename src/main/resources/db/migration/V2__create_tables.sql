-- Criação da tabela de usuários
IF OBJECT_ID('dbo.T_DIMDIM_USUARIO', 'U') IS NULL
BEGIN
CREATE TABLE dbo.T_DIMDIM_USUARIO (
                                      ID_USUARIO BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
                                      NOME_USUARIO VARCHAR(100) NOT NULL,
                                      EMAIL_USUARIO VARCHAR(120) NOT NULL UNIQUE,
                                      SENHA_USUARIO VARCHAR(255) NOT NULL
);
END;

-- Criação da tabela de categorias
IF OBJECT_ID('dbo.T_DIMDIM_CATEGORIA', 'U') IS NULL
BEGIN
CREATE TABLE dbo.T_DIMDIM_CATEGORIA (
                                        ID_CATEGORIA BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
                                        NOME_CATEGORIA VARCHAR(100) NOT NULL,
                                        DESCRICAO VARCHAR(255) NOT NULL,
                                        USUARIO_CATEGORIA_ID BIGINT NOT NULL,
                                        DATA_CRIACAO DATETIME2(6) NOT NULL
);
END;

-- Criação da tabela de lançamentos
IF OBJECT_ID('dbo.T_DIMDIM_LANCAMENTO', 'U') IS NULL
BEGIN
CREATE TABLE dbo.T_DIMDIM_LANCAMENTO (
                                         ID_LANCAMENTO BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
                                         VALOR_LANCAMENTO FLOAT NOT NULL,
                                         DATA_LANCAMENTO DATETIME2(6) NOT NULL,
                                         TIPO_LANCAMENTO VARCHAR(20) NOT NULL,
                                         CATEGORIA_LANCAMENTO_ID BIGINT NOT NULL
);
END;
