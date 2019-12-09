package br.com.hbsis.linha;


import br.com.hbsis.categoria.Categoria;
import com.opencsv.bean.CsvBindByPosition;

import javax.persistence.*;

@Entity
@Table(name = "linha")
public class Linha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CsvBindByPosition(position = 0)
    private long id;

    @Column(name = "nome", unique = false, nullable = false, length = 40)
    @CsvBindByPosition(position = 1)
    private String nome;

    @ManyToOne
    @JoinColumn(name ="categorialinha" , referencedColumnName = "id")
    @CsvBindByPosition(position = 2)
    private Categoria categorialinha;

    @Column(name = "codigolinha", unique = true, nullable = false)
    @CsvBindByPosition(position = 3)
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

    @Override
    public String toString() {
        return "linha{" +
                "id=" + id +
                ", nome ='" + nome + '\'' +
                ", categorialinha   ='" + categorialinha + '\'' +
                ", codigolinha  ='" + codigolinha + '\'' +
                '}';
    }
}
