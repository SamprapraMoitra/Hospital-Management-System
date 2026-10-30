package org.hosp1.common.model;

import java.util.Objects;

public abstract class User {

    private int userId;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String address;
    private String passwordHash;

    protected User() {
    }

    protected User(int userId, String name, String email, String phone,String gender,String address, String passwordHash) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.passwordHash = passwordHash;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender){
        this.gender=gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

       @Override
    public String toString() {
        return getClass().getSimpleName() + "{userId=" + userId + ", name='" + name + "'}";
    }
}