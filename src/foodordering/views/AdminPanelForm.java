package foodordering.views;
import  foodordering.MainLauncher;
import foodordering.db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class AdminPanelForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> modeComboBox;
    private JButton addButton, updateButton, deleteButton, analyticsButton, backButton;

    public AdminPanelForm() {
        setTitle("Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setLayout(new BorderLayout());

        initComponents();
        loadTableData("categories");

    }

    private void initComponents() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(111, 78, 55));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Admin Panel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground( Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel();
        table = new JTable(model);
        table.setRowHeight(24);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(Color.WHITE);

        modeComboBox = new JComboBox<>(new String[]{"categories", "menu_items"});
        modeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        styleButtonCombo(modeComboBox);

        addButton = createButton("Add");
        updateButton = createButton("Update");
        deleteButton = createButton("Delete");
        analyticsButton = createButton("Analytics");
        backButton = createButton("← Back");

        bottomPanel.add(modeComboBox);
        bottomPanel.add(addButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(analyticsButton);
        bottomPanel.add(backButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);


        modeComboBox.addActionListener(e -> loadTableData(modeComboBox.getSelectedItem().toString()));
        addButton.addActionListener(e -> showAddDialog());
        updateButton.addActionListener(e -> showUpdateDialog());
        deleteButton.addActionListener(e -> deleteSelected());
        analyticsButton.addActionListener(e -> new Dashboard().setVisible(true));
        backButton.addActionListener(e -> {
            dispose();
            foodordering.MainLauncher.main(null);
        });
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(111, 78, 55)); // Brown
        button.setForeground(Color.WHITE);           // White text
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 40));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    private void styleButtonCombo(JComponent comp) {
        comp.setPreferredSize(new Dimension(150, 35));
        comp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        comp.setBackground(new Color(230, 230, 230));
        comp.setForeground(new Color(60, 60, 60));
        comp.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
    }

    private void loadTableData(String mode) {
        model.setRowCount(0);
        model.setColumnCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + mode);
            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                model.addColumn(meta.getColumnName(i));
            }
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columns; i++) {
                    row.add(rs.getObject(i));
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Load error: " + e.getMessage());
        }
    }

    private void showAddDialog() {
        String mode = modeComboBox.getSelectedItem().toString();
        if (mode.equals("categories")) {
            String name = JOptionPane.showInputDialog(this, "Enter category name:");
            if (name != null && !name.trim().isEmpty()) {
                try (Connection conn = DBConnection.getConnection()) {
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO categories(name) VALUES (?)");
                    ps.setString(1, name);
                    ps.executeUpdate();
                    loadTableData("categories");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                }
            }
        } else {
            JTextField nameField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField catIdField = new JTextField();
            JTextField imgPathField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Price:"));
            panel.add(priceField);
            panel.add(new JLabel("Category ID:"));
            panel.add(catIdField);
            panel.add(new JLabel("Image Path:"));
            panel.add(imgPathField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Add Menu Item", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try (Connection conn = DBConnection.getConnection()) {
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO menu_items(name, price, category_id, image_path) VALUES (?, ?, ?, ?)");
                    ps.setString(1, nameField.getText());
                    ps.setDouble(2, Double.parseDouble(priceField.getText()));
                    ps.setInt(3, Integer.parseInt(catIdField.getText()));
                    ps.setString(4, imgPathField.getText());
                    ps.executeUpdate();
                    loadTableData("menu_items");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Insert error: " + e.getMessage());
                }
            }
        }
    }

    private void showUpdateDialog() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String mode = modeComboBox.getSelectedItem().toString();
        if (mode.equals("categories")) {
            int id = (int) model.getValueAt(row, 0);
            String currentName = model.getValueAt(row, 1).toString();
            String newName = JOptionPane.showInputDialog(this, "Update name:", currentName);
            if (newName != null) {
                try (Connection conn = DBConnection.getConnection()) {
                    PreparedStatement ps = conn.prepareStatement("UPDATE categories SET name=? WHERE id=?");
                    ps.setString(1, newName);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    loadTableData("categories");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Update error: " + e.getMessage());
                }
            }
        } else {
            int id = (int) model.getValueAt(row, 0);
            JTextField nameField = new JTextField(model.getValueAt(row, 1).toString());
            JTextField catIdField = new JTextField(model.getValueAt(row, 2).toString());
            JTextField priceField = new JTextField(model.getValueAt(row, 3).toString());
            JTextField imgField = new JTextField(model.getValueAt(row, 4).toString());

            JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Price:"));
            panel.add(priceField);
            panel.add(new JLabel("Category ID:"));
            panel.add(catIdField);
            panel.add(new JLabel("Image Path:"));
            panel.add(imgField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update Item", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try (Connection conn = DBConnection.getConnection()) {
                    PreparedStatement ps = conn.prepareStatement("UPDATE menu_items SET name=?, category_id=?, price=?, image_path=? WHERE id=?");
                    ps.setString(1, nameField.getText());
                    ps.setInt(2, Integer.parseInt(catIdField.getText()));
                    ps.setDouble(3, Double.parseDouble(priceField.getText()));
                    ps.setString(4, imgField.getText());
                    ps.setInt(5, id);
                    ps.executeUpdate();
                    loadTableData("menu_items");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Update failed: " + e.getMessage());
                }
            }
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String mode = modeComboBox.getSelectedItem().toString();
        int id = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected row?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM " + mode + " WHERE id=?");
                ps.setInt(1, id);
                ps.executeUpdate();
                loadTableData(mode);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Delete error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPanelForm().setVisible(true));
    }
}