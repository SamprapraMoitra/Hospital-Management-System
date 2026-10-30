package org.hosp1.staff.model;

import java.time.LocalDate;

public class Doctor extends Staff {
    private String specialization;
    private String licenseNo;

    public Doctor() {
        super();
    }
    public Doctor(int userId, String name, String email, String phone, String gender,String address,String passwordHash,
                 int staffId, LocalDate joiningDate, String designation,String specialization,String licenseNo) {
        super(userId, name, email, phone, gender,passwordHash,staffId,joiningDate,designation);
        this.specialization = specialization;
        this.licenseNo = licenseNo;

    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }
    @Override
    public String toString() {
        return "Doctor{staffId=" + getstaffId() + ", name='" + getName() + "', specialization='" + specialization + "'}";
    }
}