package foodordering.views;

import foodordering.db.DBConnection;
import foodordering.models.MenuItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Map;

public class CartForm extends JFrame {

    private JTable cartTable;
    private JLabel totalLabel;
    private JButton placeOrderButton;

    private Map<MenuItem, Integer> cart;

    public CartForm(Map<MenuItem, Integer> cart) {
        this.cart = cart;

        setTitle("Your Cart");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadCartItems();
    }

    private void initComponents() {
        cartTable = new JTable(new DefaultTableModel(new Object[]{"Item", "Price", "Qty", "Total"}, 0));
        JScrollPane scrollPane = new JScrollPane(cartTable);

        totalLabel = new JLabel("Total: Rs. 0.0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));

        placeOrderButton = new JButton("Place Order");

        placeOrderButton.setForeground(Color.WHITE); // Set text color to white

        placeOrderButton.setBackground(new Color(111, 78, 55));
        placeOrderButton.addActionListener(e -> placeOrder());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(placeOrderButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadCartItems() {
        DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
        double total = 0;

        for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
            MenuItem item = entry.getKey();
            int qty = entry.getValue();
            double price = item.getPrice() * qty;
            total += price;
            model.addRow(new Object[]{item.getName(), item.getPrice(), qty, price});
        }

        totalLabel.setText("Total: Rs. " + total);
    }

    private void placeOrder() {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            double totalAmount = 0.0;
            for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
                MenuItem item = entry.getKey();
                int quantity = entry.getValue();
                totalAmount += item.getPrice() * quantity;
            }

            String orderSql = "INSERT INTO orders (total_amount, status) VALUES (?, 'Pending')";
            PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setDouble(1, totalAmount);
            orderStmt.executeUpdate();

            ResultSet rs = orderStmt.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);

                String itemSql = "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)";
                PreparedStatement itemStmt = conn.prepareStatement(itemSql);

                for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, entry.getKey().getId());
                    itemStmt.setInt(3, entry.getValue());
                    itemStmt.addBatch();
                }

                itemStmt.executeBatch();
                conn.commit();

                JOptionPane.showMessageDialog(this, "Order placed successfully!\nTotal: PKR " + totalAmount);

                new ReceiptForm(cart).setVisible(true);

             cart.clear();
                this.dispose();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error placing order: " + e.getMessage());
        }
    }}
