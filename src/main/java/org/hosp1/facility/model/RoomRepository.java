package org.hosp1.facility.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** HashMap storage for rooms. */
public class RoomRepository {

    private final Map<String, Room> store = new HashMap<>();

    public void save(Room room) { store.put(room.getId(), room); }

    public Optional<Room> findById(String id) { return Optional.ofNullable(store.get(id)); }

    public Optional<Room> findByNumber(String number) {
        return store.values().stream()
                .filter(r -> r.getNumber().equalsIgnoreCase(number))
                .findFirst();
    }

    public List<Room> findAll() { return new ArrayList<>(store.values()); }

    public boolean deleteById(String id) { return store.remove(id) != null; }

    public int count() { return store.size(); }
}