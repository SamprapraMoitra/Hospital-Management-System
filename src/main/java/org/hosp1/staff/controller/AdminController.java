
package org.hosp1.staff.controller;

import org.hosp1.staff.model.StaffManagementModel;
import org.hosp1.staff.model.StaffMember;
import org.hosp1.staff.view.SwingAdminView;

import java.time.LocalDate;
import java.util.List;

public class AdminController {

    private final StaffManagementModel model;
    private final SwingAdminView view;

    public AdminController(
            StaffManagementModel model,
            SwingAdminView view) {

        this.model = model;
        this.view = view;

        loadAllStaff();
        loadStatistics();
    }

    public void onAction(String action) {

        try {

            switch (action) {

                case "admin.create":
                    createStaff();
                    break;

                case "admin.list":
                    loadAllStaff();
                    break;

                case "admin.search":
                    searchStaff();
                    break;

                case "admin.filter":
                    filterStaff();
                    break;

                case "admin.update":
                    updateStaff();
                    break;

                case "admin.view":
                    viewStaff();
                    break;

                case "admin.activate":
                    changeStatus("Active");
                    break;

                case "admin.deactivate":
                    changeStatus("Inactive");
                    break;

                case "admin.statistics":
                    loadStatistics();
                    break;

                case "admin.attendanceMark":
                    markAttendance();
                    break;

                case "admin.attendanceList":
                    loadAttendance();
                    break;

                default:
                    view.showError("Unknown Admin operation.");
            }

        } catch (IllegalArgumentException e) {

            view.showError(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();
            view.showError("Operation failed: " + e.getMessage());
        }
    }

    // ---------------- CREATE STAFF ----------------

    private void createStaff() {

        StaffMember staff = model.addStaff(
                view.getName(),
                view.getGender(),
                view.getDob(),
                view.getPhone(),
                view.getEmail(),
                view.getAddress(),
                view.getDesignation(),
                view.getDepartment(),
                view.getQualification(),
                view.getJoiningDate(),
                view.getShift(),
                view.getSalary(),
                view.getUsername()
        );

        loadAllStaff();
        loadStatistics();

        view.clearForm();

        view.showMessage(
                "Staff registered successfully!\n\n"
                        + "Generated Staff ID: " + staff.getStaffId()
        );
    }

    // ---------------- DISPLAY STAFF ----------------

    private void loadAllStaff() {

        view.setRows(
                model.getStaffRows(model.getAllStaff())
        );
    }

    // ---------------- SEARCH ----------------

    private void searchStaff() {

        List<StaffMember> result =
                model.searchStaff(view.getSearch());

        view.setRows(model.getStaffRows(result));
    }

    // ---------------- FILTER ----------------

    private void filterStaff() {

        String designation = view.getDesignationFilter();
        String department = view.getDepartmentFilter();

        List<StaffMember> result;

        if (designation != null
                && !designation.equals("All")) {

            result = model.getStaffByDesignation(designation);

        } else if (department != null
                && !department.equals("All")) {

            result = model.getStaffByDepartment(department);

        } else {

            result = model.getAllStaff();
        }

        if (department != null && !department.equals("All")) {

            result.removeIf(s ->
                    !s.getDepartment().equalsIgnoreCase(department)
            );
        }

        view.setRows(model.getStaffRows(result));
    }

    // ---------------- UPDATE ----------------

    private void updateStaff() {

        String id = view.getSelectedStaffId();

        if (id == null) {
            view.showError("Select a staff record first.");
            return;
        }

        StaffMember staff = model.getStaffById(id);

        if (staff == null) {
            view.showError("Staff record not found.");
            return;
        }

        view.loadStaffIntoForm(staff);

        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                view,
                "The selected staff details have been loaded "
                        + "into the Register Staff tab.\n"
                        + "Edit the details and press Update Selected again "
                        + "to save the changes.",
                "Edit Staff",
                javax.swing.JOptionPane.OK_CANCEL_OPTION
        );

        if (confirm != javax.swing.JOptionPane.OK_OPTION) {
            return;
        }

        // The loaded form is retained for editing.
        // The next click on Update Selected performs the save.
        // To prevent accidental creation, the existing Staff ID
        // is checked before saving.
        updateLoadedStaff(id);
    }

    private void updateLoadedStaff(String id) {

        // Confirmation is deliberately separate from editing.
        // Use the registration form fields to save changes.
        boolean updated = model.updateStaff(
                id,
                view.getName(),
                view.getGender(),
                view.getDob(),
                view.getPhone(),
                view.getEmail(),
                view.getAddress(),
                view.getDesignation(),
                view.getDepartment(),
                view.getQualification(),
                view.getJoiningDate(),
                view.getShift(),
                view.getSalary(),
                view.getUsername()
        );

        if (updated) {

            loadAllStaff();
            loadStatistics();

            view.showMessage(
                    "Staff information updated successfully."
            );
        }
    }

    // ---------------- VIEW DETAILS ----------------

    private void viewStaff() {

        String id = view.getSelectedStaffId();

        if (id == null) {
            view.showError("Select a staff member.");
            return;
        }

        StaffMember s = model.getStaffById(id);

        if (s == null) return;

        String details =
                "STAFF DETAILS\n\n"
                        + "ID: " + s.getStaffId() + "\n"
                        + "Name: " + s.getName() + "\n"
                        + "Gender: " + s.getGender() + "\n"
                        + "DOB: " + s.getDateOfBirth() + "\n"
                        + "Phone: " + s.getPhone() + "\n"
                        + "Email: " + s.getEmail() + "\n"
                        + "Address: " + s.getAddress() + "\n"
                        + "Designation: " + s.getDesignation() + "\n"
                        + "Department: " + s.getDepartment() + "\n"
                        + "Qualification: " + s.getQualification() + "\n"
                        + "Joining Date: " + s.getJoiningDate() + "\n"
                        + "Shift: " + s.getShift() + "\n"
                        + "Salary: " + s.getSalary() + "\n"
                        + "Username: " + s.getUsername() + "\n"
                        + "Status: " + s.getStatus();

        view.showMessage(details);
    }

    // ---------------- STATUS ----------------

    private void changeStatus(String status) {

        String id = view.getSelectedStaffId();

        if (id == null) {
            view.showError("Select a staff member.");
            return;
        }

        if (!model.changeStaffStatus(id, status)) {
            view.showError("Staff member not found.");
            return;
        }

        loadAllStaff();
        loadStatistics();

        view.showMessage(
                "Staff status changed to " + status + "."
        );
    }

    // ---------------- STATISTICS ----------------

    private void loadStatistics() {

        view.setStatistics(
                model.getTotalStaff(),
                model.getActiveStaff(),
                model.getInactiveStaff()
        );
    }

    // ---------------- ATTENDANCE ----------------

    private void markAttendance() {

        String id = view.getSelectedStaffId();
        String date = view.getAttendanceDate();

        if (id == null) {
            view.showError("Select a staff member first.");
            return;
        }

        try {

            LocalDate.parse(date);

        } catch (Exception e) {

            view.showError("Enter date in yyyy-mm-dd format.");
            return;
        }

        model.markAttendance(
                id,
                date,
                view.getAttendanceStatus()
        );

        view.showMessage("Attendance recorded successfully.");

        loadAttendance();
    }

    private void loadAttendance() {

        String id = view.getSelectedStaffId();

        view.setAttendanceRows(
                model.getAttendanceRows(id)
        );
    }
}
