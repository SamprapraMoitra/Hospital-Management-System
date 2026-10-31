package org.hosp1.admin.view;

import org.hosp1.patients.model.Patient;
import org.hosp1.staff.model.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Swing window for the admin. Only displays and collects input; no business logic. */
public class SwingAdminView implements AdminView {

    private final JFrame frame = new JFrame("Hospital Management - Admin");
    private final JLabel welcome = new JLabel(" ");

    private final DefaultTableModel staffModel = readOnlyModel("ID", "Name", "Email");
    private final JTable staffTable = new JTable(staffModel);
    private final DefaultTableModel patientModel = readOnlyModel("ID", "Name", "Email");
    private final JTable patientTable = new JTable(patientModel);

    private final JTextField nameField = new JTextField(18);
    private final JTextField emailField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);
    private final JTextField phoneField = new JTextField(18);
    private final JTextField genderField = new JTextField(18);
    private final JTextField addressField = new JTextField(18);
    private final JTextField staffIdField = new JTextField(18);
    private final JTextField joiningDateField = new JTextField(LocalDate.now().toString(), 18);
    private final JTextField designationField = new JTextField(18);
    private final JTextField salaryField = new JTextField(18);
    private final JTextField qualificationField = new JTextField(18);
    private final JTextField specializationField = new JTextField(18);

    private final JButton addButton = new JButton("Add Doctor");
    private final JButton removeButton = new JButton("Remove Selected");
    private final JButton refreshButton = new JButton("Refresh");
    private final JButton logoutButton = new JButton("Logout");
    private final JButton billingButton = new JButton("Billing");

    private final List<String> staffIds = new ArrayList<>(); // row index -> staff id

    public SwingAdminView() {
        staffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Staff", buildStaffTab());
        tabs.addTab("Patients", new JScrollPane(patientTable));

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        top.add(welcome, BorderLayout.WEST);
        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        topButtons.add(billingButton);
        topButtons.add(refreshButton);
        topButtons.add(logoutButton);
        top.add(topButtons, BorderLayout.EAST);

        frame.setLayout(new BorderLayout());
        frame.add(top, BorderLayout.NORTH);
        frame.add(tabs, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(760, 480);
        frame.setLocationRelativeTo(null);
    }

    private JPanel buildStaffTab() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("New doctor"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;
        addRow(form, c, 0, "Name:", nameField);
        addRow(form, c, 1, "Email:", emailField);
        addRow(form, c, 2, "Password:", passwordField);
        addRow(form, c, 3, "Phone:", phoneField);
        addRow(form, c, 4, "Gender:", genderField);
        addRow(form, c, 5, "Address:", addressField);
        addRow(form, c, 6, "Staff ID:", staffIdField);
        addRow(form, c, 7, "Joining date (YYYY-MM-DD):", joiningDateField);
        addRow(form, c, 8, "Designation:", designationField);
        addRow(form, c, 9, "Salary:", salaryField);
        addRow(form, c, 10, "Qualification:", qualificationField);
        addRow(form, c, 11, "Specialization:", specializationField);
        c.gridx = 1; c.gridy = 12;
        form.add(addButton, c);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(removeButton);

        JPanel tab = new JPanel(new BorderLayout(8, 8));
        tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        tab.add(new JScrollPane(staffTable), BorderLayout.CENTER);
        tab.add(bottom, BorderLayout.SOUTH);
        tab.add(form, BorderLayout.EAST);
        return tab;
    }

    private static void addRow(JPanel p, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridx = 0; c.gridy = row;
        p.add(new JLabel(label), c);
        c.gridx = 1;
        p.add(field, c);
    }

    private static DefaultTableModel readOnlyModel(String... cols) {
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    // ---- display ----
    @Override public void show() { frame.setVisible(true); }
    @Override public void close() { frame.dispose(); }
    @Override public void setWelcome(String adminName) { welcome.setText("Signed in as " + adminName); }

    @Override
    public void showStaff(List<Staff> staff) {
        staffModel.setRowCount(0);
        staffIds.clear();
        for (Staff s : staff) {
            staffModel.addRow(new Object[]{s.getUserId(), s.getName(), s.getEmail()});
            staffIds.add(s.getUserId());
        }
    }

    @Override
    public void showPatients(List<Patient> patients) {
        patientModel.setRowCount(0);
        for (Patient p : patients) {
            patientModel.addRow(new Object[]{p.getUserId(), p.getName(), p.getEmail()});
        }
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
    @Override public String getNewName() { return nameField.getText().trim(); }
    @Override public String getNewEmail() { return emailField.getText().trim(); }
    @Override public char[] getNewPassword() { return passwordField.getPassword(); }
    @Override public String getNewPhone() { return phoneField.getText().trim(); }
    @Override public String getNewGender() { return genderField.getText().trim(); }
    @Override public String getNewAddress() { return addressField.getText().trim(); }
    @Override public int getNewStaffId() { return Integer.parseInt(staffIdField.getText().trim()); }
    @Override public LocalDate getNewJoiningDate() {
        return LocalDate.parse(joiningDateField.getText().trim());
    }
    @Override public String getNewDesignation() { return designationField.getText().trim(); }
    @Override public double getNewSalary() { return Double.parseDouble(salaryField.getText().trim()); }
    @Override public String getNewQualification() { return qualificationField.getText().trim(); }
    @Override public String getNewSpecialization() { return specializationField.getText().trim(); }

    @Override
    public String getSelectedStaffId() {
        int row = staffTable.getSelectedRow();
        return (row < 0 || row >= staffIds.size()) ? null : staffIds.get(row);
    }

    @Override
    public void clearForm() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        phoneField.setText("");
        genderField.setText("");
        addressField.setText("");
        staffIdField.setText("");
        joiningDateField.setText(LocalDate.now().toString());
        designationField.setText("");
        salaryField.setText("");
        qualificationField.setText("");
        specializationField.setText("");
    }

    // ---- actions ----
    @Override public void setAddStaffAction(Runnable a) {
        addButton.addActionListener(e -> a.run());
    }
    @Override public void setRemoveStaffAction(Runnable a) {
        removeButton.addActionListener(e -> a.run());
    }
    @Override public void setRefreshAction(Runnable a) {
        refreshButton.addActionListener(e -> a.run());
    }
    @Override public void setLogoutAction(Runnable a) {
        logoutButton.addActionListener(e -> a.run());
    }
    @Override public void setBillingAction(Runnable a) {
        billingButton.addActionListener(e -> a.run());
    }
}