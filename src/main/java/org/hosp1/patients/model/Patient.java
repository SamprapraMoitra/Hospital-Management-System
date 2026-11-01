package org.hosp1.patients.model;

import org.hosp1.common.model.User;

public class Patient extends User {

    public Patient() {
        super();
    }

    public Patient(String userId, String name, String email, String phone,
                   String gender, String address, String role, String passwordHash) {
        super(userId, name, email, phone, gender, address, role, passwordHash);
    }
}
