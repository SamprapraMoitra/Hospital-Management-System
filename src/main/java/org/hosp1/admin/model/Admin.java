package org.hosp1.admin.model;

import org.hosp1.common.model.User;

public class Admin extends User {

    private String employeeCode;

    public Admin(String userId, String name, String email,String phone,String gender,String address,String role, String passwordHash, String employeeCode) {
        super(userId, name, email,phone,gender,address,role, passwordHash);
        this.employeeCode = employeeCode;
    }

    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }

    @Override
    public String toString() {
        return "Admin{id=" + getUserId() + ", name=" + getName() + ", code=" + employeeCode + "}";
    }
}