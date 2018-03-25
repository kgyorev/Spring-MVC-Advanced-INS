package com.insurance.ins.models.binding;

/**
 * Created by K on 15.3.2018 г..
 */
public class UserRegisterBindingModel {


    private String username;
    private String password;
    private String confirmPassword;


    public UserRegisterBindingModel() {
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
