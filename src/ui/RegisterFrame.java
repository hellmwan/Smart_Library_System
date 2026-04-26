package ui;

import javax.swing.*;

/**
 * Bu ekran su anda sadece bir yer tutucudur (placeholder).
 * Ileride yeni kullanicilarin sisteme kayit olabilmesi icin gerekli form ve alanlar eklenecektir.
 * Su an icin sadece basit bir bilgi metni gosterir.
 */
public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        // Pencere baslik ve boyutu
        setTitle("Smart Library - Sign Up");
        setSize(400, 500);
        // DISPOSE_ON_CLOSE -> sadece bu pencere kapanir, program devam eder
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // ekrani ortada ac

        // Ortalanmis bir bilgi etiketi
        add(new JLabel("Registration Screen will be here.", SwingConstants.CENTER));
    }
}
