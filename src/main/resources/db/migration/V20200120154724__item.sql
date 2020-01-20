create table item(
    id BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    quantidade INT NOT NULL,
    pedido_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL
);
ALTER TABLE item ADD CONSTRAINT fk_produto FOREIGN KEY ("produto_id") REFERENCES produto (id);
ALTER TABLE item ADD CONSTRAINT fk_pedido FOREIGN KEY ("pedido_id") REFERENCES pedido (id);

