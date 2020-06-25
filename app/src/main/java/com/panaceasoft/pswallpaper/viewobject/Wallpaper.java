package com.panaceasoft.pswallpaper.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "wallpaper_id")
public class Wallpaper {

    @SerializedName("wallpaper_id")
    @NonNull
    public String wallpaper_id;

    @SerializedName("cat_id")
    public String cat_id;

    @SerializedName("color_id")
    public String color_id;

    @SerializedName("wallpaper_name")
    public String wallpaper_name;

    @SerializedName("types")
    public String types;

    @SerializedName("is_recommended")
    public String is_recommended;

    @SerializedName("is_portrait")
    public String is_portrait;

    @SerializedName("is_landscape")
    public String is_landscape;

    @SerializedName("is_square")
    public String is_square;

    @SerializedName("is_gif")
    public String is_gif;

    @SerializedName("is_wallpaper")
    public String is_wallpaper;

    @SerializedName("is_video_wallpaper")
    public String is_video_wallpaper;

    @SerializedName("point")
    public int point;

    @SerializedName("wallpaper_is_published")
    public String wallpaper_is_published;

    @SerializedName("wallpaper_search_tags")
    public String wallpaper_search_tags;

    @SerializedName("added_date")
    public String added_date;

    @SerializedName("added_date_str")
    public String added_date_str;

    @Embedded(prefix = "default_photo")
    @SerializedName("default_photo")
    public Image default_photo;

    @Embedded(prefix = "default_video")
    @SerializedName("default_video")
    public Video default_video;

    @Embedded(prefix = "default_video_icon")
    @SerializedName("default_video_icon")
    public VideoIcon default_video_icon;

    @SerializedName("category")
    @Embedded(prefix = "category")
    public Category category;

    @SerializedName("touch_count")
    public int touch_count;

    @SerializedName("is_favourited")
    public String is_favourited;

    @SerializedName("is_buy")
    public String is_buy;

    @SerializedName("favourite_count")
    public int favourite_count;

    @SerializedName("download_count")
    public int download_count;

    @SerializedName("rating_count")
    public float rating_count;

    @SerializedName("recommended_date")
    public String recommended_date;

    @SerializedName("added_user_id")
    public String added_user_id;

    @SerializedName("credit")
    public String credit;

    @SerializedName("color")
    @Embedded(prefix = "color")
    public Color color;

    public Wallpaper(@NonNull String wallpaper_id, String cat_id, String color_id, String wallpaper_name, String types, String is_recommended, String is_portrait, String is_landscape, String is_square, String is_gif, String is_wallpaper, String is_video_wallpaper, int point, String wallpaper_is_published, String wallpaper_search_tags, String added_date, String added_date_str, Image default_photo, Video default_video, VideoIcon default_video_icon, Category category, int touch_count, String is_favourited, String is_buy, int favourite_count, int download_count, float rating_count, String recommended_date, String added_user_id, String credit, Color color) {
        this.wallpaper_id = wallpaper_id;
        this.cat_id = cat_id;
        this.color_id = color_id;
        this.wallpaper_name = wallpaper_name;
        this.types = types;
        this.is_recommended = is_recommended;
        this.is_portrait = is_portrait;
        this.is_landscape = is_landscape;
        this.is_square = is_square;
        this.is_gif = is_gif;
        this.is_wallpaper = is_wallpaper;
        this.is_video_wallpaper = is_video_wallpaper;
        this.point = point;
        this.wallpaper_is_published = wallpaper_is_published;
        this.wallpaper_search_tags = wallpaper_search_tags;
        this.added_date = added_date;
        this.added_date_str = added_date_str;
        this.default_photo = default_photo;
        this.default_video = default_video;
        this.default_video_icon = default_video_icon;
        this.category = category;
        this.touch_count = touch_count;
        this.is_favourited = is_favourited;
        this.is_buy = is_buy;
        this.favourite_count = favourite_count;
        this.download_count = download_count;
        this.rating_count = rating_count;
        this.recommended_date = recommended_date;
        this.added_user_id = added_user_id;
        this.credit = credit;
        this.color = color;
    }
}
