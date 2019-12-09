package br.com.hbsis.fornecedor;

import javax.persistence.*;

@Entity
@Table(name = "fornecedor")

public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "razao_social", unique = true, length = 60)
    private String razaoSocial;
    @Column(name = "cnpj_id", unique = true, nullable = false, length = 14)
    private String cnpj;
    @Column(name = "nome_fantasia", unique = false, length = 60)
    private String nomeFantasia;
    @Column(name = "endereco", unique = false, length = 60)
    private String endereco;
    @Column(name = "telefone", unique = true, length = 13)
    private String telefone;
    @Column(name = "email", unique = true, length = 30)
    private String email;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }

    public String getEndereco() { return endereco; }

    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTelefone() {

        return telefone;
    }

    public void setTelefone(String telefone) {

        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }
    @Override
    public String toString() {
        return "fornecedor{" +
                "id=" + id +
                ", razao_social='" + razaoSocial + '\'' +
                ", cnpj_id='" + cnpj + '\'' +
                ", nome_fantasia='" + nomeFantasia + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
