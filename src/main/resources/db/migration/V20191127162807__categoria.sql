create table categoria(

    id BIGINT IDENTITY(1,1) NOT NULL,
    nome_categoria VARCHAR(40) NOT NULL,
    fornecedor_categoria VARCHAR(40) NOT NULL,
    codigo_categoria VARCHAR(40) NOT NULL

);

    create unique index ix_categoria_01 on categoria (nome_categoria asc);