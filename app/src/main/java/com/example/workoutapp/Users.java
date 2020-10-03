package com.example.workoutapp;

public class Users {
    private String name, phone, password;
    int points;

    public Users() {
//        default
    }

    public Users(String name, String phone, String password, int points) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.points = points;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
