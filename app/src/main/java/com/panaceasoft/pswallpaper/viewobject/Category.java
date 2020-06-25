package com.panaceasoft.pswallpaper.viewobject;


import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
/**
 * Created by Panacea-Soft on 11/25/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Entity(primaryKeys = "cat_id")
public class Category{

    @SerializedName("cat_id")
    @NonNull
    public final String cat_id;

    @SerializedName("cat_name")
    public final String cat_name;

    @SerializedName("cat_ordering")
    public final String cat_ordering;

    @SerializedName("cat_is_published")
    public final String cat_is_published;

    @SerializedName("added_date")
    public final String added_date;

    @SerializedName("added_date_str")
    public final String added_date_str;

    @SerializedName("image_count")
    public final String image_count;

    @Embedded
    @SerializedName("default_photo")
    public final Image default_photo;

    public Category(@NonNull String cat_id, String cat_name, String cat_ordering, String cat_is_published, String added_date, String added_date_str, String image_count, Image default_photo) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.cat_ordering = cat_ordering;
        this.cat_is_published = cat_is_published;
        this.added_date = added_date;
        this.added_date_str = added_date_str;
        this.image_count = image_count;
        this.default_photo = default_photo;
    }
}
