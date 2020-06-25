package com.panaceasoft.pswallpaper.viewobject;


import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * Created by Panacea-Soft on 12/6/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Entity(primaryKeys = "user_id")
public class User {

    @SerializedName("user_id")
    @NonNull
    public final String user_id;

    @SerializedName("user_is_sys_admin")
    public final String user_is_sys_admin;

    @SerializedName("facebook_id")
    public final String facebook_id;

    @SerializedName("google_id")
    public final String google_id;

    @SerializedName("user_name")
    public String user_name;

    @SerializedName("user_email")
    public String user_email;

    @SerializedName("user_phone")
    public String user_phone;

    @SerializedName("user_password")
    public String user_password;

    @SerializedName("user_about_me")
    public String user_about_me;

    @SerializedName("user_cover_photo")
    public final String user_cover_photo;

    @SerializedName("user_profile_photo")
    public final String user_profile_photo;

    @SerializedName("added_date")
    public final String added_date;

    @SerializedName("like_count")
    public final String like_count;

    @SerializedName("comment_count")
    public final String comment_count;

    @SerializedName("favourite_count")
    public final String favourite_count;

    @SerializedName("total_point")
    public final String total_point;

    public User(@NonNull String user_id, String user_is_sys_admin, String facebook_id, String google_id, String user_name, String user_email, String user_phone, String user_password, String user_about_me, String user_cover_photo, String user_profile_photo, String added_date, String like_count, String comment_count, String favourite_count, String total_point) {
        this.user_id = user_id;
        this.user_is_sys_admin = user_is_sys_admin;
        this.facebook_id = facebook_id;
        this.google_id = google_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.user_password = user_password;
        this.user_about_me = user_about_me;
        this.user_cover_photo = user_cover_photo;
        this.user_profile_photo = user_profile_photo;
        this.added_date = added_date;
        this.like_count = like_count;
        this.comment_count = comment_count;
        this.favourite_count = favourite_count;
        this.total_point = total_point;
    }
}
