package br.com.hbsis.periodo;

import br.com.hbsis.fornecedor.Fornecedor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "periodo")
public class Periodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_inicio_vendas", nullable = false)
    @DateTimeFormat(pattern = "dd-mm-yyyy")
    private LocalDate dataInicioVendas;

    @Column(name = "data_fim_vendas", nullable = false)
    @DateTimeFormat(pattern = "dd-mm-yyyy")
    private LocalDate dataFimVendas;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", referencedColumnName = "id", nullable = false, unique = true)
    private Fornecedor fornecedor;

    @Column(name = "data_retirada_pedido", nullable = false)
    @DateTimeFormat(pattern = "dd-mm-yyyy")
    private LocalDate dataRetiradaPedidos;

    @Column(name = "descricao",nullable = false, length = 50)
    private String descricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataInicioVendas() {
        return dataInicioVendas;
    }

    public void setDataInicioVendas(LocalDate dataInicioVendas) {
        this.dataInicioVendas = dataInicioVendas;
    }

    public LocalDate getDataFimVendas() {
        return dataFimVendas;
    }

    public void setDataFimVendas(LocalDate dataFimVendas) {
        this.dataFimVendas = dataFimVendas;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public LocalDate getDataRetiradaPedidos() {
        return dataRetiradaPedidos;
    }

    public void setDataRetiradaPedidos(LocalDate dataRetiradaPedidos) {
        this.dataRetiradaPedidos = dataRetiradaPedidos;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Periodo{" +
                "id=" + id +
                ", dataInicioVendas=" + dataInicioVendas +
                ", dataFimVendas=" + dataFimVendas +
                ", fornecedor=" + fornecedor +
                ", dataRetiradaPedidos=" + dataRetiradaPedidos +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
