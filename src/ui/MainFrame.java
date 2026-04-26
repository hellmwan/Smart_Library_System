package ui;

import business.BookManager;
import business.LoanManager;
import business.MemberManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Login basariliysa LIBRARIAN icin acilir. Sol tarafta menu, sag tarafta
 * 5 farkli icerik (Overview, Books, Members, Loans, Reports) bulunur.
 * AdminDashboardFrame ile ayni mantikla calisir, sadece daha cok sekmesi vardir.
 * Onemli: Bu sinif Manager'lari (BookManager, MemberManager, LoanManager)
 * burada bir kez olusturup tum ic panellere paslar. Boylece tum paneller
 * AYNI veriyi paylasir.
 */
public class MainFrame extends JFrame {

    private CardLayout cardLayout;        // Sekmeler arasinda gecis icin
    private JPanel mainContentPanel;      // Karteri tutan panel

    /** Tum menu butonlari (aktif olani vurgulamak icin). */
    private List<JButton> menuButtons = new ArrayList<>();

    // --- Is mantigi yoneticileri ---
    private BookManager bookManager;
    private MemberManager memberManager;
    private LoanManager loanManager;

    // --- Ic paneller (her sekmenin icerigi) ---
    private ManageBooksFrame booksPanel;
    private ManageMembersFrame membersPanel;
    private IssueReturnFrame loansPanel;
    private ReportsFrame reportsPanel;


    /**
     * @param role kullanici rolu (su an sadece "LIBRARIAN" gonderiliyor)
     */
    public MainFrame(String role) {
        // Pencere ayarlari
        setTitle("Smart Library - Personnel Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        // Manager'lari olustur (her biri kendi dosyasini okur)
        bookManager = new BookManager();
        memberManager = new MemberManager();
        // LoanManager'a bookManager veriyoruz cunku odunc verince kitap durumunu degistiriyor
        loanManager = new LoanManager(bookManager);


        // Pencere ana duzeni: WEST=sidebar, CENTER=icerik
        setLayout(new BorderLayout());


        // ---- SIDEBAR ----
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(Color.decode("#556b2f")); // tema rengi
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        JLabel menuTitle = new JLabel("SMART LIBRARY");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 22));
        menuTitle.setForeground(Color.WHITE);
        menuTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.add(menuTitle);


        // 6 menu butonu olustur
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


        // Acilista Home aktif
        setActiveButton(btnHome);


        // ---- ANA ICERIK (CardLayout) ----
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);


        // Tum panelleri olustur (Manager'lari paylasarak)
        booksPanel = new ManageBooksFrame(bookManager);
        membersPanel = new ManageMembersFrame(memberManager);
        loansPanel = new IssueReturnFrame(bookManager, memberManager, loanManager);
        reportsPanel = new ReportsFrame(memberManager, bookManager, loanManager);


        // Panelleri CardLayout'a etiketleriyle ekle
        mainContentPanel.add(createOverviewPanel(), "HOME");
        mainContentPanel.add(booksPanel, "BOOKS");
        mainContentPanel.add(membersPanel, "MEMBERS");
        mainContentPanel.add(loansPanel, "LOANS");
        mainContentPanel.add(reportsPanel, "REPORTS");

        add(mainContentPanel, BorderLayout.CENTER);


        // ---- BUTON OLAYLARI ----
        // Home: sadece sekme degistir
        btnHome.addActionListener(e -> { switchTab("HOME"); setActiveButton(btnHome); });
        // Diger sekmeler: her gecisten once ilgili paneli yenile (refresh)
        // Cunku bir baska sekmede yapilan degisiklikleri burada da gormek istiyoruz
        btnBooks.addActionListener(e -> { switchTab("BOOKS"); setActiveButton(btnBooks); booksPanel.refreshTable(); });
        btnMembers.addActionListener(e -> { switchTab("MEMBERS"); setActiveButton(btnMembers); membersPanel.refreshTable(); });
        btnLoans.addActionListener(e -> { switchTab("LOANS"); setActiveButton(btnLoans); loansPanel.refreshAll(); });
        btnReports.addActionListener(e -> { switchTab("REPORTS"); setActiveButton(btnReports); reportsPanel.refreshTable(); });


        // Logout: onay sor, evetse Login'e don
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


    /** Aktif butonu vurgular, digerlerini varsayilan renge dondurur. */
    private void setActiveButton(JButton activeBtn) {
        for (JButton btn : menuButtons) {
            btn.setBackground(Color.decode("#556b2f"));
            btn.setForeground(Color.WHITE);
        }
        activeBtn.setBackground(Color.decode("#ffe7ba"));
        activeBtn.setForeground(Color.BLACK);
    }

    /** CardLayout uzerinde belirtilen sekmeye gec. */
    private void switchTab(String tabName) {
        cardLayout.show(mainContentPanel, tabName);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }


    /**
     * "Overview" karti: hosgeldin yazisi + 3 ozet kart
     * (toplam kitap, toplam uye, aktif odunc).
     */
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Ust kisim: buyuk hosgeldin basligi
        JLabel title = new JLabel("WELCOME TO SMART LIBRARY", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.decode("#556b2f"));
        panel.add(title, BorderLayout.NORTH);


        // Orta kisim: 1 satir 3 sutun ozet kartlar
        JPanel summary = new JPanel(new GridLayout(1, 3, 20, 20));
        summary.setBackground(Color.WHITE);
        summary.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        // Manager'lardan canli sayilari al
        summary.add(makeSummaryCard("Total Books", String.valueOf(bookManager.getAllBooks().size())));
        summary.add(makeSummaryCard("Total Members", String.valueOf(memberManager.getAllMembers().size())));
        summary.add(makeSummaryCard("Active Loans", String.valueOf(loanManager.getActiveLoans().size())));

        panel.add(summary, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Tek bir ozet karti olusturur: ustte etiket, altta buyuk sayi.
     * Ornek: ["Total Books"] / ["8"]
     */
    private JPanel makeSummaryCard(String label, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.decode("#ffe7ba")); // krem
        card.setBorder(BorderFactory.createLineBorder(Color.decode("#556b2f"), 2));

        // Ustteki kucuk etiket
        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(Color.decode("#556b2f"));
        lbl.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        // Altindaki buyuk sayi
        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setFont(new Font("Arial", Font.BOLD, 36));
        val.setForeground(Color.BLACK);

        card.add(lbl, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);
        return card;
    }


    /**
     * Sidebar icin tek tip menu butonu olusturur.
     * AdminDashboardFrame ile ayni stilde.
     */
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        btn.setBackground(Color.decode("#556b2f"));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover efekti
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

        // Aktif buton kontrolu icin tut
        menuButtons.add(btn);
        return btn;
    }


    /**
     * Bu sinifi tek basina test etmek icin baglangic noktasi.
     * Normalde program LoginFrame.main'den baslar.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame("LIBRARIAN").setVisible(true));
    }
}
