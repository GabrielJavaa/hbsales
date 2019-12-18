package br.com.hbsis.linha;
import br.com.hbsis.categoria.Categoria;


public class LinhaDTO {
    private Long id;
    private String nome;
    private String codigolinha;
    private Long categorialinha;

    public LinhaDTO(Long id, String nome, String codigolinha, Long categorialinha){
        this.id = id;
        this.nome = nome;
        this.codigolinha = codigolinha;
        this.categorialinha = categorialinha;
    }

    public static LinhaDTO of(Linha linha){
        return new LinhaDTO(
            linha.getId(),
            linha.getNome(),
            linha.getCodigolinha(),
            linha.getCategoria().getId()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigolinha() {
        return codigolinha;
    }

    public void setCodigolinha(String codigolinha) {
        this.codigolinha = codigolinha;
    }

    public Long getCategorialinha() {
        return categorialinha;
    }

    public void setCategorialinha(Long categorialinha) {
        this.categorialinha = categorialinha;
    }
}
