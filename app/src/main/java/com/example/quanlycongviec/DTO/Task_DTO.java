package com.example.quanlycongviec.DTO;

public class Task_DTO {
    private long id; // Đã là long
    private long categoryId; // Chuyển từ int sang long
    private long userId; // Chuyển từ int sang long
    private String date;
    private String time;
    private String title;
    private String description;
    private int status;

    // Constructor
    public Task_DTO(long id, long categoryId, long userId, String date, String time, String title, String description, int status) {
        this.id = id;
        this.categoryId = categoryId;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    // Getters và Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getCategoryId() { return categoryId; }
    public void setCategoryId(long categoryId) { this.categoryId = categoryId; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}
