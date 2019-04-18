package com.example.i01002706.vokabelapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CardDao {
    @Insert
    public void insert(Card card);

    @Query("SELECT * FROM Card WHERE cardset_id = :cardsetId")
    public List<Card> allCards(int cardsetId);

    @Query("DELETE FROM Card WHERE cardset_id = :cardsetId")
    public void deleteQuery(int cardsetId);

    @Delete
    public void delete(Card card);
}
