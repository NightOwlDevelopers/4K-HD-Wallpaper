package com.panaceasoft.pswallpaper.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.api.PSApiService;
import com.panaceasoft.pswallpaper.db.AboutUsDao;
import com.panaceasoft.pswallpaper.db.CategoryDao;
import com.panaceasoft.pswallpaper.db.ColorDao;
import com.panaceasoft.pswallpaper.db.DeletedObjectDao;
import com.panaceasoft.pswallpaper.db.ImageDao;
import com.panaceasoft.pswallpaper.db.PSAppInfoDao;
import com.panaceasoft.pswallpaper.db.PSAppVersionDao;
import com.panaceasoft.pswallpaper.db.PSCoreDb;
import com.panaceasoft.pswallpaper.db.PointDao;
import com.panaceasoft.pswallpaper.db.UploadWallpaperDao;
import com.panaceasoft.pswallpaper.db.UserDao;
import com.panaceasoft.pswallpaper.db.WallpaperDao;
import com.panaceasoft.pswallpaper.db.WallpaperMapDao;
import com.panaceasoft.pswallpaper.utils.Connectivity;
import com.panaceasoft.pswallpaper.utils.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Panacea-Soft on 11/15/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Module(includes = ViewModelModule.class)
class AppModule {

    @Singleton
    @Provides
    PSApiService providePSNewsService() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(Config.APP_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(PSApiService.class);

    }

    @Singleton
    @Provides
    PSCoreDb provideDb(Application app) {
        return Room.databaseBuilder(app, PSCoreDb.class, "pswallpapers.db")
                //.addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    Connectivity provideConnectivity(Application app) {
        return new Connectivity(app);
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(Application app) {
        return PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
    }

    @Singleton
    @Provides
    UserDao provideUserDao(PSCoreDb db) {
        return db.userDao();
    }

    @Singleton
    @Provides
    AboutUsDao provideAboutUsDao(PSCoreDb db) {
        return db.aboutUsDao();
    }

    @Singleton
    @Provides
    ImageDao provideImageDao(PSCoreDb db) {
        return db.imageDao();
    }

    @Singleton
    @Provides
    WallpaperDao provideWallpaperDao(PSCoreDb db) {
        return db.wallpaperDao();
    }

    @Singleton
    @Provides
    CategoryDao provideCategoryDao(PSCoreDb db) {
        return db.categoryDao();
    }

    @Singleton
    @Provides
    ColorDao provideColorDao(PSCoreDb db) {
        return db.colorDao();
    }

    @Singleton
    @Provides
    PointDao providePointDao(PSCoreDb db) {
        return db.pointDao();
    }

    @Singleton
    @Provides
    WallpaperMapDao provideWallpaperMapDao(PSCoreDb db) {
        return db.wallpaperMapDao();
    }

    @Singleton
    @Provides
    DeletedObjectDao provideDeletedObjectDao(PSCoreDb db) {
        return db.deletedObjectDao();
    }

    @Singleton
    @Provides
    PSAppVersionDao providePSAppVersionDao(PSCoreDb db) {
        return db.psAppVersionDao();
    }

    @Singleton
    @Provides
    PSAppInfoDao providePSAppInfoDao(PSCoreDb db) {
        return db.psAppInfoDao();
    }

    @Singleton
    @Provides
    UploadWallpaperDao provideUploadWallpaperDao(PSCoreDb db) {
        return db.uploadWallpaperDao();
    }

}
