package org.hosp1.billing.model;

import java.math.BigDecimal;

/** POJO: one line on a bill (consultation, medicine, room charge ...). */
public class BillItem {

    private String description;
    private int quantity;
    private BigDecimal unitPrice;

    public BillItem(String description, int quantity, BigDecimal unitPrice) {
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    /** Derived value, quantity x unit price. */
    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return description + " x" + quantity + " @ " + unitPrice;
    }
}