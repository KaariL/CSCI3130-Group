package com.example.group3.csci3130_group3_project;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Attribute implements Serializable {
    public  String id;
    public  String name;
    public  String number;

    public Attribute ()
    {

    }

    public Attribute(String id,String name,String number)
    {
        this.id=id;
        this.name=name;
        this.number=number;
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("number", number);


        return result;
    }

}