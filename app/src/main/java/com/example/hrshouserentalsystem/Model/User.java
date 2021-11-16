package com.example.hrshouserentalsystem.Model;

public class User {
    String userName,email,profile_url;

    public User() {
    }

    public User(String userName, String email, String profile_url) {
        this.userName = userName;
        this.email = email;
        this.profile_url = profile_url;
    }

    public String getUserName() {
        return userName;
    }


    public String getEmail() {
        return email;
    }


    public String getProfile_url() {
        return profile_url;
    }

}
