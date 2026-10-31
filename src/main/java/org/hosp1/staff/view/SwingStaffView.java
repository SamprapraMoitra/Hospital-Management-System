
package org.hosp1.staff.view;

import org.hosp1.staff.model.StaffMember;

import javax.swing.*;
import java.awt.*;

public class SwingStaffView extends JPanel {

    private final JLabel id = new JLabel();
    private final JLabel name = new JLabel();
    private final JLabel designation = new JLabel();
    private final JLabel department = new JLabel();
    private final JLabel shift = new JLabel();
    private final JLabel status = new JLabel();

    private final JTextField phone = new JTextField(20);
    private final JTextField email = new JTextField(20);
    private final JTextField address = new JTextField(25);

    private final JTextArea schedule = new JTextArea(5, 30);

    private StaffMember currentStaff;

    public interface ActionHandler {
        void onAction(String action);
    }

    private final ActionHandler handler;

    public SwingStaffView(ActionHandler handler) {

        this.handler = handler;

        setLayout(new BorderLayout(15, 15));

        JLabel heading = new JLabel("Staff Dashboard");
        heading.setFont(new Font("Arial", Font.BOLD, 24));

        add(heading, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("My Profile", profilePanel());
        tabs.addTab("My Schedule", schedulePanel());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel profilePanel() {

        JPanel panel = new JPanel(
                new GridLayout(0, 2, 12, 12)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        addLabel(panel, "Staff ID:", id);
        addLabel(panel, "Name:", name);
        addLabel(panel, "Designation:", designation);
        addLabel(panel, "Department:", department);
        addLabel(panel, "Shift:", shift);
        addLabel(panel, "Employment Status:", status);

        panel.add(new JLabel("Phone:"));
        panel.add(phone);

        panel.add(new JLabel("Email:"));
        panel.add(email);

        panel.add(new JLabel("Address:"));
        panel.add(address);

        JButton update = new JButton("Update Contact Details");

        update.addActionListener(e ->
                handler.onAction("staff.updateContact")
        );

        panel.add(update);

        return panel;
    }

    private JPanel schedulePanel() {

        JPanel panel = new JPanel(new BorderLayout());

        schedule.setEditable(false);
        schedule.setLineWrap(true);

        JButton refresh = new JButton("View My Attendance");

        refresh.addActionListener(e ->
                handler.onAction("staff.attendance")
        );

        panel.add(refresh, BorderLayout.NORTH);
        panel.add(new JScrollPane(schedule), BorderLayout.CENTER);

        return panel;
    }

    private void addLabel(
            JPanel panel,
            String text,
            JLabel value) {

        panel.add(new JLabel(text));
        panel.add(value);
    }

    public void setProfile(StaffMember staff) {

        currentStaff = staff;

        id.setText(staff.getStaffId());
        name.setText(staff.getName());
        designation.setText(staff.getDesignation());
        department.setText(staff.getDepartment());
        shift.setText(staff.getShift());
        status.setText(staff.getStatus());

        phone.setText(staff.getPhone());
        email.setText(staff.getEmail());
        address.setText(staff.getAddress());
    }

    public StaffMember getCurrentStaff() {
        return currentStaff;
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

    public void setSchedule(String text) {
        schedule.setText(text);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Staff Dashboard",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
