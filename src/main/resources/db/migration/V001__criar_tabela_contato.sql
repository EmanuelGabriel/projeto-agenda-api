CREATE TABLE contato (
 codigo SERIAL NOT NULL PRIMARY KEY,
 nome VARCHAR(150) NOT NULL,
 email VARCHAR(150) NOT NULL,
 favorito BOOLEAN
);