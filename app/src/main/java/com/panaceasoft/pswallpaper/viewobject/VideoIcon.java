package com.panaceasoft.pswallpaper.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "img_id")
public class VideoIcon {

    @SerializedName("img_id")
    @NonNull
    public final String img_id;

    @SerializedName("img_parent_id")
    public final String img_parent_id;

    @SerializedName("img_type")
    public final String img_type;

    @SerializedName("img_path")
    public final String img_path;

    @SerializedName("img_width")
    public final String img_width;

    @SerializedName("img_height")
    public final String img_height;

    @SerializedName("img_desc")
    public final String img_desc;

    @SerializedName("image_path_thumb")
    public final String image_path_thumb;

    @SerializedName("size")
    public final String size;

    public VideoIcon(@NonNull String img_id, String img_parent_id, String img_type, String img_path, String img_width, String img_height, String img_desc, String image_path_thumb, String size) {
        this.img_id = img_id;
        this.img_parent_id = img_parent_id;
        this.img_type = img_type;
        this.img_path = img_path;
        this.img_width = img_width;
        this.img_height = img_height;
        this.img_desc = img_desc;
        this.image_path_thumb = image_path_thumb;
        this.size = size;
    }
}
