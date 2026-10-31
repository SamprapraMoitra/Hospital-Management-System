package org.hosp1.billing.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** HashMap storage for bills (a database can replace it later). */
public class BillRepository {

    private final Map<String, Bill> store = new HashMap<>();

    public void save(Bill bill) { store.put(bill.getId(), bill); }

    public Optional<Bill> findById(String id) { return Optional.ofNullable(store.get(id)); }

    public List<Bill> findAll() { return new ArrayList<>(store.values()); }

    public List<Bill> findByPatientId(String patientId) {
        List<Bill> result = new ArrayList<>();
        for (Bill b : store.values()) {
            if (b.getPatientId().equals(patientId)) {
                result.add(b);
            }
        }
        return result;
    }

    public boolean deleteById(String id) { return store.remove(id) != null; }

    public int count() { return store.size(); }
}