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

    public MainFrame() {
        // Basic window setup
        setTitle("Smart Library - Admin Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Using BorderLayout for the main structure
        setLayout(new BorderLayout());

        // --- 1. SIDEBAR SETUP (YENİ KOYU ZEYTİN YEŞİLİ: #556b2f) ---
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, getHeight()));

        // Menünün arka plan rengi güncellendi
        sidebar.setBackground(Color.decode("#556b2f"));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        JLabel menuTitle = new JLabel("SMART LIBRARY");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 22));
        // Koyu arka plan üzerinde okunabilmesi için başlık Beyaz yapıldı
        menuTitle.setForeground(Color.WHITE);
        menuTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.add(menuTitle);

        // Define Navigation Buttons
        JButton btnHome = createMenuButton("🏠 Overview");
        JButton btnBooks = createMenuButton("📚 Manage Books");
        JButton btnMembers = createMenuButton("👥 Manage Members");
        JButton btnLoans = createMenuButton("🔄 Issue / Return");
        JButton btnReports = createMenuButton("📊 Reports");

        sidebar.add(btnHome);
        sidebar.add(btnBooks);
        sidebar.add(btnMembers);
        sidebar.add(btnLoans);
        sidebar.add(btnReports);

        add(sidebar, BorderLayout.WEST);

        // Varsayılan olarak ilk açılışta "Overview (Home)" butonunu aktif/basılı hale getir
        setActiveButton(btnHome);

        // --- 2. CARDLAYOUT CONTENT AREA ---
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

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
    }

    /**
     * Tıklanan butonu krem rengi yapar (yazısını siyahlaştırır),
     * diğerlerini orijinal zeytin yeşiline (ve beyaz yazıya) döndürür.
     */
    private void setActiveButton(JButton activeBtn) {
        // Önce tüm butonları orijinal haline getir
        for (JButton btn : menuButtons) {
            btn.setBackground(Color.decode("#556b2f"));
            btn.setForeground(Color.WHITE); // Koyu yeşilde yazı beyaz
        }
        // Sadece tıklanan (aktif) butonu krem rengi yap ve yazısını okunabilirlik için SİYAH yap
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
        panel.add(label);

        return panel;
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setFont(new Font("Arial", Font.BOLD, 15));

        // Butonların varsayılan arka planı yeni zeytin yeşili
        btn.setBackground(Color.decode("#556b2f"));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover (Üzerine gelince hafif renk değiştirme efekti)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Buton aktif (krem) değilse, üzerine gelince yeşili bir tık aç (daha belirgin hover)
                if (!btn.getBackground().equals(Color.decode("#ffe7ba"))) {
                    btn.setBackground(Color.decode("#6b873b"));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Fare üzerinden gidince ve aktif (krem) değilse orijinal koyu yeşile dön
                if (!btn.getBackground().equals(Color.decode("#ffe7ba"))) {
                    btn.setBackground(Color.decode("#556b2f"));
                }
            }
        });

        // Oluşturulan her butonu kontrol listesine ekle
        menuButtons.add(btn);

        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}