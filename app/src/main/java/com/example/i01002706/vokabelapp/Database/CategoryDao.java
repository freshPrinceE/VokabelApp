package com.example.i01002706.vokabelapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.i01002706.vokabelapp.Database.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    public void insert(Category category);

    @Query("SELECT * FROM category")
    public LiveData<List<Category>> allCategories();

    @Delete
    void delete(Category category);
}
