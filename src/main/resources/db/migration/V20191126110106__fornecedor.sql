create table fornecedor(

    id BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    razao_social VARCHAR (60) NOT NULL,
    cnpj_id    VARCHAR(14) UNIQUE NOT NULL,
    nome_fantasia   VARCHAR (60) NOT NULL,
    endereco    VARCHAR (40) NOT NULL,
    telefone    VARCHAR (15) NOT NULL,
    email       VARCHAR (30) NOT NULL,


);

    create unique index ix_fornecedor_01 on fornecedor (razao_social asc);
    create unique index ix_fornecedor_02 on fornecedor (cnpj_id asc);
    create unique index ix_fornecedor_03 on fornecedor (nome_fantasia asc);
    create unique index ix_fornecedor_04 on fornecedor (telefone asc);



