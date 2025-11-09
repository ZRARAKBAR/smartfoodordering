package foodordering.views;

import foodordering.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;

    public LoginForm() {
        setTitle(" Admin Login");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(64, 11, 11)); // Deep red-brown background

        JPanel loginBox = new JPanel(new GridBagLayout());
        loginBox.setBackground(new Color(184, 157, 121)); // Light brown box
        loginBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(64, 11, 11), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        loginBox.setPreferredSize(new Dimension(350, 300));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Admin Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 33, 33));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        usernameField = new JTextField(16);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        passwordField = new JPasswordField(16);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBackground(new Color(64, 11, 11));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setBackground(new Color(64, 11, 11));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginBox.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        loginBox.add(usernameLabel, gbc);
        gbc.gridx = 1;
        loginBox.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        loginBox.add(passwordLabel, gbc);
        gbc.gridx = 1;
        loginBox.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        loginBox.add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        loginBox.add(backButton, gbc);


        mainPanel.add(loginBox);

        add(mainPanel);


        loginButton.addActionListener(e -> loginAdmin());
        backButton.addActionListener(e -> {
            dispose();
            foodordering.MainLauncher.main(null);
        });
    }

    private void loginAdmin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose();
                new AdminPanelForm().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());

        }


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
