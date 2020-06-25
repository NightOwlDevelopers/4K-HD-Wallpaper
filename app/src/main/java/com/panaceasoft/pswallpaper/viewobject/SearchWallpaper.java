package com.panaceasoft.pswallpaper.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class SearchWallpaper {
    @NonNull
    @PrimaryKey
    @SerializedName("id")
    public final String id;

    public SearchWallpaper(@NonNull String id) {
        this.id = id;
    }
}
