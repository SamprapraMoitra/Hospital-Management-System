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
import org.hosp1.staff.model.StaffRepository;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Starting point. Wires everything built so far: auth (login) + admin dashboard.
 * Doctor and patient dashboards and patient registration are not built yet,
 * so those paths show a "coming soon" message instead of crashing.
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

    private static AuthController auth;

    public static void main(String[] args) {
        seedData();

        SwingUtilities.invokeLater(() -> {          // Swing must start on its own thread
            auth = new AuthController(
                    List.of(adminRepo),             // add staffRepo / patientRepo once they implement UserProvider
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
            new PatientBillingController(new SwingPatientBillingView(), billRepo, patient,
                    () -> auth.start()).start();
        } else {
            JOptionPane.showMessageDialog(null,
                    "The dashboard for " + user.getClass().getSimpleName() + " is not built yet.",
                    "Coming soon", JOptionPane.INFORMATION_MESSAGE);
            auth.start();
        }
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
        JOptionPane.showMessageDialog(null,
                "Patient registration is not built yet.",
                "Coming soon", JOptionPane.INFORMATION_MESSAGE);
    }
}