package com.example.doan_ltmb.data.model;

import com.google.gson.annotations.SerializedName;

public class User {
    private String id;
    private String name;
    private String email;
    private String avatar;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getAvatar() { return avatar; }
}
