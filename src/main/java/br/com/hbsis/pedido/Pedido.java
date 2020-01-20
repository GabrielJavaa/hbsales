package br.com.hbsis.pedido;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.periodo.Periodo;
import br.com.hbsis.produto.Produto;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", unique = true)
    private String codigoUnico;
    @Column(name = "status")
    private String statusPedido;
    @Column(name = "dataPedido")
    private LocalDate dataCriacaoPedido;
    @ManyToOne
    @JoinColumn(name = "fornecedorid", referencedColumnName = "id")
    private Fornecedor fornecedor;
    @ManyToOne
    @JoinColumn(name = "produtoid", referencedColumnName = "id")
    private Produto produto;
    @ManyToOne
    @JoinColumn(name = "periodoid", referencedColumnName = "id")
    private Periodo periodo;
    @ManyToOne
    @JoinColumn(name = "funcionarioid", referencedColumnName = "id")
    private Funcionario funcionario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public String getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(String statusPedido) {
        this.statusPedido = statusPedido;
    }

    public LocalDate getDataCriacaoPedido() {
        return dataCriacaoPedido;
    }

    public void setDataCriacaoPedido(LocalDate dataCriacaoPedido) {
        this.dataCriacaoPedido = dataCriacaoPedido;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}
