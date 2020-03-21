package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface MyDao {

    @Insert
    public void addMaterial(UseElement useElement);

}
