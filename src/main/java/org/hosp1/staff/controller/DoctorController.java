
package org.hosp1.staff.controller;

import org.hosp1.common.view.UiActionListener;
import org.hosp1.staff.model.DoctorWorkspaceModel;
import org.hosp1.staff.model.DoctorWorkspaceModel.*;

import org.hosp1.staff.view.DoctorView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DoctorController implements UiActionListener {

    private final DoctorWorkspaceModel model;
    private final DoctorView view;

    public DoctorController(
            DoctorWorkspaceModel model,
            DoctorView view) {
        this.model = model;
        this.view = view;
    }

    // ---------------- ACTION HANDLER ----------------

    @Override
    public void onAction(String action, Map<String, Object> data) {

        try {

            switch (action) {

                case "doctorWorkspace.patientList":
                    loadPatients();
                    break;

                case "doctorWorkspace.patientSearch":
                    searchPatients(data);
                    break;

                case "doctorWorkspace.patientRecord":
                    viewPatientRecord(data);
                    break;

                case "doctorWorkspace.diagnosisSave":
                    saveDiagnosis(data);
                    break;

                case "doctorWorkspace.diagnosisList":
                    loadDiagnoses(data);
                    break;

                case "doctorWorkspace.prescriptionSave":
                    savePrescription(data);
                    break;

                case "doctorWorkspace.prescriptionList":
                    loadPrescriptions(data);
                    break;

                case "doctorWorkspace.availabilitySave":
                    saveAvailability(data);
                    break;

                case "doctorWorkspace.availabilityList":
                    loadAvailability();
                    break;

                case "doctorWorkspace.reportGenerate":
                    generateReport(data);
                    break;

                case "doctorWorkspace.reportList":
                    loadReports(data);
                    break;

                case "doctorWorkspace.reportUpdate":
                    updateReport(data);
                    break;

                default:
                    System.out.println(
                            "Unknown Doctor Workspace action: " + action
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

    // ---------------- PATIENT OPERATIONS ----------------

    private void loadPatients() {

        List<PatientRecord> patients = model.getPatients();

        view.setPatientRows(
                model.getPatientRows(patients)
        );

        view.setStatus(
                patients.size() + " patient(s) loaded."
        );
    }

    private void searchPatients(Map<String, Object> data) {

        String query = get(data, "query");

        List<PatientRecord> patients =
                model.searchPatients(query);

        view.setPatientRows(
                model.getPatientRows(patients)
        );

        view.setStatus(
                patients.size() + " matching patient(s) found."
        );
    }

    private void viewPatientRecord(Map<String, Object> data) {

        String id = get(data, "patientId");

        PatientRecord patient = model.getPatient(id);

        if (patient == null) {
            throw new IllegalArgumentException(
                    "Patient record not found."
            );
        }

        StringBuilder details = new StringBuilder();

        details.append("PATIENT MEDICAL RECORD\n\n");

        details.append("Patient ID: ")
                .append(patient.patientId).append("\n");

        details.append("Name: ")
                .append(patient.name).append("\n");

        details.append("Age: ")
                .append(patient.age).append("\n");

        details.append("Gender: ")
                .append(patient.gender).append("\n");

        details.append("Phone: ")
                .append(patient.phone).append("\n");

        details.append("Blood Group: ")
                .append(patient.bloodGroup).append("\n");

        details.append("Medical History: ")
                .append(patient.medicalHistory).append("\n");

        details.append("Allergies: ")
                .append(patient.allergies).append("\n\n");

        details.append("DIAGNOSES\n");

        for (Diagnosis d : model.getDiagnoses(id)) {
            details.append(d.diagnosisId)
                    .append(" | ")
                    .append(d.date)
                    .append(" | ")
                    .append(d.diagnosis)
                    .append("\n");
        }

        details.append("\nPRESCRIPTIONS\n");

        for (Prescription p : model.getPrescriptions(id)) {
            details.append(p.medicine)
                    .append(" | ")
                    .append(p.dosage)
                    .append(" | ")
                    .append(p.frequency)
                    .append(" | ")
                    .append(p.duration)
                    .append("\n");
        }

        view.showPatientDetails(details.toString());
    }

    // ---------------- DIAGNOSIS OPERATIONS ----------------

    private void saveDiagnosis(Map<String, Object> data) {

        String patientId = get(data, "patientId");
        String doctorId = get(data, "doctorId");
        String diagnosis = get(data, "diagnosis");
        String notes = get(data, "notes");

        if (diagnosis.isEmpty()) {
            throw new IllegalArgumentException(
                    "Diagnosis cannot be empty."
            );
        }

        Diagnosis result = model.addDiagnosis(
                patientId,
                doctorId,
                diagnosis,
                notes
        );

        loadDiagnoses(data);

        view.showMessage(
                "Diagnosis saved successfully!\n\nDiagnosis ID: "
                        + result.diagnosisId
        );
    }

    private void loadDiagnoses(Map<String, Object> data) {

        String patientId = get(data, "patientId");

        view.setDiagnosisRows(
                model.getDiagnosisRows(patientId)
        );
    }

    // ---------------- PRESCRIPTION OPERATIONS ----------------

    private void savePrescription(Map<String, Object> data) {

        String patientId = get(data, "patientId");
        String doctorId = get(data, "doctorId");
        String diagnosisId = get(data, "diagnosisId");
        String medicine = get(data, "medicine");
        String dosage = get(data, "dosage");
        String frequency = get(data, "frequency");
        String duration = get(data, "duration");
        String instructions = get(data, "instructions");

        if (medicine.isEmpty()
                || dosage.isEmpty()
                || frequency.isEmpty()
                || duration.isEmpty()) {

            throw new IllegalArgumentException(
                    "Complete all required prescription details."
            );
        }

        Prescription prescription = model.addPrescription(
                diagnosisId,
                patientId,
                doctorId,
                medicine,
                dosage,
                frequency,
                duration,
                instructions
        );

        loadPrescriptions(data);

        view.showMessage(
                "Prescription saved successfully!\n"
                        + "Prescription ID: "
                        + prescription.prescriptionId
        );
    }

    private void loadPrescriptions(Map<String, Object> data) {

        String patientId = get(data, "patientId");

        view.setPrescriptionRows(
                model.getPrescriptionRows(patientId)
        );
    }

    // ---------------- AVAILABILITY OPERATIONS ----------------

    private void saveAvailability(Map<String, Object> data) {

        String doctorId = get(data, "doctorId");
        String days = get(data, "days");
        String time = get(data, "time");

        if (days.isEmpty() || time.isEmpty()) {
            throw new IllegalArgumentException(
                    "Available days and time are required."
            );
        }

        model.saveAvailability(
                doctorId,
                days,
                time
        );

        view.showMessage(
                "Doctor availability saved successfully."
        );
    }

    private void loadAvailability() {

        List<Object[]> rows = new ArrayList<>();

        for (Availability a : model.getAvailabilities()) {

            rows.add(new Object[]{
                    a.doctorId,
                    a.availableDays,
                    a.availableTime
            });
        }

        view.showAvailabilityRows(rows);
    }

    // ---------------- REPORT OPERATIONS ----------------

    private void generateReport(Map<String, Object> data) {

        String patientId = get(data, "patientId");
        String doctorId = get(data, "doctorId");
        String title = get(data, "title");
        String content = get(data, "content");

        if (title.isEmpty() || content.isEmpty()) {
            throw new IllegalArgumentException(
                    "Report title and content are required."
            );
        }

        TreatmentReport report = model.generateReport(
                patientId,
                doctorId,
                title,
                content
        );

        loadReports(data);

        view.showMessage(
                "Treatment report generated successfully!\n"
                        + "Report ID: " + report.reportId
        );
    }

    private void loadReports(Map<String, Object> data) {

        String patientId = get(data, "patientId");

        view.setReportRows(
                model.getReportRows(patientId)
        );
    }

    private void updateReport(Map<String, Object> data) {

        String id = get(data, "reportId");
        String title = get(data, "title");
        String content = get(data, "content");

        if (title.isEmpty() || content.isEmpty()) {
            throw new IllegalArgumentException(
                    "Report title and content are required."
            );
        }

        boolean updated = model.updateReport(
                id,
                title,
                content
        );

        if (!updated) {
            throw new IllegalArgumentException(
                    "Report not found."
            );
        }

        loadReports(data);

        view.showMessage(
                "Treatment report updated successfully."
        );
    }

    // ---------------- HELPER METHODS ----------------

    private String get(
            Map<String, Object> data,
            String key) {

        Object value = data.get(key);

        return value == null
                ? ""
                : value.toString().trim();
    }

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                view,
                message,
                "Doctor Module",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
