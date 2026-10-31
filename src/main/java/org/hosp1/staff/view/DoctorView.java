
package org.hosp1.staff.view;

import org.hosp1.common.view.UiActionListener;
import org.hosp1.common.view.UiActions;
import org.hosp1.common.view.UiStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorView extends JPanel {

    private final UiActionListener listener;

    private final JTextField doctorId = UiStyle.textField(15);
    private final JTextField patientSearch = UiStyle.textField(15);

    private final JTable patientTable = UiStyle.table(
            "Patient ID", "Name", "Age", "Gender", "Phone", "Blood Group"
    );

    private final JTable diagnosisTable = UiStyle.table(
            "Diagnosis ID", "Date", "Diagnosis", "Notes"
    );

    private final JTable prescriptionTable = UiStyle.table(
            "Prescription ID", "Diagnosis ID", "Medicine",
            "Dosage", "Frequency", "Duration", "Date"
    );

    private final JTable reportTable = UiStyle.table(
            "Report ID", "Patient ID", "Title", "Date", "Status"
    );

    private final JTextField diagnosisPatient = UiStyle.textField(15);
    private final JTextField diagnosisName = UiStyle.textField(15);
    private final JTextArea diagnosisNotes = new JTextArea(4, 25);

    private final JTextField prescriptionPatient = UiStyle.textField(15);
    private final JTextField diagnosisId = UiStyle.textField(15);
    private final JTextField medicine = UiStyle.textField(15);
    private final JTextField dosage = UiStyle.textField(15);
    private final JTextField frequency = UiStyle.textField(15);
    private final JTextField duration = UiStyle.textField(15);
    private final JTextArea instructions = new JTextArea(3, 25);

    private final JTextField availableDays = UiStyle.textField(20);
    private final JTextField availableTime = UiStyle.textField(20);

    private final JTextField reportPatient = UiStyle.textField(15);
    private final JTextField reportTitle = UiStyle.textField(20);
    private final JTextArea reportContent = new JTextArea(5, 30);

    private final JTextField billingPatient = UiStyle.textField(15);
    private final JTextField billingDescription = UiStyle.textField(20);
    private final JTextField billingAmount = UiStyle.textField(15);

    private final JTextField facilityPatient = UiStyle.textField(15);
    private final JComboBox<String> roomType = new JComboBox<>(
            new String[]{
                    "General", "Semi-private",
                    "Private", "ICU", "Emergency"
            }
    );
    private final JTextArea facilityReason = new JTextArea(4, 25);

    private final JLabel status = new JLabel(
            "Doctor workspace ready."
    );

    public SwingDoctorWorkspaceView(UiActionListener listener) {

        this.listener = listener;

        setLayout(new BorderLayout(0, 14));
        setBackground(UiStyle.BACKGROUND);
        setBorder(new EmptyBorder(22, 25, 22, 25));

        add(createHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Patient Records", patientPanel());
        tabs.addTab("Diagnosis", diagnosisPanel());
        tabs.addTab("Prescription", prescriptionPanel());
        tabs.addTab("Availability", availabilityPanel());
        tabs.addTab("Treatment Reports", reportPanel());
        tabs.addTab("Billing", billingPanel());
        tabs.addTab("Room / Facility", facilityPanel());

        add(tabs, BorderLayout.CENTER);

        status.setForeground(UiStyle.MUTED);
        add(status, BorderLayout.SOUTH);

        styleTables();
    }

    // ---------------- HEADER ----------------

    private JPanel createHeader() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Doctor Workspace");
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(UiStyle.TEXT);

        JLabel subtitle = new JLabel(
                "Manage patients, diagnosis, prescriptions, "
                        + "availability and treatment reports."
        );

        subtitle.setForeground(UiStyle.MUTED);

        panel.add(title);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(12));

        JPanel doctor = new JPanel(new FlowLayout(FlowLayout.LEFT));
        doctor.setOpaque(false);
        doctor.add(new JLabel("Doctor ID:"));
        doctor.add(doctorId);

        JButton setDoctor = UiStyle.primaryButton("Set Doctor ID");

        setDoctor.addActionListener(e -> {
            if (doctorId.getText().trim().isEmpty()) {
                message("Enter your Doctor ID.");
                return;
            }

            status.setText(
                    "Current Doctor: " + doctorId.getText().trim()
            );
        });

        doctor.add(setDoctor);
        panel.add(doctor);

        return panel;
    }

    // ---------------- PATIENT PANEL ----------------

    private JPanel patientPanel() {

        JPanel panel = UiStyle.card();

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);

        top.add(new JLabel("Search patient:"));
        top.add(patientSearch);

        JButton search = UiStyle.primaryButton("Search");
        JButton load = UiStyle.secondaryButton("Load All");
        JButton view = UiStyle.secondaryButton("View Record");

        top.add(search);
        top.add(load);
        top.add(view);

        search.addActionListener(e ->
                UiActions.send(listener,
                        "doctorWorkspace.patientSearch",
                        UiActions.data(
                                "query",
                                patientSearch.getText().trim()
                        ))
        );

        load.addActionListener(e ->
                UiActions.send(listener,
                        "doctorWorkspace.patientList",
                        UiActions.data()
                )
        );

        view.addActionListener(e -> {

            String id = selectedId(patientTable);

            if (id != null) {
                UiActions.send(listener,
                        "doctorWorkspace.patientRecord",
                        UiActions.data("patientId", id));
            }
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(UiStyle.scroll(patientTable), BorderLayout.CENTER);

        return panel;
    }

    // ---------------- DIAGNOSIS PANEL ----------------

    private JPanel diagnosisPanel() {

        JPanel panel = UiStyle.card();

        JPanel form = new JPanel(new GridLayout(0, 2, 12, 12));
        form.setOpaque(false);

        form.add(UiStyle.labeledField("Patient ID *", diagnosisPatient));
        form.add(UiStyle.labeledField("Diagnosis *", diagnosisName));

        form.add(UiStyle.labeledField(
                "Clinical notes",
                new JScrollPane(diagnosisNotes)
        ));

        JButton save = UiStyle.primaryButton("Save Diagnosis");
        JButton history = UiStyle.secondaryButton("View Patient Diagnoses");

        save.addActionListener(e -> {

            if (!validateDoctorId()
                    || diagnosisPatient.getText().trim().isEmpty()
                    || diagnosisName.getText().trim().isEmpty()) {

                message("Doctor ID, Patient ID and Diagnosis are required.");
                return;
            }

            UiActions.send(listener,
                    "doctorWorkspace.diagnosisSave",
                    UiActions.data(
                            "doctorId", doctorId.getText().trim(),
                            "patientId", diagnosisPatient.getText().trim(),
                            "diagnosis", diagnosisName.getText().trim(),
                            "notes", diagnosisNotes.getText().trim()
                    )
            );
        });

        history.addActionListener(e ->
                UiActions.send(listener,
                        "doctorWorkspace.diagnosisList",
                        UiActions.data(
                                "patientId",
                                diagnosisPatient.getText().trim()
                        ))
        );

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setOpaque(false);
        buttons.add(save);
        buttons.add(history);

        panel.add(form, BorderLayout.NORTH);
        panel.add(UiStyle.scroll(diagnosisTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    // ---------------- PRESCRIPTION PANEL ----------------

    private JPanel prescriptionPanel() {

        JPanel panel = UiStyle.card();

        JPanel form = new JPanel(new GridLayout(0, 2, 12, 12));
        form.setOpaque(false);

        form.add(UiStyle.labeledField("Patient ID *", prescriptionPatient));
        form.add(UiStyle.labeledField("Diagnosis ID *", diagnosisId));
        form.add(UiStyle.labeledField("Medicine *", medicine));
        form.add(UiStyle.labeledField("Dosage *", dosage));
        form.add(UiStyle.labeledField("Frequency *", frequency));
        form.add(UiStyle.labeledField("Duration *", duration));
        form.add(UiStyle.labeledField(
                "Instructions",
                new JScrollPane(instructions)
        ));

        JButton save = UiStyle.primaryButton("Assign Medicine");
        JButton load = UiStyle.secondaryButton("View Prescriptions");

        save.addActionListener(e -> {

            if (!validateDoctorId()
                    || prescriptionPatient.getText().trim().isEmpty()
                    || diagnosisId.getText().trim().isEmpty()
                    || medicine.getText().trim().isEmpty()
                    || dosage.getText().trim().isEmpty()
                    || frequency.getText().trim().isEmpty()
                    || duration.getText().trim().isEmpty()) {

                message("Fill all required prescription fields.");
                return;
            }

            UiActions.send(listener,
                    "doctorWorkspace.prescriptionSave",
                    UiActions.data(
                            "doctorId", doctorId.getText().trim(),
                            "patientId", prescriptionPatient.getText().trim(),
                            "diagnosisId", diagnosisId.getText().trim(),
                            "medicine", medicine.getText().trim(),
                            "dosage", dosage.getText().trim(),
                            "frequency", frequency.getText().trim(),
                            "duration", duration.getText().trim(),
                            "instructions", instructions.getText().trim()
                    )
            );
        });

        load.addActionListener(e ->
                UiActions.send(listener,
                        "doctorWorkspace.prescriptionList",
                        UiActions.data(
                                "patientId",
                                prescriptionPatient.getText().trim()
                        ))
        );

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setOpaque(false);
        buttons.add(save);
        buttons.add(load);

        panel.add(form, BorderLayout.NORTH);
        panel.add(UiStyle.scroll(prescriptionTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    // ---------------- AVAILABILITY PANEL ----------------

    private JPanel availabilityPanel() {

        JPanel panel = UiStyle.card();

        JPanel form = new JPanel(new GridLayout(0, 2, 15, 15));
        form.setOpaque(false);

        form.add(UiStyle.labeledField(
                "Available Days",
                availableDays
        ));

        form.add(UiStyle.labeledField(
                "Available Time",
                availableTime
        ));

        JButton save = UiStyle.primaryButton("Save Availability");
        JButton load = UiStyle.secondaryButton("View Availability");

        save.addActionListener(e -> {

            if (!validateDoctorId()
                    || availableDays.getText().trim().isEmpty()
                    || availableTime.getText().trim().isEmpty()) {

                message("Enter Doctor ID, available days and time.");
                return;
            }

            UiActions.send(listener,
                    "doctorWorkspace.availabilitySave",
                    UiActions.data(
                            "doctorId", doctorId.getText().trim(),
                            "days", availableDays.getText().trim(),
                            "time", availableTime.getText().trim()
                    )
            );
        });

        load.addActionListener(e ->
                UiActions.send(listener,
                        "doctorWorkspace.availabilityList",
                        UiActions.data()
                )
        );

        form.add(save);
        form.add(load);

        panel.add(form, BorderLayout.NORTH);

        return panel;
    }

    // ---------------- REPORT PANEL ----------------

    private JPanel reportPanel() {

        JPanel panel = UiStyle.card();

        JPanel form = new JPanel(new GridLayout(0, 2, 12, 12));
        form.setOpaque(false);

        form.add(UiStyle.labeledField("Patient ID *", reportPatient));
        form.add(UiStyle.labeledField("Report Title *", reportTitle));

        form.add(UiStyle.labeledField(
                "Report Content *",
                new JScrollPane(reportContent)
        ));

        JButton generate = UiStyle.primaryButton("Generate Report");
        JButton load = UiStyle.secondaryButton("View Reports");

        generate.addActionListener(e -> {

            if (!validateDoctorId()
                    || reportPatient.getText().trim().isEmpty()
                    || reportTitle.getText().trim().isEmpty()
                    || reportContent.getText().trim().isEmpty()) {

                message("Complete all required report fields.");
                return;
            }

            UiActions.send(listener,
                    "doctorWorkspace.reportGenerate",
                    UiActions.data(
                            "doctorId", doctorId.getText().trim(),
                            "patientId", reportPatient.getText().trim(),
                            "title", reportTitle.getText().trim(),
                            "content", reportContent.getText().trim()
                    )
            );
        });

        load.addActionListener(e ->
                UiActions.send(listener,
                        "doctorWorkspace.reportList",
                        UiActions.data(
                                "patientId",
                                reportPatient.getText().trim()
                        ))
        );

        JButton update = UiStyle.secondaryButton("Update Selected Report");

        update.addActionListener(e -> {

            String id = selectedId(reportTable);

            if (id == null) return;

            UiActions.send(listener,
                    "doctorWorkspace.reportUpdate",
                    UiActions.data(
                            "reportId", id,
                            "title", reportTitle.getText().trim(),
                            "content", reportContent.getText().trim()
                    )
            );
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setOpaque(false);
        buttons.add(generate);
        buttons.add(load);
        buttons.add(update);

        panel.add(form, BorderLayout.NORTH);
        panel.add(UiStyle.scroll(reportTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    // ---------------- BILLING PANEL ----------------

    private JPanel billingPanel() {

        JPanel panel = UiStyle.card();

        JPanel form = new JPanel(new GridLayout(0, 2, 12, 12));
        form.setOpaque(false);

        form.add(UiStyle.labeledField("Patient ID *", billingPatient));
        form.add(UiStyle.labeledField("Charge Description *", billingDescription));
        form.add(UiStyle.labeledField("Amount (Rs.) *", billingAmount));

        JButton request = UiStyle.primaryButton("Submit Billing Request");

        request.addActionListener(e -> {

            if (!validateDoctorId()
                    || billingPatient.getText().trim().isEmpty()
                    || billingDescription.getText().trim().isEmpty()
                    || billingAmount.getText().trim().isEmpty()) {

                message("Complete all billing fields.");
                return;
            }

            try {
                double amount = Double.parseDouble(
                        billingAmount.getText().trim()
                );

                if (!Double.isFinite(amount) || amount < 0) {
                    message("Enter a valid non-negative amount.");
                    return;
                }

            } catch (NumberFormatException ex) {
                message("Enter a valid billing amount.");
                return;
            }

            UiActions.send(listener,
                    "doctorWorkspace.billingRequest",
                    UiActions.data(
                            "doctorId", doctorId.getText().trim(),
                            "patientId", billingPatient.getText().trim(),
                            "description", billingDescription.getText().trim(),
                            "amount", billingAmount.getText().trim()
                    )
            );
        });

        form.add(request);
        panel.add(form, BorderLayout.NORTH);

        return panel;
    }

    // ---------------- FACILITY PANEL ----------------

    private JPanel facilityPanel() {

        JPanel panel = UiStyle.card();

        JPanel form = new JPanel(new GridLayout(0, 2, 12, 12));
        form.setOpaque(false);

        form.add(UiStyle.labeledField("Patient ID *", facilityPatient));
        form.add(UiStyle.labeledField("Room Type", roomType));

        form.add(UiStyle.labeledField(
                "Reason for Request",
                new JScrollPane(facilityReason)
        ));

        JButton request = UiStyle.primaryButton("Request Facility");

        request.addActionListener(e -> {

            if (!validateDoctorId()
                    || facilityPatient.getText().trim().isEmpty()
                    || facilityReason.getText().trim().isEmpty()) {

                message("Enter Doctor ID, Patient ID and reason.");
                return;
            }

            UiActions.send(listener,
                    "doctorWorkspace.facilityRequest",
                    UiActions.data(
                            "doctorId", doctorId.getText().trim(),
                            "patientId", facilityPatient.getText().trim(),
                            "roomType", roomType.getSelectedItem(),
                            "reason", facilityReason.getText().trim()
                    )
            );
        });

        form.add(request);
        panel.add(form, BorderLayout.NORTH);

        return panel;
    }

    // ---------------- COMMON METHODS ----------------

    private boolean validateDoctorId() {
        if (doctorId.getText().trim().isEmpty()) {
            message("Enter Doctor ID first.");
            return false;
        }
        return true;
    }

    private String selectedId(JTable table) {

        int row = table.getSelectedRow();

        if (row < 0) {
            message("Select a record first.");
            return null;
        }

        int modelRow = table.convertRowIndexToModel(row);

        return String.valueOf(table.getModel().getValueAt(modelRow, 0));
    }

    private void styleTables() {
        UiStyle.styleTable(patientTable);
        UiStyle.styleTable(diagnosisTable);
        UiStyle.styleTable(prescriptionTable);
        UiStyle.styleTable(reportTable);
    }

    private void message(String text) {
        JOptionPane.showMessageDialog(
                this,
                text,
                "Doctor Workspace",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void setRows(JTable table, List<Object[]> rows) {

        DefaultTableModel model =
                (DefaultTableModel) table.getModel();

        model.setRowCount(0);

        if (rows != null) {
            for (Object[] row : rows) {
                model.addRow(row);
            }
        }
    }

    // ---------------- CONTROLLER CALLBACKS ----------------

    public void setPatientRows(List<Object[]> rows) {
        setRows(patientTable, rows);
    }

    public void setDiagnosisRows(List<Object[]> rows) {
        setRows(diagnosisTable, rows);
    }

    public void setPrescriptionRows(List<Object[]> rows) {
        setRows(prescriptionTable, rows);
    }

    public void setReportRows(List<Object[]> rows) {
        setRows(reportTable, rows);
    }

    public void showPatientDetails(String details) {
        JOptionPane.showMessageDialog(
                this,
                details,
                "Patient Medical Record",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void showAvailabilityRows(List<Object[]> rows) {

        StringBuilder text = new StringBuilder();

        for (Object[] row : rows) {
            text.append("Doctor ID: ").append(row[0])
                    .append("\nDays: ").append(row[1])
                    .append("\nTime: ").append(row[2])
                    .append("\n\n");
        }

        JOptionPane.showMessageDialog(
                this,
                text.length() == 0 ? "No availability records." : text.toString(),
                "Doctor Availability",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void showMessage(String text) {
        message(text);
    }

    public void setStatus(String text) {
        status.setText(text);
    }
}
