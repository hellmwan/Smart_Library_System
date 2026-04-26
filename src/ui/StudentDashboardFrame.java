package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Bu ekran ileride ogrencilerin (uyelerin) kendi hesaplarindan
 * giris yapip kitap arayabilecekleri, odunc aldiklarini gorebilecekleri
 * ve cezalarini takip edebilecekleri bir panel olacak.
 * Su anki halinde dort buton var ama sadece "Logout" calisir; digerleri sonradan implement edilecek.
 */
public class StudentDashboardFrame extends JFrame {

    /**
     * @param studentName basliga yazilacak ogrenci ismi
     */
    public StudentDashboardFrame(String studentName) {
        // Pencerenin baslik, boyut ve davranisi
        setTitle("Smart Library - Student Dashboard (" + studentName + ")");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 2x2 izgaralı duzen, butonlar arasi 20 px bosluk
        setLayout(new GridLayout(2, 2, 20, 20));
        getContentPane().setBackground(Color.WHITE);
        // Pencerenin etrafina genis bir bos kenarluk koy
        getRootPane().setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // 4 ana islev butonu
        JButton btnBrowse = new JButton("Browse Books");
        JButton btnMyBooks = new JButton("My Borrowed Books");
        JButton btnProfile = new JButton("My Profile & Fines");
        JButton btnLogout = new JButton("Logout");

        // Logout: bu pencereyi kapat, yeni LoginFrame ac
        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });

        // Butonlari panele ekle
        add(btnBrowse);
        add(btnMyBooks);
        add(btnProfile);
        add(btnLogout);
    }
}
