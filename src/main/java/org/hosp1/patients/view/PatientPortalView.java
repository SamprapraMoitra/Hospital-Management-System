
package org.hosp1.patients.view;

import org.hosp1.common.view.UiActionListener;
import org.hosp1.common.view.UiActions;
import org.hosp1.common.view.UiStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientPortalView extends JPanel {

    private final UiActionListener listener;

    private final JTextField patientId = UiStyle.textField(15);

    private final JTextField doctorSearch = UiStyle.textField(16);
    private final JTable doctorTable = UiStyle.table(
            "Doctor ID", "Name", "Specialization",
            "Department", "Available Days", "Available Time"
    );

    private final JTextField appointmentDoctor = UiStyle.textField(15);
    private final JTextField appointmentDate = UiStyle.textField(15);
    private final JTextField appointmentTime = UiStyle.textField(15);
    private final JTextField appointmentReason = UiStyle.textField(20);

    private final JTable appointmentTable = UiStyle.table(
            "Appointment ID", "Doctor ID", "Date",
            "Time", "Reason", "Status"
    );

    private final JTable treatmentTable = UiStyle.table(
            "Treatment ID", "Doctor ID", "Date",
            "Diagnosis", "Notes"
    );

    private final JTable medicineTable = UiStyle.table(
            "Prescription ID", "Doctor ID", "Medicine",
            "Dosage", "Frequency", "Duration", "Instructions"
    );

    private final JTextField documentSearch = UiStyle.textField(16);
    private final JTable reportTable = UiStyle.table(
            "Report ID", "Report Name", "Date", "Status"
    );

    private final JTextArea reportContent = new JTextArea(8, 35);

    private final JTable billTable = UiStyle.table(
            "Bill ID", "Description", "Amount",
            "Date", "Payment Status"
    );

    private final JComboBox<String> roomFilter =
            new JComboBox<>(new String[]{
                    "All", "General", "Semi-private",
                    "Private", "ICU", "Emergency"
            });

    private final JTable roomTable = UiStyle.table(
            "Room ID", "Room Type", "Floor",
            "Available Beds", "Status"
    );

    private final JComboBox<String> feedbackCategory =
            new JComboBox<>(new String[]{
                    "Hospital Service",
                    "Doctor Consultation",
                    "Nursing Service",
                    "Cleanliness",
                    "Facilities",
                    "Other"
            });

    private final JComboBox<Integer> rating =
            new JComboBox<>(new Integer[]{5, 4, 3, 2, 1});

    private final JTextArea feedbackComments = new JTextArea(5, 25);

    private final JTable feedbackTable = UiStyle.table(
            "Feedback ID", "Category", "Rating",
            "Comments", "Date"
    );

    private final JLabel status =
            new JLabel("Patient portal ready.");

    public SwingPatientPortalView(UiActionListener listener) {

        this.listener = listener;

        setLayout(new BorderLayout(0, 15));
        setBackground(UiStyle.BACKGROUND);
        setBorder(new EmptyBorder(22, 25, 22, 25));

        add(header(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Search Doctors", doctorPanel());
        tabs.addTab("Appointments", appointmentPanel());
        tabs.addTab("Treatment", treatmentPanel());
        tabs.addTab("Medicines", medicinePanel());
        tabs.addTab("Medical Reports", reportPanel());
        tabs.addTab("Payments", billingPanel());
        tabs.addTab("Room Availability", roomPanel());
        tabs.addTab("Feedback", feedbackPanel());

        add(tabs, BorderLayout.CENTER);

        status.setForeground(UiStyle.MUTED);
        add(status, BorderLayout.SOUTH);

        styleTables();
    }

    // ---------------- HEADER ----------------

    private JPanel header() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Patient Portal");

        title.setFont(new Font(
                "SansSerif", Font.BOLD, 25
        ));

        title.setForeground(UiStyle.TEXT);

        JLabel subtitle = new JLabel(
                "View treatment, search doctors, book appointments "
                        + "and access hospital services."
        );

        subtitle.setForeground(UiStyle.MUTED);

        panel.add(title);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(12));

        JPanel idPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        idPanel.setOpaque(false);

        idPanel.add(new JLabel("Patient ID:"));
        idPanel.add(patientId);

        JButton setId = UiStyle.primaryButton("Set Patient ID");

        setId.addActionListener(e -> {

            if (patientId.getText().trim().isEmpty()) {
                message("Enter your Patient ID.");
                return;
            }

            status.setText(
                    "Current Patient: " + patientId.getText().trim()
            );

            refreshAll();
        });

        idPanel.add(setId);

        panel.add(idPanel);

        return panel;
    }

    private boolean validPatientId() {

        if (patientId.getText().trim().isEmpty()) {
            message("Enter Patient ID first.");
            return false;
        }

        return true;
    }

    private String patient() {
        return patientId.getText().trim();
    }

    // ---------------- DOCTOR SEARCH ----------------

    private JPanel doctorPanel() {

        JPanel panel = UiStyle.card();

        JPanel top = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        top.setOpaque(false);

        top.add(new JLabel("Doctor name / ID / Specialization:"));
        top.add(doctorSearch);

        JButton search = UiStyle.primaryButton("Search");
        JButton all = UiStyle.secondaryButton("Show All Doctors");

        top.add(search);
        top.add(all);

        search.addActionListener(e ->
                UiActions.send(listener,
                        "patientPortal.doctorSearch",
                        UiActions.data(
                                "query",
                                doctorSearch.getText().trim()
                        ))
        );

        all.addActionListener(e ->
                UiActions.send(listener,
                        "patientPortal.doctorList",
                        UiActions.data()
                )
        );

        panel.add(top, BorderLayout.NORTH);
        panel.add(UiStyle.scroll(doctorTable), BorderLayout.CENTER);

        return panel;
    }

    // ---------------- APPOINTMENT PANEL ----------------

    private JPanel appointmentPanel() {

        JPanel panel = UiStyle.card();

        JPanel form = new JPanel(
                new GridLayout(0, 2, 12, 12)
        );

        form.setOpaque(false);

        form.add(UiStyle.labeledField(
                "Doctor ID *", appointmentDoctor
        ));

        form.add(UiStyle.labeledField(
                "Appointment Date (yyyy-mm-dd) *",
                appointmentDate
        ));

        form.add(UiStyle.labeledField(
                "Appointment Time (HH:mm) *",
                appointmentTime
        ));

        form.add(UiStyle.labeledField(
                "Reason for Appointment",
                appointmentReason
        ));

        JButton book = UiStyle.primaryButton("Book Appointment");
        JButton load = UiStyle.secondaryButton("View My Appointments");
        JButton cancel = UiStyle.secondaryButton("Cancel Selected");

        book.addActionListener(e -> {

            if (!validPatientId()
                    || appointmentDoctor.getText().trim().isEmpty()
                    || appointmentDate.getText().trim().isEmpty()
                    || appointmentTime.getText().trim().isEmpty()) {

                message("Complete all required appointment fields.");
                return;
            }

            UiActions.send(listener,
                    "patientPortal.appointmentBook",
                    UiActions.data(
                            "patientId", patient(),
                            "doctorId", appointmentDoctor.getText().trim(),
                            "date", appointmentDate.getText().trim(),
                            "time", appointmentTime.getText().trim(),
                            "reason", appointmentReason.getText().trim()
                    )
            );
        });

        load.addActionListener(e -> {

            if (!validPatientId()) return;

            UiActions.send(listener,
                    "patientPortal.appointmentList",
                    UiActions.data("patientId", patient())
            );
        });

        cancel.addActionListener(e -> {

            String id = selectedId(appointmentTable);

            if (id == null || !validPatientId()) return;

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Cancel this appointment?",
                    "Confirm cancellation",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                UiActions.send(listener,
                        "patientPortal.appointmentCancel",
                        UiActions.data(
                                "appointmentId", id,
                                "patientId", patient()
                        )
                );
            }
        });

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        buttons.setOpaque(false);
        buttons.add(book);
        buttons.add(load);
        buttons.add(cancel);

        panel.add(form, BorderLayout.NORTH);
        panel.add(UiStyle.scroll(appointmentTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    // ---------------- TREATMENT PANEL ----------------

    private JPanel treatmentPanel() {

        JPanel panel = UiStyle.card();

        JButton load = UiStyle.primaryButton("View My Treatment");

        load.addActionListener(e -> {

            if (!validPatientId()) return;

            UiActions.send(listener,
                    "patientPortal.treatmentList",
                    UiActions.data("patientId", patient())
            );
        });

        panel.add(load, BorderLayout.NORTH);
        panel.add(UiStyle.scroll(treatmentTable), BorderLayout.CENTER);

        return panel;
    }

    // ---------------- MEDICINE PANEL ----------------

    private JPanel medicinePanel() {

        JPanel panel = UiStyle.card();

        JButton load = UiStyle.primaryButton("View Prescribed Medicines");

        load.addActionListener(e -> {

            if (!validPatientId()) return;

            UiActions.send(listener,
                    "patientPortal.medicineList",
                    UiActions.data("patientId", patient())
            );
        });

        panel.add(load, BorderLayout.NORTH);
        panel.add(UiStyle.scroll(medicineTable), BorderLayout.CENTER);

        return panel;
    }

    // ---------------- REPORT PANEL ----------------

    private JPanel reportPanel() {

        JPanel panel = UiStyle.card();

        JPanel top = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        top.setOpaque(false);

        top.add(new JLabel("Search document:"));
        top.add(documentSearch);

        JButton search = UiStyle.primaryButton("Search");
        JButton load = UiStyle.secondaryButton("View All Reports");
        JButton open = UiStyle.secondaryButton("Open Selected Report");

        top.add(search);
        top.add(load);
        top.add(open);

        search.addActionListener(e -> {

            if (!validPatientId()) return;

            UiActions.send(listener,
                    "patientPortal.documentSearch",
                    UiActions.data(
                            "patientId", patient(),
                            "query", documentSearch.getText().trim()
                    )
            );
        });

        load.addActionListener(e -> {

            if (!validPatientId()) return;

            UiActions.send(listener,
                    "patientPortal.reportList",
                    UiActions.data("patientId", patient())
            );
        });

        open.addActionListener(e -> {

            String id = selectedId(reportTable);

            if (id == null || !validPatientId()) return;

            UiActions.send(listener,
                    "patientPortal.reportView",
                    UiActions.data(
                            "patientId", patient(),
                            "reportId", id
                    )
            );
        });

        reportContent.setEditable(false);
        reportContent.setLineWrap(true);
        reportContent.setWrapStyleWord(true);

        JPanel right = new JPanel(new BorderLayout(0, 8));
        right.setOpaque(false);

        right.add(new JLabel("Report contents"), BorderLayout.NORTH);
        right.add(UiStyle.scroll(reportContent), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                UiStyle.scroll(reportTable),
                right
        );

        split.setResizeWeight(0.55);
        split.setBorder(null);

        panel.add(top, BorderLayout.NORTH);
        panel.add(split, BorderLayout.CENTER);

        return panel;
    }

    // ---------------- BILLING PANEL ----------------

    private JPanel billingPanel() {

        JPanel panel = UiStyle.card();

        JPanel top = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        top.setOpaque(false);

        JButton load = UiStyle.primaryButton("View My Bills");
        JButton pay = UiStyle.secondaryButton("Request Payment");

        top.add(load);
        top.add(pay);

        load.addActionListener(e -> {

            if (!validPatientId()) return;

            UiActions.send(listener,
                    "patientPortal.billList",
                    UiActions.data("patientId", patient())
            );
        });

        pay.addActionListener(e -> {

            String id = selectedId(billTable);

            if (id == null || !validPatientId()) return;

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Submit a payment request for bill " + id + "?",
                    "Payment confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                UiActions.send(listener,
                        "patientPortal.paymentRequest",
                        UiActions.data(
                                "patientId", patient(),
                                "billId", id
                        )
                );
            }
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(UiStyle.scroll(billTable), BorderLayout.CENTER);

        return panel;
    }

    // ---------------- ROOM PANEL ----------------

    private JPanel roomPanel() {

        JPanel panel = UiStyle.card();

        JPanel top = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        top.setOpaque(false);

        top.add(new JLabel("Room Type:"));
        top.add(roomFilter);

        JButton check = UiStyle.primaryButton("Check Availability");

        top.add(check);

        check.addActionListener(e -> {

            UiActions.send(listener,
                    "patientPortal.roomSearch",
                    UiActions.data(
                            "roomType",
                            roomFilter.getSelectedItem()
                    )
            );
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(UiStyle.scroll(roomTable), BorderLayout.CENTER);

        return panel;
    }

    // ---------------- FEEDBACK PANEL ----------------

    private JPanel feedbackPanel() {

        JPanel panel = UiStyle.card();

        JPanel form = new JPanel(
                new GridLayout(0, 2, 12, 12)
        );

        form.setOpaque(false);

        form.add(UiStyle.labeledField(
                "Feedback Category", feedbackCategory
        ));

        form.add(UiStyle.labeledField(
                "Rating (1 to 5)", rating
        ));

        form.add(UiStyle.labeledField(
                "Comments",
                UiStyle.scroll(feedbackComments)
        ));

        JButton submit = UiStyle.primaryButton("Submit Feedback");
        JButton load = UiStyle.secondaryButton("View My Feedback");

        submit.addActionListener(e -> {

            if (!validPatientId()
                    || feedbackComments.getText().trim().isEmpty()) {

                message("Enter your Patient ID and feedback comments.");
                return;
            }

            UiActions.send(listener,
                    "patientPortal.feedbackSubmit",
                    UiActions.data(
                            "patientId", patient(),
                            "category", feedbackCategory.getSelectedItem(),
                            "rating", rating.getSelectedItem(),
                            "comments", feedbackComments.getText().trim()
                    )
            );
        });

        load.addActionListener(e -> {

            if (!validPatientId()) return;

            UiActions.send(listener,
                    "patientPortal.feedbackList",
                    UiActions.data("patientId", patient())
            );
        });

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        buttons.setOpaque(false);
        buttons.add(submit);
        buttons.add(load);

        panel.add(form, BorderLayout.NORTH);
        panel.add(UiStyle.scroll(feedbackTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    // ---------------- COMMON METHODS ----------------

    private String selectedId(JTable table) {

        int row = table.getSelectedRow();

        if (row < 0) {
            message("Select a record first.");
            return null;
        }

        int modelRow = table.convertRowIndexToModel(row);

        return String.valueOf(
                table.getModel().getValueAt(modelRow, 0)
        );
    }

    private void styleTables() {

        UiStyle.styleTable(doctorTable);
        UiStyle.styleTable(appointmentTable);
        UiStyle.styleTable(treatmentTable);
        UiStyle.styleTable(medicineTable);
        UiStyle.styleTable(reportTable);
        UiStyle.styleTable(billTable);
        UiStyle.styleTable(roomTable);
        UiStyle.styleTable(feedbackTable);
    }

    private void message(String text) {

        JOptionPane.showMessageDialog(
                this,
                text,
                "Patient Portal",
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

    private void refreshAll() {

        if (!validPatientId()) return;

        UiActions.send(listener,
                "patientPortal.appointmentList",
                UiActions.data("patientId", patient())
        );

        UiActions.send(listener,
                "patientPortal.treatmentList",
                UiActions.data("patientId", patient())
        );

        UiActions.send(listener,
                "patientPortal.medicineList",
                UiActions.data("patientId", patient())
        );

        UiActions.send(listener,
                "patientPortal.reportList",
                UiActions.data("patientId", patient())
        );

        UiActions.send(listener,
                "patientPortal.billList",
                UiActions.data("patientId", patient())
        );
    }

    // ---------------- CONTROLLER CALLBACKS ----------------

    public void setDoctorRows(List<Object[]> rows) {
        setRows(doctorTable, rows);
    }

    public void setAppointmentRows(List<Object[]> rows) {
        setRows(appointmentTable, rows);
    }

    public void setTreatmentRows(List<Object[]> rows) {
        setRows(treatmentTable, rows);
    }

    public void setMedicineRows(List<Object[]> rows) {
        setRows(medicineTable, rows);
    }

    public void setReportRows(List<Object[]> rows) {
        setRows(reportTable, rows);
    }

    public void setBillRows(List<Object[]> rows) {
        setRows(billTable, rows);
    }

    public void setRoomRows(List<Object[]> rows) {
        setRows(roomTable, rows);
    }

    public void setFeedbackRows(List<Object[]> rows) {
        setRows(feedbackTable, rows);
    }

    public void displayReport(String title, String content) {

        reportContent.setText(
                (title == null ? "" : title + "\n\n")
                        + (content == null ? "" : content)
        );

        reportContent.setCaretPosition(0);
    }

    public void showMessage(String text) {
        message(text);
    }

    public void setStatus(String text) {
        status.setText(text);
    }
}
