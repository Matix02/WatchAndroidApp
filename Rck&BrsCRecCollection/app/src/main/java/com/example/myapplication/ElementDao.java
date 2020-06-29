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

    //Pierwsze trzy to jako argument były RoomElementy
    @Insert
    public long addElement(Element element);

    @Update
    public void updateElemet(Element element);

    @Delete
    public void deleteElement(Element element);

    @Query("select * from Element")
    public List<Element> getElements();

    //Przydałoby się zmienić na z tym id, bo nie występuje w samej klasie Element
    //Poprawić to Query, by dalej wyszukiwało informacje
   // @Query("select * from Element where id ==:elementId")
    public Element getElement(long elementId);
}
