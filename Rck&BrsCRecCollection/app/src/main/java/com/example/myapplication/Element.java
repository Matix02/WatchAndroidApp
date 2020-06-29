package com.example.myapplication;

import androidx.room.Entity;

@Entity(tableName = "Element")
public class Element {
    public String title;
    public String category;
    public boolean isWatched;

    Element() { }

    public Element(String title, String category, boolean isWatched) {
        this.title = title;
        this.category = category;
        this.isWatched = isWatched;
    }

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

