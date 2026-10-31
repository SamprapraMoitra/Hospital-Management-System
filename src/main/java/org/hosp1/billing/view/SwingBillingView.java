package org.hosp1.billing.view;

import org.hosp1.billing.model.Bill;
import org.hosp1.billing.model.BillItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Swing window for billing staff. Displays and collects input only. */
public class SwingBillingView implements BillingView {

    private final JFrame frame = new JFrame("Hospital Management - Billing");

    private final DefaultTableModel billModel =
            readOnlyModel("Bill ID", "Patient ID", "Date", "Total", "Paid", "Balance", "Status");
    private final JTable billTable = new JTable(billModel);
    private final List<String> billIds = new ArrayList<>();   // row index -> bill id

    private final JTextField patientIdField = new JTextField(10);
    private final JTextField itemDescField = new JTextField(14);
    private final JTextField qtyField = new JTextField(3);
    private final JTextField priceField = new JTextField(7);
    private final JTextField paymentField = new JTextField(8);

    private final DefaultListModel<String> draftModel = new DefaultListModel<>();
    private final JLabel draftTotal = new JLabel("Draft total: 0.00");

    private final JButton addItemButton = new JButton("Add Item");
    private final JButton createBillButton = new JButton("Create Bill");
    private final JButton recordPaymentButton = new JButton("Record Payment");
    private final JButton cancelBillButton = new JButton("Cancel Bill");
    private final JButton refreshButton = new JButton("Refresh");

    public SwingBillingView() {
        billTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.add(refreshButton);

        frame.setLayout(new BorderLayout(6, 6));
        frame.add(top, BorderLayout.NORTH);
        frame.add(new JScrollPane(billTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(1, 2, 8, 8));
        bottom.add(buildNewBillPanel());
        bottom.add(buildPaymentPanel());
        frame.add(bottom, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   // closing does not exit the app
        frame.setSize(900, 560);
        frame.setLocationRelativeTo(null);
    }

    private JPanel buildNewBillPanel() {
        JPanel fields = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        fields.add(new JLabel("Patient ID:"));
        fields.add(patientIdField);
        fields.add(new JLabel("Item:"));
        fields.add(itemDescField);
        fields.add(new JLabel("Qty:"));
        fields.add(qtyField);
        fields.add(new JLabel("Price:"));
        fields.add(priceField);
        fields.add(addItemButton);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.add(createBillButton);
        footer.add(draftTotal);

        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBorder(BorderFactory.createTitledBorder("New bill"));
        p.add(fields, BorderLayout.NORTH);
        p.add(new JScrollPane(new JList<>(draftModel)), BorderLayout.CENTER);
        p.add(footer, BorderLayout.SOUTH);
        p.setPreferredSize(new Dimension(450, 190));
        return p;
    }

    private JPanel buildPaymentPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        p.setBorder(BorderFactory.createTitledBorder("Selected bill"));
        p.add(new JLabel("Amount:"));
        p.add(paymentField);
        p.add(recordPaymentButton);
        p.add(cancelBillButton);
        return p;
    }

    private static DefaultTableModel readOnlyModel(String... cols) {
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private static String money(BigDecimal v) { return String.format("%.2f", v); }

    // ---- display ----
    @Override public void show() { frame.setVisible(true); frame.toFront(); }
    @Override public void close() { frame.dispose(); }

    @Override
    public void showBills(List<Bill> bills) {
        billModel.setRowCount(0);
        billIds.clear();
        for (Bill b : bills) {
            billModel.addRow(new Object[]{b.getId(), b.getPatientId(), b.getIssuedDate(),
                    money(b.getTotal()), money(b.getAmountPaid()), money(b.getBalance()), b.getStatus()});
            billIds.add(b.getId());
        }
    }

    @Override
    public void showDraft(List<BillItem> items, BigDecimal total) {
        draftModel.clear();
        for (BillItem i : items) {
            draftModel.addElement(i.getDescription() + "  x" + i.getQuantity()
                    + "  @ " + money(i.getUnitPrice()) + "  = " + money(i.getLineTotal()));
        }
        draftTotal.setText("Draft total: " + money(total));
    }

    @Override public void showMessage(String m) {
        JOptionPane.showMessageDialog(frame, m, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    @Override public void showError(String m) {
        JOptionPane.showMessageDialog(frame, m, "Error", JOptionPane.ERROR_MESSAGE);
    }
    @Override public boolean confirm(String q) {
        return JOptionPane.showConfirmDialog(frame, q, "Confirm", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION;
    }

    // ---- input ----
    @Override public String getPatientId() { return patientIdField.getText().trim(); }
    @Override public String getItemDescription() { return itemDescField.getText().trim(); }
    @Override public String getItemQuantity() { return qtyField.getText().trim(); }
    @Override public String getItemUnitPrice() { return priceField.getText().trim(); }
    @Override public String getPaymentAmount() { return paymentField.getText().trim(); }

    @Override
    public String getSelectedBillId() {
        int row = billTable.getSelectedRow();
        return (row < 0 || row >= billIds.size()) ? null : billIds.get(row);
    }

    @Override public void clearItemFields() {
        itemDescField.setText("");
        qtyField.setText("");
        priceField.setText("");
    }
    @Override public void clearPaymentField() { paymentField.setText(""); }
    @Override public void clearNewBillForm() {
        patientIdField.setText("");
        clearItemFields();
    }

    // ---- actions ----
    @Override public void setAddItemAction(Runnable a) { addItemButton.addActionListener(e -> a.run()); }
    @Override public void setCreateBillAction(Runnable a) { createBillButton.addActionListener(e -> a.run()); }
    @Override public void setRecordPaymentAction(Runnable a) { recordPaymentButton.addActionListener(e -> a.run()); }
    @Override public void setCancelBillAction(Runnable a) { cancelBillButton.addActionListener(e -> a.run()); }
    @Override public void setRefreshAction(Runnable a) { refreshButton.addActionListener(e -> a.run()); }
}