package com.panaceasoft.pswallpaper.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.panaceasoft.pswallpaper.db.common.Converters;
import com.panaceasoft.pswallpaper.viewobject.AboutUs;
import com.panaceasoft.pswallpaper.viewobject.App;
import com.panaceasoft.pswallpaper.viewobject.Category;
import com.panaceasoft.pswallpaper.viewobject.Color;
import com.panaceasoft.pswallpaper.viewobject.DailyPoint;
import com.panaceasoft.pswallpaper.viewobject.DeletedObject;
import com.panaceasoft.pswallpaper.viewobject.Image;
import com.panaceasoft.pswallpaper.viewobject.PSAppInfo;
import com.panaceasoft.pswallpaper.viewobject.PSAppVersion;
import com.panaceasoft.pswallpaper.viewobject.SearchLog;
import com.panaceasoft.pswallpaper.viewobject.SearchWallpaper;
import com.panaceasoft.pswallpaper.viewobject.TrendingWallpaper;
import com.panaceasoft.pswallpaper.viewobject.UploadedWallpaper;
import com.panaceasoft.pswallpaper.viewobject.User;
import com.panaceasoft.pswallpaper.viewobject.UserLogin;
import com.panaceasoft.pswallpaper.viewobject.Video;
import com.panaceasoft.pswallpaper.viewobject.VideoIcon;
import com.panaceasoft.pswallpaper.viewobject.Wallpaper;
import com.panaceasoft.pswallpaper.viewobject.WallpaperMap;


/**
 * Created by Panacea-Soft on 11/20/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Database(entities = {
        Image.class,
        Category.class,
        User.class,
        UserLogin.class,
        AboutUs.class,
        SearchLog.class,
        Wallpaper.class,
        App.class,
        TrendingWallpaper.class,
        SearchWallpaper.class,
        UploadedWallpaper.class,
        Color.class,
        DailyPoint.class,
        WallpaperMap.class,
        PSAppVersion.class,
        PSAppInfo.class,
        DeletedObject.class,
        Video.class,
        VideoIcon.class

}, version = 7, exportSchema = false)

//******************
// DB Version Log
//******************
// V2.8 => DB Ver 7
// V2.6 => DB Ver 7
// V2.5 => DB Ver 7
// V2.4 => DB Ver 7
// V2.3 => DB Ver 6
// V2.2 => DB Ver 5
// V1.6 => DB Ver 5
// V1.5 => DB Ver 4
// V1.4 => DB Ver 4
// V1.3 => DB Ver 3
// V1.2 => DB Ver 3
// V1.1 => DB Ver 2
// V1.0 => DB Ver 1

@TypeConverters({Converters.class})

public abstract class PSCoreDb extends RoomDatabase {

    abstract public UserDao userDao();

    abstract public AboutUsDao aboutUsDao();

    abstract public ImageDao imageDao();

    abstract public WallpaperDao wallpaperDao();

    abstract public CategoryDao categoryDao();

    abstract public ColorDao colorDao();

    abstract public PointDao pointDao();

    abstract public WallpaperMapDao wallpaperMapDao();

    abstract public PSAppInfoDao psAppInfoDao();

    abstract public PSAppVersionDao psAppVersionDao();

    abstract public DeletedObjectDao deletedObjectDao();

    abstract public UploadWallpaperDao uploadWallpaperDao();


    /**
     * Migrate from:
     * version 1 - using Room
     * to
     * version 2 - using Room where the {@link } has an extra field: added_date_str
     */
//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE news "
//                    + " ADD COLUMN added_date_str INTEGER NOT NULL DEFAULT 0");
//        }
//    };

    /* More migration write here */
}