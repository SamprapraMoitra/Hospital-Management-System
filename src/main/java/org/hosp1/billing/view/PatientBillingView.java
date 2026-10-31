package org.hosp1.billing.view;

import org.hosp1.billing.model.Bill;

import java.math.BigDecimal;
import java.util.List;

/** Billing screen for a patient: sees only their own bills and can pay. */
public interface PatientBillingView {

    void show();
    void close();
    void setWelcome(String patientName);
    void showBills(List<Bill> bills);
    void setOutstanding(BigDecimal amount);
    void showMessage(String message);
    void showError(String message);

    String getPaymentAmount();
    String getSelectedBillId();          // null if nothing is selected
    void clearPaymentField();

    void setPayAction(Runnable action);
    void setLogoutAction(Runnable action);
}