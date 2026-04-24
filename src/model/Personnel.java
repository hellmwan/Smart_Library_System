package model;

// Kutuphane personeli (admin tarafindan eklenen calisanlar)
public class Personnel {

    private String fullName;
    private String username;
    private String password;

    public Personnel(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
