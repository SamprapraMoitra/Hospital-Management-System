package org.hosp1.patients.model;

import org.hosp1.common.model.User;
import org.hosp1.common.repository.UserProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** HashMap storage for patients. Implements UserProvider so patients can log in. */
public class PatientRepository implements UserProvider {

    private final Map<String, Patient> store = new HashMap<>();

    public void save(Patient patient) {
        store.put(patient.getUserId(), patient);      // same id = update
    }

    public Optional<Patient> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Patient> findAll() {
        return new ArrayList<>(store.values());
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
                .filter(p -> p.getEmail().equalsIgnoreCase(email.trim()))
                .map(p -> (User) p)
                .findFirst();
    }
}