create table categoria(

    id BIGINT IDENTITY(1,1) NOT NULL,
    nome_categoria VARCHAR(40) NOT NULL,
    fornecedor_categoria BIGINT NOT NULL,
    codigo_categoria INTEGER NOT NULL,
    CONSTRAINT fk_forn FOREIGN KEY (fornecedor_categoria) REFERENCES fornecedor (id)

);

    create unique index ix_categoria_01 on categoria (nome_categoria asc);