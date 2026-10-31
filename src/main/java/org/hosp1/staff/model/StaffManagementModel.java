
package org.hosp1.staff.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StaffManagementModel {

    private final List<StaffMember> staffList = new ArrayList<>();
    private final List<String[]> attendanceList = new ArrayList<>();

    private int staffCounter = 1;

    public StaffManagementModel() {
        loadSampleData();
    }

    // ---------------- DESIGNATIONS ----------------

    public static final String[] DESIGNATIONS = {
            "Admin",
            "Doctor",
            "Nurse",
            "Pharmacist",
            "Ward Boy",
            "Ward Attendant",
            "Nursing Assistant",
            "Receptionist",
            "Registration Clerk",
            "Lab Technician",
            "Radiology Technician",
            "HR Officer",
            "Accountant",
            "Housekeeping Staff",
            "Security Guard",
            "Maintenance Staff"
    };

    // ---------------- DEPARTMENTS ----------------

    public static final String[] DEPARTMENTS = {
            "Administration",
            "Cardiology",
            "Neurology",
            "General Medicine",
            "Emergency",
            "ICU",
            "Nursing",
            "Pharmacy",
            "Laboratory",
            "Radiology",
            "Reception",
            "Human Resources",
            "Accounts",
            "Housekeeping",
            "Security",
            "Maintenance",
            "General"
    };

    public static final String[] SHIFTS = {
            "Morning",
            "Evening",
            "Night",
            "Rotational",
            "General"
    };

    // ---------------- SAMPLE DATA ----------------

    private void loadSampleData() {

        addSample(
                "Admin User", "Other", "1990-01-01",
                "9876500001", "admin@hospital.com",
                "Hospital Address", "Admin",
                "Administration", "Management",
                "2020-01-01", "General",
                "50000", "admin", "Active"
        );

        addSample(
                "Dr. Ananya Sen", "Female", "1985-05-10",
                "9876500002", "ananya@hospital.com",
                "Kolkata", "Doctor", "Cardiology",
                "MBBS, MD", "2021-02-10",
                "Morning", "80000", "ananya", "Active"
        );

        addSample(
                "Riya Das", "Female", "1995-03-15",
                "9876500003", "riya@hospital.com",
                "Kolkata", "Nurse", "Nursing",
                "BSc Nursing", "2022-04-01",
                "Morning", "25000", "riya", "Active"
        );

        addSample(
                "Rahul Roy", "Male", "1992-07-20",
                "9876500004", "rahul@hospital.com",
                "Kolkata", "Ward Boy", "General",
                "Higher Secondary", "2023-01-10",
                "Night", "15000", "rahul", "Active"
        );

        addSample(
                "Amit Ghosh", "Male", "1994-08-11",
                "9876500005", "amit@hospital.com",
                "Kolkata", "Lab Technician", "Laboratory",
                "Diploma in Medical Lab Technology",
                "2022-08-15", "Evening",
                "22000", "amit", "Active"
        );

        addSample(
                "Priya Sen", "Female", "1997-06-12",
                "9876500006", "priya@hospital.com",
                "Kolkata", "Receptionist", "Reception",
                "Graduate", "2023-06-01",
                "General", "18000", "priya", "Active"
        );
    }

    private void addSample(
            String name,
            String gender,
            String dob,
            String phone,
            String email,
            String address,
            String designation,
            String department,
            String qualification,
            String joiningDate,
            String shift,
            String salary,
            String username,
            String status) {

        StaffMember member = new StaffMember(
                generateStaffId(),
                name, gender, dob, phone, email, address,
                designation, department, qualification,
                joiningDate, shift, salary, username, status
        );

        staffList.add(member);
    }

    // ---------------- ID GENERATION ----------------

    private String generateStaffId() {

        return String.format(
                "STF%03d",
                staffCounter++
        );
    }

    // ---------------- ADD STAFF ----------------

    public StaffMember addStaff(
            String name,
            String gender,
            String dob,
            String phone,
            String email,
            String address,
            String designation,
            String department,
            String qualification,
            String joiningDate,
            String shift,
            String salary,
            String username) {

        validateStaff(
                name, phone, email, designation,
                department, username, salary
        );

        for (StaffMember s : staffList) {

            if (s.getUsername().equalsIgnoreCase(username)) {
                throw new IllegalArgumentException(
                        "Username already exists."
                );
            }

            if (s.getPhone().equals(phone)) {
                throw new IllegalArgumentException(
                        "Phone number already exists."
                );
            }
        }

        StaffMember member = new StaffMember(
                generateStaffId(),
                name, gender, dob, phone, email, address,
                designation, department, qualification,
                joiningDate, shift, salary, username, "Active"
        );

        staffList.add(member);

        return member;
    }

    // ---------------- UPDATE STAFF ----------------

    public boolean updateStaff(
            String id,
            String name,
            String gender,
            String dob,
            String phone,
            String email,
            String address,
            String designation,
            String department,
            String qualification,
            String joiningDate,
            String shift,
            String salary,
            String username) {

        validateStaff(
                name, phone, email, designation,
                department, username, salary
        );

        StaffMember member = getStaffById(id);

        if (member == null) {
            return false;
        }

        for (StaffMember s : staffList) {

            if (!s.getStaffId().equalsIgnoreCase(id)
                    && s.getUsername().equalsIgnoreCase(username)) {

                throw new IllegalArgumentException(
                        "Username already belongs to another staff member."
                );
            }

            if (!s.getStaffId().equalsIgnoreCase(id)
                    && s.getPhone().equals(phone)) {

                throw new IllegalArgumentException(
                        "Phone number already belongs to another staff member."
                );
            }
        }

        if (!member.getStatus().equals("Active")) {
            throw new IllegalArgumentException(
                    "Inactive staff records cannot be updated."
            );
        }

        member.updateDetails(
                name, gender, dob, phone, email, address,
                designation, department, qualification,
                joiningDate, shift, salary, username
        );

        return true;
    }

    // ---------------- SEARCH STAFF ----------------

    public List<StaffMember> getAllStaff() {
        return new ArrayList<>(staffList);
    }

    public StaffMember getStaffById(String id) {

        for (StaffMember s : staffList) {

            if (s.getStaffId().equalsIgnoreCase(id)) {
                return s;
            }
        }

        return null;
    }

    public List<StaffMember> searchStaff(String query) {

        List<StaffMember> result = new ArrayList<>();

        String search = query == null
                ? "" : query.trim().toLowerCase(Locale.ROOT);

        for (StaffMember s : staffList) {

            if (s.getStaffId().toLowerCase().contains(search)
                    || s.getName().toLowerCase().contains(search)
                    || s.getDesignation().toLowerCase().contains(search)
                    || s.getDepartment().toLowerCase().contains(search)
                    || s.getPhone().contains(search)) {

                result.add(s);
            }
        }

        return result;
    }

    // ---------------- FILTER BY DESIGNATION ----------------

    public List<StaffMember> getStaffByDesignation(
            String designation) {

        List<StaffMember> result = new ArrayList<>();

        for (StaffMember s : staffList) {

            if (designation == null
                    || designation.equalsIgnoreCase("All")
                    || s.getDesignation().equalsIgnoreCase(designation)) {

                result.add(s);
            }
        }

        return result;
    }

    // ---------------- FILTER BY DEPARTMENT ----------------

    public List<StaffMember> getStaffByDepartment(
            String department) {

        List<StaffMember> result = new ArrayList<>();

        for (StaffMember s : staffList) {

            if (department == null
                    || department.equalsIgnoreCase("All")
                    || s.getDepartment().equalsIgnoreCase(department)) {

                result.add(s);
            }
        }

        return result;
    }

    // ---------------- ACTIVATE / DEACTIVATE ----------------

    public boolean changeStaffStatus(
            String id,
            String newStatus) {

        StaffMember member = getStaffById(id);

        if (member == null) {
            return false;
        }

        if (!newStatus.equals("Active")
                && !newStatus.equals("Inactive")) {

            throw new IllegalArgumentException(
                    "Invalid staff status."
            );
        }

        member.setStatus(newStatus);

        return true;
    }

    // ---------------- ATTENDANCE ----------------

    public void markAttendance(
            String staffId,
            String date,
            String attendanceStatus) {

        if (getStaffById(staffId) == null) {
            throw new IllegalArgumentException(
                    "Staff member not found."
            );
        }

        if (!attendanceStatus.equals("Present")
                && !attendanceStatus.equals("Absent")
                && !attendanceStatus.equals("Leave")) {

            throw new IllegalArgumentException(
                    "Invalid attendance status."
            );
        }

        for (String[] record : attendanceList) {

            if (record[0].equalsIgnoreCase(staffId)
                    && record[1].equals(date)) {

                record[2] = attendanceStatus;
                return;
            }
        }

        attendanceList.add(new String[]{
                staffId, date, attendanceStatus
        });
    }

    public List<String[]> getAttendance(String staffId) {

        List<String[]> result = new ArrayList<>();

        for (String[] record : attendanceList) {

            if (staffId == null
                    || record[0].equalsIgnoreCase(staffId)) {

                result.add(record.clone());
            }
        }

        return result;
    }

    // ---------------- STATISTICS ----------------

    public int getTotalStaff() {
        return staffList.size();
    }

    public int getActiveStaff() {

        int count = 0;

        for (StaffMember s : staffList) {
            if (s.getStatus().equals("Active")) {
                count++;
            }
        }

        return count;
    }

    public int getInactiveStaff() {
        return getTotalStaff() - getActiveStaff();
    }

    public int getDesignationCount(String designation) {

        int count = 0;

        for (StaffMember s : staffList) {

            if (s.getDesignation().equalsIgnoreCase(designation)) {
                count++;
            }
        }

        return count;
    }

    // ---------------- TABLE DATA ----------------

    public List<Object[]> getStaffRows(
            List<StaffMember> list) {

        List<Object[]> rows = new ArrayList<>();

        for (StaffMember s : list) {
            rows.add(s.toTableRow());
        }

        return rows;
    }

    public List<Object[]> getAttendanceRows(String staffId) {

        List<Object[]> rows = new ArrayList<>();

        for (String[] a : getAttendance(staffId)) {
            rows.add(a);
        }

        return rows;
    }

    // ---------------- VALIDATION ----------------

    private void validateStaff(
            String name,
            String phone,
            String email,
            String designation,
            String department,
            String username,
            String salary) {

        if (name == null || name.trim().isEmpty()
                || phone == null || phone.trim().isEmpty()
                || designation == null || designation.trim().isEmpty()
                || department == null || department.trim().isEmpty()
                || username == null || username.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Name, phone, designation, department and username are required."
            );
        }

        if (!phone.matches("[0-9+() -]{7,20}")) {
            throw new IllegalArgumentException(
                    "Enter a valid phone number."
            );
        }

        if (email != null && !email.trim().isEmpty()
                && !email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            throw new IllegalArgumentException(
                    "Enter a valid email address."
            );
        }

        try {

            double amount = Double.parseDouble(salary);

            if (!Double.isFinite(amount) || amount < 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Enter a valid non-negative salary."
            );
        }
    }
}
