package com.panaceasoft.pswallpaper.viewobject;


import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

/**
 * Created by Panacea-Soft on 12/29/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Entity(primaryKeys = "about_id")
public class AboutUs {

    @SerializedName("about_id")
    @NonNull
    public final String about_id;

    @SerializedName("about_title")
    public final String about_title;

    @SerializedName("about_description")
    public final String about_description;

    @SerializedName("about_email")
    public final String about_email;

    @SerializedName("about_phone")
    public final String about_phone;

    @SerializedName("about_website")
    public final String about_website;

    @SerializedName("facebook")
    public final String facebook;

    @SerializedName("google_plus")
    public final String google_plus;

    @SerializedName("instagram")
    public final String instagram;

    @SerializedName("youtube")
    public final String youtube;

    @SerializedName("pinterest")
    public final String pinterest;

    @SerializedName("twitter")
    public final String twitter;

    @SerializedName("privacypolicy")
    public final String privacypolicy;

    @Embedded
    @SerializedName("default_photo")
    public final Image default_photo;

    public AboutUs(@NonNull String about_id, String about_title, String about_description, String about_email, String about_phone, String about_website, String facebook, String google_plus, String instagram, String youtube, String pinterest, String twitter,String privacypolicy, Image default_photo) {
        this.about_id = about_id;
        this.about_title = about_title;
        this.about_description = about_description;
        this.about_email = about_email;
        this.about_phone = about_phone;
        this.about_website = about_website;
        this.facebook = facebook;
        this.google_plus = google_plus;
        this.instagram = instagram;
        this.youtube = youtube;
        this.pinterest = pinterest;
        this.twitter = twitter;
        this.privacypolicy = privacypolicy;
        this.default_photo = default_photo;
    }
}
