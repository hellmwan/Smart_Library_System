package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Programin ANA giris noktasidir.
 * Kullanici adi/sifre girilir; admin'se AdminDashboardFrame'e,
 * personel (kutuphaneci) ise MainFrame'e yonlendirilir.
 * Arka plan resmi vardir ve seffafligi (opacity) %75'tir.
 * Sabit kullanici bilgileri (test/demo amacli):
 *   admin / admin123  -> Admin
 *   lib   / lib123    -> Librarian (varsayilan, ilk acilista zaten ekli)
 */
public class LoginFrame extends JFrame {

    // --- Form bileşenleri ---
    private JTextField usernameField;       // Kullanici adi giris kutusu
    private JPasswordField passwordField;   // Sifre giris kutusu (yildizlarla gosterir)
    private JButton loginButton;
    private Image backgroundImage;          // Arka plan resmi


    /**
     * Tum kayitli personel listesi. Her bir personel:
     *   String[]{ ad-soyad, kullanici-adi, sifre }
     *
     * static List -> tum siniflar (orn: ManagePersonnelFrame) ayni
     * listeyi paylasir. Admin yeni personel eklediginde Login burayi okuyacak ve giris izni verecek.
     */
    public static List<String[]> registeredPersonnel = new ArrayList<>();

    // Static blok: Sinif yuklenir yuklenmez calisir, varsayilan
    // librarian'i listeye ekler.
    static {
        registeredPersonnel.add(new String[]{"Default Librarian", "lib", "lib123"});
    }


    public LoginFrame() {
        // Pencere ayarlari
        setTitle("Smart Library - Login");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // ekran ortasinda ac


        // Arka plan resmini yuklemeye calis
        try {
            // Resmi src/ klasorundeki classpath'ten al
            backgroundImage = new ImageIcon(getClass().getResource("/xyz.jpeg")).getImage();
        } catch (Exception ex) {
            // Resim yoksa programi durdurma, sadece haber ver
            System.out.println("Resim bulunamadı! Lütfen yolu kontrol edin.");
            backgroundImage = null;
        }

        /*
         * Arka plan paneli: paintComponent metodunu override ederek
         * resmi yari seffaf cizeriz. Boylece form elemanlari ustte kolay okunur.
         */
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    float opacity = 0.75f; // %75 opaklik
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g2d.dispose();
                } else {
                    // Resim yuklenemediyse acik gri arka plan
                    setBackground(Color.decode("#f5f5f5"));
                }
            }
        };
        backgroundPanel.setLayout(null); // mutlak konumlandirma kullaniyoruz
        setContentPane(backgroundPanel);

        // Form elemanlarini olusturup yerlestir
        buildUI();
    }


    /**
     * Form elemanlarini olusturup pencereye ekler.
     * setBounds(x, y, width, height) ile mutlak konumlama yapilir.
     */
    private void buildUI() {
        // ---- Username etiketi ve kutusu ----
        JLabel userLabel = new JLabel("Username / E-mail");
        userLabel.setBounds(50, 200, 300, 20);
        userLabel.setForeground(Color.BLACK);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(50, 220, 300, 30);
        usernameField.setOpaque(false); // arka planini gostersin diye seffaf
        // Sadece alta siyah cizgi ekle, kutu cercevesi olmasin
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        add(usernameField);

        // ---- Password etiketi ve kutusu ----
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

        // ---- Login butonu ----
        loginButton = new JButton("LOG IN");
        loginButton.setBounds(50, 390, 300, 45);
        loginButton.setBackground(new Color(74, 144, 226)); // mavi tonu
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false); // odak cercevesini kapat
        add(loginButton);


        // ---- Demo bilgi etiketi ----
        // Kullaniciya ornek giris bilgilerini gosterir
        JLabel hint = new JLabel(
                "<html><center>" +
                        "Admin → admin / admin123<br>" +
                        "Librarian → lib / lib123" +
                        "</center></html>"
        );
        hint.setBounds(50, 450, 300, 60);
        hint.setForeground(Color.DARK_GRAY);
        hint.setFont(new Font("Arial", Font.PLAIN, 12));
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        add(hint);

        // Login butonuna tiklayinca tryLogin'i calistir
        loginButton.addActionListener(e -> tryLogin());

        // Sifre kutusunda Enter'a basildiginda da giris denesin
        passwordField.addActionListener(e -> tryLogin());
    }


    /**
     * Giris butonuna basildiginda calisir.
     * Adimlar:
     *  1. Bos alan kontrolu
     *  2. admin/admin123 esleme -> AdminDashboardFrame
     *  3. registeredPersonnel listesinde varsa -> MainFrame (LIBRARIAN)
     *  4. Hicbiri eslesmezse hata mesaji
     */
    private void tryLogin() {
        String username = usernameField.getText().trim();
        // PasswordField'dan getPassword() char[] doner, String'e cevir
        String password = new String(passwordField.getPassword());

        // 1. Bos alan kontrolu
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen kullanıcı adı ve şifre giriniz.",
                    "Eksik Bilgi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Admin kontrolu (sabit kullanici)
        if (username.equals("admin") && password.equals("admin123")) {
            new AdminDashboardFrame().setVisible(true);
            this.dispose(); // Login penceresini kapat
            return;
        }

        // 3. Personel listesinde ara
        for (String[] personnel : registeredPersonnel) {
            // index 1 = kullanici adi, index 2 = sifre
            if (personnel[1].equals(username) && personnel[2].equals(password)) {
                new MainFrame("LIBRARIAN").setVisible(true);
                this.dispose();
                return;
            }
        }

        // 4. Hicbiri eslesmedi
        JOptionPane.showMessageDialog(this,
                "Giriş izniniz bulunmamaktadır!",
                "Yetki Hatası", JOptionPane.WARNING_MESSAGE);
    }


    /**
     * Programin baslangic noktasi.
     * Adimlar:
     *  1. Ilk acilista ornek odunc kayitlari yarat (data/loans.txt yoksa)
     *  2. Swing thread'inde LoginFrame'i goster
     * SwingUtilities.invokeLater -> UI thread'i (EDT) uzerinde calistir.
     * Swing kodu ana thread'te calistirilirsa donme/cokme olabilir.
     */
    public static void main(String[] args) {
        data.SeedData.seedIfEmpty();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
