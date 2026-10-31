
package org.hosp1.staff.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Doctor {

<<<<<<< HEAD
    public Doctor() {
        super();
    }
    public Doctor(String userId, String name, String email, String phone, String gender,String address,String role,String passwordHash,
                 int staffId, LocalDate joiningDate, String designation,double salary,String qualification,String specialization,String licenseNo) {
        super(userId, name, email, phone, gender,address,role,passwordHash,staffId,joiningDate,designation,salary,qualification);
        this.specialization = specialization;
        this.licenseNo = licenseNo;
=======
    // ---------------- DATA CLASSES ----------------
>>>>>>> ffc6294 (Updated)

    public static class PatientRecord {
        public String patientId;
        public String name;
        public String age;
        public String gender;
        public String phone;
        public String bloodGroup;
        public String medicalHistory;
        public String allergies;

        public PatientRecord(String id, String name, String age,
                             String gender, String phone,
                             String bloodGroup, String history,
                             String allergies) {
            this.patientId = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.phone = phone;
            this.bloodGroup = bloodGroup;
            this.medicalHistory = history;
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

        public Diagnosis(String id, String patientId,
                         String doctorId, String diagnosis,
                         String notes) {
            this.diagnosisId = id;
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

        public Prescription(String id, String diagnosisId,
                            String patientId, String doctorId,
                            String medicine, String dosage,
                            String frequency, String duration,
                            String instructions) {
            this.prescriptionId = id;
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

        public Availability(String id, String days, String time) {
            this.doctorId = id;
            this.availableDays = days;
            this.availableTime = time;
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

        public TreatmentReport(String id, String patientId,
                               String doctorId, String title,
                               String content) {
            this.reportId = id;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.title = title;
            this.content = content;
            this.date = LocalDate.now().toString();
            this.status = "Generated";
        }
    }
<<<<<<< HEAD
    @Override
    public String toString() {
        return "Doctor{staffId=" + getStaffId() + ", name='" + getName() + "', specialization='" + specialization + "'}";
=======

    public static class BillingRequest {
        public String requestId;
        public String patientId;
        public String doctorId;
        public String description;
        public double amount;
        public String date;
        public String status;

        public BillingRequest(String id, String patientId,
                              String doctorId, String description,
                              double amount) {
            this.requestId = id;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.description = description;
            this.amount = amount;
            this.date = LocalDate.now().toString();
            this.status = "Pending";
        }
>>>>>>> ffc6294 (Updated)
    }

    public static class FacilityRequest {
        public String requestId;
        public String patientId;
        public String doctorId;
        public String roomType;
        public String reason;
        public String date;
        public String status;

        public FacilityRequest(String id, String patientId,
                               String doctorId, String roomType,
                               String reason) {
            this.requestId = id;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.roomType = roomType;
            this.reason = reason;
            this.date = LocalDate.now().toString();
            this.status = "Pending";
        }
    }

    // ---------------- DATA STORAGE ----------------

    private final List<PatientRecord> patients = new ArrayList<>();
    private final List<Diagnosis> diagnoses = new ArrayList<>();
    private final List<Prescription> prescriptions = new ArrayList<>();
    private final List<Availability> availabilities = new ArrayList<>();
    private final List<TreatmentReport> reports = new ArrayList<>();
    private final List<BillingRequest> billingRequests = new ArrayList<>();
    private final List<FacilityRequest> facilityRequests = new ArrayList<>();

    private int diagnosisCounter = 1;
    private int prescriptionCounter = 1;
    private int reportCounter = 1;
    private int billingCounter = 1;
    private int facilityCounter = 1;

    // ---------------- CONSTRUCTOR ----------------

    public DoctorWorkspaceModel() {
        loadSamplePatients();
    }

    // Sample data for standalone testing
    private void loadSamplePatients() {

        patients.add(new PatientRecord(
                "PAT001", "Rahul Sharma", "35",
                "Male", "9876543210", "B+",
                "Previous history of hypertension", "None"
        ));

        patients.add(new PatientRecord(
                "PAT002", "Priya Das", "28",
                "Female", "9876543211", "O+",
                "No major medical history", "Penicillin"
        ));

        patients.add(new PatientRecord(
                "PAT003", "Amit Roy", "45",
                "Male", "9876543212", "A+",
                "Diabetes", "None"
        ));
    }

    // ---------------- PATIENT OPERATIONS ----------------

    public List<PatientRecord> getPatients() {
        return new ArrayList<>(patients);
    }

    public List<PatientRecord> searchPatients(String query) {

        List<PatientRecord> result = new ArrayList<>();

        String search = query == null ? "" : query.trim().toLowerCase();

        for (PatientRecord p : patients) {

            if (p.patientId.toLowerCase().contains(search)
                    || p.name.toLowerCase().contains(search)
                    || p.phone.contains(search)) {
                result.add(p);
            }
        }

        return result;
    }

    public PatientRecord getPatient(String id) {

        for (PatientRecord p : patients) {
            if (p.patientId.equalsIgnoreCase(id)) {
                return p;
            }
        }

        return null;
    }

    // Allows connection to the actual Patient module
    public void addOrUpdatePatient(PatientRecord patient) {

        for (int i = 0; i < patients.size(); i++) {

            if (patients.get(i).patientId.equals(patient.patientId)) {
                patients.set(i, patient);
                return;
            }
        }

        patients.add(patient);
    }

    // ---------------- DIAGNOSIS OPERATIONS ----------------

    public Diagnosis addDiagnosis(String patientId,
                                  String doctorId,
                                  String diagnosis,
                                  String notes) {

        if (getPatient(patientId) == null) {
            throw new IllegalArgumentException("Patient not found.");
        }

        Diagnosis d = new Diagnosis(
                String.format("DIA%03d", diagnosisCounter++),
                patientId, doctorId, diagnosis, notes
        );

        diagnoses.add(d);

        return d;
    }

    public List<Diagnosis> getDiagnoses(String patientId) {

        List<Diagnosis> result = new ArrayList<>();

        for (Diagnosis d : diagnoses) {
            if (d.patientId.equalsIgnoreCase(patientId)) {
                result.add(d);
            }
        }

        return result;
    }

    // ---------------- PRESCRIPTION OPERATIONS ----------------

    public Prescription addPrescription(
            String diagnosisId,
            String patientId,
            String doctorId,
            String medicine,
            String dosage,
            String frequency,
            String duration,
            String instructions) {

        boolean validDiagnosis = false;

        for (Diagnosis d : diagnoses) {
            if (d.diagnosisId.equalsIgnoreCase(diagnosisId)
                    && d.patientId.equalsIgnoreCase(patientId)) {
                validDiagnosis = true;
                break;
            }
        }

        if (!validDiagnosis) {
            throw new IllegalArgumentException(
                    "Enter a valid diagnosis ID belonging to this patient."
            );
        }

        Prescription p = new Prescription(
                String.format("PRE%03d", prescriptionCounter++),
                diagnosisId, patientId, doctorId,
                medicine, dosage, frequency,
                duration, instructions
        );

        prescriptions.add(p);

        return p;
    }

    public List<Prescription> getPrescriptions(String patientId) {

        List<Prescription> result = new ArrayList<>();

        for (Prescription p : prescriptions) {
            if (p.patientId.equalsIgnoreCase(patientId)) {
                result.add(p);
            }
        }

        return result;
    }

    // ---------------- AVAILABILITY OPERATIONS ----------------

    public void saveAvailability(String doctorId,
                                 String days,
                                 String time) {

        if (doctorId == null || doctorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor ID is required.");
        }

        for (Availability a : availabilities) {

            if (a.doctorId.equalsIgnoreCase(doctorId)) {
                a.availableDays = days;
                a.availableTime = time;
                return;
            }
        }

        availabilities.add(new Availability(doctorId, days, time));
    }

    public List<Availability> getAvailabilities() {
        return new ArrayList<>(availabilities);
    }

    // ---------------- REPORT OPERATIONS ----------------

    public TreatmentReport generateReport(
            String patientId,
            String doctorId,
            String title,
            String content) {

        if (getPatient(patientId) == null) {
            throw new IllegalArgumentException("Patient not found.");
        }

        TreatmentReport r = new TreatmentReport(
                String.format("REP%03d", reportCounter++),
                patientId, doctorId, title, content
        );

        reports.add(r);

        return r;
    }

    public boolean updateReport(String id,
                                String title,
                                String content) {

        for (TreatmentReport r : reports) {

            if (r.reportId.equalsIgnoreCase(id)) {
                r.title = title;
                r.content = content;
                r.status = "Updated";
                return true;
            }
        }

        return false;
    }

    public List<TreatmentReport> getReports(String patientId) {

        List<TreatmentReport> result = new ArrayList<>();

        for (TreatmentReport r : reports) {
            if (patientId == null || patientId.trim().isEmpty()
                    || r.patientId.equalsIgnoreCase(patientId)) {
                result.add(r);
            }
        }

        return result;
    }

    // ---------------- BILLING REQUEST ----------------

    public BillingRequest createBillingRequest(
            String patientId,
            String doctorId,
            String description,
            double amount) {

        if (getPatient(patientId) == null) {
            throw new IllegalArgumentException("Patient not found.");
        }

        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException("Invalid billing amount.");
        }

        BillingRequest request = new BillingRequest(
                String.format("BCR%03d", billingCounter++),
                patientId, doctorId, description, amount
        );

        billingRequests.add(request);

        return request;
    }

    public List<BillingRequest> getBillingRequests() {
        return new ArrayList<>(billingRequests);
    }

    // ---------------- FACILITY REQUEST ----------------

    public FacilityRequest createFacilityRequest(
            String patientId,
            String doctorId,
            String roomType,
            String reason) {

        if (getPatient(patientId) == null) {
            throw new IllegalArgumentException("Patient not found.");
        }

        FacilityRequest request = new FacilityRequest(
                String.format("FAC%03d", facilityCounter++),
                patientId, doctorId, roomType, reason
        );

        facilityRequests.add(request);

        return request;
    }

    public List<FacilityRequest> getFacilityRequests() {
        return new ArrayList<>(facilityRequests);
    }

    // ---------------- TABLE DATA ----------------

    public List<Object[]> getPatientRows(List<PatientRecord> records) {

        List<Object[]> rows = new ArrayList<>();

        for (PatientRecord p : records) {
            rows.add(new Object[]{
                    p.patientId, p.name, p.age,
                    p.gender, p.phone, p.bloodGroup
            });
        }

        return rows;
    }

    public List<Object[]> getDiagnosisRows(String patientId) {

        List<Object[]> rows = new ArrayList<>();

        for (Diagnosis d : getDiagnoses(patientId)) {
            rows.add(new Object[]{
                    d.diagnosisId, d.date,
                    d.diagnosis, d.notes
            });
        }

        return rows;
    }

    public List<Object[]> getPrescriptionRows(String patientId) {

        List<Object[]> rows = new ArrayList<>();

        for (Prescription p : getPrescriptions(patientId)) {
            rows.add(new Object[]{
                    p.prescriptionId, p.diagnosisId,
                    p.medicine, p.dosage,
                    p.frequency, p.duration,
                    p.date
            });
        }

        return rows;
    }

    public List<Object[]> getReportRows(String patientId) {

        List<Object[]> rows = new ArrayList<>();

        for (TreatmentReport r : getReports(patientId)) {
            rows.add(new Object[]{
                    r.reportId, r.patientId,
                    r.title, r.date, r.status
            });
        }

        return rows;
    }
}
