package br.com.hbsis.categoria;

import br.com.hbsis.fornecedor.Fornecedor;

import javax.persistence.*;


@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nome_categoria", unique = true, nullable = false, length = 40)
    private String nomeCategoria;

    @ManyToOne
    @JoinColumn(name= "idfornecedor_categoria", referencedColumnName = "id")
    private Fornecedor fornecedor;

    @Column(name = "codigo_categoria", unique = true, nullable = false, length = 40)
    private String codigoCategoria;

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

    public Fornecedor getFornecedor() { return fornecedor; }

    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(String codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }


    @Override
    public String toString() {
        return "categoria{" +
                "id=" + id +
                ", nome_categoria='" + nomeCategoria + '\'' +
                ", idfornecedor_categoria='" + fornecedor + '\'' +
                ", codigo_categoria='" + codigoCategoria + '\'' +
                '}';
    }
}