package ui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Image backgroundImage;

    public LoginFrame() {
        setTitle("Smart Library - Login");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- LOAD THE BACKGROUND IMAGE ---
        backgroundImage = new ImageIcon("src/background.png").getImage();

        // --- CREATE A CUSTOM PANEL WITH OPACITY CONTROL ---
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                float opacity = 0.5f; // Saydamlık ayarı
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
        usernameField.setForeground(Color.BLACK);
        usernameField.setCaretColor(Color.BLACK);
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
        add(usernameField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(50, 280, 300, 20);
        passLabel.setForeground(Color.BLACK);
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(50, 300, 300, 30);
        passwordField.setOpaque(false);
        passwordField.setForeground(Color.BLACK);
        passwordField.setCaretColor(Color.BLACK);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
        add(passwordField);

        loginButton = new JButton("LOG IN");
        loginButton.setBounds(50, 380, 300, 45); // Sadece Login butonu kaldı
        loginButton.setBackground(new Color(74, 144, 226));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        add(loginButton);

        // --- GİRİŞ MANTIĞI ---
        loginButton.addActionListener(e -> {
            // Şimdilik giriş yapan herkesi doğrudan Personel/Admin paneline (MainFrame) atıyoruz.
            // İleride "admin" ve "personel" yetki ayrımını veritabanı bağladığımızda buraya ekleyeceğiz.
            new MainFrame().setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        new LoginFrame().setVisible(true);
    }
}