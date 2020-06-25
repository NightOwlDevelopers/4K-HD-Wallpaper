package com.panaceasoft.pswallpaper.db;


import com.panaceasoft.pswallpaper.viewobject.Image;
import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
/**
 * Created by Panacea-Soft on 12/8/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Dao
public interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Image image);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Image> imageList);

    @Query("SELECT * FROM Image")
    LiveData<List<Image>> getAll();

    @Query("SELECT * FROM Image WHERE img_parent_id = :news_id")
    LiveData<List<Image>> getByNewsId(String news_id);

    @Query("DELETE FROM Image WHERE img_parent_id = :news_id")
    void deleteById(String news_id);

    @Query("DELETE FROM Image")
    void deleteTable();

}
