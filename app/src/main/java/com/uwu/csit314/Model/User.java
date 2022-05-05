package com.uwu.csit314.Model;


public class User {
    private String Username;
    private String Password;
    private String IsStaff;

    public User() {
    }


    public User(String name, String password) {
        Username = name;
        Password = password;
        IsStaff ="false";
    }


    public String getName() {
        return Username;
    }

    public void setName(String name) {
        Username = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

}