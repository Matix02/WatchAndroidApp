package com.example.myapplication;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Element.class}, version = 1)
public abstract class RoomDatabaseHelper extends RoomDatabase {

    public abstract ElementDao getElementDao();

    //Ostatni filmik z rooma, gdzie mainAcitivty jest wykorzystywany. Czas próby, dodanai zawartości bazy

}
