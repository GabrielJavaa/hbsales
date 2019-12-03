package br.com.hbsis.categoria;


import br.com.hbsis.fornecedor.Fornecedor;
import com.opencsv.bean.CsvBindByPosition;


import javax.persistence.*;


@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CsvBindByPosition(position = 0)
    private long id;


    @Column(name = "nome_categoria", unique = true, nullable = false, length = 40)
    @CsvBindByPosition(position = 1)
    private String nomeCategoria;


    @ManyToOne
    @JoinColumn(name= "fornecedor_categoria", referencedColumnName = "id")
    @CsvBindByPosition(position = 2)
    private Fornecedor fornecedorCategoria;


    @Column(name = "codigo_categoria", unique = true, nullable = false, length = 40)
    @CsvBindByPosition(position = 3)
    private Integer codigoCategoria;


    public Categoria() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public Fornecedor getFornecedorCategoria() {
        return fornecedorCategoria;
    }

    public void setFornecedorCategoria(Fornecedor fornecedorCategoria) {
        this.fornecedorCategoria = fornecedorCategoria;
    }

    public Integer getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(Integer codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }


    @Override
    public String toString() {
        return "categoria{" +
                "id=" + id +
                ", nome_categoria='" + nomeCategoria + '\'' +
                ", fornecedor_categoria='" + fornecedorCategoria + '\'' +
                ", codigo_categoria='" + codigoCategoria + '\'' +
                '}';
    }
}