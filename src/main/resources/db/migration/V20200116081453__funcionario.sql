create table funcionario(
    id BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    nome VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    uuid VARCHAR(36) NOT NULL
)