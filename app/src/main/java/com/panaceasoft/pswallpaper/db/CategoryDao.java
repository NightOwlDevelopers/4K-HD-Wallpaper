package com.panaceasoft.pswallpaper.db;

import com.panaceasoft.pswallpaper.viewobject.Category;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Category> categoriesList);

    @Query("SELECT * FROM Category order by added_date desc ")
    LiveData<List<Category>> getByNewsId();

    @Query("DELETE FROM Category WHERE cat_id = :id")
    void deleteById(String id);

    @Query("DELETE FROM Category")
    void deleteTable();

    @Query("SELECT * FROM Category order by added_date DESC LIMIT :limit OFFSET :offset")
    LiveData<List<Category>> getCategoryListByLimit(String limit,String offset);

    @Query("SELECT * FROM Category order by added_date DESC")
    LiveData<List<Category>> getCategoryList();


}
