package data;

import model.Loan;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoanData {

    private static final String FILE = "data/loans.txt";

    public static List<Loan> loadAll() {
        List<Loan> list = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    Loan l = Loan.fromCsvLine(line);
                    if (l != null) list.add(l);
                } catch (Exception ex) {
                    // bozuk satiri atla
                    System.out.println("Bozuk satir: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Loan dosyasi okunamadi: " + e.getMessage());
        }
        return list;
    }

    public static void saveAll(List<Loan> loans) {
        new File("data").mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Loan l : loans) {
                bw.write(l.toCsvLine());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Loan dosyasi yazilamadi: " + e.getMessage());
        }
    }

    public static int nextId(List<Loan> loans) {
        int max = 0;
        for (Loan l : loans) {
            if (l.getId() > max) max = l.getId();
        }
        return max + 1;
    }
}
