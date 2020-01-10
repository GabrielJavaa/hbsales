package br.com.hbsis.periodo;

import java.time.LocalDate;

public class PeriodoDTO {
    private Long id;
    private LocalDate dataInicioVendas;
    private LocalDate dataFimVendas;
    private Long fornecedor;
    private LocalDate dataRetiradaPedidos;
    private String descricao;

    public PeriodoDTO() {
    }

    public PeriodoDTO(Long id, LocalDate dataInicioVendas, LocalDate dataFimVendas, Long fornecedor, LocalDate dataRetiradaPedidos, String descricao) {
        this.id = id;
        this.dataInicioVendas = dataInicioVendas;
        this.dataFimVendas = dataFimVendas;
        this.fornecedor = fornecedor;
        this.dataRetiradaPedidos = dataRetiradaPedidos;
        this.descricao = descricao;
    }

    public static PeriodoDTO of(Periodo periodo) {
        return new PeriodoDTO(
                periodo.getId(),
                periodo.getDataInicioVendas(),
                periodo.getDataFimVendas(),
                periodo.getFornecedor().getId(),
                periodo.getDataRetiradaPedidos(),
                periodo.getDescricao()
        );
    }

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

    public Long getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Long fornecedor) {
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
        return "PeriodoDTO{" +
                "id=" + id +
                ", dataInicioVendas=" + dataInicioVendas +
                ", dataFimVendas=" + dataFimVendas +
                ", fornecedor=" + fornecedor +
                ", dataRetiradaPedidos=" + dataRetiradaPedidos +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
