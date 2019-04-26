package com.example.i01002706.vokabelapp.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Category.class, Cardset.class, Card.class}, version = 12)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();
    public abstract CardsetDao cardsetDao();
    public abstract CardDao cardDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null)    {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class, "database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sOnOpenCallback)
                            .allowMainThreadQueries()
                            .build();
                }}}
        return INSTANCE;
    }
    private static RoomDatabase.Callback sOnOpenCallback =
            new RoomDatabase.Callback(){
            };



}
