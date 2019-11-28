package br.com.hbsis.fornecedor;

public class FornecedorDTO {

    private Long Id;
    private String razaoSocial;
    private String cnpj;
    private String nomeFantasia;
    private String endereco;
    private String telefone;
    private String email;


    public FornecedorDTO() {
    }

    public FornecedorDTO(Long Id, String razaoSocial, String cnpj, String nomeFantasia, String endereco, String telefone, String email ){
        this.Id = Id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;


    }

    public static FornecedorDTO of(Fornecedor fornecedor){
        return new FornecedorDTO(
            fornecedor.getId(),
            fornecedor.getRazaoSocial(),
            fornecedor.getCNPJ(),
            fornecedor.getNomeFantasia(),
            fornecedor.getEndereco(),
            fornecedor.getTelefone(),
            fornecedor.getEmail()

        );


    }

    public Long getId() { return Id; }

    public String getRazaoSocial() { return razaoSocial; }

    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getCnpj() { return cnpj; }

    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getNomeFantasia() { return nomeFantasia; }

    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }

    public String getEndereco() { return endereco; }

    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTelefone() { return telefone; }

    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

}
