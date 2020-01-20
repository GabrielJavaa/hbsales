create table pedido(
    id BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
    codigo VARCHAR NOT NULL,
    status VARCHAR NOT NULL,
    dataPedido DATE NOT NULL,
    fornecedorid BIGINT NOT NULL,
    produtoid BIGINT NOT NULL,
    periodoid BIGINT NOT NULL,
    funcionarioid BIGINT NOT NULL
   );

   ALTER TABLE pedido ADD CONSTRAINT fk_forne FOREIGN KEY ("fornecedorid") REFERENCES fornecedor (id);
   ALTER TABLE pedido ADD CONSTRAINT fk_produ FOREIGN KEY ("produtoid") REFERENCES produto (id);
   ALTER TABLE pedido ADD CONSTRAINT fk_perio FOREIGN KEY ("periodoid") REFERENCES periodo (id);
   ALTER TABLE pedido ADD CONSTRAINT fk_funcio FOREIGN KEY ("funcionarioid") REFERENCES funcionario (id)

