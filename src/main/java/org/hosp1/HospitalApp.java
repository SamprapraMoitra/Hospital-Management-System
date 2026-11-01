package org.hosp1;

import org.hosp1.admin.controller.AdminController;
import org.hosp1.admin.model.Admin;
import org.hosp1.admin.model.AdminRepository;
import org.hosp1.admin.view.SwingAdminView;
import org.hosp1.auth.controller.AuthController;
import org.hosp1.billing.controller.BillingController;
import org.hosp1.billing.controller.PatientBillingController;
import org.hosp1.billing.model.Bill;
import org.hosp1.billing.model.BillItem;
import org.hosp1.billing.model.BillRepository;
import org.hosp1.billing.view.SwingBillingView;
import org.hosp1.billing.view.SwingPatientBillingView;
import org.hosp1.auth.view.SwingLoginView;
import org.hosp1.auth.model.SessionManager;
import org.hosp1.common.model.User;
import org.hosp1.facility.controller.FacilityController;
import org.hosp1.facility.model.RoomAllocationRepository;
import org.hosp1.facility.model.RoomRepository;
import org.hosp1.facility.view.SwingFacilityView;
import org.hosp1.common.util.PasswordUtil;
import org.hosp1.patients.model.Patient;
import org.hosp1.patients.model.PatientRepository;
import org.hosp1.patients.controller.PatientController;
import org.hosp1.patients.model.PatientPortalModel;
import org.hosp1.patients.view.PatientPortalView;
import org.hosp1.staff.controller.DoctorController;
import org.hosp1.staff.controller.StaffController;
import org.hosp1.staff.model.Doctor;
import org.hosp1.staff.model.DoctorWorkspaceModel;
import org.hosp1.staff.model.Staff;
import org.hosp1.staff.model.StaffRepository;
import org.hosp1.staff.view.DoctorView;
import org.hosp1.staff.view.SwingStaffView;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Starting point. Wires authentication, admin, staff, and patient billing screens.
 *
 * Demo login:  admin@hosp.org  /  Admin@1234
 */
public class HospitalApp {

    // shared storage (in-memory, HashMap based)
    private static final AdminRepository adminRepo = new AdminRepository();
    private static final StaffRepository staffRepo = new StaffRepository();
    private static final PatientRepository patientRepo = new PatientRepository();
    private static final BillRepository billRepo = new BillRepository();
    private static final RoomRepository roomRepo = new RoomRepository();
    private static final RoomAllocationRepository allocationRepo = new RoomAllocationRepository();
    private static final DoctorWorkspaceModel doctorWorkspaceModel =
            new DoctorWorkspaceModel(patientRepo);
    private static final PatientPortalModel patientPortalModel =
            new PatientPortalModel(staffRepo, doctorWorkspaceModel, billRepo,
                    roomRepo, allocationRepo);

    private static AuthController auth;

    public static void main(String[] args) {
        seedData();

        SwingUtilities.invokeLater(() -> {          // Swing must start on its own thread
            auth = new AuthController(
                    List.of(adminRepo, staffRepo, patientRepo),
                    new SwingLoginView(),
                    new SessionManager(),
                    HospitalApp::openDashboardFor,
                    HospitalApp::openPatientRegistration);
            auth.start();
        });
    }

    /** Creates the first admin, otherwise nobody could log in. */
    private static void seedData() {
        adminRepo.save(new Admin("A-001", "Root Admin", "admin@hosp.org","9830503961","Male","kolkata","ADMIN",
                PasswordUtil.hash("Admin@1234".toCharArray()), "EMP-001"));
    }

    /** Chooses the window by user type. */
    private static void openDashboardFor(User user) {
        if (user instanceof Admin admin) {
            new AdminController(new SwingAdminView(), staffRepo, patientRepo, admin,
                    () -> auth.start(),// logout -> back to login screen

                    HospitalApp::openBilling,
                    HospitalApp::openFacility).start();
        } else if (user instanceof Patient patient) {
            openPatientDashboard(patient);
        } else if (user instanceof Staff staff) {
            openStaffDashboard(staff);
        } else {
            JOptionPane.showMessageDialog(null,
                    "The dashboard for " + user.getClass().getSimpleName() + " is not built yet.",
                    "Coming soon", JOptionPane.INFORMATION_MESSAGE);
            auth.start();
        }
    }

