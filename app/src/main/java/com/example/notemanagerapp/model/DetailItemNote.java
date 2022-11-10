package com.example.notemanagerapp.model;

public class DetailItemNote {
    private String name;
    private String dateCreate;
    private String email;

    public DetailItemNote(String name, String dateCreate, String email) {
        this.name = name;
        this.dateCreate = dateCreate;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
