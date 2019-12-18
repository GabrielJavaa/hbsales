package br.com.hbsis.linha;


import br.com.hbsis.categoria.Categoria;


import javax.persistence.*;

@Entity
@Table(name = "linha")
public class Linha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nome", unique = false, nullable = false, length = 40)
    private String nome;

    @ManyToOne
    @JoinColumn(name ="categorialinha" , referencedColumnName = "id")
    private Categoria categoria;

    @Column(name = "codigolinha", unique = true, nullable = false, length = 10)
    private String codigolinha;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categorialinha) {
        this.categoria = categorialinha;
    }

    public String getCodigolinha() {
        return codigolinha;
    }

    public void setCodigolinha(String codigolinha) {
        this.codigolinha = codigolinha;
    }

    @Override
    public String toString() {
        return "linha{" +
                "id=" + id +
                ", nome ='" + nome + '\'' +
                ", categorialinha   ='" + categoria + '\'' +
                ", codigolinha  ='" + codigolinha + '\'' +
                '}';
    }
}
