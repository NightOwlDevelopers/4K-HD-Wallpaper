package com.panaceasoft.pswallpaper.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = "id")
public class WallpaperMap {

    @NonNull
    public final String id;

    public final String mapKey;

    public final String wallpaperId;

    public final int sorting;

    public final String addedDate;

    public WallpaperMap(@NonNull String id, String mapKey, String wallpaperId, int sorting, String addedDate) {
        this.id = id;
        this.mapKey = mapKey;
        this.wallpaperId = wallpaperId;
        this.sorting = sorting;
        this.addedDate = addedDate;
    }
}



