package br.com.hbsis.fornecedor;



import javax.persistence.*;


@Entity
@Table(name = "fornecedor")

public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "razao_social", unique = true, length = 60)
    private String RazaoSocial;
    @Column(name = "cnpj_id", unique = true, length = 20)
    private String CNPJ;
    @Column(name = "nome_fantasia", unique = false, length = 60)
    private String NomeFantasia;
    @Column(name = "endereco", unique = false, length = 60)
    private String Endereco;
    @Column(name = "telefone", unique = true, length = 11)
    private String Telefone;
    @Column(name = "email", unique = true, length = 30)
    private String Email;


    public Long getId() {
        return id;
    }

    public String getRazaoSocial() {
        return RazaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.RazaoSocial = razaoSocial;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public String getNomeFantasia() {
        return NomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {

        this.NomeFantasia = nomeFantasia;
    }

    public String getEndereco() {

        return Endereco;
    }

    public void setEndereco(String endereco) {

        this.Endereco = endereco;
    }

    public String getTelefone() {

        return Telefone;
    }

    public void setTelefone(String telefone) {

        this.Telefone = telefone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {

        this.Email = email;
    }
}
