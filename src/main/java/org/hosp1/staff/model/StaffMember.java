
package org.hosp1.staff.model;

public class StaffMember {

    private String staffId;
    private String name;
    private String gender;
    private String dateOfBirth;
    private String phone;
    private String email;
    private String address;

    private String designation;
    private String department;
    private String qualification;
    private String joiningDate;
    private String shift;
    private String salary;

    private String username;
    private String status;

    public StaffMember(
            String staffId,
            String name,
            String gender,
            String dateOfBirth,
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

        this.staffId = staffId;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.designation = designation;
        this.department = department;
        this.qualification = qualification;
        this.joiningDate = joiningDate;
        this.shift = shift;
        this.salary = salary;
        this.username = username;
        this.status = status;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getDesignation() {
        return designation;
    }

    public String getDepartment() {
        return department;
    }

    public String getQualification() {
        return qualification;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public String getShift() {
        return shift;
    }

    public String getSalary() {
        return salary;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public void updateDetails(
            String name,
            String gender,
            String dateOfBirth,
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

        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.designation = designation;
        this.department = department;
        this.qualification = qualification;
        this.joiningDate = joiningDate;
        this.shift = shift;
        this.salary = salary;
        this.username = username;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object[] toTableRow() {

        return new Object[]{
                staffId,
                name,
                designation,
                department,
                phone,
                shift,
                status
        };
    }
}
