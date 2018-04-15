package com.insurance.ins.technical.models;


import javax.validation.constraints.Size;

public class UserModel {
    private Long id;
    @Size(min=4,message="Please enter Username minimum 5 symbols")
    private String username;
    @Size(min=4,message="Please enter Password minimum 5 symbols")
    private String password;
    @Size(min=4,message="Please enter Confirm Password minimum 5 symbols")
    private String confirmPassword;

    public UserModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
