package com.vaavdevelopers.fantasysportsapp.models;

import android.widget.EditText;

public class UserProfile {
    String username;
    String mobileNo;
    String email;
    int coins;

    public String getUsername() {
        return username;
    }

    public UserProfile(String username, String mobileNo, String email, int coins) {
        this.username = username;
        this.mobileNo = mobileNo;
        this.email = email;
        this.coins = coins;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
