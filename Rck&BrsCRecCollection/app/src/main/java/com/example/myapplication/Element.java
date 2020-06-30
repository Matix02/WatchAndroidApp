package com.example.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Element")
public class Element {

    @PrimaryKey(autoGenerate = true)
    private long id;

    public String title;
    public String category;
    public boolean isWatched;

    Element() { }

    public Element(String title, String category, boolean isWatched) {
        this.title = title;
        this.category = category;
        this.isWatched = isWatched;
    }

    public Element(long id, String title, String category, boolean isWatched) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.isWatched = isWatched;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }
    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getCategory() {
        return category;
    }

    void setCategory(String category) {
        this.category = category;
    }

    boolean isWatched() {
        return isWatched;
    }

    void setWatched(boolean watched) {
        isWatched = watched;
    }
}

