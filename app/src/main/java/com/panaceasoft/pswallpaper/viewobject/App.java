package com.panaceasoft.pswallpaper.viewobject;

import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class App {

    @SerializedName("app_id")
    @NonNull
    @PrimaryKey
    public final String appId;

    @SerializedName("app_home")
    public final String appHome;

    @SerializedName("app_grid")
    public final String appGrid;

    @SerializedName("app_detail")
    public final String appDetail;

    @SerializedName("added_date")
    public final String addedDate;

    @SerializedName("added_date_str")
    public final String addedDateStr;

    public App(@NonNull String appId, String appHome, String appGrid, String appDetail, String addedDate, String addedDateStr) {
        this.appId = appId;
        this.appHome = appHome;
        this.appGrid = appGrid;
        this.appDetail = appDetail;
        this.addedDate = addedDate;
        this.addedDateStr = addedDateStr;
    }
}
