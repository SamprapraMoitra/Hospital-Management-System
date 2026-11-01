
package org.hosp1.staff.controller;

import org.hosp1.staff.model.Staff;
import org.hosp1.staff.model.StaffRepository;
import org.hosp1.staff.view.SwingStaffView;

public class StaffController {

    private final StaffRepository repository;
    private final SwingStaffView view;
    private final String loggedInStaffId;

    public StaffController(
            StaffRepository repository,
            SwingStaffView view,
            String staffId) {

        this.repository = repository;
        this.view = view;
        this.loggedInStaffId = staffId;

        loadProfile();
    }

    public void onAction(String action) {

        try {

            switch (action) {

                case "staff.updateContact":
                    updateContact();
                    break;

                case "staff.attendance":
                    loadAttendance();
                    break;

                default:
                    view.showError("Unknown staff operation.");
            }

        } catch (IllegalArgumentException e) {

            view.showError(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            view.showError(
                    "Operation failed: " + e.getMessage()
            );
        }
    }

    // ---------------- LOAD PROFILE ----------------

    private void loadProfile() {

        Staff staff = repository.findById(loggedInStaffId).orElse(null);

        if (staff == null) {
            throw new IllegalArgumentException(
                    "Staff account not found."
            );
        }

        view.setProfile(staff);
    }

    // ---------------- UPDATE CONTACT DETAILS ----------------

    private void updateContact() {

        Staff staff = repository.findById(loggedInStaffId).orElse(null);

        if (staff == null) {
            throw new IllegalArgumentException(
                    "Staff account not found."
            );
        }

        String phone = view.getPhone();
        String email = view.getEmail();
        String address = view.getAddress();

        if (!phone.matches("[0-9+() -]{7,20}")) {
            throw new IllegalArgumentException(
                    "Enter a valid phone number."
            );
        }

        if (!email.isEmpty()
                && !email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            throw new IllegalArgumentException(
                    "Enter a valid email address."
            );
        }

        for (Staff other : repository.findAll()) {
            if (!other.getUserId().equalsIgnoreCase(loggedInStaffId)
                    && phone.equals(other.getPhone())) {

                throw new IllegalArgumentException(
                        "Phone number already belongs to another staff member."
                );
            }
        }

        if (!email.isEmpty() && repository.findByEmail(email)
                .filter(existing -> !existing.getUserId().equalsIgnoreCase(loggedInStaffId))
                .isPresent()) {
            throw new IllegalArgumentException(
                    "Email address already belongs to another staff member."
            );
        }

        staff.setPhone(phone);
        staff.setEmail(email);
        staff.setAddress(address);
        repository.save(staff);

        loadProfile();
        view.showMessage("Contact information updated successfully.");
    }

    // ---------------- ATTENDANCE ----------------

    private void loadAttendance() {
        view.setSchedule("Attendance records are not available yet.");
    }
}
