package org.hosp1.billing.controller;

import org.hosp1.billing.model.Bill;
import org.hosp1.billing.model.BillRepository;
import org.hosp1.billing.model.PaymentStatus;
import org.hosp1.billing.view.PatientBillingView;
import org.hosp1.patients.model.Patient;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/** Controls the patient's billing screen. A patient can only see and pay their own bills. */
public class PatientBillingController {

    private final PatientBillingView view;
    private final BillRepository billRepo;
    private final Patient patient;
    private final Runnable onLogout;

    public PatientBillingController(PatientBillingView view, BillRepository billRepo,
                                    Patient patient, Runnable onLogout) {
        this.view = view;
        this.billRepo = billRepo;
        this.patient = patient;
        this.onLogout = onLogout;

        view.setPayAction(this::pay);
        view.setLogoutAction(this::logout);
    }

    public void start() {
        view.setWelcome(patient.getName());
        refresh();
        view.show();
    }

    public void refresh() {
        List<Bill> bills = billRepo.findByPatientId(patient.getUserId());
        bills.sort(Comparator.comparing(Bill::getIssuedDate).reversed());
        view.showBills(bills);

        BigDecimal due = bills.stream()
                .filter(b -> b.getStatus() != PaymentStatus.CANCELLED)
                .map(Bill::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        view.setOutstanding(due);
    }

    public void pay() {
        String id = view.getSelectedBillId();
        if (id == null) {
            view.showError("Select a bill first.");
            return;
        }
        Bill bill = billRepo.findById(id).orElse(null);
        if (bill == null || !bill.getPatientId().equals(patient.getUserId())) {   // never touch someone else's bill
            view.showError("Bill not found.");
            return;
        }
        BigDecimal amount = PaymentRules.parseMoney(view.getPaymentAmount());
        String error = PaymentRules.applyPayment(bill, amount);
        if (error != null) {
            view.showError(error);
            return;
        }
        billRepo.save(bill);
        view.clearPaymentField();
        refresh();
        view.showMessage("Payment of " + amount + " received. Thank you.");
    }

    public void logout() {
        view.close();
        onLogout.run();
    }
}