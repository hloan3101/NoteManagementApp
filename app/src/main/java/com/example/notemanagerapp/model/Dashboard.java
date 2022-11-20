package com.example.notemanagerapp.model;

public class Dashboard {

    private String nameStatus;
    private String numberOfStatus;

    public Dashboard(String nameStatus, String numberOfStatus) {
        this.nameStatus = nameStatus;
        this.numberOfStatus = numberOfStatus;
    }

    public String getNameStatus() {
        return nameStatus;
    }

    public void setNameStatus(String nameStatus) {
        this.nameStatus = nameStatus;
    }

    public String getNumberOfStatus() {
        return numberOfStatus;
    }

    public void setNumberOfStatus(String numberOfStatus) {
        this.numberOfStatus = numberOfStatus;
    }
}
