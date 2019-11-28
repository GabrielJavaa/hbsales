package br.com.hbsis.categoria;

public class CategoriaDTO {
    private Long id;
    private String nomeCategoria;
    private String fornecedorCategoria;
    private String codigoCategoria;



    public CategoriaDTO() {
    }

    public CategoriaDTO(long id, String nomeCategoria, String fornecedorCategoria, String codigoCategoria ){
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
    public long getId() {
        return id;
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

    public String getCodigoCategoria() { return codigoCategoria; }

    public void setCodigoCategoria(String codigoCategoria) { this.codigoCategoria = codigoCategoria; }

}
