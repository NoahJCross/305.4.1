package com.example.a41;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {

    private long id;
    private String title;
    private String description;
    private String dueDate;

    public Task(){

    }
    public Task(String title, String description, String dueDate){
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }


    // Getters and setters
    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

}
