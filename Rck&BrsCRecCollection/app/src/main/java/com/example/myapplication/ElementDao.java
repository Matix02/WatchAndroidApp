package com.example.myapplication;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.Update;

@Dao
public interface ElementDao {

    @Insert
    public long addElement(RoomElement element);

    @Update
    public void updateElemet(RoomElement element);

    @Delete
    public void deleteElement(RoomElement element);

    @Query("select * from Element")
    public List<Element> getElements();

    @Query("select * from Element where id ==:elementId")
    public Element getElement(long elementId);
}
