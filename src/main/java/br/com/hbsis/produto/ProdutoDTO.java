package br.com.hbsis.produto;

import br.com.hbsis.linha.Linha;

public class ProdutoDTO {
    private Long id;
    private String codigoProduto;
    private String nome;
    private Float preco;
    private Linha linhaCategoria;
    private String unidadeCaixa;
    private Float pesoUnidade;
    private Integer validade;

    public ProdutoDTO(Long id, String codigoProduto, String nome, Float preco, Linha linhaCategoria, String unidadeCaixa, Float pesoUnidade, Integer validade){
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.nome = nome;
        this.preco = preco;
        this.linhaCategoria = linhaCategoria;
        this.unidadeCaixa = unidadeCaixa;
        this.pesoUnidade = pesoUnidade;
        this.validade = validade;
    }

    public static ProdutoDTO of(Produto produto){
        return new ProdutoDTO(
                produto.getId(),
                produto.getCodigoProduto(),
                produto.getNome(),
                produto.getPreco(),
                produto.getLinhaCategoria(),
                produto.getUnidadeCaixa(),
                produto.getPesoUnidade(),
                produto.getValidade()

        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public Linha getLinhaCategoria() {
        return linhaCategoria;
    }

    public void setLinhaCategoria(Linha linhaCategoria) {
        this.linhaCategoria = linhaCategoria;
    }

    public String getUnidadeCaixa() {
        return unidadeCaixa;
    }

    public void setUnidadeCaixa(String unidadeCaixa) {
        this.unidadeCaixa = unidadeCaixa;
    }

    public Float getPesoUnidade() {
        return pesoUnidade;
    }

    public void setPesoUnidade(Float pesoUnidade) {
        this.pesoUnidade = pesoUnidade;
    }

    public Integer getValidade() {
        return validade;
    }

    public void setValidade(Integer validade) {
        this.validade = validade;
    }
    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "id=" + id +
                ", codigoProduto='" + codigoProduto + '\'' +
                ", nome='" + nome + '\'' +
                ", preco='" + preco + '\'' +
                ", linhaCategoria='" + linhaCategoria + '\'' +
                ", unidadeCaixa='" + unidadeCaixa + '\'' +
                ", pesoUnidade='" + pesoUnidade + '\'' +
                ", validade='" + validade + '\'' +
                '}';
    }
}
