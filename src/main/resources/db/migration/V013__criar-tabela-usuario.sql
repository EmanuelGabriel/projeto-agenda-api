
CREATE TABLE IF NOT EXISTS usuario (
 codigo SERIAL NOT NULL PRIMARY KEY,
 nome VARCHAR(150) NOT NULL,
 email VARCHAR(70) NOT NULL,
 senha VARCHAR(150) NOT NULL,
 ativo BOOLEAN NOT NULL DEFAULT TRUE,
 data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
 
);


CREATE TABLE IF NOT EXISTS permissao (
 codigo SERIAL NOT NULL PRIMARY KEY,
 descricao VARCHAR(50) NOT NULL
 
);

CREATE TABLE IF NOT EXISTS usuario_permissao (
codigo_usuario SERIAL NOT NULL,
codigo_permissao SERIAL NOT NULL,
PRIMARY KEY (codigo_usuario, codigo_permissao),
FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo),
FOREIGN KEY (codigo_permissao) REFERENCES permissao(codigo)

);

-- INSERE NOVOS USUARIOS
INSERT INTO usuario (codigo, nome, email, senha) values (1, 'Administrador', 'admin@email.com.br', '$2a$10$5X59swFWDvc4xXnf.5Zy1.mhsUZNZg.pD5qmDcUL9kLQMSP/jPvpG');
INSERT INTO usuario (codigo, nome, email, senha) values (2, 'Usuário', 'usuario@email.com.br', '$2a$10$a/4AaHbkglff9Q9ade9XU.svsG13IZDnsfIKk.OfY/aasmULUU/Xe');


-- INSERE AS PERMISSOES
INSERT INTO permissao (codigo, descricao) values (1, 'ROLE_CADASTRAR_CATEGORIA');
INSERT INTO permissao (codigo, descricao) values (2, 'ROLE_PESQUISAR_CATEGORIA');
INSERT INTO permissao (codigo, descricao) values (3, 'ROLE_REMOVER_CONTATO');


INSERT INTO permissao (codigo, descricao) values (4, 'ROLE_CADASTRAR_CLIENTE');
INSERT INTO permissao (codigo, descricao) values (5, 'ROLE_PESQUISAR_CLIENTE');
INSERT INTO permissao (codigo, descricao) values (6, 'ROLE_REMOVER_CLIENTE');

INSERT INTO permissao (codigo, descricao) values (7, 'ROLE_CADASTRAR_CONTATO');
INSERT INTO permissao (codigo, descricao) values (8, 'ROLE_PESQUISAR_CONTATO');
INSERT INTO permissao (codigo, descricao) values (9, 'ROLE_REMOVER_CONTATO');

-- INSERE AS PERMISSOES DO USUARIO - ADMIN
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 1);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 2);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 3);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 4);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 5);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 6);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 7);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 8);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 9);


-- INSERE AS PERMISSÕES O USUÁRIO - USUARIO
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (2, 1);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (2, 2);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (2, 4);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (2, 5);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (2, 7);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (2, 8);







