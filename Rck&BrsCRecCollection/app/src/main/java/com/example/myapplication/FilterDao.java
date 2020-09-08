package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface FilterDao {
    //  Interfejst do filtracji
    @Insert
    long addFilter(ElementFilter elementFilter);

    @Update
    void updateFilter(ElementFilter elementFilter);

    @Delete
    void deleteFilter(Element element);

    @Query("select * from ElementFilter LIMIT 1")
    List<ElementFilter> getFilters();

    @Query("select * from ElementFilter")
    Flowable<List<ElementFilter>> getFiltersFlow();

    @Query("select * from ElementFilter LIMIT 1")
    List<ElementFilter> getOneFilter();

    @Query("SELECT * \n" +
            "FROM ElementFilter \n" +
            "LIMIT 1")
    Single<ElementFilter> getSingleFilters();
}
