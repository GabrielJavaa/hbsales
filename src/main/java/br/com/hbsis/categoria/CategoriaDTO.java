package br.com.hbsis.categoria;

import br.com.hbsis.fornecedor.Fornecedor;

public class CategoriaDTO {
    private Long id;
    private String nomeCategoria;
    private String codigoCategoria;
    private Fornecedor fornecedor;


    public CategoriaDTO(Long id, String nomeCategoria, String codigoCategoria) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
        this.codigoCategoria = codigoCategoria;

    }

    public static CategoriaDTO of(Categoria categoria){
        return new CategoriaDTO(
            categoria.getId(),
            categoria.getNomeCategoria(),
            categoria.getCodigoCategoria()


        );
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(String codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public String toString() {
        return "CategoriaDTO{" +
                "id=" + id +
                ", nomeCategoria='" + nomeCategoria + '\'' +
                ", codigoCategoria='" + codigoCategoria + '\'' +
                '}';
    }
}
