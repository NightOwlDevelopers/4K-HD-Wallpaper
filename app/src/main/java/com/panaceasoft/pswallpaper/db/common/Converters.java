package com.panaceasoft.pswallpaper.db.common;


import java.util.Date;

import androidx.room.TypeConverter;

/**
 * Created by Panacea-Soft on 12/27/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
