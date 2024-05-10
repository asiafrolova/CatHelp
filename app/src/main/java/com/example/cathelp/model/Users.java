package com.example.cathelp.model;

public class Users {
    private String name;
    private String phone;
    private String password;
    private String image;
    public Users(){


    }

    public Users(String name, String phone, String password) {
        this.name = name;
        this.phone = phone;
        this.password = password;

    }
    public Users(String name, String phone, String password,String image) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.image = image;

    }

    public String getImage() {
        return this.image;
    }


    public void setImage(final String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
