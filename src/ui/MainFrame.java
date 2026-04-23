package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    // Menü butonlarını takip etmek için bir liste
    private List<JButton> menuButtons = new ArrayList<>();

    public MainFrame(String role) {
        // Personel paneli pencere başlığı
        setTitle("Smart Library - Personnel Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout
        setLayout(new BorderLayout());

        // --- 1. SIDEBAR SETUP (KOYU ZEYTİN YEŞİLİ: #556b2f) ---
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(Color.decode("#556b2f"));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        JLabel menuTitle = new JLabel("SMART LIBRARY");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 22));
        menuTitle.setForeground(Color.WHITE);
        menuTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.add(menuTitle);

        // Define Navigation Buttons
        JButton btnHome = createMenuButton("🏠 Overview");
        JButton btnBooks = createMenuButton("📚 Manage Books");
        JButton btnMembers = createMenuButton("👥 Manage Members");
        JButton btnLoans = createMenuButton("🔄 Issue / Return");
        JButton btnReports = createMenuButton("📊 Reports");
        JButton btnLogout = createMenuButton("🚪 Logout"); // Yeni Logout butonu

        sidebar.add(btnHome);
        sidebar.add(btnBooks);
        sidebar.add(btnMembers);
        sidebar.add(btnLoans);
        sidebar.add(btnReports);
        sidebar.add(btnLogout); // Sidebar'a eklendi

        add(sidebar, BorderLayout.WEST);

        // Varsayılan olarak ilk açılışta "Overview" aktif olsun
        setActiveButton(btnHome);

        // --- 2. CARDLAYOUT CONTENT AREA ---
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        // Panelleri CardLayout'a ekliyoruz
        mainContentPanel.add(createPlaceholderPanel("WELCOME TO SMART LIBRARY", Color.WHITE), "HOME");
        mainContentPanel.add(new ManageBooksFrame(), "BOOKS");
        mainContentPanel.add(new ManageMembersFrame(), "MEMBERS");
        mainContentPanel.add(new IssueReturnFrame(), "LOANS");
        mainContentPanel.add(new ReportsFrame(), "REPORTS");

        add(mainContentPanel, BorderLayout.CENTER);

        // --- 3. EVENT LISTENERS (Tıklama Olayları) ---
        btnHome.addActionListener(e -> { switchTab("HOME"); setActiveButton(btnHome); });
        btnBooks.addActionListener(e -> { switchTab("BOOKS"); setActiveButton(btnBooks); });
        btnMembers.addActionListener(e -> { switchTab("MEMBERS"); setActiveButton(btnMembers); });
        btnLoans.addActionListener(e -> { switchTab("LOANS"); setActiveButton(btnLoans); });
        btnReports.addActionListener(e -> { switchTab("REPORTS"); setActiveButton(btnReports); });

        // Logout İşlemi
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true); // Login ekranına dön
                this.dispose(); // Mevcut dashboard'u kapat
            }
        });
    }

    /**
     * Tıklanan butonu aktif stile sokar.
     */
    private void setActiveButton(JButton activeBtn) {
        for (JButton btn : menuButtons) {
            btn.setBackground(Color.decode("#556b2f"));
            btn.setForeground(Color.WHITE);
        }
        // Aktif buton Krem (#ffe7ba) ve yazı Siyah
        activeBtn.setBackground(Color.decode("#ffe7ba"));
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
        label.setForeground(Color.decode("#556b2f"));
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
                    btn.setBackground(Color.decode("#6b873b"));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame("LIBRARIAN").setVisible(true));
    }
}