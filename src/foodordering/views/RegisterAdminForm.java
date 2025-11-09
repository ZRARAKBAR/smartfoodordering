package foodordering.views;

import foodordering.db.DBConnection;
import foodordering.MainLauncher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterAdminForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;

    public RegisterAdminForm() {
        setTitle(" Admin Registration");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(64, 11, 11));
        leftPanel.setPreferredSize(new Dimension(450, 500));

        JPanel formBox = new JPanel(new GridBagLayout());
        formBox.setBackground(new Color(184, 157, 121));
        formBox.setPreferredSize(new Dimension(350, 300));
        formBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(64, 11, 11), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Register Admin");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 33, 33));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField = new JPasswordField(20);

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        registerButton.setBackground(new Color(64, 11, 11));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);

        JButton backButton = new JButton(" Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.setBackground(new Color(240, 240, 240));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formBox.add(titleLabel, gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        formBox.add(usernameLabel, gbc);
        gbc.gridx = 1;
        formBox.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formBox.add(passwordLabel, gbc);
        gbc.gridx = 1;
        formBox.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        formBox.add(registerButton, gbc);

        gbc.gridy++;
        formBox.add(backButton, gbc);

        leftPanel.add(formBox);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(450, 500));
        rightPanel.setBackground(Color.WHITE);

        try {
            ImageIcon imgIcon = new ImageIcon("assets/restllogo.jpg");
            Image img = imgIcon.getImage().getScaledInstance(450, 500, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(img));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            rightPanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Image not found", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            errorLabel.setForeground(Color.RED);
            rightPanel.add(errorLabel, BorderLayout.CENTER);
        }

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        registerButton.addActionListener(e -> registerAdmin());
        backButton.addActionListener(e -> {
            dispose();
            MainLauncher.main(null);
        });
    }

    private void registerAdmin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO admins (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Admin registered successfully.");
                this.dispose();
                new LoginForm().setVisible(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
}
