create table linha(

    id BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    nome VARCHAR (40) NOT NULL,
    categorialinha BIGINT NOT NULL,
    codigolinha VARCHAR (10) NOT NULL,
    CONSTRAINT fk_lin FOREIGN KEY (categorialinha) REFERENCES categoria (id)
);