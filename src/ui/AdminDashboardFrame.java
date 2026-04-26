package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Sol tarafta menu, sag tarafta dinamik icerik (CardLayout) vardir.
 * Admin'in iki ana islevi olur:
 *   1. Genel goruntu (placeholder)
 *   2. Personel yonetimi (kutuphaneci ekle/sil)
 * Ayrica logout butonu vardir.
 *
 * Layout duzeni:
 *   BorderLayout: WEST = sidebar, CENTER = mainContentPanel
 */
public class AdminDashboardFrame extends JFrame {

    private CardLayout cardLayout;            // Tab gecisi icin (sayfa karti gibi)
    private JPanel mainContentPanel;          // CardLayout'un yerlestigi panel
    private List<JButton> menuButtons = new ArrayList<>();  // Tum menu butonlari


    public AdminDashboardFrame() {
        // Pencere ayarlari
        setTitle("Smart Library - Admin Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout()); // ust-duzen


        // ---- SIDEBAR (sol menu) ----
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        // Tema rengi: zeytin yesili (#556b2f)
        sidebar.setBackground(Color.decode("#556b2f"));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        // Sidebar basligi
        JLabel menuTitle = new JLabel("ADMIN PANEL");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 22));
        menuTitle.setForeground(Color.WHITE);
        menuTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.add(menuTitle);


        // Menu butonlari 
        JButton btnHome = createMenuButton("🏠 Admin Overview");
        JButton btnPersonnel = createMenuButton("🧑‍💼 Manage Personnel");
        JButton btnLogout = createMenuButton("🚪 Logout");

        sidebar.add(btnHome);
        sidebar.add(btnPersonnel);
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);


        // Acilista "Home" sekmesi aktif gozuksun
        setActiveButton(btnHome);


        // ---- ANA ICERIK ALANI (CardLayout) ----
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        // Kartlar:
        //   "HOME"      -> placeholder yazi
        //   "PERSONNEL" -> ManagePersonnelFrame paneli
        mainContentPanel.add(createPlaceholderPanel("ADMIN OVERVIEW", Color.WHITE), "HOME");
        mainContentPanel.add(new ManagePersonnelFrame(), "PERSONNEL");

        add(mainContentPanel, BorderLayout.CENTER);


        // ---- BUTON OLAYLARI ----
        // Home butonu -> HOME kartina ge ve home butonunu aktiflesir
        btnHome.addActionListener(e -> { switchTab("HOME"); setActiveButton(btnHome); });
        // Personnel butonu -> PERSONNEL kartina gec
        btnPersonnel.addActionListener(e -> { switchTab("PERSONNEL"); setActiveButton(btnPersonnel); });

        // Logout: onay sor, evet derse Login'e don
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Çıkış yapmak istediğinize emin misiniz?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                this.dispose(); // bu pencereyi kapat
            }
        });
    }


    /**
     * Tum menu butonlarini varsayilan renge boyar, sonra seçili olanı aktif renge.
     * Bu sayede hangi sekmenin acik oldugu gorsel olarak belli olur.
     */
    private void setActiveButton(JButton activeBtn) {
        // Tum butonlari pasif renk yap (varsayilan)
        for (JButton btn : menuButtons) {
            btn.setBackground(Color.decode("#556b2f")); // zeytin yesili
            btn.setForeground(Color.WHITE);
        }
        // Aktif butonu vurgu rengiyle isaretle
        activeBtn.setBackground(Color.decode("#ffe7ba")); // krem
        activeBtn.setForeground(Color.BLACK);
    }

    /**
     * CardLayout uzerinde belirtilen karta gecer ve panel yenilenir.
     */
    private void switchTab(String tabName) {
        cardLayout.show(mainContentPanel, tabName);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }


    /**
     * Ortada buyuk yazi olan basit bir placeholder panel olusturur.
     * Asagidaki "HOME" karti icin kullaniliyor.
     */
    private JPanel createPlaceholderPanel(String text, Color bgColor) {
        JPanel panel = new JPanel(new GridBagLayout()); // ortalamak icin
        panel.setBackground(bgColor);
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.decode("#556b2f"));
        panel.add(label);
        return panel;
    }


    /**
     * Sidebar icin standart stilde menu butonu uretir.
     * Hover (mouse uzerine geldiginde) rengi acilir.
     */
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(220, 45));
        // Segoe UI Emoji -> emojileri renkli gosterir (Windows)
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        btn.setBackground(Color.decode("#556b2f"));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);   // odak cizgisi yok
        btn.setBorderPainted(false);  // varsayilan kenarluk yok
        btn.setOpaque(true);          // arka plan gozuksun
        btn.setHorizontalAlignment(SwingConstants.LEFT); // sola yasli yazi
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));   // mouse uzerine geldiginde el


        // Mouse hareket olaylari -> hover efekti
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            // Mouse butonun ustune girince
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Eger buton "aktif" durumda degilse hover rengi ata
                // (aktif butonu kremde tutmak istiyoruz)
                if (!btn.getBackground().equals(Color.decode("#ffe7ba"))) {
                    btn.setBackground(Color.decode("#6b873b"));
                }
            }
            // Mouse butondan ayrilinca
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(Color.decode("#ffe7ba"))) {
                    btn.setBackground(Color.decode("#556b2f"));
                }
            }
        });

        // Aktif kontrolu icin listeye ekle
        menuButtons.add(btn);
        return btn;
    }
}
