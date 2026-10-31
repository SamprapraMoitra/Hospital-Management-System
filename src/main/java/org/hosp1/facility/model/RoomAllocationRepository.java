package org.hosp1.facility.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** HashMap storage for room allocations (admissions and discharges). */
public class RoomAllocationRepository {

    private final Map<String, RoomAllocation> store = new HashMap<>();

    public void save(RoomAllocation allocation) { store.put(allocation.getId(), allocation); }

    public Optional<RoomAllocation> findById(String id) { return Optional.ofNullable(store.get(id)); }

    public List<RoomAllocation> findAll() { return new ArrayList<>(store.values()); }

    public List<RoomAllocation> findByPatientId(String patientId) {
        List<RoomAllocation> result = new ArrayList<>();
        for (RoomAllocation a : store.values()) {
            if (a.getPatientId().equals(patientId)) {
                result.add(a);
            }
        }
        return result;
    }

    public Optional<RoomAllocation> findActiveByPatientId(String patientId) {
        return store.values().stream()
                .filter(a -> a.isActive() && a.getPatientId().equals(patientId))
                .findFirst();
    }

    public int countActiveByRoomId(String roomId) {
        int count = 0;
        for (RoomAllocation a : store.values()) {
            if (a.isActive() && a.getRoomId().equals(roomId)) {
                count++;
            }
        }
        return count;
    }
}
