package com.panaceasoft.pswallpaper.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = "date")
public class DailyPoint {

    @SerializedName("date")
    @NonNull
    public final String date;

    @SerializedName("claimed_status")
    public final String claimed_status;

    @SerializedName("user_id")
    public final String user_id;

    public DailyPoint(String date, String claimed_status, String user_id) {
        this.date = date;
        this.claimed_status = claimed_status;
        this.user_id = user_id;
    }
}
