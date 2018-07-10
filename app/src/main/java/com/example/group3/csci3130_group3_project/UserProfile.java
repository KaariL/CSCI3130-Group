package com.example.group3.csci3130_group3_project;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserProfile implements Serializable {
    private String uid;
    private String username;
    private String favoriteColor;

    public UserProfile(){}
    public UserProfile(String uid, String username, String favoriteColor){
        this.uid = uid;
        this.username = username;
        this.favoriteColor = favoriteColor;
    }
    public void setUid(String uid){
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setFavoriteColor(String favoriteColor) {
        this.favoriteColor = favoriteColor;
    }

    public String getFavoriteColor() {
        return favoriteColor;
    }
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", uid);
        result.put("username", username);
        result.put("favoriteColor", favoriteColor);
        return result;
    }
}
