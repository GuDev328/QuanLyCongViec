package com.example.quanlycongviec.StatisticAction;

public class UserSession {
    private static UserSession instance;
    private int userId;

    private UserSession(){

    }
    public static synchronized UserSession getInstance(){
        if (instance == null){
            instance = new UserSession();
        }
        return instance;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void clearSession() {
        userId = 0;
    }
}
