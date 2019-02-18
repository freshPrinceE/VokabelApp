package com.example.i01002706.vokabelapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CardsetDao {
    @Insert
    public long insert(Cardset cardset);

    @Query("SELECT * FROM Cardset WHERE category_id = :categoryId")
    public List<Cardset> allCardsets(int categoryId);
}
