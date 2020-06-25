package com.panaceasoft.pswallpaper.viewobject;


import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

@Entity(primaryKeys = "cityId")
public class SearchLog {

    @NonNull
    public final String cityId;

    public final String cityName;

    public final String hotelName;

    public final String stars;

    public final Integer minPrice;

    public final Integer maxPrice;

    public final Integer rating;

    public SearchLog(@NonNull String cityId, String cityName, String hotelName, String stars, Integer minPrice, Integer maxPrice, Integer rating) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.hotelName = hotelName;
        this.stars = stars;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.rating = rating;
    }
}