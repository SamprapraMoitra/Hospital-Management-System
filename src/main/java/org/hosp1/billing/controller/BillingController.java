package org.hosp1.billing.controller;

import org.hosp1.billing.model.Bill;
import org.hosp1.billing.model.BillItem;
import org.hosp1.billing.model.BillRepository;
import org.hosp1.billing.model.PaymentStatus;
import org.hosp1.billing.view.BillingView;
import org.hosp1.patients.model.PatientRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/** Controls the billing screen for admin / billing staff. */
public class BillingController {

    private final BillingView view;
    private final BillRepository billRepo;
    private final PatientRepository patientRepo;
    private final List<BillItem> draft = new ArrayList<>();   // items of the bill being built

    public BillingController(BillingView view, BillRepository billRepo, PatientRepository patientRepo) {
        this.view = view;
        this.billRepo = billRepo;
        this.patientRepo = patientRepo;

        view.setAddItemAction(this::addItem);
        view.setCreateBillAction(this::createBill);
        view.setRecordPaymentAction(this::recordPayment);
        view.setCancelBillAction(this::cancelBill);
        view.setRefreshAction(this::refresh);
    }

    public void start() {
        refresh();
        refreshDraft();
        view.show();
    }

    public void refresh() {
        List<Bill> bills = billRepo.findAll();
        bills.sort(Comparator.comparing(Bill::getIssuedDate).reversed());
        view.showBills(bills);
    }

    public void addItem() {
        String description = view.getItemDescription();
        if (description.isEmpty()) {
            view.showError("Enter an item description.");
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(view.getItemQuantity());
        } catch (NumberFormatException e) {
            view.showError("Quantity must be a whole number.");
            return;
        }
        BigDecimal price = PaymentRules.parseMoney(view.getItemUnitPrice());
        if (quantity <= 0 || price == null || price.signum() <= 0) {
            view.showError("Quantity and price must be greater than zero.");
            return;
        }
        draft.add(new BillItem(description, quantity, price));
        view.clearItemFields();
        refreshDraft();
    }

    public void createBill() {
        String patientId = view.getPatientId();
        if (patientRepo.findById(patientId).isEmpty()) {
            view.showError("No patient found with ID '" + patientId + "'.");
            return;
        }
        if (draft.isEmpty()) {
            view.showError("Add at least one item first.");
            return;
        }
        String id = "B-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        billRepo.save(new Bill(id, patientId, LocalDate.now(), draft));
        draft.clear();
        view.clearNewBillForm();
        refreshDraft();
        refresh();
        view.showMessage("Bill " + id + " created.");
    }

    public void recordPayment() {
        Bill bill = selectedBill();
        if (bill == null) {
            return;
        }
        String error = PaymentRules.applyPayment(bill, PaymentRules.parseMoney(view.getPaymentAmount()));
        if (error != null) {
            view.showError(error);
            return;
        }
        billRepo.save(bill);
        view.clearPaymentField();
        refresh();
        view.showMessage("Payment recorded. Status: " + bill.getStatus());
    }

    public void cancelBill() {
        Bill bill = selectedBill();
        if (bill == null) {
            return;
        }
        if (bill.getAmountPaid().signum() > 0) {
            view.showError("A bill with payments cannot be cancelled.");
            return;
        }
        if (bill.getStatus() == PaymentStatus.CANCELLED) {
            view.showError("This bill is already cancelled.");
            return;
        }
        if (!view.confirm("Cancel bill " + bill.getId() + "?")) {
            return;
        }
        bill.setStatus(PaymentStatus.CANCELLED);
        billRepo.save(bill);
        refresh();
    }

    private Bill selectedBill() {
        String id = view.getSelectedBillId();
        if (id == null) {
            view.showError("Select a bill first.");
            return null;
        }
        return billRepo.findById(id).orElseGet(() -> {
            view.showError("Bill not found.");
            return null;
        });
    }

    private void refreshDraft() {
        BigDecimal total = draft.stream().map(BillItem::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        view.showDraft(draft, total);
    }
}