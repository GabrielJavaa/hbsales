package br.com.hbsis.api.invoice;

import br.com.hbsis.item.Item;

import java.util.ArrayList;
import java.util.List;

public class InvoiceItemDTO {
    private int amount;
    private String itemName;

    public InvoiceItemDTO(int amount, String itemName) {
        this.amount = amount;
        this.itemName = itemName;
    }

    public static List<InvoiceItemDTO> dados(List<Item> itens){
        List<InvoiceItemDTO> invoiceItemDTOS = new ArrayList<>();

        for (Item item : itens){
            invoiceItemDTOS.add(
                    new InvoiceItemDTO(item.getQuantidade(), item.getProduto().getNome())
            );
        }
        return invoiceItemDTOS;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return "InvoiceItemDTO{" +
                "amount=" + amount +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
