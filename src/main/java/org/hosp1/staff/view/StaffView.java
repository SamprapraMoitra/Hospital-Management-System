package org.hosp1.staff.view;

import java.util.List;

import org.hosp1.staff.model.Staff;

public interface StaffView {

    void showStaffProfile(Staff staff);

    void showStaffList(List<Staff> staffList);

    void showMessage(String message);

    void showError(String message);


    int promptStaffId();

    Staff promptNewStaffDetails();

    String promptNewValue(String fieldName);
}