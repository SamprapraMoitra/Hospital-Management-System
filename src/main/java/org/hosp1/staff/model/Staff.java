package org.hosp1.staff.model;

import java.time.LocalDate;

import org.hosp1.common.model.User;

public class Staff extends User {

    private int staffId;
    private LocalDate joiningDate;
    private String designation;
    private double salary;
    private String qualification;

    public Staff() {
        super();
    }

    public Staff(String userId, String name, String email, String phone,
                 String gender, String address, String role, String passwordHash,
                 int staffId, LocalDate joiningDate, String designation,
                 double salary, String qualification) {
        super(userId, name, email, phone, gender, address, role, passwordHash);
        this.staffId = staffId;
        this.joiningDate = joiningDate;
        this.designation = designation;
        this.salary = salary;
        this.qualification = qualification;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    @Override
    public String toString() {
        return "Staff{id=" + getUserId() + ", name='" + getName()
                + "', staffId=" + staffId + ", designation='" + designation + "'}";
    }
}