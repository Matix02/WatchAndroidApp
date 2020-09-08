package com.example.myapplication;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.Flowable;
import io.reactivex.Single;

/*
Zastosować opcję z Medium.com, gdzie są podane zasady optymalizacji Room'a.
Unikanie kopiowanych metod jak i przykład z dokumentacji Androida - paging
Ad.Kopiowanie metod, to gdzie jest zwróć całą listę jest tym samym - skrócić, nie powielać.
 */
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

    @Query("UPDATE Element\n" +
            "SET " +
            "title =:title,\n" +
            "recom =:recommendation,\n" +
            "category =:cat\n" +
            "WHERE id =:Id")
    void updateElementById(long Id, String title, String recommendation, String cat);

    /*  @Query("select * from Element")
      List<Element> getElements();
  */
    @Query("select * from Element")
    Flowable<List<Element>> getElements();

    @Query("UPDATE Element\n" +
            "SET isWatched =:resultWatch\n" +
            "WHERE id =:Id")
    void updateWatchElementById(long Id, boolean resultWatch);

    @Query("SELECT *\n" +
            "FROM Element\n" +
            "WHERE category =:lookCategory\n")
    List<Element> randomListElement(String lookCategory);

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

    @Query("SELECT * \n" +
            "FROM Element\n" +
            "ORDER BY id\n" +
            "DESC LIMIT 1")
    int getLastIndex();

    @Query("SELECT * \n" +
            "FROM Element \n" +
            "WHERE isWatched = 0\n" +
            "ORDER BY RANDOM()\n" +
            "LIMIT 1")
    Single<Element> getNoWatchedRandomElement();

    @Query("SELECT *\n" +
            "FROM Element\n" +
            "WHERE isWatched = 0\n" +
            "AND category =:categoryName\n" +
            "ORDER BY RANDOM()\n" +
            "LIMIT 1")
    Single<Element> getNoWatchedRandomElementByCategory(String categoryName);



}
