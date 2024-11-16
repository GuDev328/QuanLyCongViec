package com.example.quanlycongviec.DTO;

public class Category_DTO {
    private long id; // Đã là long
    private long userId; // Chuyển từ int sang long
    private String name;
    private String description;

    // Constructor
    public Category_DTO(long id, long userId, String name, String description) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
    }

    // Getters và Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
