package model;

// Uye bilgilerini tutan sinif
public class Member {

    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String status; // "Active" veya "Passive"

    public Member(int id, String fullName, String email, String phone, String status) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    public Member(int id, String fullName, String email, String phone) {
        this(id, fullName, email, phone, "Active");
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getStatus() { return status; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setStatus(String status) { this.status = status; }

    public String toCsvLine() {
        return id + ";" + fullName + ";" + email + ";" + phone + ";" + status;
    }

    public static Member fromCsvLine(String line) {
        String[] parts = line.split(";");
        if (parts.length < 5) return null;
        int id = Integer.parseInt(parts[0]);
        return new Member(id, parts[1], parts[2], parts[3], parts[4]);
    }
}
