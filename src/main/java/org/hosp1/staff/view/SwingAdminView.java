
package org.hosp1.staff.view;

import org.hosp1.staff.model.StaffManagementModel;
import org.hosp1.staff.model.StaffMember;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SwingAdminView extends JPanel {

    private final JTextField staffId = new JTextField();
    private final JTextField name = new JTextField();
    private final JComboBox<String> gender =
            new JComboBox<>(new String[]{"Female", "Male", "Other"});

    private final JTextField dob = new JTextField();
    private final JTextField phone = new JTextField();
    private final JTextField email = new JTextField();
    private final JTextField address = new JTextField();

    private final JComboBox<String> designation =
            new JComboBox<>(StaffManagementModel.DESIGNATIONS);

    private final JComboBox<String> department =
            new JComboBox<>(StaffManagementModel.DEPARTMENTS);

    private final JTextField qualification = new JTextField();
    private final JTextField joiningDate = new JTextField();

    private final JComboBox<String> shift =
            new JComboBox<>(StaffManagementModel.SHIFTS);

    private final JTextField salary = new JTextField();
    private final JTextField username = new JTextField();

    private final JTextField search = new JTextField(15);

    private final JComboBox<String> designationFilter =
            new JComboBox<>(StaffManagementModel.DESIGNATIONS);

    private final JComboBox<String> departmentFilter =
            new JComboBox<>(StaffManagementModel.DEPARTMENTS);

    private final JTable staffTable = new JTable();

    private final JTable attendanceTable = new JTable();

    private final JLabel totalLabel = new JLabel();
    private final JLabel activeLabel = new JLabel();
    private final JLabel inactiveLabel = new JLabel();

    private final JComboBox<String> attendanceStatus =
            new JComboBox<>(new String[]{
                    "Present", "Absent", "Leave"
            });

    private final JTextField attendanceDate = new JTextField(12);

    public interface ActionHandler {
        void onAction(String action);
    }

    private final ActionHandler handler;

    public SwingAdminView(ActionHandler handler) {

        this.handler = handler;

        setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel("Admin - Staff Management");
        heading.setFont(new Font("Arial", Font.BOLD, 24));

        add(heading, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Dashboard", dashboardPanel());
        tabs.addTab("Register Staff", registrationPanel());
        tabs.addTab("Manage Staff", managementPanel());
        tabs.addTab("Attendance", attendancePanel());

        add(tabs, BorderLayout.CENTER);
    }

    // ---------------- DASHBOARD ----------------

    private JPanel dashboardPanel() {

        JPanel panel = new JPanel(
                new GridLayout(2, 2, 15, 15)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        activeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        inactiveLabel.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(totalLabel);
        panel.add(activeLabel);
        panel.add(inactiveLabel);

        JButton refresh = new JButton("Refresh Statistics");

        refresh.addActionListener(e ->
                handler.onAction("admin.statistics")
        );

        panel.add(refresh);

        return panel;
    }

    // ---------------- REGISTRATION ----------------

    private JPanel registrationPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(
                new GridLayout(0, 2, 10, 10)
        );

        form.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        staffId.setEditable(false);

        addField(form, "Staff ID (Automatic)", staffId);
        addField(form, "Name *", name);
        addField(form, "Gender", gender);
        addField(form, "Date of Birth", dob);
        addField(form, "Phone *", phone);
        addField(form, "Email", email);
        addField(form, "Address", address);
        addField(form, "Designation *", designation);
        addField(form, "Department *", department);
        addField(form, "Qualification", qualification);
        addField(form, "Joining Date", joiningDate);
        addField(form, "Shift", shift);
        addField(form, "Salary", salary);
        addField(form, "Username *", username);

        JButton save = new JButton("Register Staff");
        JButton clear = new JButton("Clear Form");

        save.addActionListener(e ->
                handler.onAction("admin.create")
        );

        clear.addActionListener(e -> clearForm());

        JPanel buttons = new JPanel();
        buttons.add(save);
        buttons.add(clear);

        panel.add(new JScrollPane(form), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    // ---------------- STAFF MANAGEMENT ----------------

    private JPanel managementPanel() {

        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));

        top.add(new JLabel("Search:"));
        top.add(search);

        JButton searchButton = new JButton("Search");
        JButton allButton = new JButton("Show All");
        JButton updateButton = new JButton("Update Selected");
        JButton deactivateButton = new JButton("Deactivate");
        JButton activateButton = new JButton("Activate");
        JButton viewButton = new JButton("View Details");

        top.add(searchButton);
        top.add(allButton);
        top.add(updateButton);
        top.add(deactivateButton);
        top.add(activateButton);
        top.add(viewButton);

        top.add(new JLabel("Designation:"));
        top.add(designationFilter);

        top.add(new JLabel("Department:"));
        top.add(departmentFilter);

        searchButton.addActionListener(e ->
                handler.onAction("admin.search")
        );

        allButton.addActionListener(e ->
                handler.onAction("admin.list")
        );

        updateButton.addActionListener(e ->
                handler.onAction("admin.update")
        );

        deactivateButton.addActionListener(e ->
                handler.onAction("admin.deactivate")
        );

        activateButton.addActionListener(e ->
                handler.onAction("admin.activate")
        );

        viewButton.addActionListener(e ->
                handler.onAction("admin.view")
        );

        designationFilter.addActionListener(e ->
                handler.onAction("admin.filter")
        );

        departmentFilter.addActionListener(e ->
                handler.onAction("admin.filter")
        );

        staffTable.setModel(new DefaultTableModel(
                new Object[]{
                        "Staff ID", "Name", "Designation",
                        "Department", "Phone", "Shift", "Status"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(staffTable), BorderLayout.CENTER);

        return panel;
    }

    // ---------------- ATTENDANCE ----------------

    private JPanel attendancePanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel top = new JPanel();

        top.add(new JLabel("Date (yyyy-mm-dd):"));
        top.add(attendanceDate);

        top.add(new JLabel("Status:"));
        top.add(attendanceStatus);

        JButton mark = new JButton("Mark Selected Staff");
        JButton load = new JButton("View Attendance");

        top.add(mark);
        top.add(load);

        mark.addActionListener(e ->
                handler.onAction("admin.attendanceMark")
        );

        load.addActionListener(e ->
                handler.onAction("admin.attendanceList")
        );

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(attendanceTable), BorderLayout.CENTER);

        attendanceTable.setModel(new DefaultTableModel(
                new Object[]{"Staff ID", "Date", "Attendance"}, 0
        ));

        return panel;
    }

    private void addField(
            JPanel panel,
            String label,
            JComponent component) {

        panel.add(new JLabel(label));
        panel.add(component);
    }

    // ---------------- GET FORM DATA ----------------

    public String getName() {
        return name.getText().trim();
    }

    public String getGender() {
        return String.valueOf(gender.getSelectedItem());
    }

    public String getDob() {
        return dob.getText().trim();
    }

    public String getPhone() {
        return phone.getText().trim();
    }

    public String getEmail() {
        return email.getText().trim();
    }

    public String getAddress() {
        return address.getText().trim();
    }

    public String getDesignation() {
        return String.valueOf(designation.getSelectedItem());
    }

    public String getDepartment() {
        return String.valueOf(department.getSelectedItem());
    }

    public String getQualification() {
        return qualification.getText().trim();
    }

    public String getJoiningDate() {
        return joiningDate.getText().trim();
    }

    public String getShift() {
        return String.valueOf(shift.getSelectedItem());
    }

    public String getSalary() {
        return salary.getText().trim();
    }

    public String getUsername() {
        return username.getText().trim();
    }

    public String getSearch() {
        return search.getText().trim();
    }

    public String getDesignationFilter() {
        return String.valueOf(designationFilter.getSelectedItem());
    }

    public String getDepartmentFilter() {
        return String.valueOf(departmentFilter.getSelectedItem());
    }

    public String getSelectedStaffId() {

        int row = staffTable.getSelectedRow();

        if (row < 0) {
            return null;
        }

        int modelRow = staffTable.convertRowIndexToModel(row);

        return String.valueOf(
                staffTable.getModel().getValueAt(modelRow, 0)
        );
    }

    public String getAttendanceDate() {
        return attendanceDate.getText().trim();
    }

    public String getAttendanceStatus() {
        return String.valueOf(attendanceStatus.getSelectedItem());
    }

    // ---------------- UPDATE FORM ----------------

    public void loadStaffIntoForm(StaffMember s) {

        staffId.setText(s.getStaffId());
        name.setText(s.getName());
        gender.setSelectedItem(s.getGender());
        dob.setText(s.getDateOfBirth());
        phone.setText(s.getPhone());
        email.setText(s.getEmail());
        address.setText(s.getAddress());
        designation.setSelectedItem(s.getDesignation());
        department.setSelectedItem(s.getDepartment());
        qualification.setText(s.getQualification());
        joiningDate.setText(s.getJoiningDate());
        shift.setSelectedItem(s.getShift());
        salary.setText(s.getSalary());
        username.setText(s.getUsername());
    }

    public void setRows(List<Object[]> rows) {

        DefaultTableModel tableModel =
                (DefaultTableModel) staffTable.getModel();

        tableModel.setRowCount(0);

        for (Object[] row : rows) {
            tableModel.addRow(row);
        }
    }

    public void setAttendanceRows(List<Object[]> rows) {

        DefaultTableModel tableModel =
                (DefaultTableModel) attendanceTable.getModel();

        tableModel.setRowCount(0);

        for (Object[] row : rows) {
            tableModel.addRow(row);
        }
    }

    public void setStatistics(
            int total,
            int active,
            int inactive) {

        totalLabel.setText("Total Staff: " + total);
        activeLabel.setText("Active Staff: " + active);
        inactiveLabel.setText("Inactive Staff: " + inactive);
    }

    public void clearForm() {

        staffId.setText("");
        name.setText("");
        dob.setText("");
        phone.setText("");
        email.setText("");
        address.setText("");
        qualification.setText("");
        joiningDate.setText("");
        salary.setText("");
        username.setText("");
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Admin",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
