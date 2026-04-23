package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Image backgroundImage;

    // --- ÖNEMLİ: Admin'in eklediği personellerin tutulduğu ortak liste ---
    public static List<String[]> registeredPersonnel = new ArrayList<>();

    static {
        // Varsayılan kütüphaneci hesabı
        registeredPersonnel.add(new String[]{"Default Librarian", "lib", "lib123"});
    }

    public LoginFrame() {
        setTitle("Smart Library - Login");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        backgroundImage = new ImageIcon("src/xyz.jpeg").getImage();

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                float opacity = 0.75f;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                if (backgroundImage != null) {
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
                g2d.dispose();
            }
        };
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        buildUI();
    }

    private void buildUI() {
        JLabel userLabel = new JLabel("Username / E-mail");
        userLabel.setBounds(50, 200, 300, 20);
        userLabel.setForeground(Color.BLACK);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(50, 220, 300, 30);
        usernameField.setOpaque(false);
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        add(usernameField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(50, 280, 300, 20);
        passLabel.setForeground(Color.BLACK);
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(50, 300, 300, 30);
        passwordField.setOpaque(false);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        add(passwordField);

        loginButton = new JButton("LOG IN");
        loginButton.setBounds(50, 390, 300, 45);
        loginButton.setBackground(new Color(74, 144, 226));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        add(loginButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // 1. Admin Kontrolü
            if (username.equals("admin") && password.equals("admin123")) {
                new AdminDashboardFrame().setVisible(true);
                this.dispose();
                return;
            }

            // 2. Dinamik Personel Kontrolü (Admin'in ekledikleri dahil)
            boolean accessGranted = false;
            for (String[] personnel : registeredPersonnel) {
                // personnel[1] = username, personnel[2] = password
                if (personnel[1].equals(username) && personnel[2].equals(password)) {
                    new MainFrame("LIBRARIAN").setVisible(true);
                    this.dispose();
                    accessGranted = true;
                    break;
                }
            }

            if (!accessGranted) {
                JOptionPane.showMessageDialog(this, "Giriş izniniz bulunmamaktadır!", "Yetki Hatası", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}