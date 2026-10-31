package org.hosp1.billing.view;

import org.hosp1.billing.model.Bill;
import org.hosp1.billing.model.BillItem;

import java.math.BigDecimal;
import java.util.List;

/** Billing screen for admin / billing staff. */
public interface BillingView {

    // ---- display ----
    void show();
    void close();
    void showBills(List<Bill> bills);
    void showDraft(List<BillItem> items, BigDecimal total);   // items of the bill being built
    void showMessage(String message);
    void showError(String message);
    boolean confirm(String question);

    // ---- read the current input ----
    String getPatientId();
    String getItemDescription();
    String getItemQuantity();
    String getItemUnitPrice();
    String getPaymentAmount();
    String getSelectedBillId();          // null if nothing is selected
    void clearItemFields();
    void clearPaymentField();
    void clearNewBillForm();

    // ---- controller registers what to run on each click ----
    void setAddItemAction(Runnable action);
    void setCreateBillAction(Runnable action);
    void setRecordPaymentAction(Runnable action);
    void setCancelBillAction(Runnable action);
    void setRefreshAction(Runnable action);
}