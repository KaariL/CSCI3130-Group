package com.example.group3.csci3130_group3_project;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


class Course implements Serializable{
    public String name;
    public String building;
    public String campus;
    public String description;
    public String room;

    public Course(){

    }

    public Course(String b, String c, String d,String r) {
        //this.name = n;
        this.building = b;
        this.campus = c;
        this.description = d;
        this.room = r;
    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name",name);
        result.put("building",building);
        result.put("campus",campus);
        result.put("description",description);
        result.put("room",room);
        return result;
    }

}
