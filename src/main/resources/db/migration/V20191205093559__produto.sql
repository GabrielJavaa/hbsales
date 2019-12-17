create table produto(

    id BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    codigo_produto VARCHAR (10) NOT NULL,
    nome VARCHAR (40) NOT NULL,
    preco FLOAT NOT NULL,
    linha_categoria BIGINT NOT NULL,
    unidade_caixa VARCHAR (40) NOT NULL,
    peso_unidade FLOAT NOT NULL,
    validade INT NOT NULL
    CONSTRAINT fk_prod FOREIGN KEY (linha_categoria) REFERENCES linha (id)
);