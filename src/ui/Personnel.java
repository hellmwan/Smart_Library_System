package model;

public class Personnel {
    private String fullName;
    private String username;
    private String password;

    public Personnel(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    // Login kontrolü için getter metodları
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}