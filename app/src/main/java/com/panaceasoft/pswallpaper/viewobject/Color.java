package com.panaceasoft.pswallpaper.viewobject;


import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = "id" )
public class Color {

    @SerializedName("id")
    @NonNull
    public final String id;

    @SerializedName("name")
    public final String name;

    @SerializedName("code")
    public final String code;

    @SerializedName("added_date")
    public final String added_date;

    @SerializedName("addedUserId")
    public final String added_user_id;

    @SerializedName("updated_date")
    public final String updated_date;

    @SerializedName("updated_user_id")
    public final String updated_user_id;

    @SerializedName("added_date_str")
    public final String added_date_str;

    public Color(@NonNull String id, String name, String code, String added_date, String added_user_id, String updated_date, String updated_user_id, String added_date_str) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.added_date = added_date;
        this.added_user_id = added_user_id;
        this.updated_date = updated_date;
        this.updated_user_id = updated_user_id;
        this.added_date_str = added_date_str;
    }
}
