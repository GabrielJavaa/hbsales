package br.com.hbsis.produto;


import br.com.hbsis.linha.Linha;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "codigo_produto", nullable = false, length = 10)
    private String codigoProduto;

    @Column(name = "nome", nullable = false, length = 40)
    private String nome;

    @Column(name = "preco", nullable = false, length = 5)
    private double preco;

    @ManyToOne
    @JoinColumn(name = "linha_categoria", referencedColumnName = "id")
    private Linha linhaCategoria;

    @Column(name = "unidade_caixa", nullable = false, length = 40)
    private String unidadeCaixa;

    @Column(name = "peso_unidade", nullable = false)
    private float pesoUnidade;

    @Column(name = "unidademedida", nullable = false, length = 5)
    private String unidademedida;

    @Column(name = "validade", nullable = false)
    private LocalDate validade;


    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
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

    public float getPesoUnidade() {
        return pesoUnidade;
    }

    public void setPesoUnidade(float pesoUnidade) {
        this.pesoUnidade = pesoUnidade;
    }

    public String getUnidadeMedida() { return unidademedida; }

    public void setUnidadeMedida(String unidademedida) { this.unidademedida = unidademedida; }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", codigoProduto='" + codigoProduto + '\'' +
                ", nome='" + nome + '\'' +
                ", preco='" + preco + '\'' +
                ", linhaCategoria='" + linhaCategoria + '\'' +
                ", unidadeCaixa='" + unidadeCaixa + '\'' +
                ", pesoUnidade='" + pesoUnidade + '\'' +
                ", unidademedida='" + unidademedida + '\'' +
                ", validade='" + validade + '\'' +
                '}';
    }
}
