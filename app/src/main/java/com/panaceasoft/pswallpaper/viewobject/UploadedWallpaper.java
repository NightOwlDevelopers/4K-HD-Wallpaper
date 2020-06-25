package com.panaceasoft.pswallpaper.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UploadedWallpaper {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    public final String id;

    public final int sorting;

    public final String added_date;

    public UploadedWallpaper(@NonNull String id, int sorting, String added_date) {
        this.id = id;
        this.sorting = sorting;
        this.added_date = added_date;
    }
}
