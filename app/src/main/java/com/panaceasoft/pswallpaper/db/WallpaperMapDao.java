package com.panaceasoft.pswallpaper.db;

import com.panaceasoft.pswallpaper.viewobject.WallpaperMap;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface WallpaperMapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WallpaperMap wallpaperMap);

    @Query("DELETE FROM WallpaperMap WHERE mapKey = :key")
    void deleteByMapKey(String key);

    @Query("DELETE FROM WallpaperMap WHERE mapKey = :key AND wallpaperId = :id")
    void deleteSpecificItemByMapKeyAndId(String key, String id);

    @Query("SELECT max(sorting) from WallpaperMap WHERE mapKey = :value ")
    int getMaxSortingByValue(String value);

    @Query("SELECT * FROM WallpaperMap WHERE mapKey = :key Order by sorting")
    List<WallpaperMap> getAll(String key);

    @Query("DELETE FROM WallpaperMap")
    void deleteAll();

}
