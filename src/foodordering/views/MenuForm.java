package foodordering.views;

import foodordering.dao.CategoryDAO;
import foodordering.dao.MenuItemDAO;
import foodordering.models.Category;
import foodordering.models.MenuItem;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuForm extends JFrame {

    private JPanel mainPanel;
    private Map<MenuItem, Integer> cart = new HashMap<>();

    public MenuForm() {
        setTitle("Customer Menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.setUndecorated(false);
        this.setIconImage(new ImageIcon("images/icon1.png").getImage());
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(this.mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        JButton cartButton = createStyledButton("🛒 View Cart");
        cartButton.addActionListener(e -> showCart());

        JButton backButton = createStyledButton("⬅ Back");
        backButton.addActionListener(e -> {
            dispose();
            foodordering.MainLauncher.main(null);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(backButton);
        bottomPanel.add(cartButton);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadMenu();
    }

    private void loadMenu() {
        CategoryDAO categoryDAO = new CategoryDAO();
        MenuItemDAO menuItemDAO = new MenuItemDAO();

        List<Category> categories = categoryDAO.getAllCategories();
        List<MenuItem> items = menuItemDAO.getAllMenuItems();


        if (categories.isEmpty() || items.isEmpty()) {
            JLabel noData = new JLabel("No menu items or categories found in the database.", SwingConstants.CENTER);
            noData.setFont(new Font("Segoe UI", Font.BOLD, 18));
            mainPanel.add(noData);
            return;
        }

        for (Category category : categories) {
            JLabel categoryLabel = new JLabel(" " + category.getName());
            categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            categoryLabel.setForeground(new Color(60, 60, 60));
            categoryLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            mainPanel.add(categoryLabel);

            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
            itemPanel.setBackground(new Color(111, 78, 55));

            for (MenuItem item : items) {
                if (item.getCategoryId() == category.getId()) {
                    JPanel itemCard = createItemCard(item);
                    itemPanel.add(itemCard);
                }
            }

            mainPanel.add(itemPanel);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createItemCard(MenuItem item) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200, 260));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(111, 78, 55)));
        panel.setBackground(Color.WHITE);

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
                ImageIcon icon = new ImageIcon(item.getImagePath());
                Image scaledImage = icon.getImage().getScaledInstance(180, 100, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                imageLabel.setText("No Image");
                imageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            }
        } catch (Exception e) {
            imageLabel.setText("No Image");
            imageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        }

        JLabel nameLabel = new JLabel(item.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel priceLabel = new JLabel("Rs. " + item.getPrice(), SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton addButton = createStyledButton("Add to Cart");
        addButton.setPreferredSize(new Dimension(140, 30));
        addButton.addActionListener(e -> {
            cart.put(item, cart.getOrDefault(item, 0) + 1);
            JOptionPane.showMessageDialog(this, item.getName() + " added to cart.");
        });

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(nameLabel);
        textPanel.add(priceLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(addButton);

        panel.add(imageLabel, BorderLayout.NORTH);
        panel.add(textPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showCart() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty.");
        } else {
            new CartForm(cart).setVisible(true);
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(111, 78, 55)); //
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuForm().setVisible(true));
    }
}
