package br.com.hbsis.produto;

import java.time.LocalDate;

public class ProdutoDTO {
    private Long id;
    private String codigoProduto;
    private String nome;
    private Double preco;
    private Long linhaCategoria;
    private String unidadeCaixa;
    private Float pesoUnidade;
    private String unidadeMedida;
    private LocalDate validade;

    public ProdutoDTO(Long id, String codigoProduto, String nome, Double preco, Long linhaCategoria, String unidadeCaixa, Float pesoUnidade, String unidadeMedida, LocalDate validade) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.nome = nome;
        this.preco = preco;
        this.linhaCategoria = linhaCategoria;
        this.unidadeCaixa = unidadeCaixa;
        this.pesoUnidade = pesoUnidade;
        this.unidadeMedida = unidadeMedida;
        this.validade = validade;
    }

    public static ProdutoDTO of(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getCodigoProduto(),
                produto.getNome(),
                produto.getPreco(),
                produto.getLinhaCategoria().getId(),
                produto.getUnidadeCaixa(),
                produto.getPesoUnidade(),
                produto.getUnidadeMedida(),
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

    public void setNome(String nome) { this.nome = nome; }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Long getLinhaCategoria() { return linhaCategoria; }

    public void setLinhaCategoria(Long linhaCategoria) {
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

    public String getUnidadeMedida() { return unidadeMedida; }

    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
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
                ", unidademedida='" + unidadeMedida + '\''+
                ", validade='" + validade + '\'' +
                '}';
    }
}
