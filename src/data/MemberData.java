package data;

import model.Member;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MemberData {

    private static final String FILE = "data/members.txt";

    public static List<Member> loadAll() {
        List<Member> list = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Member m = Member.fromCsvLine(line);
                if (m != null) list.add(m);
            }
        } catch (IOException e) {
            System.out.println("Uye dosyasi okunamadi: " + e.getMessage());
        }
        return list;
    }

    public static void saveAll(List<Member> members) {
        new File("data").mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Member m : members) {
                bw.write(m.toCsvLine());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Uye dosyasi yazilamadi: " + e.getMessage());
        }
    }

    public static int nextId(List<Member> members) {
        int max = 100; // uye id'leri 101'den baslasin
        for (Member m : members) {
            if (m.getId() > max) max = m.getId();
        }
        return max + 1;
    }
}
