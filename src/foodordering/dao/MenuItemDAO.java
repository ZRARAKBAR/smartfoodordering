package foodordering.dao;

import foodordering.db.DBConnection;
import foodordering.models.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {

    // Get All Menu Items
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT * FROM menu_items order by price desc";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MenuItem item = new MenuItem();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setCategoryId(rs.getInt("category_id"));
                item.setPrice(rs.getDouble("price"));
                item.setImagePath(rs.getString("image_path"));
                menuItems.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menuItems;
    }

    // Add New Menu Item
    public boolean addMenuItem(MenuItem item) {
        String query = "INSERT INTO menu_items (name, category_id, price, image_path) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getCategoryId());
            stmt.setDouble(3, item.getPrice());
            stmt.setString(4, item.getImagePath());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Update Menu Item
    public boolean updateMenuItem(MenuItem item) {
        String query = "UPDATE menu_items SET name=?, category_id=?, price=?, image_path=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getCategoryId());
            stmt.setDouble(3, item.getPrice());
            stmt.setString(4, item.getImagePath());
            stmt.setInt(5, item.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete Menu Item by ID
    public boolean deleteMenuItem(int itemId) {
        String query = "DELETE FROM menu_items WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, itemId);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
