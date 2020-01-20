package br.com.hbsis.item;

import br.com.hbsis.produto.Produto;

public class ItemDTO {

    private Long id;
    private int quantidade;
    private Long pedido;
    private Produto produto;

    public ItemDTO(){}

    public ItemDTO(Long id, int quantidade, Long pedido, Produto produto) {
        this.id = id;
        this.quantidade = quantidade;
        this.pedido = pedido;
        this.produto = produto;
    }
    public static ItemDTO of(Item item){
        return new ItemDTO(
                item.getId(),
                item.getQuantidade(),
                item.getPedido().getId(),
                item.getProduto()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Long getPedido() {
        return pedido;
    }

    public void setPedido(Long pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
