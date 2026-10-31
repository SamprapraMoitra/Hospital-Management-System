package org.hosp1.billing.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bill {

    private String id;
    private String patientId;
    private LocalDate issuedDate;
    private List<BillItem> items;
    private BigDecimal amountPaid;
    private PaymentStatus status;

    public Bill(String id, String patientId, LocalDate issuedDate, List<BillItem> items) {
        this.id = id;
        this.patientId = patientId;
        this.issuedDate = issuedDate;
        this.items = new ArrayList<>(items);
        this.amountPaid = BigDecimal.ZERO.setScale(2);
        this.status = PaymentStatus.UNPAID;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public LocalDate getIssuedDate() { return issuedDate; }
    public void setIssuedDate(LocalDate issuedDate) { this.issuedDate = issuedDate; }

    public List<BillItem> getItems() { return items; }
    public void setItems(List<BillItem> items) { this.items = new ArrayList<>(items); }

    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    /** Derived values (computed from the fields, not stored). */
    public BigDecimal getTotal() {
        return items.stream().map(BillItem::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getBalance() {
        return getTotal().subtract(amountPaid);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Bill b && Objects.equals(id, b.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Bill{id=" + id + ", patient=" + patientId + ", total=" + getTotal() + ", status=" + status + "}";
    }
}