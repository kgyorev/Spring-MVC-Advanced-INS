package com.insurance.ins.models.view;

/**
 * Created by K on 15.3.2018 г..
 */
public class UserRegisterDto {


    private String username;

    private String password;


    public UserRegisterDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
