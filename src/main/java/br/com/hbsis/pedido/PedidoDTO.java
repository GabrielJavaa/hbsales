package br.com.hbsis.pedido;

import br.com.hbsis.item.ItemDTO;

import java.time.LocalDate;
import java.util.List;

public class PedidoDTO {
    private Long id;
    private String codigoUnico;
    private String statusPedido;
    private LocalDate dataCriacaoPedido;
    private Long fornecedor;

    private Long periodo;
    private Long funcionario;
    private List<ItemDTO> itemDTOS;

    public PedidoDTO(Long id, String codigoUnico, String statusPedido, LocalDate dataCriacaoPedido, Long fornecedor, Long periodo, Long funcionario) {
        this.id = id;
        this.codigoUnico = codigoUnico;
        this.statusPedido = statusPedido;
        this.dataCriacaoPedido = dataCriacaoPedido;
        this.fornecedor = fornecedor;
        this.periodo = periodo;
        this.funcionario = funcionario;
    }
    public static PedidoDTO of(Pedido pedido){
        return new PedidoDTO(
                pedido.getId(),
                pedido.getCodigoUnico(),
                pedido.getStatusPedido(),
                pedido.getDataCriacaoPedido(),
                pedido.getFornecedor().getId(),
                pedido.getPeriodo().getId(),
                pedido.getFuncionario().getId()

        );
    }

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

    public Long getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Long fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Long getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Long periodo) {
        this.periodo = periodo;
    }

    public Long getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Long funcionario) {
        this.funcionario = funcionario;
    }

    public List<ItemDTO> getItemDTOS() {
        return itemDTOS;
    }

    public void setItemDTOS(List<ItemDTO> itemDTOS) {
        this.itemDTOS = itemDTOS;
    }
}
