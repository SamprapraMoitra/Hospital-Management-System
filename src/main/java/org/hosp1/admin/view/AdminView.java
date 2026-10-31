package org.hosp1.admin.view;

import org.hosp1.patients.model.Patient;
import org.hosp1.staff.model.Staff;

import java.time.LocalDate;
import java.util.List;

public interface AdminView {

    // ---- display ----
    void show();
    void close();
    void setWelcome(String adminName);
    void showStaff(List<Staff> staff);
    void showPatients(List<Patient> patients);
    void showMessage(String message);
    void showError(String message);
    boolean confirm(String question);

    // ---- read the current input ----
    String getNewName();
    String getNewEmail();
    String getNewPhone();
    String getNewGender();
    String getNewAddress();
    int getNewStaffId();
    LocalDate getNewJoiningDate();
    String getNewDesignation();
    double getNewSalary();
    String getNewQualification();
    char[] getNewPassword();
    String getNewSpecialization();
    String getSelectedStaffId();     // null if nothing is selected
    void clearForm();

    // ---- controller registers what to run on each click ----
    void setAddStaffAction(Runnable action);
    void setRemoveStaffAction(Runnable action);
    void setRefreshAction(Runnable action);
    void setLogoutAction(Runnable action);
    void setBillingAction(Runnable action);
    void setFacilityAction(Runnable action);


}