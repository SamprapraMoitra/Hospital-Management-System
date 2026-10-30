package org.hosp1.patients.model;

import java.time.LocalDate;

import org.hosp1.common.model.User;

public class Patient extends User {

    private int patientId;
    private LocalDate dob;
    private String bloodGroup;


    public Patient() {
        super();
    }

    public Patient(int userId, String name, String email, String phone, String gender,String address,String passwordHash,
                   int patientId, LocalDate dob,  String bloodGroup) {
        super(userId, name, email, phone, gender,address,passwordHash);
        this.patientId = patientId;
        this.dob = dob;
        this.bloodGroup = bloodGroup;
    }

    public int getPatientId() {

        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public LocalDate getDob() {
        return dob;
    }
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }


    @Override
    public String toString() {
        return "Patient{patientId=" + patientId + ", name='" + getName() + "', bloodGroup='" + bloodGroup + "'}";
    }
}