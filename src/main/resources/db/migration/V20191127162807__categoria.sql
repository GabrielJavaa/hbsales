create table categoria(

    id BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    nome_categoria VARCHAR(40) NOT NULL,
    idfornecedor_categoria BIGINT NOT NULL,
    codigo_categoria VARCHAR (10) NOT NULL,
    CONSTRAINT fk_forn FOREIGN KEY (idfornecedor_categoria) REFERENCES fornecedor (id)

);

    create unique index ix_categoria_01 on categoria (nome_categoria asc);

