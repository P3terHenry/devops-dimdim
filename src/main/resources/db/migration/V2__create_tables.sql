CREATE TABLE t_dimdim_usuario (
                                  id_usuario BIGINT IDENTITY(1,1) PRIMARY KEY,
                                  nome_usuario VARCHAR(100) NOT NULL,
                                  email_usuario VARCHAR(120) NOT NULL UNIQUE,
                                  senha_usuario VARCHAR(255) NOT NULL
);

CREATE TABLE t_dimdim_categoria (
                                    id_categoria BIGINT IDENTITY(1,1) PRIMARY KEY,
                                    nome_categoria VARCHAR(100) NOT NULL,
                                    descricao VARCHAR(255) NOT NULL,
                                    usuario_categoria_id BIGINT NOT NULL,
                                    data_criacao DATETIME2 NOT NULL
);

CREATE TABLE t_dimdim_lancamento (
                                     id_lancamento BIGINT IDENTITY(1,1) PRIMARY KEY,
                                     valor_lancamento FLOAT NOT NULL,
                                     data_lancamento DATETIME2 NOT NULL,
                                     tipo_lancamento VARCHAR(20) NOT NULL,
                                     categoria_lancamento_id BIGINT NOT NULL
);
