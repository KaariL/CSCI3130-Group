package com.example.group3.csci3130_group3_project;
import java.util.ArrayList;

public class User {
    String name;
    String password;
    Location home;
    ArrayList<Location> Starred;

    public User(String n){
        name = n;
        Starred = new ArrayList<Location>();
    }
}
