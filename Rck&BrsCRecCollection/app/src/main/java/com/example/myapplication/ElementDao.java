package com.example.myapplication;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ElementDao {

    @Insert
    long addElement(Element element);

    @Update
    void updateElement(Element element);

    @Delete
    void deleteElement(Element element);

    @Query("delete from Element")
    void deleteAllElements();

    @Query("select * from Element")
    List<Element> getElements();

    @Query("UPDATE Element\n" +
            "SET isWatched =:resultWatch\n" +
            "WHERE id =:Id")
    void updateElementById(long Id, boolean resultWatch);
    //Przydałoby się zmienić na z tym id, bo nie występuje w samej klasie Element
    //Poprawić to Query, by dalej wyszukiwało informacje
   // @Query("select * from Element where id ==:elementId")

    //Jeden z możliwych pomysłów - below
   // @Query("select * from Element where title ==:elementId AND category ==:")

    @Query("delete from Element where id ==:elementId")
    void deleteIdElement(int elementId);

    @Query("select * from Element where id ==:elementId")
    Element getElement(long elementId);

    @Query("UPDATE Element\n" +
            "SET id =:actualID\n" +
            "WHERE ID =:oldId ")
    void updateID(int actualID, int oldId);

  //  Interfejst do filtracji
    @Insert
    long addFilter(ElementFilter elementFilter);

    @Update
    void updateFilter(ElementFilter elementFilter);

    @Delete
    void deleteFilter(Element element);

    @Query("select * from ElementFilter")
    List<ElementFilter> getFilters();
}
