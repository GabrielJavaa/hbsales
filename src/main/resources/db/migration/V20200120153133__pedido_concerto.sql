alter table pedido
    drop column dataPedido;

    alter table pedido add data_pedido DATE NOT NULL;