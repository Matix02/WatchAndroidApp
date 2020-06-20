package com.example.myapplication;

public class Element {
    private String title;
    private String category;
    private boolean isWatched;

    public Element() { }

    public Element(String title, String category, boolean isWatched) {
        this.title = title;
        this.category = category;
        this.isWatched = isWatched;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }
}
