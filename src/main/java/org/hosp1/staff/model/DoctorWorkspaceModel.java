package org.hosp1.staff.model;

import org.hosp1.patients.model.Patient;
import org.hosp1.patients.model.PatientRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class DoctorWorkspaceModel {

    public static class PatientRecord {
        public String patientId;
        public String name;
        public String age;
        public String gender;
        public String phone;
        public String bloodGroup;
        public String medicalHistory;
        public String allergies;

        public PatientRecord(String patientId, String name, String age,
                             String gender, String phone, String bloodGroup,
                             String medicalHistory, String allergies) {
            this.patientId = patientId;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.phone = phone;
            this.bloodGroup = bloodGroup;
            this.medicalHistory = medicalHistory;
            this.allergies = allergies;
        }
    }

    public static class Diagnosis {
        public String diagnosisId;
        public String patientId;
        public String doctorId;
        public String date;
        public String diagnosis;
        public String notes;

        public Diagnosis(String diagnosisId, String patientId, String doctorId,
                         String diagnosis, String notes) {
            this.diagnosisId = diagnosisId;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.date = LocalDate.now().toString();
            this.diagnosis = diagnosis;
            this.notes = notes;
        }
    }

    public static class Prescription {
        public String prescriptionId;
        public String diagnosisId;
        public String patientId;
        public String doctorId;
        public String medicine;
        public String dosage;
        public String frequency;
        public String duration;
        public String instructions;
        public String date;

        public Prescription(String prescriptionId, String diagnosisId,
                            String patientId, String doctorId, String medicine,
                            String dosage, String frequency, String duration,
                            String instructions) {
            this.prescriptionId = prescriptionId;
            this.diagnosisId = diagnosisId;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.medicine = medicine;
            this.dosage = dosage;
            this.frequency = frequency;
            this.duration = duration;
            this.instructions = instructions;
            this.date = LocalDate.now().toString();
        }
    }

    public static class Availability {
        public String doctorId;
        public String availableDays;
        public String availableTime;

        public Availability(String doctorId, String availableDays,
                            String availableTime) {
            this.doctorId = doctorId;
            this.availableDays = availableDays;
            this.availableTime = availableTime;
        }
    }

    public static class TreatmentReport {
        public String reportId;
        public String patientId;
        public String doctorId;
        public String title;
        public String content;
        public String date;
        public String status;

        public TreatmentReport(String reportId, String patientId,
                               String doctorId, String title, String content) {
            this.reportId = reportId;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.title = title;
            this.content = content;
            this.date = LocalDate.now().toString();
            this.status = "Generated";
        }
    }

    private final List<PatientRecord> patients = new ArrayList<>();
    private final List<Diagnosis> diagnoses = new ArrayList<>();
    private final List<Prescription> prescriptions = new ArrayList<>();
    private final List<Availability> availabilities = new ArrayList<>();
    private final List<TreatmentReport> reports = new ArrayList<>();
    private PatientRepository patientRepository;

    private int diagnosisCounter = 1;
    private int prescriptionCounter = 1;
    private int reportCounter = 1;

    public DoctorWorkspaceModel() {
    }

    public DoctorWorkspaceModel(PatientRepository patientRepository) {
        this.patientRepository =
                Objects.requireNonNull(patientRepository, "patientRepository");
        syncPatients();
    }

    public List<PatientRecord> getPatients() {
        syncPatients();
        List<PatientRecord> result = new ArrayList<>(patients);
        result.sort(Comparator.comparing(
                patient -> patient.name == null ? "" : patient.name,
                String.CASE_INSENSITIVE_ORDER
        ));
        return result;
    }

    public List<PatientRecord> searchPatients(String query) {
        String search = query == null ? "" : query.trim().toLowerCase();
        List<PatientRecord> result = new ArrayList<>();
        for (PatientRecord patient : getPatients()) {
            if (search.isEmpty()
                    || contains(patient.patientId, search)
                    || contains(patient.name, search)
                    || contains(patient.phone, search)) {
                result.add(patient);
            }
        }
        return result;
    }

    public PatientRecord getPatient(String patientId) {
        syncPatients();
        for (PatientRecord patient : patients) {
            if (Objects.equals(patient.patientId, patientId)) {
                return patient;
            }
        }
        return null;
    }

    public void addOrUpdatePatient(PatientRecord patient) {
        Objects.requireNonNull(patient, "patient");
        String id = required(patient.patientId, "Patient ID is required.");
        for (int i = 0; i < patients.size(); i++) {
            if (Objects.equals(patients.get(i).patientId, id)) {
                patients.set(i, patient);
                return;
            }
        }
        patients.add(patient);
    }

    public Diagnosis addDiagnosis(String patientId, String doctorId,
                                  String diagnosis, String notes) {
        requirePatient(patientId);
        Diagnosis result = new Diagnosis(
                nextId("D-", diagnosisCounter++),
                patientId,
                required(doctorId, "Doctor ID is required."),
                required(diagnosis, "Diagnosis cannot be empty."),
                clean(notes)
        );
        diagnoses.add(result);
        return result;
    }

    public List<Diagnosis> getDiagnoses(String patientId) {
        List<Diagnosis> result = new ArrayList<>();
        for (Diagnosis diagnosis : diagnoses) {
            if (Objects.equals(diagnosis.patientId, patientId)) {
                result.add(diagnosis);
            }
        }
        return result;
    }

    public Prescription addPrescription(String diagnosisId, String patientId,
                                        String doctorId, String medicine,
                                        String dosage, String frequency,
                                        String duration, String instructions) {
        requirePatient(patientId);
        Diagnosis diagnosis = findDiagnosis(diagnosisId);
        if (diagnosis == null || !Objects.equals(diagnosis.patientId, patientId)) {
            throw new IllegalArgumentException(
                    "Diagnosis not found for the selected patient."
            );
        }

        Prescription result = new Prescription(
                nextId("RX-", prescriptionCounter++),
                diagnosisId,
                patientId,
                required(doctorId, "Doctor ID is required."),
                required(medicine, "Medicine is required."),
                required(dosage, "Dosage is required."),
                required(frequency, "Frequency is required."),
                required(duration, "Duration is required."),
                clean(instructions)
        );
        prescriptions.add(result);
        return result;
    }

    public List<Prescription> getPrescriptions(String patientId) {
        List<Prescription> result = new ArrayList<>();
        for (Prescription prescription : prescriptions) {
            if (Objects.equals(prescription.patientId, patientId)) {
                result.add(prescription);
            }
        }
        return result;
    }

    public void saveAvailability(String doctorId, String days, String time) {
        String id = required(doctorId, "Doctor ID is required.");
        String availableDays = required(days, "Available days are required.");
        String availableTime = required(time, "Available time is required.");

        for (Availability availability : availabilities) {
            if (Objects.equals(availability.doctorId, id)) {
                availability.availableDays = availableDays;
                availability.availableTime = availableTime;
                return;
            }
        }
        availabilities.add(new Availability(id, availableDays, availableTime));
    }

    public List<Availability> getAvailabilities() {
        return new ArrayList<>(availabilities);
    }

    public TreatmentReport generateReport(String patientId, String doctorId,
                                         String title, String content) {
        requirePatient(patientId);
        TreatmentReport report = new TreatmentReport(
                nextId("TR-", reportCounter++),
                patientId,
                required(doctorId, "Doctor ID is required."),
                required(title, "Report title is required."),
                required(content, "Report content is required.")
        );
        reports.add(report);
        return report;
    }

    public boolean updateReport(String reportId, String title, String content) {
        for (TreatmentReport report : reports) {
            if (Objects.equals(report.reportId, reportId)) {
                report.title = required(title, "Report title is required.");
                report.content = required(content, "Report content is required.");
                return true;
            }
        }
        return false;
    }

    public List<TreatmentReport> getReports(String patientId) {
        List<TreatmentReport> result = new ArrayList<>();
        for (TreatmentReport report : reports) {
            if (Objects.equals(report.patientId, patientId)) {
                result.add(report);
            }
        }
        return result;
    }

    public List<Object[]> getPatientRows(List<PatientRecord> records) {
        List<Object[]> rows = new ArrayList<>();
        for (PatientRecord patient : records) {
            rows.add(new Object[]{
                    patient.patientId, patient.name, patient.age, patient.gender,
                    patient.phone, patient.bloodGroup
            });
        }
        return rows;
    }

    public List<Object[]> getDiagnosisRows(String patientId) {
        List<Object[]> rows = new ArrayList<>();
        for (Diagnosis diagnosis : getDiagnoses(patientId)) {
            rows.add(new Object[]{
                    diagnosis.diagnosisId, diagnosis.date,
                    diagnosis.diagnosis, diagnosis.notes
            });
        }
        return rows;
    }

    public List<Object[]> getPrescriptionRows(String patientId) {
        List<Object[]> rows = new ArrayList<>();
        for (Prescription prescription : getPrescriptions(patientId)) {
            rows.add(new Object[]{
                    prescription.prescriptionId, prescription.diagnosisId,
                    prescription.medicine, prescription.dosage,
                    prescription.frequency, prescription.duration,
                    prescription.date
            });
        }
        return rows;
    }

    public List<Object[]> getReportRows(String patientId) {
        List<Object[]> rows = new ArrayList<>();
        for (TreatmentReport report : getReports(patientId)) {
            rows.add(new Object[]{
                    report.reportId, report.patientId, report.title,
                    report.date, report.status
            });
        }
        return rows;
    }

    private void requirePatient(String patientId) {
        if (getPatient(patientId) == null) {
            throw new IllegalArgumentException("Patient record not found.");
        }
    }

    private void syncPatients() {
        if (patientRepository == null) {
            return;
        }
        for (Patient patient : patientRepository.findAll()) {
            if (findPatient(patient.getUserId()) == null) {
                addOrUpdatePatient(new PatientRecord(
                        patient.getUserId(),
                        patient.getName(),
                        "Not provided",
                        patient.getGender(),
                        patient.getPhone(),
                        "Not provided",
                        "Not provided",
                        "Not provided"
                ));
            }
        }
    }

    private PatientRecord findPatient(String patientId) {
        for (PatientRecord patient : patients) {
            if (Objects.equals(patient.patientId, patientId)) {
                return patient;
            }
        }
        return null;
    }

    private Diagnosis findDiagnosis(String diagnosisId) {
        for (Diagnosis diagnosis : diagnoses) {
            if (Objects.equals(diagnosis.diagnosisId, diagnosisId)) {
                return diagnosis;
            }
        }
        return null;
    }

    private static String nextId(String prefix, int counter) {
        return prefix + String.format("%03d", counter);
    }

    private static String required(String value, String message) {
        String cleanValue = clean(value);
        if (cleanValue.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return cleanValue;
    }

    private static String clean(String value) {
        return value == null ? "" : value.trim();
    }

    private static boolean contains(String value, String query) {
        return value != null && value.toLowerCase().contains(query);
    }
}
