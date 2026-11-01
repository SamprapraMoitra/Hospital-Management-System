package org.hosp1.common.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public final class UiStyle {

    public static final Color BACKGROUND = new Color(245, 247, 250);
    public static final Color TEXT = new Color(36, 45, 58);
    public static final Color MUTED = new Color(105, 117, 132);

    private static final Color PRIMARY = new Color(40, 100, 180);

    private UiStyle() {
    }

    public static JTextField textField(int columns) {
        return new JTextField(columns);
    }

    public static JTable table(String... columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        styleTable(table);
        return table;
    }

    public static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY);
        button.setFocusPainted(false);
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        return button;
    }

    public static JPanel card() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 236)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }

    public static JScrollPane scroll(Component component) {
        return new JScrollPane(component);
    }

    public static JPanel labeledField(String label, Component component) {
        JPanel field = new JPanel(new BorderLayout(0, 5));
        field.setOpaque(false);
        field.add(new JLabel(label), BorderLayout.NORTH);
        field.add(component, BorderLayout.CENTER);
        return field;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(24);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(225, 230, 236));
        table.getTableHeader().setReorderingAllowed(false);
    }
}
