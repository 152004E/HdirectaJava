package com.exe.Huerta_directa.DTO;



import java.math.BigDecimal;

public class ItemDTO {

    private String title;
    private BigDecimal unitPrice;
    private Integer quantity;

    public ItemDTO() {}

    public ItemDTO(String title, BigDecimal unitPrice, Integer quantity) {
        this.title = title;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "title='" + title + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                '}';
    }
}
