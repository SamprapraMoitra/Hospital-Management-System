
package org.hosp1.patients.controller;

import org.hosp1.common.view.UiActionListener;
import org.hosp1.common.view.UiActions;
import org.hosp1.patients.model.PatientPortalModel;
import org.hosp1.patients.model.PatientPortalModel.*;

import org.hosp1.patients.view.PatientPortalView;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class PatientController implements UiActionListener {

    private final PatientPortalModel model;
    private final PatientPortalView view;

    // Optional connection to other HMS module controllers
    private final UiActionListener moduleBridge;
    private final String authenticatedPatientId;

    public PatientController(
            PatientPortalModel model,
            PatientPortalView view) {

        this(model, view, null, null);
    }

    public PatientController(
            PatientPortalModel model,
            PatientPortalView view,
            UiActionListener moduleBridge) {

        this(model, view, moduleBridge, null);
    }

    public PatientController(
            PatientPortalModel model,
            PatientPortalView view,
            UiActionListener moduleBridge,
            String authenticatedPatientId) {

        this.model = model;
        this.view = view;
        this.moduleBridge = moduleBridge;
        this.authenticatedPatientId = authenticatedPatientId;
    }

    // ---------------- ACTION HANDLER ----------------

    @Override
    public void onAction(
            String action,
            Map<String, Object> data) {

        try {

            switch (action) {

                case "patientPortal.doctorList":
                    loadDoctors();
                    break;

                case "patientPortal.doctorSearch":
                    searchDoctors(data);
                    break;

                case "patientPortal.appointmentBook":
                    bookAppointment(data);
                    break;

                case "patientPortal.appointmentList":
                    loadAppointments(data);
                    break;

                case "patientPortal.appointmentCancel":
                    cancelAppointment(data);
                    break;

                case "patientPortal.treatmentList":
                    loadTreatments(data);
                    break;

                case "patientPortal.reportList":
                    loadReports(data);
                    break;

                case "patientPortal.documentSearch":
                    searchDocuments(data);
                    break;

                case "patientPortal.reportView":
                    viewReport(data);
                    break;

                case "patientPortal.billList":
                    loadBills(data);
                    break;

                case "patientPortal.paymentRequest":
                    submitPayment(data);
                    break;

                case "patientPortal.roomSearch":
                    searchRooms(data);
                    break;

                case "patientPortal.feedbackSubmit":
                    submitFeedback(data);
                    break;

                case "patientPortal.feedbackList":
                    loadFeedback(data);
                    break;

                default:
                    System.out.println(
                            "Unknown Patient Action: " + action
                    );
            }

        } catch (IllegalArgumentException e) {

            showError(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Operation failed: " + e.getMessage()
            );
        }
    }

    // ---------------- DOCTOR SEARCH ----------------

    private void loadDoctors() {

        view.setDoctorRows(
                model.doctorRows(model.getDoctors())
        );

        view.setStatus("Doctor list loaded.");
    }

    private void searchDoctors(Map<String, Object> data) {

        String query = get(data, "query");

        view.setDoctorRows(
                model.doctorRows(
                        model.searchDoctors(query)
                )
        );

        view.setStatus("Doctor search completed.");
    }

    // ---------------- APPOINTMENT BOOKING ----------------

    private void bookAppointment(Map<String, Object> data) {

        String patientId = get(data, "patientId");
        String doctorId = get(data, "doctorId");
        String date = get(data, "date");
        String time = get(data, "time");
        String reason = get(data, "reason");

        validatePatientId(patientId);

        if (doctorId.isEmpty() || date.isEmpty() || time.isEmpty()) {
            throw new IllegalArgumentException(
                    "Doctor ID, date and time are required."
            );
        }

        try {

            LocalDate appointmentDate =
                    LocalDate.parse(date);

            if (appointmentDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException(
                        "Appointment date cannot be in the past."
                );
            }

            LocalTime.parse(time);

        } catch (DateTimeParseException e) {

            throw new IllegalArgumentException(
                    "Use yyyy-mm-dd for date and HH:mm for time."
            );
        }

        Appointment appointment = model.bookAppointment(
                patientId,
                doctorId,
                date,
                time,
                reason
        );

        // Forward booking request to Appointment module
        if (moduleBridge != null) {

            moduleBridge.onAction(
                    "appointment.book",
                    UiActions.data(
                            "patientId", patientId,
                            "doctorId", doctorId,
                            "date", date,
                            "time", time,
                            "reason", reason
                    )
            );
        }

        loadAppointments(data);

        view.showMessage(
                "Appointment request submitted successfully!\n\n"
                        + "Appointment ID: "
                        + appointment.appointmentId
                        + "\nStatus: Pending"
        );
    }

    private void loadAppointments(Map<String, Object> data) {

        String patientId = get(data, "patientId");

        validatePatientId(patientId);

        view.setAppointmentRows(
                model.appointmentRows(patientId)
        );
    }

    private void cancelAppointment(Map<String, Object> data) {

        String patientId = get(data, "patientId");
        String appointmentId = get(data, "appointmentId");

        validatePatientId(patientId);

        boolean cancelled = model.cancelAppointment(
                appointmentId,
                patientId
        );

        if (!cancelled) {
            throw new IllegalArgumentException(
                    "Appointment not found or cannot be cancelled."
            );
        }

        if (moduleBridge != null) {

            moduleBridge.onAction(
                    "appointment.cancel",
                    UiActions.data(
                            "appointmentId", appointmentId
                    )
            );
        }

        loadAppointments(data);

        view.showMessage(
                "Appointment cancelled successfully."
        );
    }

    // ---------------- TREATMENT ----------------

    private void loadTreatments(Map<String, Object> data) {

        String patientId = get(data, "patientId");

        validatePatientId(patientId);

        view.setTreatmentRows(
                model.treatmentRows(patientId)
        );

        view.setStatus("Treatment history loaded.");
    }

    // ---------------- MEDICAL REPORTS ----------------

    private void loadReports(Map<String, Object> data) {

        String patientId = get(data, "patientId");

        validatePatientId(patientId);

        view.setReportRows(
                model.reportRows(patientId)
        );

        view.setStatus("Medical reports loaded.");
    }

    private void searchDocuments(Map<String, Object> data) {

        String patientId = get(data, "patientId");
        String query = get(data, "query");

        validatePatientId(patientId);

        view.setReportRows(
                model.reportRows(
                        model.searchDocuments(patientId, query)
                )
        );

        view.setStatus("Document search completed.");
    }

    private void viewReport(Map<String, Object> data) {

        String patientId = get(data, "patientId");
        String reportId = get(data, "reportId");

        validatePatientId(patientId);

        MedicalReport report = model.getReport(
                reportId,
                patientId
        );

        if (report == null) {
            throw new IllegalArgumentException(
                    "Report not found or access denied."
            );
        }

        view.displayReport(
                report.reportName,
                "Report ID: " + report.reportId
                        + "\nPatient ID: " + report.patientId
                        + "\nDate: " + report.reportDate
                        + "\nStatus: " + report.status
                        + "\n\n" + report.content
        );
    }

    // ---------------- BILLING AND PAYMENT ----------------

    private void loadBills(Map<String, Object> data) {

        String patientId = get(data, "patientId");

        validatePatientId(patientId);

        view.setBillRows(
                model.billRows(patientId)
        );
    }

    private void submitPayment(Map<String, Object> data) {

        String patientId = get(data, "patientId");
        String billId = get(data, "billId");

        validatePatientId(patientId);

        boolean submitted = model.submitPaymentRequest(
                billId,
                patientId
        );

        if (!submitted) {
            throw new IllegalArgumentException(
                    "Bill not found or payment already requested."
            );
        }

        // Forward request to Billing module
        if (moduleBridge != null) {

            moduleBridge.onAction(
                    "billing.paymentRequest",
                    UiActions.data(
                            "patientId", patientId,
                            "billId", billId
                    )
            );
        }

        loadBills(data);

        view.showMessage(
                "Payment request submitted.\n"
                        + "The Billing module must verify and process it."
        );
    }

    // ---------------- ROOM AVAILABILITY ----------------

    private void searchRooms(Map<String, Object> data) {

        String roomType = get(data, "roomType");

        view.setRoomRows(
                model.roomRows(roomType)
        );

        view.setStatus("Room availability checked.");
    }

    // ---------------- FEEDBACK ----------------

    private void submitFeedback(Map<String, Object> data) {

        String patientId = get(data, "patientId");
        String category = get(data, "category");
        String comments = get(data, "comments");

        validatePatientId(patientId);

        int rating;

        try {

            rating = Integer.parseInt(
                    get(data, "rating")
            );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Select a valid feedback rating."
            );
        }

        if (category.isEmpty() || comments.isEmpty()) {
            throw new IllegalArgumentException(
                    "Feedback category and comments are required."
            );
        }

        Feedback feedback = model.submitFeedback(
                patientId,
                category,
                rating,
                comments
        );

        view.showMessage(
                "Feedback submitted successfully!\n"
                        + "Feedback ID: " + feedback.feedbackId
        );

        loadFeedback(data);
    }

    private void loadFeedback(Map<String, Object> data) {

        String patientId = get(data, "patientId");

        validatePatientId(patientId);

        view.setFeedbackRows(
                model.feedbackRows(patientId)
        );
    }

    // ---------------- VALIDATION ----------------

    private void validatePatientId(String patientId) {

        if (patientId == null || patientId.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Patient ID is required."
            );
        }
    }

    private String get(
            Map<String, Object> data,
            String key) {

        if ("patientId".equals(key) && authenticatedPatientId != null) {
            return authenticatedPatientId;
        }

        Object value = data.get(key);

        return value == null
                ? ""
                : value.toString().trim();
    }

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                view,
                message,
                "Patient Management",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