    private static void openStaffDashboard(Staff staff) {
        if (staff instanceof Doctor doctor) {
            openDoctorDashboard(doctor);
            return;
        }

        JFrame frame = new JFrame("Hospital Management - Staff Dashboard");
        AtomicReference<StaffController> controllerReference = new AtomicReference<>();
        SwingStaffView view = new SwingStaffView(action -> {
            if ("staff.logout".equals(action)) {
                frame.dispose();
                auth.start();
                return;
            }

            StaffController controller = controllerReference.get();
            if (controller == null) {
                throw new IllegalStateException("Staff controller is not initialized.");
            }
            controller.onAction(action);
        });

        controllerReference.set(new StaffController(staffRepo, view, staff.getUserId()));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(view);
        frame.setSize(850, 600);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                auth.start();
            }
        });
        frame.setVisible(true);
    }

    private static void openDoctorDashboard(Doctor doctor) {
        JFrame frame = new JFrame("Hospital Management - Doctor Dashboard");
        AtomicReference<DoctorController> doctorControllerReference =
                new AtomicReference<>();

        DoctorView doctorView = new DoctorView((action, data) -> {
            if ("doctorWorkspace.logout".equals(action)) {
                frame.dispose();
                auth.start();
                return;
            }
            DoctorController controller = doctorControllerReference.get();
            if (controller == null) {
                throw new IllegalStateException("Doctor controller is not initialized.");
            }
            controller.onAction(action, data);
        });
        doctorView.setDoctorIdentity(doctor.getUserId());

        doctorControllerReference.set(
                new DoctorController(doctorWorkspaceModel, doctorView));

        JButton logout = new JButton("Logout");
        logout.addActionListener(event -> {
            frame.dispose();
            auth.start();
        });
        JPanel dashboard = new JPanel(new java.awt.BorderLayout());
        dashboard.add(doctorView, java.awt.BorderLayout.CENTER);
        dashboard.add(logout, java.awt.BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(dashboard);
        frame.setSize(1100, 780);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                auth.start();
            }
        });
        frame.setVisible(true);
    }

    private static void openPatientDashboard(Patient patient) {
        JFrame frame = new JFrame("Hospital Management - Patient Portal");
        AtomicReference<PatientController> controllerReference =
                new AtomicReference<>();
        PatientPortalView view = new PatientPortalView((action, data) -> {
            if ("patientPortal.logout".equals(action)) {
                frame.dispose();
                auth.start();
                return;
            }
            if ("patientPortal.paymentRequest".equals(action)) {
                openPatientBilling(patient);
                return;
            }
            PatientController controller = controllerReference.get();
            if (controller == null) {
                throw new IllegalStateException("Patient controller is not initialized.");
            }
            controller.onAction(action, data);
        });
        controllerReference.set(new PatientController(
                patientPortalModel, view, null, patient.getUserId()));
        view.setPatientIdentity(patient.getUserId(), patient.getName());

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(view);
        frame.setSize(1100, 800);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                auth.start();
            }
        });
        frame.setVisible(true);
    }

    private static void openPatientBilling(Patient patient) {
        new PatientBillingController(
                new SwingPatientBillingView(),
                billRepo,
                patient,
                () -> { }
        ).start();
    }

    private static void openBilling() {
        new BillingController(new SwingBillingView(), billRepo, patientRepo).start();
    }

    private static void openFacility() {
        new FacilityController(new SwingFacilityView(), roomRepo, allocationRepo, patientRepo,
                (room, stay, days, charge) -> {
                    BillItem item = new BillItem(
                            "Room " + room.getNumber() + " (" + room.getType() + ") per day",
                            (int) days, room.getDailyRate());
                    String billId = "B-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    billRepo.save(new Bill(billId, stay.getPatientId(), LocalDate.now(), List.of(item)));
                }).start();
    }

    private static void openPatientRegistration() {
        JTextField name = new JTextField(20);
        JTextField email = new JTextField(20);
        JTextField phone = new JTextField(20);
        JTextField gender = new JTextField(20);
        JTextField address = new JTextField(20);
        JPasswordField password = new JPasswordField(20);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Full name:"));
        form.add(name);
        form.add(new JLabel("Email:"));
        form.add(email);
        form.add(new JLabel("Phone:"));
        form.add(phone);
        form.add(new JLabel("Gender:"));
        form.add(gender);
        form.add(new JLabel("Address:"));
        form.add(address);
        form.add(new JLabel("Password (8+ characters):"));
        form.add(password);

        int choice = JOptionPane.showConfirmDialog(
                null,
                form,
                "Create Patient Account",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        char[] rawPassword = password.getPassword();
        try {
            String cleanName = name.getText().trim();
            String cleanEmail = email.getText().trim();
            String cleanPhone = phone.getText().trim();
            if (cleanName.isEmpty() || cleanEmail.isEmpty()
                    || cleanPhone.isEmpty() || gender.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Name, email, phone and gender are required.",
                        "Registration Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!cleanEmail.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
                JOptionPane.showMessageDialog(null,
                        "Enter a valid email address.",
                        "Registration Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!cleanPhone.matches("[0-9+() -]{7,20}")) {
                JOptionPane.showMessageDialog(null,
                        "Enter a valid phone number.",
                        "Registration Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (rawPassword.length < 8) {
                JOptionPane.showMessageDialog(null,
                        "Password must contain at least 8 characters.",
                        "Registration Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean emailAlreadyUsed = patientRepo.existsByEmail(cleanEmail)
                    || staffRepo.findByEmail(cleanEmail).isPresent()
                    || adminRepo.findByEmail(cleanEmail).isPresent();
            if (emailAlreadyUsed) {
                JOptionPane.showMessageDialog(null,
                        "An account with this email already exists.",
                        "Registration Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String patientId = "P-"
                    + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            patientRepo.save(new Patient(
                    patientId,
                    cleanName,
                    cleanEmail,
                    cleanPhone,
                    gender.getText().trim(),
                    address.getText().trim(),
                    "PATIENT",
                    PasswordUtil.hash(rawPassword)
            ));
            JOptionPane.showMessageDialog(null,
                    "Patient account created. Your Patient ID is "
                            + patientId + ". You can now log in.",
                    "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
        } finally {
            Arrays.fill(rawPassword, '\0');
        }
    }
}