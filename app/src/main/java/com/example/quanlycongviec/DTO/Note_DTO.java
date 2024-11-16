package com.example.quanlycongviec.DTO;

public class Note_DTO {
    private long id; // Đã là long
    private long userId; // Chuyển từ int sang long
    private String title;
    private String content;

    // Constructor
    public Note_DTO(long id, long userId, String title, String content) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    // Getters và Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
