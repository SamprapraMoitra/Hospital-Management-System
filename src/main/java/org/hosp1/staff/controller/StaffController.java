
package org.hosp1.staff.controller;

import org.hosp1.staff.model.StaffManagementModel;
import org.hosp1.staff.model.StaffMember;
import org.hosp1.staff.view.SwingStaffView;

import java.util.List;

public class StaffController {

    private final StaffManagementModel model;
    private final SwingStaffView view;
    private final String loggedInStaffId;

    public StaffController(
            StaffManagementModel model,
            SwingStaffView view,
            String staffId) {

        this.model = model;
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

        StaffMember staff =
                model.getStaffById(loggedInStaffId);

        if (staff == null) {
            throw new IllegalArgumentException(
                    "Staff account not found."
            );
        }

        view.setProfile(staff);
    }

    // ---------------- UPDATE CONTACT DETAILS ----------------

    private void updateContact() {

        StaffMember staff =
                model.getStaffById(loggedInStaffId);

        if (staff == null) {
            throw new IllegalArgumentException(
                    "Staff account not found."
            );
        }

        if (!"Active".equals(staff.getStatus())) {
            throw new IllegalArgumentException(
                    "Inactive staff cannot update their profile."
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

        for (StaffMember other : model.getAllStaff()) {

            if (!other.getStaffId().equalsIgnoreCase(loggedInStaffId)
                    && other.getPhone().equals(phone)) {

                throw new IllegalArgumentException(
                        "Phone number already belongs to another staff member."
                );
            }
        }

        boolean updated = model.updateStaff(
                staff.getStaffId(),
                staff.getName(),
                staff.getGender(),
                staff.getDateOfBirth(),
                phone,
                email,
                address,
                staff.getDesignation(),
                staff.getDepartment(),
                staff.getQualification(),
                staff.getJoiningDate(),
                staff.getShift(),
                staff.getSalary(),
                staff.getUsername()
        );

        if (updated) {

            loadProfile();

            view.showMessage(
                    "Contact information updated successfully."
            );
        }
    }

    // ---------------- ATTENDANCE ----------------

    private void loadAttendance() {

        List<String[]> records =
                model.getAttendance(loggedInStaffId);

        StringBuilder result = new StringBuilder();

        result.append("MY ATTENDANCE HISTORY\n\n");

        if (records.isEmpty()) {

            result.append("No attendance records available.");

        } else {

            for (String[] record : records) {

                result.append("Date: ")
                        .append(record[1])
                        .append(" | Status: ")
                        .append(record[2])
                        .append("\n");
            }
        }

        view.setSchedule(result.toString());
    }
}
