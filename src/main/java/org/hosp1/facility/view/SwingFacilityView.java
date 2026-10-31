package org.hosp1.facility.view;

import org.hosp1.facility.model.Room;
import org.hosp1.facility.model.RoomAllocation;
import org.hosp1.facility.model.RoomType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Swing window for room management. Displays and collects input only. */
public class SwingFacilityView implements FacilityView {

    private final JFrame frame = new JFrame("Hospital Management - Facilities");

    // rooms tab
    private final DefaultTableModel roomModel =
            readOnlyModel("Room", "Type", "Beds", "Occupied", "Free", "Rate / day");
    private final JTable roomTable = new JTable(roomModel);
    private final List<String> roomIds = new ArrayList<>();
    private final JTextField numberField = new JTextField(10);
    private final JComboBox<RoomType> typeBox = new JComboBox<>(RoomType.values());
    private final JTextField capacityField = new JTextField(10);
    private final JTextField rateField = new JTextField(10);
    private final JButton addRoomButton = new JButton("Add Room");
    private final JButton removeRoomButton = new JButton("Remove Selected Room");

    // admissions tab
    private final DefaultTableModel stayModel =
            readOnlyModel("Stay ID", "Room", "Patient ID", "Admitted", "Discharged");
    private final JTable stayTable = new JTable(stayModel);
    private final List<String> stayIds = new ArrayList<>();
    private final JTextField patientIdField = new JTextField(10);
    private final JComboBox<String> roomChoice = new JComboBox<>();
    private final List<String> choiceRoomIds = new ArrayList<>();   // combo index -> room id
    private final JButton admitButton = new JButton("Admit");
    private final JButton dischargeButton = new JButton("Discharge Selected");

    private final JButton refreshButton = new JButton("Refresh");

    public SwingFacilityView() {
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stayTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Rooms", buildRoomsTab());
        tabs.addTab("Admissions", buildAdmissionsTab());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.add(refreshButton);

        frame.setLayout(new BorderLayout());
        frame.add(top, BorderLayout.NORTH);
        frame.add(tabs, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   // closing does not exit the app
        frame.setSize(800, 480);
        frame.setLocationRelativeTo(null);
    }

    private JPanel buildRoomsTab() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("New room"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;
        addRow(form, c, 0, "Room no.:", numberField);
        addRow(form, c, 1, "Type:", typeBox);
        addRow(form, c, 2, "Beds:", capacityField);
        addRow(form, c, 3, "Rate / day:", rateField);
        c.gridx = 1; c.gridy = 4;
        form.add(addRoomButton, c);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(removeRoomButton);

        JPanel tab = new JPanel(new BorderLayout(8, 8));
        tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        tab.add(new JScrollPane(roomTable), BorderLayout.CENTER);
        tab.add(form, BorderLayout.EAST);
        tab.add(bottom, BorderLayout.SOUTH);
        return tab;
    }

    private JPanel buildAdmissionsTab() {
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Admit a patient"));
        form.add(new JLabel("Patient ID:"));
        form.add(patientIdField);
        form.add(new JLabel("Room:"));
        form.add(roomChoice);
        form.add(admitButton);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(dischargeButton);

        JPanel tab = new JPanel(new BorderLayout(8, 8));
        tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        tab.add(form, BorderLayout.NORTH);
        tab.add(new JScrollPane(stayTable), BorderLayout.CENTER);
        tab.add(bottom, BorderLayout.SOUTH);
        return tab;
    }

    private static void addRow(JPanel p, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridx = 0; c.gridy = row;
        p.add(new JLabel(label), c);
        c.gridx = 1;
        p.add(field, c);
    }

    private static DefaultTableModel readOnlyModel(String... cols) {
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    // ---- display ----
    @Override public void show() { frame.setVisible(true); frame.toFront(); }
    @Override public void close() { frame.dispose(); }

    @Override
    public void showRooms(List<Room> rooms, Map<String, Integer> occupied) {
        roomModel.setRowCount(0);
        roomIds.clear();
        roomChoice.removeAllItems();
        choiceRoomIds.clear();
        for (Room r : rooms) {
            int used = occupied.getOrDefault(r.getId(), 0);
            int free = r.getCapacity() - used;
            roomModel.addRow(new Object[]{r.getNumber(), r.getType(), r.getCapacity(), used, free,
                    String.format("%.2f", r.getDailyRate())});
            roomIds.add(r.getId());
            if (free > 0) {                       // only rooms with a free bed can be chosen for admission
                roomChoice.addItem(r.getNumber() + " (" + r.getType() + ", " + free + " free)");
                choiceRoomIds.add(r.getId());
            }
        }
    }

    @Override
    public void showAllocations(List<RoomAllocation> allocations, Map<String, String> roomNumberById) {
        stayModel.setRowCount(0);
        stayIds.clear();
        for (RoomAllocation a : allocations) {
            stayModel.addRow(new Object[]{a.getId(), roomNumberById.getOrDefault(a.getRoomId(), "?"),
                    a.getPatientId(), a.getAdmittedOn(),
                    a.getDischargedOn() == null ? "-" : a.getDischargedOn()});
            stayIds.add(a.getId());
        }
    }

    @Override public void showMessage(String m) {
        JOptionPane.showMessageDialog(frame, m, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    @Override public void showError(String m) {
        JOptionPane.showMessageDialog(frame, m, "Error", JOptionPane.ERROR_MESSAGE);
    }
    @Override public boolean confirm(String q) {
        return JOptionPane.showConfirmDialog(frame, q, "Confirm", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION;
    }

    // ---- input ----
    @Override public String getNewRoomNumber() { return numberField.getText().trim(); }
    @Override public RoomType getNewRoomType() { return (RoomType) typeBox.getSelectedItem(); }
    @Override public String getNewRoomCapacity() { return capacityField.getText().trim(); }
    @Override public String getNewRoomDailyRate() { return rateField.getText().trim(); }
    @Override public String getAdmitPatientId() { return patientIdField.getText().trim(); }

    @Override
    public String getSelectedRoomId() {
        int row = roomTable.getSelectedRow();
        return (row < 0 || row >= roomIds.size()) ? null : roomIds.get(row);
    }

    @Override
    public String getAdmissionRoomId() {
        int i = roomChoice.getSelectedIndex();
        return (i < 0 || i >= choiceRoomIds.size()) ? null : choiceRoomIds.get(i);
    }

    @Override
    public String getSelectedAllocationId() {
        int row = stayTable.getSelectedRow();
        return (row < 0 || row >= stayIds.size()) ? null : stayIds.get(row);
    }

    @Override public void clearRoomForm() {
        numberField.setText("");
        capacityField.setText("");
        rateField.setText("");
    }
    @Override public void clearAdmitForm() { patientIdField.setText(""); }

    // ---- actions ----
    @Override public void setAddRoomAction(Runnable a) { addRoomButton.addActionListener(e -> a.run()); }
    @Override public void setRemoveRoomAction(Runnable a) { removeRoomButton.addActionListener(e -> a.run()); }
    @Override public void setAdmitAction(Runnable a) { admitButton.addActionListener(e -> a.run()); }
    @Override public void setDischargeAction(Runnable a) { dischargeButton.addActionListener(e -> a.run()); }
    @Override public void setRefreshAction(Runnable a) { refreshButton.addActionListener(e -> a.run()); }
}