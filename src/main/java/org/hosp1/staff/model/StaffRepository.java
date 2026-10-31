package org.hosp1.staff.model;

import org.hosp1.common.model.User;
import org.hosp1.common.repository.UserProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** HashMap storage for staff (Staff, Doctor, ...). Implements UserProvider so staff can log in. */
public class StaffRepository implements UserProvider {

    private final Map<String, Staff> store = new HashMap<>();

    public void save(Staff staff) {
        store.put(staff.getUserId(), staff);          // same id = update
    }

    public Optional<Staff> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Staff> findAll() {
        return new ArrayList<>(store.values());
    }

    /** Only the doctors, handy for appointment and prescription screens later. */
    public List<Doctor> findAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        for (Staff s : store.values()) {
            if (s instanceof Doctor d) {
                doctors.add(d);
            }
        }
        return doctors;
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public boolean deleteById(String id) {
        return store.remove(id) != null;
    }

    public int count() {
        return store.size();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return store.values().stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email.trim()))
                .map(s -> (User) s)
                .findFirst();
    }
}