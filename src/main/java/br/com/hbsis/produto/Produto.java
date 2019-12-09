package br.com.hbsis.produto;


import br.com.hbsis.linha.Linha;
import com.opencsv.bean.CsvBindByPosition;

import javax.persistence.*;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CsvBindByPosition(position = 0)
    private long id;

    @Column(name = "codigo_produto",nullable = false , length = 10)
    @CsvBindByPosition(position = 1)
    private String codigoProduto;

    @Column(name = "nome", nullable = false, length = 40)
    @CsvBindByPosition(position = 2)
    private String nome;

    @Column(name = "preco", nullable = false)
    @CsvBindByPosition(position = 3)
    private float preco;

    @ManyToOne
    @JoinColumn(name = "linha_categoria", referencedColumnName = "id")
    @CsvBindByPosition(position = 4)
    private Linha linhaCategoria;

    @Column(name = "unidade_caixa", nullable = false, length = 40)
    @CsvBindByPosition(position = 5)
    private String unidadeCaixa;

    @Column(name = "peso_unidade", nullable = false)
    @CsvBindByPosition(position = 6)
    private float pesoUnidade;

    @Column(name = "validade", nullable = false)
    @CsvBindByPosition(position = 7)
    private int validade;


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

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
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

    public int getValidade() {
        return validade;
    }

    public void setValidade(int validade) {
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
                ", validade='" + validade + '\'' +
                '}';
    }
}
