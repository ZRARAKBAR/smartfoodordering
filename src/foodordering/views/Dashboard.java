package foodordering.views;

import foodordering.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Dashboard extends JFrame {

    private JLabel totalOrdersLabel, totalRevenueLabel, topItemLabel;
    private JButton refreshButton;

    public Dashboard() {
        setTitle("Admin Dashboard");
        setSize(500, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadAnalytics();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(111, 78, 55)); // Brown background
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Dashboard Analytics", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBackground(new Color(184, 157, 121)); // Light brown background

        totalOrdersLabel = createLabel("Total Orders: ");
        totalRevenueLabel = createLabel("Total Revenue: ");
        topItemLabel = createLabel("Top Selling Item: ");

        refreshButton = new JButton(" Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        refreshButton.setBackground(new Color(111, 78, 55));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> resetAnalytics());

        centerPanel.add(totalOrdersLabel);
        centerPanel.add(totalRevenueLabel);
        centerPanel.add(topItemLabel);
        centerPanel.add(refreshButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(Color.BLACK);
        return label;
    }

    private void loadAnalytics() {
        try (Connection conn = DBConnection.getConnection()) {


            PreparedStatement stmt1 = conn.prepareStatement("SELECT COUNT(*) FROM orders");
            ResultSet rs1 = stmt1.executeQuery();
            if (rs1.next()) {
                totalOrdersLabel.setText("Total Orders: " + rs1.getInt(1));
            }

            // Total Revenue
            PreparedStatement stmt2 = conn.prepareStatement(
                    "SELECT SUM(mi.price * oi.quantity) FROM order_items oi " +
                            "JOIN menu_items mi ON oi.menu_item_id = mi.id");
            ResultSet rs2 = stmt2.executeQuery();
            if (rs2.next()) {
                double revenue = rs2.getDouble(1);
                totalRevenueLabel.setText("Total Sales : Rs. " + String.format("%.2f", revenue));
            }

            // Top Selling Item
            PreparedStatement stmt3 = conn.prepareStatement(
                    "SELECT mi.name, SUM(oi.quantity) AS total_qty FROM order_items oi " +
                            "JOIN menu_items mi ON oi.menu_item_id = mi.id " +
                            "GROUP BY mi.name ORDER BY total_qty DESC LIMIT 1");
            ResultSet rs3 = stmt3.executeQuery();
            if (rs3.next()) {
                topItemLabel.setText("Top Selling Item: " + rs3.getString("name") + " (" + rs3.getInt("total_qty") + ")");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading analytics: " + e.getMessage());
        }
    }

    private void resetAnalytics() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "This will permanently clear all orders and analytics. Continue?",
                "Confirm Reset", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {

                // Delete from orders and order_items
                PreparedStatement deleteOrderItems = conn.prepareStatement("DELETE FROM order_items");
                PreparedStatement deleteOrders = conn.prepareStatement("DELETE FROM orders");
                deleteOrderItems.executeUpdate();
                deleteOrders.executeUpdate();

                // Reset UI labels
                totalOrdersLabel.setText("Total Orders: 0");
                totalRevenueLabel.setText("Total Revenue: Rs. 0.00");
                topItemLabel.setText("Top Selling Item: N/A");

                JOptionPane.showMessageDialog(this, "Analytics data has been reset.");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Reset failed: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}
