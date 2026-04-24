package ui;

import business.BookManager;
import business.LoanManager;
import business.MemberManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    // Menü butonlarını takip etmek için bir liste
    private List<JButton> menuButtons = new ArrayList<>();

    // Tum paneller ayni manager'i kullansin diye burada olusturuyoruz.
    // Aksi halde bir panelde kitap eklesek diger panel goremez.
    private BookManager bookManager;
    private MemberManager memberManager;
    private LoanManager loanManager;

    // Panel referanslari (refresh icin)
    private ManageBooksFrame booksPanel;
    private ManageMembersFrame membersPanel;
    private IssueReturnFrame loansPanel;
    private ReportsFrame reportsPanel;

    public MainFrame(String role) {
        // Personel paneli pencere başlığı
        setTitle("Smart Library - Personnel Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Manager'lar - sirali olusturulmali cunku LoanManager BookManager'a ihtiyac duyuyor
        bookManager = new BookManager();
        memberManager = new MemberManager();
        loanManager = new LoanManager(bookManager);

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
        JButton btnLogout = createMenuButton("🚪 Logout");

        sidebar.add(btnHome);
        sidebar.add(btnBooks);
        sidebar.add(btnMembers);
        sidebar.add(btnLoans);
        sidebar.add(btnReports);
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // Varsayılan olarak ilk açılışta "Overview" aktif olsun
        setActiveButton(btnHome);

        // --- 2. CARDLAYOUT CONTENT AREA ---
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        // Panelleri olustur (manager'lari paylasiyor)
        booksPanel = new ManageBooksFrame(bookManager);
        membersPanel = new ManageMembersFrame(memberManager);
        loansPanel = new IssueReturnFrame(bookManager, memberManager, loanManager);
        reportsPanel = new ReportsFrame(memberManager, bookManager, loanManager);

        // Panelleri CardLayout'a ekliyoruz
        mainContentPanel.add(createOverviewPanel(), "HOME");
        mainContentPanel.add(booksPanel, "BOOKS");
        mainContentPanel.add(membersPanel, "MEMBERS");
        mainContentPanel.add(loansPanel, "LOANS");
        mainContentPanel.add(reportsPanel, "REPORTS");

        add(mainContentPanel, BorderLayout.CENTER);

        // --- 3. EVENT LISTENERS (Tıklama Olayları) ---
        btnHome.addActionListener(e -> { switchTab("HOME"); setActiveButton(btnHome); });
        btnBooks.addActionListener(e -> { switchTab("BOOKS"); setActiveButton(btnBooks); booksPanel.refreshTable(); });
        btnMembers.addActionListener(e -> { switchTab("MEMBERS"); setActiveButton(btnMembers); membersPanel.refreshTable(); });
        btnLoans.addActionListener(e -> { switchTab("LOANS"); setActiveButton(btnLoans); loansPanel.refreshAll(); });
        btnReports.addActionListener(e -> { switchTab("REPORTS"); setActiveButton(btnReports); reportsPanel.refreshTable(); });

        // Logout İşlemi
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                this.dispose();
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

    // Overview - basit bir hosgeldin ekrani + ozet sayilar
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("WELCOME TO SMART LIBRARY", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.decode("#556b2f"));
        panel.add(title, BorderLayout.NORTH);

        // Ozet kartlari (sayilar)
        JPanel summary = new JPanel(new GridLayout(1, 3, 20, 20));
        summary.setBackground(Color.WHITE);
        summary.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        summary.add(makeSummaryCard("Total Books", String.valueOf(bookManager.getAllBooks().size())));
        summary.add(makeSummaryCard("Total Members", String.valueOf(memberManager.getAllMembers().size())));
        summary.add(makeSummaryCard("Active Loans", String.valueOf(loanManager.getActiveLoans().size())));

        panel.add(summary, BorderLayout.CENTER);

        return panel;
    }

    private JPanel makeSummaryCard(String label, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.decode("#ffe7ba"));
        card.setBorder(BorderFactory.createLineBorder(Color.decode("#556b2f"), 2));

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(Color.decode("#556b2f"));
        lbl.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setFont(new Font("Arial", Font.BOLD, 36));
        val.setForeground(Color.BLACK);

        card.add(lbl, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);
        return card;
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
