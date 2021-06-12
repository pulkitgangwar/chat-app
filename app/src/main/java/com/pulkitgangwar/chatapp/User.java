package com.pulkitgangwar.chatapp;

public class User {

    private String name,email,imageUri,uid;

    public User() {

    }

    public User(String name ,String email,String imageUri ,String uid) {
        this.name = name;
        this.email = email;
        this.imageUri = imageUri;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
