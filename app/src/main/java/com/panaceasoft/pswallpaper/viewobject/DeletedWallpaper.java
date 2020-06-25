package com.panaceasoft.pswallpaper.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = "id")
public class DeletedWallpaper {

    @SerializedName("id")
    @NonNull
    public final String id;

    @SerializedName("wallpaper_id")
    public final String wallpaper_id;

    @SerializedName("deleted_date")
    public final String deleted_date;

    public DeletedWallpaper(@NonNull String id, String wallpaper_id, String deleted_date) {
        this.id = id;
        this.wallpaper_id = wallpaper_id;
        this.deleted_date = deleted_date;
    }
}
