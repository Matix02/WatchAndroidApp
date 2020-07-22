package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Element.class}, version = 3)
public abstract class RoomDatabaseHelper extends RoomDatabase {

    public abstract ElementDao getElementDao();

    static Migration migration = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'Element' ADD COLUMN 'recom' TEXT DEFAULT ''");
        }
    };
    public static final Migration FILTERMIGRATION = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };
}
