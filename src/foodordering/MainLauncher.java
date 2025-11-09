package foodordering;

import foodordering.views.LoginForm;
import foodordering.views.MenuForm;
import foodordering.views.RegisterAdminForm;
import foodordering.views.KitchenPanelForm;

import javax.swing.*;
import java.awt.*;

public class MainLauncher {
    public static void main(String[] args) {
        showSplashScreen();
        SwingUtilities.invokeLater(MainLauncher::showRoleSelection);
    }

    private static void showSplashScreen() {
        JWindow splash = new JWindow();
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(184, 157, 121));

        ImageIcon gifIcon = new ImageIcon("assets/splash.gif");
        JLabel gifLabel = new JLabel(gifIcon);
        gifLabel.setBounds(150, 100, 400, 350);

        JLabel title = new JLabel(" Order Management System  ", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(33, 33, 33));
        title.setBounds(150, 460, 400, 30);

        JLabel loading = new JLabel("", SwingConstants.CENTER);
        loading.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        loading.setForeground(Color.GRAY);
        loading.setBounds(0, 210, 500, 30);

        panel.add(gifLabel);
        panel.add(title);
        panel.add(loading);

        splash.add(panel);
        splash.setSize(700, 550);
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        splash.dispose();
    }

    public static void showRoleSelection() {
        JFrame frame = new JFrame("Select Role");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize);
        frame.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension((int) (screenSize.width * 0.25), screenSize.height));
        leftPanel.setBackground(new Color(184, 157, 121));

        JLabel titleLabel = new JLabel(" Choose Your Role", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(33, 33, 33));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        JButton adminLoginBtn = createStyledButton(" Admin Login");
        JButton adminRegisterBtn = createStyledButton(" Admin Registration");
        JButton customerBtn = createStyledButton(" Continue as Customer");
        JButton kitchenBtn = createStyledButton(" Kitchen Staff Panel");

        adminLoginBtn.addActionListener(e -> {
            frame.dispose();
            new LoginForm().setVisible(true);
        });

        adminRegisterBtn.addActionListener(e -> {
            frame.dispose();
            new RegisterAdminForm().setVisible(true);
        });

        customerBtn.addActionListener(e -> {
            frame.dispose();
            new MenuForm().setVisible(true);
        });

        kitchenBtn.addActionListener(e -> {
            frame.dispose();
            new KitchenPanelForm().setVisible(true);
        });

        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(adminLoginBtn);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(adminRegisterBtn);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(customerBtn);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(kitchenBtn);

        JPanel divider = new JPanel();
        divider.setBackground(Color.GRAY);
        divider.setPreferredSize(new Dimension(2, screenSize.height));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(184, 157, 121));

        try {
            ImageIcon gifIcon = new ImageIcon("assets/R.jpeg");
            JLabel gifLabel = new JLabel(gifIcon);
            gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gifLabel.setVerticalAlignment(SwingConstants.CENTER);
            rightPanel.add(gifLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("GIF not found", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            errorLabel.setForeground(Color.RED);
            rightPanel.add(errorLabel, BorderLayout.CENTER);
        }

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(divider, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
        button.setBackground(new Color(111, 78, 55));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
