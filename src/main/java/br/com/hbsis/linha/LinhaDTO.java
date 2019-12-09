package br.com.hbsis.linha;

import br.com.hbsis.categoria.Categoria;

public class LinhaDTO {
    private Long id;
    private String nome;
    private Categoria categorialinha;
    private String codigolinha;

    public LinhaDTO(Long id, String nome, Categoria categorialinha, String codigolinha){
        this.id = id;
        this.nome = nome;
        this.categorialinha = categorialinha;
        this.codigolinha = codigolinha;
    }

    public static LinhaDTO of(Linha linha){
        return new LinhaDTO(
            linha.getId(),
            linha.getNome(),
            linha.getCategorialinha(),
            linha.getCodigolinha()
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

    public Categoria getCategorialinha() {
        return categorialinha;
    }

    public void setCategorialinha(Categoria categorialinha) {
        this.categorialinha = categorialinha;
    }

    public String getCodigolinha() {
        return codigolinha;
    }

    public void setCodigolinha(String codigolinha) {
        this.codigolinha = codigolinha;
    }
}
