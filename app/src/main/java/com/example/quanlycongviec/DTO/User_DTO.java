package com.example.quanlycongviec.DTO;

public class User_DTO {
    private long id; // Đã là long
    private String email;
    private String password;
    private String name;
    private String gender;
    private String birthday;
    private byte[] avatar;

    // Constructor
    public User_DTO(long id, String email, String password, String name, String gender, String birthday, byte[] avatar) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.avatar = avatar;
    }

    // Getters và Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public byte[] getAvatar() { return avatar; }
    public void setAvatar(byte[] avatar) { this.avatar = avatar; }
}
