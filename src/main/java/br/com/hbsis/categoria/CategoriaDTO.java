package br.com.hbsis.categoria;

import br.com.hbsis.fornecedor.Fornecedor;

public class CategoriaDTO {
    private Long id;
    private String nomeCategoria;
    private Fornecedor fornecedorCategoria;
    private Integer codigoCategoria;


    public CategoriaDTO(Long id, String nomeCategoria, Fornecedor fornecedorCategoria, Integer codigoCategoria) {
        this.id=id;
        this.nomeCategoria = nomeCategoria;
        this.fornecedorCategoria = fornecedorCategoria;
        this.codigoCategoria = codigoCategoria;
    }

    public static CategoriaDTO of(Categoria categoria){
        return new CategoriaDTO(
            categoria.getId(),
            categoria.getNomeCategoria(),
            categoria.getFornecedorCategoria(),
            categoria.getCodigoCategoria()

        );
    }
    public Long getId() {
        return id;
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
}
