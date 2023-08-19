package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class Repository {
    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;


    public Repository(String name, String description) {
        this.name = name;
        this.description = description;
    }



    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
