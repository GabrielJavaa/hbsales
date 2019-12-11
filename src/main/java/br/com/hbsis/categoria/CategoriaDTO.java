package br.com.hbsis.categoria;

public class CategoriaDTO {
    private Long id;
    private String nomeCategoria;
    private Long idFornecedorCategoria;
    private String codigoCategoria;


    public CategoriaDTO(Long id, String nomeCategoria, Long idFornecedorCategoria, String codigoCategoria) {
        this.id=id;
        this.nomeCategoria = nomeCategoria;
        this.idFornecedorCategoria = idFornecedorCategoria;
        this.codigoCategoria = codigoCategoria;
    }

    public static CategoriaDTO of(Categoria categoria){
        return new CategoriaDTO(
            categoria.getId(),
            categoria.getNomeCategoria(),
            categoria.getFornecedor().getId(),
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

    public Long getIdFornecedorCategoria() { return idFornecedorCategoria; }

    public void setIdFornecedorCategoria(Long idFornecedorCategoria) { this.idFornecedorCategoria = idFornecedorCategoria; }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(String codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    @Override
    public String toString() {
        return "CategoriaDTO{" +
                "id=" + id +
                ", nomeCategoria='" + nomeCategoria + '\'' +
                ", fornecedorCategoria='" + idFornecedorCategoria + '\'' +
                ", codigoCategoria='" + codigoCategoria + '\'' +
                '}';
    }
}
