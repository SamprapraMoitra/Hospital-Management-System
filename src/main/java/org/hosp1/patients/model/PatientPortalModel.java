
package org.hosp1.patients.model;

import org.hosp1.billing.model.BillRepository;
import org.hosp1.facility.model.RoomAllocationRepository;
import org.hosp1.facility.model.RoomRepository;
import org.hosp1.staff.model.Doctor;
import org.hosp1.staff.model.DoctorWorkspaceModel;
import org.hosp1.staff.model.StaffRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientPortalModel {

    // ---------------- DATA CLASSES ----------------

    public static class DoctorInfo {
        public String doctorId;
        public String name;
        public String specialization;
        public String department;
        public String availableDays;
        public String availableTime;

        public DoctorInfo(String id, String name,
                          String specialization, String department,
                          String days, String time) {
            this.doctorId = id;
            this.name = name;
            this.specialization = specialization;
            this.department = department;
            this.availableDays = days;
            this.availableTime = time;
        }
    }

    public static class Appointment {
        public String appointmentId;
        public String patientId;
        public String doctorId;
        public String date;
        public String time;
        public String reason;
        public String status;

        public Appointment(String id, String patientId,
                           String doctorId, String date,
                           String time, String reason) {
            this.appointmentId = id;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.date = date;
            this.time = time;
            this.reason = reason;
            this.status = "Pending";
        }
    }

    public static class Treatment {
        public String treatmentId;
        public String patientId;
        public String doctorId;
        public String diagnosis;
        public String notes;
        public String date;

        public Treatment(String id, String patientId,
                         String doctorId, String diagnosis,
                         String notes) {
            this.treatmentId = id;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.diagnosis = diagnosis;
            this.notes = notes;
            this.date = LocalDate.now().toString();
        }
    }

    public static class Medicine {
        public String prescriptionId;
        public String patientId;
        public String doctorId;
        public String medicineName;
        public String dosage;
        public String frequency;
        public String duration;
        public String instructions;
        public String date;

        public Medicine(String id, String patientId,
                        String doctorId, String name,
                        String dosage, String frequency,
                        String duration, String instructions) {
            this.prescriptionId = id;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.medicineName = name;
            this.dosage = dosage;
            this.frequency = frequency;
            this.duration = duration;
            this.instructions = instructions;
            this.date = LocalDate.now().toString();
        }
    }

    public static class MedicalReport {
        public String reportId;
        public String patientId;
        public String reportName;
        public String reportDate;
        public String content;
        public String status;

        public MedicalReport(String id, String patientId,
                             String name, String date,
                             String content) {
            this.reportId = id;
            this.patientId = patientId;
            this.reportName = name;
            this.reportDate = date;
            this.content = content;
            this.status = "Available";
        }
    }

    public static class Bill {
        public String billId;
        public String patientId;
        public String description;
        public double amount;
        public String date;
        public String status;

        public Bill(String id, String patientId,
                    String description, double amount) {
            this.billId = id;
            this.patientId = patientId;
            this.description = description;
            this.amount = amount;
            this.date = LocalDate.now().toString();
            this.status = "Unpaid";
        }
    }

    public static class RoomInfo {
        public String roomId;
        public String roomType;
        public String floor;
        public int availableBeds;
        public String status;

        public RoomInfo(String id, String type,
                        String floor, int beds,
                        String status) {
            this.roomId = id;
            this.roomType = type;
            this.floor = floor;
            this.availableBeds = beds;
            this.status = status;
        }
    }

    public static class Feedback {
        public String feedbackId;
        public String patientId;
        public String category;
        public int rating;
        public String comments;
        public String date;

        public Feedback(String id, String patientId,
                        String category, int rating,
                        String comments) {
            this.feedbackId = id;
            this.patientId = patientId;
            this.category = category;
            this.rating = rating;
            this.comments = comments;
            this.date = LocalDate.now().toString();
        }
    }

    // ---------------- STORAGE ----------------

    private final List<DoctorInfo> doctors = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();
    private final List<Treatment> treatments = new ArrayList<>();
    private final List<Medicine> medicines = new ArrayList<>();
    private final List<MedicalReport> reports = new ArrayList<>();
    private final List<Bill> bills = new ArrayList<>();
    private final List<RoomInfo> rooms = new ArrayList<>();
    private final List<Feedback> feedbacks = new ArrayList<>();
    private final StaffRepository staffRepository;
    private final DoctorWorkspaceModel doctorWorkspaceModel;
    private final BillRepository billRepository;
    private final RoomRepository roomRepository;
    private final RoomAllocationRepository allocationRepository;

    private int appointmentCounter = 1;
    private int feedbackCounter = 1;

    // ---------------- CONSTRUCTOR ----------------

    public PatientPortalModel() {
        staffRepository = null;
        doctorWorkspaceModel = null;
        billRepository = null;
        roomRepository = null;
        allocationRepository = null;
        loadSampleData();
    }

    public PatientPortalModel(StaffRepository staffRepository,
                              DoctorWorkspaceModel doctorWorkspaceModel,
                              BillRepository billRepository,
                              RoomRepository roomRepository,
                              RoomAllocationRepository allocationRepository) {
        this.staffRepository = staffRepository;
        this.doctorWorkspaceModel = doctorWorkspaceModel;
        this.billRepository = billRepository;
        this.roomRepository = roomRepository;
        this.allocationRepository = allocationRepository;
    }

    // ---------------- SAMPLE DATA ----------------

    private void loadSampleData() {

        doctors.add(new DoctorInfo(
                "DOC001", "Dr. Ananya Sen",
                "Cardiology", "Heart Department",
                "Monday, Wednesday, Friday", "10:00 AM - 2:00 PM"
        ));

        doctors.add(new DoctorInfo(
                "DOC002", "Dr. Arjun Das",
                "Neurology", "Neurology Department",
                "Tuesday, Thursday", "11:00 AM - 3:00 PM"
        ));

        doctors.add(new DoctorInfo(
                "DOC003", "Dr. Priya Roy",
                "General Medicine", "General Department",
                "Monday - Saturday", "9:00 AM - 1:00 PM"
        ));

        treatments.add(new Treatment(
                "TRT001", "PAT001", "DOC001",
                "Hypertension",
                "Blood pressure monitoring and follow-up advised."
        ));

        medicines.add(new Medicine(
                "PRE001", "PAT001", "DOC001",
                "Sample prescribed medicine",
                "As prescribed", "As prescribed",
                "As prescribed", "Follow your doctor's instructions."
        ));

        reports.add(new MedicalReport(
                "REP001", "PAT001",
                "Blood Test Report",
                LocalDate.now().toString(),
                "Sample report. Replace with an authorized clinical report."
        ));

        bills.add(new Bill(
                "BILL001", "PAT001",
                "Consultation Charge", 500.00
        ));

        rooms.add(new RoomInfo(
                "R101", "General", "1", 4, "Available"
        ));

        rooms.add(new RoomInfo(
                "R202", "Private", "2", 1, "Available"
        ));

        rooms.add(new RoomInfo(
                "R301", "ICU", "3", 0, "Full"
        ));
    }

    // ---------------- DOCTOR SEARCH ----------------

    public List<DoctorInfo> searchDoctors(String query) {

        List<DoctorInfo> result = new ArrayList<>();

        String search = query == null
                ? "" : query.trim().toLowerCase();

        for (DoctorInfo d : getDoctors()) {

            if (d.doctorId.toLowerCase().contains(search)
                    || d.name.toLowerCase().contains(search)
                    || d.specialization.toLowerCase().contains(search)
                    || d.department.toLowerCase().contains(search)) {

                result.add(d);
            }
        }

        return result;
    }

    public List<DoctorInfo> getDoctors() {
        if (staffRepository != null) {
            List<DoctorInfo> result = new ArrayList<>();
            for (Doctor doctor : staffRepository.findAllDoctors()) {
                DoctorWorkspaceModel.Availability availability =
                        getAvailability(doctor.getUserId());
                result.add(new DoctorInfo(
                        doctor.getUserId(),
                        doctor.getName(),
                        valueOrDefault(doctor.getSpecialization(), "Not specified"),
                        valueOrDefault(doctor.getDesignation(), "Medical"),
                        availability == null ? "Not set" : availability.availableDays,
                        availability == null ? "Not set" : availability.availableTime
                ));
            }
            return result;
        }
        return new ArrayList<>(doctors);
    }

    // ---------------- APPOINTMENTS ----------------

    public Appointment bookAppointment(
            String patientId,
            String doctorId,
            String date,
            String time,
            String reason) {

        boolean doctorExists = false;

        for (DoctorInfo d : getDoctors()) {
            if (d.doctorId.equalsIgnoreCase(doctorId)) {
                doctorExists = true;
                break;
            }
        }

        if (!doctorExists) {
            throw new IllegalArgumentException(
                    "Doctor ID does not exist."
            );
        }

        Appointment appointment = new Appointment(
                String.format("APT%03d", appointmentCounter++),
                patientId, doctorId, date, time, reason
        );

        appointments.add(appointment);

        return appointment;
    }

    public List<Appointment> getAppointments(String patientId) {

        List<Appointment> result = new ArrayList<>();

        for (Appointment a : appointments) {
            if (a.patientId.equalsIgnoreCase(patientId)) {
                result.add(a);
            }
        }

        return result;
    }

    public boolean cancelAppointment(String appointmentId,
                                     String patientId) {

        for (Appointment a : appointments) {

            if (a.appointmentId.equalsIgnoreCase(appointmentId)
                    && a.patientId.equalsIgnoreCase(patientId)
                    && "Pending".equalsIgnoreCase(a.status)) {

                a.status = "Cancelled";
                return true;
            }
        }

        return false;
    }

    // ---------------- TREATMENT ----------------

    public List<Treatment> getTreatments(String patientId) {

        List<Treatment> result = new ArrayList<>();

        if (doctorWorkspaceModel != null) {
            for (DoctorWorkspaceModel.Diagnosis diagnosis
                    : doctorWorkspaceModel.getDiagnoses(patientId)) {
                Treatment treatment = new Treatment(
                        diagnosis.diagnosisId,
                        diagnosis.patientId,
                        diagnosis.doctorId,
                        diagnosis.diagnosis,
                        diagnosis.notes
                );
                treatment.date = diagnosis.date;
                result.add(treatment);
            }
            return result;
        }

        for (Treatment t : treatments) {
            if (t.patientId.equalsIgnoreCase(patientId)) {
                result.add(t);
            }
        }

        return result;
    }

    // ---------------- MEDICINES ----------------

    public List<Medicine> getMedicines(String patientId) {

        List<Medicine> result = new ArrayList<>();

        if (doctorWorkspaceModel != null) {
            for (DoctorWorkspaceModel.Prescription prescription
                    : doctorWorkspaceModel.getPrescriptions(patientId)) {
                Medicine medicine = new Medicine(
                        prescription.prescriptionId,
                        prescription.patientId,
                        prescription.doctorId,
                        prescription.medicine,
                        prescription.dosage,
                        prescription.frequency,
                        prescription.duration,
                        prescription.instructions
                );
                medicine.date = prescription.date;
                result.add(medicine);
            }
            return result;
        }

        for (Medicine m : medicines) {
            if (m.patientId.equalsIgnoreCase(patientId)) {
                result.add(m);
            }
        }

        return result;
    }

    // ---------------- REPORTS ----------------

    public List<MedicalReport> getReports(String patientId) {

        List<MedicalReport> result = new ArrayList<>();

        if (doctorWorkspaceModel != null) {
            for (DoctorWorkspaceModel.TreatmentReport report
                    : doctorWorkspaceModel.getReports(patientId)) {
                MedicalReport medicalReport = new MedicalReport(
                        report.reportId,
                        report.patientId,
                        report.title,
                        report.date,
                        report.content
                );
                medicalReport.status = report.status;
                result.add(medicalReport);
            }
            return result;
        }

        for (MedicalReport r : reports) {
            if (r.patientId.equalsIgnoreCase(patientId)) {
                result.add(r);
            }
        }

        return result;
    }

    public MedicalReport getReport(String reportId,
                                   String patientId) {

        for (MedicalReport r : getReports(patientId)) {

            if (r.reportId.equalsIgnoreCase(reportId)) {
                return r;
            }
        }

        return null;
    }

    // ---------------- BILLING ----------------

    public List<Bill> getBills(String patientId) {

        List<Bill> result = new ArrayList<>();

        if (billRepository != null) {
            for (org.hosp1.billing.model.Bill bill
                    : billRepository.findByPatientId(patientId)) {
                StringBuilder description = new StringBuilder();
                for (org.hosp1.billing.model.BillItem item : bill.getItems()) {
                    if (description.length() > 0) {
                        description.append(", ");
                    }
                    description.append(item.getDescription());
                }
                result.add(new Bill(
                        bill.getId(),
                        bill.getPatientId(),
                        description.toString(),
                        bill.getTotal().doubleValue()
                ));
                result.get(result.size() - 1).date = bill.getIssuedDate().toString();
                result.get(result.size() - 1).status = bill.getStatus().toString();
            }
            return result;
        }

        for (Bill b : bills) {
            if (b.patientId.equalsIgnoreCase(patientId)) {
                result.add(b);
            }
        }

        return result;
    }

    public boolean submitPaymentRequest(
            String billId, String patientId) {

        if (billRepository != null) {
            return false;
        }

        for (Bill b : bills) {

            if (b.billId.equalsIgnoreCase(billId)
                    && b.patientId.equalsIgnoreCase(patientId)
                    && "Unpaid".equalsIgnoreCase(b.status)) {

                b.status = "Payment Requested";
                return true;
            }
        }

        return false;
    }

    // ---------------- ROOM AVAILABILITY ----------------

    public List<RoomInfo> getRooms() {
        if (roomRepository != null) {
            List<RoomInfo> result = new ArrayList<>();
            for (org.hosp1.facility.model.Room room : roomRepository.findAll()) {
                int occupied = allocationRepository == null
                        ? 0
                        : allocationRepository.countActiveByRoomId(room.getId());
                int available = Math.max(0, room.getCapacity() - occupied);
                result.add(new RoomInfo(
                        room.getId(),
                        roomTypeLabel(room.getType().name()),
                        "Not specified",
                        available,
                        available > 0 ? "Available" : "Full"
                ));
            }
            return result;
        }
        return new ArrayList<>(rooms);
    }

    public List<RoomInfo> searchRooms(String roomType) {

        List<RoomInfo> result = new ArrayList<>();

        for (RoomInfo r : getRooms()) {

            if (roomType == null
                    || roomType.trim().isEmpty()
                    || "All".equalsIgnoreCase(roomType)
                    || r.roomType.equalsIgnoreCase(roomType)) {

                result.add(r);
            }
        }

        return result;
    }

    private DoctorWorkspaceModel.Availability getAvailability(String doctorId) {
        if (doctorWorkspaceModel == null) {
            return null;
        }
        for (DoctorWorkspaceModel.Availability availability
                : doctorWorkspaceModel.getAvailabilities()) {
            if (availability.doctorId.equalsIgnoreCase(doctorId)) {
                return availability;
            }
        }
        return null;
    }

    private static String valueOrDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private static String roomTypeLabel(String type) {
        return switch (type) {
            case "GENERAL_WARD" -> "General";
            case "PRIVATE" -> "Private";
            case "ICU" -> "ICU";
            default -> type.replace('_', ' ');
        };
    }

    // ---------------- FEEDBACK ----------------

    public Feedback submitFeedback(
            String patientId,
            String category,
            int rating,
            String comments) {

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5."
            );
        }

        Feedback feedback = new Feedback(
                String.format("FDB%03d", feedbackCounter++),
                patientId,
                category,
                rating,
                comments
        );

        feedbacks.add(feedback);

        return feedback;
    }

    public List<Feedback> getFeedbacks(String patientId) {

        List<Feedback> result = new ArrayList<>();

        for (Feedback f : feedbacks) {
            if (f.patientId.equalsIgnoreCase(patientId)) {
                result.add(f);
            }
        }

        return result;
    }

    // ---------------- SEARCH DOCUMENTS ----------------

    public List<MedicalReport> searchDocuments(
            String patientId, String query) {

        List<MedicalReport> result = new ArrayList<>();

        String search = query == null
                ? "" : query.trim().toLowerCase();

        for (MedicalReport r : getReports(patientId)) {

            if (r.patientId.equalsIgnoreCase(patientId)
                    && (r.reportId.toLowerCase().contains(search)
                    || r.reportName.toLowerCase().contains(search)
                    || r.content.toLowerCase().contains(search))) {

                result.add(r);
            }
        }

        return result;
    }

    // ---------------- TABLE CONVERSION METHODS ----------------

    public List<Object[]> doctorRows(List<DoctorInfo> list) {

        List<Object[]> rows = new ArrayList<>();

        for (DoctorInfo d : list) {
            rows.add(new Object[]{
                    d.doctorId,
                    d.name,
                    d.specialization,
                    d.department,
                    d.availableDays,
                    d.availableTime
            });
        }

        return rows;
    }

    public List<Object[]> appointmentRows(String patientId) {

        List<Object[]> rows = new ArrayList<>();

        for (Appointment a : getAppointments(patientId)) {
            rows.add(new Object[]{
                    a.appointmentId,
                    a.doctorId,
                    a.date,
                    a.time,
                    a.reason,
                    a.status
            });
        }

        return rows;
    }

    public List<Object[]> treatmentRows(String patientId) {

        List<Object[]> rows = new ArrayList<>();

        for (Treatment t : getTreatments(patientId)) {
            rows.add(new Object[]{
                    t.treatmentId,
                    t.doctorId,
                    t.date,
                    t.diagnosis,
                    t.notes
            });
        }

        return rows;
    }

    public List<Object[]> medicineRows(String patientId) {

        List<Object[]> rows = new ArrayList<>();

        for (Medicine m : getMedicines(patientId)) {
            rows.add(new Object[]{
                    m.prescriptionId,
                    m.doctorId,
                    m.medicineName,
                    m.dosage,
                    m.frequency,
                    m.duration,
                    m.instructions
            });
        }

        return rows;
    }

    public List<Object[]> reportRows(String patientId) {
        return reportRows(getReports(patientId));
    }

    public List<Object[]> reportRows(List<MedicalReport> reports) {

        List<Object[]> rows = new ArrayList<>();

        for (MedicalReport r : reports) {
            rows.add(new Object[]{
                    r.reportId,
                    r.reportName,
                    r.reportDate,
                    r.status
            });
        }

        return rows;
    }

    public List<Object[]> billRows(String patientId) {

        List<Object[]> rows = new ArrayList<>();

        for (Bill b : getBills(patientId)) {
            rows.add(new Object[]{
                    b.billId,
                    b.description,
                    b.amount,
                    b.date,
                    b.status
            });
        }

        return rows;
    }

    public List<Object[]> roomRows(String roomType) {

        List<Object[]> rows = new ArrayList<>();

        for (RoomInfo r : searchRooms(roomType)) {
            rows.add(new Object[]{
                    r.roomId,
                    r.roomType,
                    r.floor,
                    r.availableBeds,
                    r.status
            });
        }

        return rows;
    }

    public List<Object[]> feedbackRows(String patientId) {

        List<Object[]> rows = new ArrayList<>();

        for (Feedback f : getFeedbacks(patientId)) {
            rows.add(new Object[]{
                    f.feedbackId,
                    f.category,
                    f.rating,
                    f.comments,
                    f.date
            });
        }

        return rows;
    }
}
