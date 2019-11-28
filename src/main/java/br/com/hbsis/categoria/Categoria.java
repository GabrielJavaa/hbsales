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


    @Column(name = "fornecedor_categoria", unique = true, nullable = false, length = 40)
    private String fornecedorCategoria;


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

    public String getFornecedorCategoria() {
        return fornecedorCategoria;
    }

    public void setFornecedorCategoria(String fornecedorCategoria) {
        this.fornecedorCategoria = fornecedorCategoria;
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(String codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }



}