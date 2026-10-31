package org.hosp1.facility.model;

import java.time.LocalDate;
import java.util.Objects;

/** POJO: one patient staying in one room. dischargedOn is null while the stay is active. */
public class RoomAllocation {

    private String id;
    private String roomId;
    private String patientId;
    private LocalDate admittedOn;
    private LocalDate dischargedOn;

    public RoomAllocation(String id, String roomId, String patientId, LocalDate admittedOn) {
        this.id = id;
        this.roomId = roomId;
        this.patientId = patientId;
        this.admittedOn = admittedOn;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public LocalDate getAdmittedOn() { return admittedOn; }
    public void setAdmittedOn(LocalDate admittedOn) { this.admittedOn = admittedOn; }

    public LocalDate getDischargedOn() { return dischargedOn; }
    public void setDischargedOn(LocalDate dischargedOn) { this.dischargedOn = dischargedOn; }

    /** Derived from dischargedOn. */
    public boolean isActive() { return dischargedOn == null; }

    @Override
    public boolean equals(Object o) { return o instanceof RoomAllocation a && Objects.equals(id, a.id); }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "RoomAllocation{room=" + roomId + ", patient=" + patientId + ", active=" + isActive() + "}";
    }
}