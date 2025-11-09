package foodordering.views;

import foodordering.db.DBConnection;
import foodordering.MainLauncher;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class KitchenPanelForm extends JFrame {

    private JTable ordersTable;
    private JButton refreshButton, completeButton, backButton;

    public KitchenPanelForm() {
        setTitle("Kitchen Panel");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        abc();
        loadPendingOrders();
    }

    private void abc() {
        ordersTable = new JTable(new DefaultTableModel(new Object[]{"Order ID", "Item", "Quantity"}, 0));
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        refreshButton = createStyledButton("Refresh Orders");
        completeButton = createStyledButton("Mark Selected Order Completed");
        backButton = createStyledButton("Back");

        refreshButton.addActionListener(e -> loadPendingOrders());
        completeButton.addActionListener(e -> markOrderCompleted());
        backButton.addActionListener(e -> {
            dispose();
            MainLauncher.showRoleSelection();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(refreshButton);
        bottomPanel.add(completeButton);
        bottomPanel.add(backButton);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(111, 78, 55));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadPendingOrders() {
        DefaultTableModel model = (DefaultTableModel) ordersTable.getModel();
        model.setRowCount(0);

        String sql = "SELECT oi.order_id, mi.name AS item_name, oi.quantity FROM order_items oi " +
                "JOIN orders o ON o.id = oi.order_id " +
                "JOIN menu_items mi ON mi.id = oi.menu_item_id " +
                "WHERE o.status = 'Pending' ORDER BY oi.order_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");

                model.addRow(new Object[]{orderId, itemName, quantity});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading orders:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void markOrderCompleted() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row from the table.");
            return;
        }

        int orderId = (int) ordersTable.getValueAt(selectedRow, 0);

        String sql = "UPDATE orders SET status = 'Completed' WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            int updated = stmt.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Order #" + orderId + " marked as completed.");
                loadPendingOrders();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update order.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating order:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
