package org.hosp1.admin.controller;

import org.hosp1.admin.model.Admin;
import org.hosp1.admin.view.AdminView;
import org.hosp1.common.util.PasswordUtil;
import org.hosp1.patients.model.PatientRepository;
import org.hosp1.staff.model.Doctor;
import org.hosp1.staff.model.StaffRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.UUID;

/** Controls the admin screen: validates input, calls repositories, tells the view what to show. */
public class AdminController {

    private final AdminView view;
    private final StaffRepository staffRepo;
    private final PatientRepository patientRepo;
    private final Admin admin;
    private final Runnable onLogout;
    private final Runnable onOpenBilling;

    public AdminController(AdminView view, StaffRepository staffRepo, PatientRepository patientRepo,
                           Admin admin, Runnable onLogout, Runnable onOpenBilling) {
        this.view = view;
        this.staffRepo = staffRepo;
        this.patientRepo = patientRepo;
        this.admin = admin;
        this.onLogout = onLogout;
        this.onOpenBilling = onOpenBilling;

        view.setAddStaffAction(this::addDoctor);
        view.setRemoveStaffAction(this::removeStaff);
        view.setRefreshAction(this::refresh);
        view.setLogoutAction(this::logout);
        view.setBillingAction(onOpenBilling);

    }

    public void start() {
        view.setWelcome(admin.getName());
        refresh();
        view.show();
    }

    public void refresh() {
        view.showStaff(staffRepo.findAll());
        view.showPatients(patientRepo.findAll());
    }

    public void addDoctor() {
        char[] password = view.getNewPassword();
        try {
            String name = view.getNewName();
            String email = view.getNewEmail();
            String phone = view.getNewPhone();
            String gender = view.getNewGender();
            String address = view.getNewAddress();
            int staffId = view.getNewStaffId();
            LocalDate joiningDate = view.getNewJoiningDate();
            String designation = view.getNewDesignation();
            double salary = view.getNewSalary();
            String qualification = view.getNewQualification();
            String specialization = view.getNewSpecialization();

            if (name.isEmpty() || email.isEmpty() || specialization.isEmpty()) {
                view.showError("Name, email and specialization are required.");
                return;
            }
            if (!email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
                view.showError("Enter a valid email address.");
                return;
            }
            if (password.length < 8) {
                view.showError("Password must be at least 8 characters.");
                return;
            }
            boolean emailTaken = staffRepo.findAll().stream()
                    .anyMatch(s -> s.getEmail().equalsIgnoreCase(email));
            if (emailTaken) {
                view.showError("A staff member with this email already exists.");
                return;
            }
            String id = "D-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            staffRepo.save(new Doctor(id, name, email, phone, gender, address, "DOCTOR",
                    PasswordUtil.hash(password), staffId, joiningDate, designation, salary,
                    qualification, specialization, ""));
            view.clearForm();
            refresh();
            view.showMessage("Doctor " + name + " added (ID " + id + ").");
        } catch (NumberFormatException e) {
            view.showError("Staff ID and salary must be valid numbers.");
        } catch (DateTimeParseException e) {
            view.showError("Joining date must use YYYY-MM-DD format.");
        } finally {
            Arrays.fill(password, '\0');   // wipe the password from memory
        }
    }

    public void removeStaff() {
        String id = view.getSelectedStaffId();
        if (id == null) {
            view.showError("Select a staff member first.");
            return;
        }
        if (!view.confirm("Remove staff member " + id + "? This cannot be undone.")) {
            return;
        }
        staffRepo.deleteById(id);
        refresh();
    }

    public void logout() {
        view.close();
        onLogout.run();
    }
}