package org.hosp1.admin.model;

import org.hosp1.common.model.User;
import org.hosp1.common.repository.UserProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** HashMap storage for admins. Implements UserProvider so admins can log in. */
public class AdminRepository implements UserProvider {

    private final Map<String, Admin> store = new HashMap<>();

    public void save(Admin admin) { store.put(admin.getUserId(), admin); }

    public Optional<Admin> findById(String id) { return Optional.ofNullable(store.get(id)); }

    public List<Admin> findAll() { return new ArrayList<>(store.values()); }

    public boolean deleteById(String id) { return store.remove(id) != null; }

    @Override
    public Optional<User> findByEmail(String email) {
        return store.values().stream()
                .filter(a -> a.getEmail().equalsIgnoreCase(email))
                .map(a -> (User) a)
                .findFirst();
    }
}