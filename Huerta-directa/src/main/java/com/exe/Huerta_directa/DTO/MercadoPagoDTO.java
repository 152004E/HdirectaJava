package com.exe.Huerta_directa.DTO;


import java.util.List;

public class MercadoPagoDTO {

    private Long orderId;
    private List<ItemDTO> items;
    private String payerEmail;

    public MercadoPagoDTO() {}

    public MercadoPagoDTO(Long orderId, List<ItemDTO> items, String payerEmail) {
        this.orderId = orderId;
        this.items = items;
        this.payerEmail = payerEmail;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    @Override
    public String toString() {
        return "MercadoPagoDTO{" +
                "orderId=" + orderId +
                ", items=" + items +
                ", payerEmail='" + payerEmail + '\'' +
                '}';
    }
}
