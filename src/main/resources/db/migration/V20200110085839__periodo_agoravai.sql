DROP TABLE periodo

create table periodo
(
    id BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    data_inicio_vendas DATE NOT NULL,
    data_fim_vendas DATE NOT NULL,
    fornecedor_id BIGINT NOT NULL,
    data_retirada_pedido  DATE NOT NULL,
    descricao VARCHAR (50) NOT NULL,
    CONSTRAINT fk_peri FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id)
);
