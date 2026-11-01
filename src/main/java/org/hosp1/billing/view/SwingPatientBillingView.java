package org.hosp1.billing.view;

import org.hosp1.billing.model.Bill;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Swing window where a patient sees their bills and pays. */
public class SwingPatientBillingView implements PatientBillingView {

    private final JFrame frame = new JFrame("Hospital Management - My Bills");
    private final JLabel welcome = new JLabel(" ");
    private final JLabel outstanding = new JLabel("Outstanding: 0.00");

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Bill ID", "Date", "Total", "Paid", "Balance", "Status"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final List<String> billIds = new ArrayList<>();

    private final JTextField paymentField = new JTextField(8);
    private final JButton payButton = new JButton("Pay");
    private final JButton logoutButton = new JButton("Logout");

    public SwingPatientBillingView() {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        top.add(welcome, BorderLayout.WEST);
        top.add(logoutButton, BorderLayout.EAST);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        bottom.add(outstanding);
        bottom.add(new JLabel("   Amount to pay:"));
        bottom.add(paymentField);
        bottom.add(payButton);

        frame.setLayout(new BorderLayout());
        frame.add(top, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(720, 420);
        frame.setLocationRelativeTo(null);
    }

    private static String money(BigDecimal v) { return String.format("%.2f", v); }

    @Override public void show() { frame.setVisible(true); }
    @Override public void close() { frame.dispose(); }
    @Override public void setWelcome(String name) { welcome.setText("Signed in as " + name); }

    @Override
    public void showBills(List<Bill> bills) {
        model.setRowCount(0);
        billIds.clear();
        for (Bill b : bills) {
            model.addRow(new Object[]{b.getId(), b.getIssuedDate(), money(b.getTotal()),
                    money(b.getAmountPaid()), money(b.getBalance()), b.getStatus()});
            billIds.add(b.getId());
        }
    }

    @Override public void setOutstanding(BigDecimal amount) {
        outstanding.setText("Outstanding: " + money(amount));
    }

    @Override public void showMessage(String m) {
        JOptionPane.showMessageDialog(frame, m, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    @Override public void showError(String m) {
        JOptionPane.showMessageDialog(frame, m, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override public String getPaymentAmount() { return paymentField.getText().trim(); }

    @Override
    public String getSelectedBillId() {
        int row = table.getSelectedRow();
        return (row < 0 || row >= billIds.size()) ? null : billIds.get(row);
    }

    @Override public void clearPaymentField() { paymentField.setText(""); }

    @Override public void setPayAction(Runnable a) { payButton.addActionListener(e -> a.run()); }
    @Override public void setLogoutAction(Runnable a) { logoutButton.addActionListener(e -> a.run()); }
}