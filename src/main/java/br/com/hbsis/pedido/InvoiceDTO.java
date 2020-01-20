package br.com.hbsis.pedido;

import br.com.hbsis.api.invoice.InvoiceItemDTO;
import br.com.hbsis.item.Item;

import java.util.List;

public class InvoiceDTO {
    private String cnpjFornecedor;
    private String employeeUuid;
    private List<InvoiceItemDTO> invoiceItemDTOSet;
    private Double total;

    public InvoiceDTO(String cnpjFornecedor, String employeeUuid, List<InvoiceItemDTO> invoiceItemDTOSet, Double total) {
        this.cnpjFornecedor = cnpjFornecedor;
        this.employeeUuid = employeeUuid;
        this.invoiceItemDTOSet = invoiceItemDTOSet;
        this.total = total;
    }
    public static InvoiceDTO dados(String cnpjFornecedor, String employeeUuid, List<Item> items, Double total){
        return new InvoiceDTO(cnpjFornecedor,employeeUuid,InvoiceItemDTO.dados(items), total);
    }


    public String getCnpjFornecedor() {
        return cnpjFornecedor;
    }

    public void setCnpjFornecedor(String cnpjFornecedor) {
        this.cnpjFornecedor = cnpjFornecedor;
    }

    public String getEmployeeUuid() {
        return employeeUuid;
    }

    public void setEmployeeUuid(String employeeUuid) {
        this.employeeUuid = employeeUuid;
    }

    public List<InvoiceItemDTO> getInvoiceItemDTOSet() {
        return invoiceItemDTOSet;
    }

    public void setInvoiceItemDTOSet(List<InvoiceItemDTO> invoiceItemDTOSet) {
        this.invoiceItemDTOSet = invoiceItemDTOSet;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
