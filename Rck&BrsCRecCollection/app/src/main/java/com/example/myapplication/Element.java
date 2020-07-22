package com.example.myapplication;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Element")
public class Element {

    @PrimaryKey(autoGenerate = true)
    private long id;
    //muszą byc public, bo innaczej bedzie przypał
    public String recom;
    public String title;
    public String category;
    boolean isWatched;

    Element() { }

    public Element(String title, String category, boolean isWatched, String recom) {
        this.title = title;
        this.category = category;
        this.isWatched = isWatched;
        this.recom = recom;
    }

    public Element(long id, String title, String category, boolean isWatched, String recom) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.isWatched = isWatched;
        this.recom = recom;
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

    String getRecom() { return recom; }

    void setRecom(String recom) { this.recom = recom; }
}

@Entity(tableName = "ElementFilter")
class ElementFilter {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(defaultValue = "true")
    private boolean finished;

    @ColumnInfo(defaultValue = "true")
    private boolean unFinished;

    @ColumnInfo(defaultValue = "true")
    private boolean filmCategory;

    @ColumnInfo(defaultValue = "true")
    private boolean seriesCategory;

    @ColumnInfo(defaultValue = "true")
    private boolean bookCategory;

    @ColumnInfo(defaultValue = "true")
    private boolean gamesCategory;

    @ColumnInfo(defaultValue = "true")
    private boolean rockRecommedation;

    @ColumnInfo(defaultValue = "true")
    private boolean borysRecommedation;

    @ColumnInfo(defaultValue = "true")
    private boolean rockBorysRecommedation;

    @ColumnInfo(defaultValue = "true")
    private boolean otherRecommedation;

    ElementFilter(boolean finished, boolean unFinished, boolean filmCategory, boolean seriesCategory,
                  boolean bookCategory, boolean gamesCategory, boolean rockRecommedation, boolean borysRecommedation,
                  boolean rockBorysRecommedation, boolean otherRecommedation) {
        this.finished = finished;
        this.unFinished = unFinished;
        this.filmCategory = filmCategory;
        this.seriesCategory = seriesCategory;
        this.bookCategory = bookCategory;
        this.gamesCategory = gamesCategory;
        this.rockRecommedation = rockRecommedation;
        this.borysRecommedation = borysRecommedation;
        this.rockBorysRecommedation = rockBorysRecommedation;
        this.otherRecommedation = otherRecommedation;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isUnFinished() {
        return unFinished;
    }

    public void setUnFinished(boolean unFinished) {
        this.unFinished = unFinished;
    }

    public boolean isFilmCategory() {
        return filmCategory;
    }

    public void setFilmCategory(boolean filmCategory) {
        this.filmCategory = filmCategory;
    }

    public boolean isSeriesCategory() {
        return seriesCategory;
    }

    public void setSeriesCategory(boolean seriesCategory) {
        this.seriesCategory = seriesCategory;
    }

    public boolean isBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(boolean bookCategory) {
        this.bookCategory = bookCategory;
    }

    public boolean isGamesCategory() {
        return gamesCategory;
    }

    public void setGamesCategory(boolean gamesCategory) {
        this.gamesCategory = gamesCategory;
    }

    public boolean isRockRecommedation() {
        return rockRecommedation;
    }

    public void setRockRecommedation(boolean rockRecommedation) {
        this.rockRecommedation = rockRecommedation;
    }

    public boolean isBorysRecommedation() {
        return borysRecommedation;
    }

    public void setBorysRecommedation(boolean borysRecommedation) {
        this.borysRecommedation = borysRecommedation;
    }

    public boolean isRockBorysRecommedation() {
        return rockBorysRecommedation;
    }

    public void setRockBorysRecommedation(boolean rockBorysRecommedation) {
        this.rockBorysRecommedation = rockBorysRecommedation;
    }

    public boolean isOtherRecommedation() {
        return otherRecommedation;
    }

    public void setOtherRecommedation(boolean otherRecommedation) {
        this.otherRecommedation = otherRecommedation;
    }
}

