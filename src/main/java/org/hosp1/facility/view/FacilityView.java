package org.hosp1.facility.view;

import org.hosp1.facility.model.Room;
import org.hosp1.facility.model.RoomAllocation;
import org.hosp1.facility.model.RoomType;

import java.util.List;
import java.util.Map;

/** Facility screen for admin: manage rooms, admit patients to rooms, discharge them. */
public interface FacilityView {

    // ---- display ----
    void show();
    void close();
    void showRooms(List<Room> rooms, Map<String, Integer> occupiedByRoomId);
    void showAllocations(List<RoomAllocation> allocations, Map<String, String> roomNumberById);
    void showMessage(String message);
    void showError(String message);
    boolean confirm(String question);

    // ---- read the current input ----
    String getNewRoomNumber();
    RoomType getNewRoomType();
    String getNewRoomCapacity();
    String getNewRoomDailyRate();
    String getSelectedRoomId();          // room selected in the rooms table, or null
    String getAdmitPatientId();
    String getAdmissionRoomId();         // room chosen in the admission drop-down, or null
    String getSelectedAllocationId();    // allocation selected in the stays table, or null
    void clearRoomForm();
    void clearAdmitForm();

    // ---- controller registers what to run on each click ----
    void setAddRoomAction(Runnable action);
    void setRemoveRoomAction(Runnable action);
    void setAdmitAction(Runnable action);
    void setDischargeAction(Runnable action);
    void setRefreshAction(Runnable action);
}