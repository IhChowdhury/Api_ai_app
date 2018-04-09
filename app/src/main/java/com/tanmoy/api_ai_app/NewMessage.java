package com.tanmoy.api_ai_app;

public class NewMessage {
    String user, message, time;
    int userPhoto;

    public NewMessage(String user, String message, String time, int userPhoto){
        this.user = user;
        this.message = message;
        this.time = time;
        this.userPhoto = userPhoto;

    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public int getUserPhoto() {
        return userPhoto;
    }
}
