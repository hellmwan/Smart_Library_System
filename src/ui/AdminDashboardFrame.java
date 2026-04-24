package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardFrame extends JFrame {
    //bu en son

    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private List<JButton> menuButtons = new ArrayList<>();

    public AdminDashboardFrame() {
        // Personel paneli ile aynı başlık stili ve boyutlar
        setTitle("Smart Library - Admin Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // --- 1. SIDEBAR SETUP (PERSONEL İLE AYNI: KOYU ZEYTİN YEŞİLİ #556b2f) ---
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(Color.decode("#556b2f")); // Personel menü rengi
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        JLabel menuTitle = new JLabel("ADMIN PANEL");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 22));
        menuTitle.setForeground(Color.WHITE);
        menuTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.add(menuTitle);

        // Admin görevleri için butonlar
        JButton btnHome = createMenuButton("🏠 Admin Overview");
        JButton btnPersonnel = createMenuButton("🧑‍💼 Manage Personnel");
        JButton btnLogout = createMenuButton("🚪 Logout");

        sidebar.add(btnHome);
        sidebar.add(btnPersonnel);
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // Varsayılan butonu aktif yap
        setActiveButton(btnHome);

        // --- 2. CARDLAYOUT CONTENT AREA ---
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        // Panelleri ekle
        mainContentPanel.add(createPlaceholderPanel("ADMIN OVERVIEW", Color.WHITE), "HOME");
        mainContentPanel.add(new ManagePersonnelFrame(), "PERSONNEL");

        add(mainContentPanel, BorderLayout.CENTER);

        // --- 3. EVENT LISTENERS ---
        btnHome.addActionListener(e -> { switchTab("HOME"); setActiveButton(btnHome); });
        btnPersonnel.addActionListener(e -> { switchTab("PERSONNEL"); setActiveButton(btnPersonnel); });

        btnLogout.addActionListener(e -> {

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Çıkış yapmak istediğinize emin misiniz?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                this.dispose();
            }
        });
    }

    private void setActiveButton(JButton activeBtn) {
        for (JButton btn : menuButtons) {
            btn.setBackground(Color.decode("#556b2f")); // Pasif: Zeytin Yeşili
            btn.setForeground(Color.WHITE);
        }
        activeBtn.setBackground(Color.decode("#ffe7ba")); // Aktif: Krem Rengi
        activeBtn.setForeground(Color.BLACK);
    }

    private void switchTab(String tabName) {
        cardLayout.show(mainContentPanel, tabName);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private JPanel createPlaceholderPanel(String text, Color bgColor) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.decode("#556b2f")); // Metinler de uyumlu olsun
        panel.add(label);
        return panel;
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setBackground(Color.decode("#556b2f"));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(Color.decode("#ffe7ba"))) {
                    btn.setBackground(Color.decode("#6b873b")); // Hover efekti
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(Color.decode("#ffe7ba"))) {
                    btn.setBackground(Color.decode("#556b2f"));
                }
            }
        });

        menuButtons.add(btn);
        return btn;
    }
}