package org.hosp1.facility.controller;

import org.hosp1.facility.model.Room;
import org.hosp1.facility.model.RoomAllocation;
import org.hosp1.facility.model.RoomAllocationRepository;
import org.hosp1.facility.model.RoomRepository;
import org.hosp1.facility.model.RoomType;
import org.hosp1.facility.view.FacilityView;
import org.hosp1.patients.model.PatientRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Controls the facility screen: rooms, admissions and discharges. */
public class FacilityController {

    /** Called after a discharge so another module (billing) can charge for the stay. */
    public interface DischargeHandler {
        void onDischarged(Room room, RoomAllocation allocation, long days, BigDecimal charge);
    }

    private final FacilityView view;
    private final RoomRepository roomRepo;
    private final RoomAllocationRepository allocationRepo;
    private final PatientRepository patientRepo;
    private final DischargeHandler dischargeHandler;   // may be null

    public FacilityController(FacilityView view, RoomRepository roomRepo,
                              RoomAllocationRepository allocationRepo, PatientRepository patientRepo,
                              DischargeHandler dischargeHandler) {
        this.view = view;
        this.roomRepo = roomRepo;
        this.allocationRepo = allocationRepo;
        this.patientRepo = patientRepo;
        this.dischargeHandler = dischargeHandler;

        view.setAddRoomAction(this::addRoom);
        view.setRemoveRoomAction(this::removeRoom);
        view.setAdmitAction(this::admitPatient);
        view.setDischargeAction(this::discharge);
        view.setRefreshAction(this::refresh);
    }

    public void start() {
        refresh();
        view.show();
    }

    public void refresh() {
        List<Room> rooms = roomRepo.findAll();
        rooms.sort(Comparator.comparing(Room::getNumber));
        Map<String, Integer> occupied = new HashMap<>();
        Map<String, String> numbers = new HashMap<>();
        for (Room r : rooms) {
            occupied.put(r.getId(), allocationRepo.countActiveByRoomId(r.getId()));
            numbers.put(r.getId(), r.getNumber());
        }
        view.showRooms(rooms, occupied);

        List<RoomAllocation> stays = allocationRepo.findAll();
        stays.sort(Comparator.comparing(RoomAllocation::isActive).reversed()      // active stays first
                .thenComparing(RoomAllocation::getAdmittedOn, Comparator.reverseOrder()));
        view.showAllocations(stays, numbers);
    }

    public void addRoom() {
        String number = view.getNewRoomNumber();
        RoomType type = view.getNewRoomType();
        if (number.isEmpty()) {
            view.showError("Enter a room number.");
            return;
        }
        if (roomRepo.findByNumber(number).isPresent()) {
            view.showError("Room " + number + " already exists.");
            return;
        }
        int capacity;
        try {
            capacity = Integer.parseInt(view.getNewRoomCapacity());
        } catch (NumberFormatException e) {
            view.showError("Beds must be a whole number.");
            return;
        }
        BigDecimal rate = parseMoney(view.getNewRoomDailyRate());
        if (capacity <= 0 || rate == null || rate.signum() <= 0) {
            view.showError("Beds and daily rate must be greater than zero.");
            return;
        }
        String id = "R-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        roomRepo.save(new Room(id, number, type, capacity, rate));
        view.clearRoomForm();
        refresh();
        view.showMessage("Room " + number + " added.");
    }

    public void removeRoom() {
        String id = view.getSelectedRoomId();
        if (id == null) {
            view.showError("Select a room first.");
            return;
        }
        if (allocationRepo.countActiveByRoomId(id) > 0) {
            view.showError("This room has patients in it. Discharge them first.");
            return;
        }
        if (!view.confirm("Remove this room?")) {
            return;
        }
        roomRepo.deleteById(id);
        refresh();
    }

    public void admitPatient() {
        String patientId = view.getAdmitPatientId();
        String roomId = view.getAdmissionRoomId();
        if (patientRepo.findById(patientId).isEmpty()) {
            view.showError("No patient found with ID '" + patientId + "'.");
            return;
        }
        if (roomId == null) {
            view.showError("No room with a free bed is available.");
            return;
        }
        if (allocationRepo.findActiveByPatientId(patientId).isPresent()) {
            view.showError("This patient is already admitted. Discharge them first.");
            return;
        }
        Room room = roomRepo.findById(roomId).orElse(null);
        if (room == null || allocationRepo.countActiveByRoomId(roomId) >= room.getCapacity()) {
            view.showError("That room is full.");
            refresh();
            return;
        }
        String id = "RA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        allocationRepo.save(new RoomAllocation(id, roomId, patientId, LocalDate.now()));
        view.clearAdmitForm();
        refresh();
        view.showMessage("Patient admitted to room " + room.getNumber() + ".");
    }

    public void discharge() {
        String id = view.getSelectedAllocationId();
        if (id == null) {
            view.showError("Select a stay first.");
            return;
        }
        RoomAllocation stay = allocationRepo.findById(id).orElse(null);
        if (stay == null) {
            view.showError("Stay not found.");
            return;
        }
        if (!stay.isActive()) {
            view.showError("This patient is already discharged.");
            return;
        }
        Room room = roomRepo.findById(stay.getRoomId()).orElse(null);
        if (room == null) {
            view.showError("The room for this stay no longer exists.");
            return;
        }
        LocalDate today = LocalDate.now();
        long days = Math.max(1, ChronoUnit.DAYS.between(stay.getAdmittedOn(), today));   // part of a day counts as one
        BigDecimal charge = room.getDailyRate().multiply(BigDecimal.valueOf(days));

        stay.setDischargedOn(today);
        allocationRepo.save(stay);
        if (dischargeHandler != null) {
            dischargeHandler.onDischarged(room, stay, days, charge);
        }
        refresh();
        view.showMessage("Discharged after " + days + " day(s). Room charge: " + charge);
    }

    private static BigDecimal parseMoney(String text) {
        try {
            return new BigDecimal(text.trim()).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }
}