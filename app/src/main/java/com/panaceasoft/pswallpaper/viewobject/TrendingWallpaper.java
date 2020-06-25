package com.panaceasoft.pswallpaper.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TrendingWallpaper {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    public final String id;

    public TrendingWallpaper(@NonNull String id) {
        this.id = id;
    }
}
