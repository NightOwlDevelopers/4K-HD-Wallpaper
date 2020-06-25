package com.panaceasoft.pswallpaper.db;

import com.panaceasoft.pswallpaper.viewobject.UploadedWallpaper;
import com.panaceasoft.pswallpaper.viewobject.Wallpaper;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UploadWallpaperDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(UploadedWallpaper uploadedWallpaper);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(UploadedWallpaper uploadedWallpaper);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAllUploadedWallpapers(List<UploadedWallpaper> uploadedWallpaperList);

    @Query("DELETE FROM UploadedWallpaper")
    public abstract void deleteUploadedWallpapers();

    @Query("DELETE FROM UploadedWallpaper where id = :wallpaperId")
    public abstract void deleteUploadedWallpaperByWallpaperId(String wallpaperId);

    @Query("SELECT max(sorting) FROM UploadedWallpaper")
    public abstract int getLastUploadSorting();

//    @Query("SELECT * FROM Wallpaper WHERE wallpaper_id IN (SELECT id FROM UploadedWallpaper order by sorting)")
    @Query("SELECT w.* FROM UploadedWallpaper uw, Wallpaper w WHERE uw.id = w.wallpaper_id order by uw.sorting asc, uw.added_date desc")
    public abstract LiveData<List<Wallpaper>> getAllUploadedWallpapers();
}
