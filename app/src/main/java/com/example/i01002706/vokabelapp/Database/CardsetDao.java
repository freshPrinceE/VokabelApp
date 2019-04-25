package com.example.i01002706.vokabelapp.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.i01002706.vokabelapp.Database.Cardset;

import java.util.List;

@Dao
public interface CardsetDao {
    @Insert
    public long insert(Cardset cardset);

    @Query("SELECT * FROM Cardset WHERE category_id = :categoryId")
    public List<Cardset> allCardsets(int categoryId);

    @Query("DELETE FROM Cardset WHERE category_id = :categoryId")
    public void deleteQuery (int categoryId);

    @Delete
    void delete(Cardset cardset);
}
