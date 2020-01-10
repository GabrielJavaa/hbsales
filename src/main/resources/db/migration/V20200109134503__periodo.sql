create table periodo
(
    data_inicio_vendas DATE NOT NULL,
    data_fim_vendas DATE NOT NULL,
    fornecedor_id BIGINT NOT NULL,
    data_retirada_pedido  DATE NOT NULL,
    descricao VARCHAR (50) NOT NULL,
    CONSTRAINT fk_peri FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id)
);