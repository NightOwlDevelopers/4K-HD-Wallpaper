package com.panaceasoft.pswallpaper.db;

import android.database.Cursor;

import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.utils.Constants;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewobject.UploadedWallpaper;
import com.panaceasoft.pswallpaper.viewobject.Wallpaper;
import com.panaceasoft.pswallpaper.viewobject.holder.WallpaperParamsHolder;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
public abstract class WallpaperDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Wallpaper wallpaper);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<Wallpaper> wallpaperList);

    @Query("SELECT * FROM Wallpaper")
    public abstract LiveData<List<Wallpaper>> getAll();

    @Query("DELETE FROM Wallpaper WHERE wallpaper_id = :id")
    public abstract void deleteById(String id);

    @Query("DELETE FROM Wallpaper")
    public abstract void deleteTable();

    @Query("DELETE FROM Wallpaper WHERE wallpaper_id Not In(SELECT id FROM TrendingWallpaper)")
    public abstract void deleteTableById();

    @Query("SELECT * FROM Wallpaper order by added_date DESC LIMIT :limit OFFSET :offset")
    public abstract LiveData<List<Wallpaper>> getByLimit(String limit, String offset);

    @Query("SELECT * FROM Wallpaper order by added_date DESC")
    public abstract LiveData<List<Wallpaper>> get();

    @Query("SELECT * FROM Wallpaper WHERE wallpaper_id = :id")
    public abstract LiveData<Wallpaper> getWallpaperById(String id);

    @Query("SELECT * FROM Wallpaper WHERE wallpaper_id = :id")
    public abstract Wallpaper getWallpaperObjectById(String id);

    @Query("SELECT * FROM Wallpaper WHERE cat_id = :id")
    public abstract LiveData<List<Wallpaper>> getAllWallpaperByIdList(String id);

    @Query("SELECT is_favourited FROM Wallpaper WHERE wallpaper_id =:wallpaperId")
    public abstract String selectFavouriteById(String wallpaperId);

    @Query("UPDATE Wallpaper SET is_favourited =:is_favourited WHERE wallpaper_id =:wallpaper_id")
    public abstract void updateProductForFavById(String is_favourited, String wallpaper_id);

    @Query("UPDATE Wallpaper SET rating_count =:rating WHERE wallpaper_id =:wallpaper_id")
    public abstract void updateRatingById(float rating, String wallpaper_id);

    @Query("SELECT rating_count FROM Wallpaper WHERE wallpaper_id =:wallpaperId")
    public abstract float selectRatingById(String wallpaperId);

    @Query("SELECT is_buy FROM Wallpaper WHERE wallpaper_id = :wallpaperId")
    public abstract String getBuyingStatusById(String wallpaperId);

    @Query("UPDATE WALLPAPER SET is_buy = :is_buy WHERE wallpaper_id = :wallpaperId")
    public abstract void updateBuyingStatusById(String wallpaperId, String is_buy);

    @Query("DELETE FROM Wallpaper where wallpaper_id = :wallpaperId")
    public abstract void deleteFavouriteProductByProductId(String wallpaperId);

    @Query("SELECT * FROM Wallpaper where wallpaper_id = :wallpaperId")
    public abstract Cursor getWallpaperCursorById(String wallpaperId);

    @Query("select count(*) from Wallpaper")
    public abstract Integer getTotalRowCount();

    @Query("DELETE from Wallpaper WHERE Wallpaper.wallpaper_id IN (SELECT Wallpaper.wallpaper_id FROM Wallpaper order by Wallpaper.added_date asc LIMIT :count)")
    public abstract void deleteExtraRowCount(String count);

    @RawQuery(observedEntities = Wallpaper.class)
    public abstract LiveData<List<Wallpaper>> getAllWallpaperListData(SupportSQLiteQuery query);

    @Query("SELECT wallpaper.* FROM Wallpaper wallpaper, WallpaperMap wallpaperMap WHERE wallpaper.wallpaper_id = wallpaperMap.wallpaperId AND wallpaperMap.mapKey = :value ORDER BY wallpaperMap.sorting asc")
    public abstract LiveData<List<Wallpaper>> getWallpaperByKey(String value);

    public LiveData<List<Wallpaper>> getAllWallpaperList(WallpaperParamsHolder paramsHolder, String limit, String offset) {

        String start = "";
        String condition = " WHERE ";
        String order = " ORDER BY ";
        String baseSql = "SELECT * FROM Wallpaper ";

        if (paramsHolder.queryTypeFlag.isEmpty()) {

            if (paramsHolder.wallpaperIsPublished != null && (!paramsHolder.wallpaperIsPublished.equals("")) && (!paramsHolder.wallpaperIsPublished.isEmpty())) {
                baseSql += condition + start + " wallpaper_is_published = '1' ";
                start = " AND ";
                condition = "";
            }

            if (paramsHolder.wallpaperName != null && (!paramsHolder.wallpaperName.equals("")) && (!paramsHolder.wallpaperName.isEmpty())) {
                baseSql += condition + start + "wallpaper_name like '%" + paramsHolder.wallpaperName + "%' ";
                start = " AND ";
                condition = "";
            }

            if (paramsHolder.addedUserId != null && (!paramsHolder.addedUserId.equals("")) && !paramsHolder.addedUserId.isEmpty()) {
                baseSql += condition + start + "added_user_id = '" + paramsHolder.addedUserId + "'";
                start = " AND ";
                condition = "";
            }

            if (paramsHolder.catId != null && (!paramsHolder.catId.equals("")) && !paramsHolder.catId.isEmpty()) {
                baseSql += condition + start + "cat_id = '" + paramsHolder.catId + "'";
                start = " AND ";
                condition = "";
            }

            if (paramsHolder.type != null && (!paramsHolder.type.equals("")) && !paramsHolder.type.isEmpty()) {
                if (!paramsHolder.type.equals("3")) {
                    baseSql += condition + start + "types = '" + paramsHolder.type + "'";
                    start = " AND ";
                    condition = "";
                }
            }

            if (paramsHolder.isRecommended != null && (!paramsHolder.isRecommended.equals("")) && !paramsHolder.isRecommended.isEmpty()) {
                baseSql += condition + start + "is_recommended ='" + paramsHolder.isRecommended + "' ";
                start = " AND ";
                condition = "";
            }


            // Type Checking
            String typeSql = start + " ( ";
            boolean hasType = false;
            boolean hasStart = false;
            if (!start.equals("")) {
                hasStart = true;
            }
            if (!condition.equals("")) {
                typeSql = condition + start + " ( ";
                start = "";
            } else {
                start = "";
            }

            if (paramsHolder.isPortrait != null && (!paramsHolder.isPortrait.equals("")) && !paramsHolder.isPortrait.isEmpty()) {
                hasType = true;
                typeSql += start + "is_portrait ='" + paramsHolder.isPortrait + "' ";
                start = " OR ";
                condition = "";
            }

            if (paramsHolder.isLandscape != null && (!paramsHolder.isLandscape.equals("")) && !paramsHolder.isLandscape.isEmpty()) {
                hasType = true;
                typeSql += start + "is_landscape ='" + paramsHolder.isLandscape + "' ";
                start = " OR ";
                condition = "";
            }

            if (paramsHolder.isSquare != null && (!paramsHolder.isSquare.equals("")) && !paramsHolder.isSquare.isEmpty()) {
                hasType = true;
                typeSql += start + "is_square = '" + paramsHolder.isSquare + "'";
                start = " AND ";
                condition = "";
            }

            if (hasType) {
                baseSql += typeSql + ") ";
                start = " AND ";
            } else {

                if (hasStart) {
                    start = " AND ";
                }
            }
            // End Type Checking

            if (paramsHolder.isSquare != null && (!paramsHolder.colorId.equals("")) && !paramsHolder.colorId.isEmpty()) {
                baseSql += condition + start + "color_id = '" + paramsHolder.colorId + "'";
                start = " AND ";
                condition = "";
            }

            if ((!paramsHolder.rating_min.equals("")) && !paramsHolder.rating_min.isEmpty()) {
                baseSql += condition + start + "rating_count >=" + paramsHolder.rating_min;
                start = " AND ";
                condition = "";
            }

            if ((!paramsHolder.rating_max.equals("")) && !paramsHolder.rating_max.isEmpty()) {
                baseSql += condition + start + "rating_count <=" + paramsHolder.rating_max;
                start = " AND ";
                condition = "";
            }

            if ((!paramsHolder.point_min.equals("")) && !paramsHolder.point_min.isEmpty()) {
                baseSql += condition + start + "point >=" + paramsHolder.point_min;
                start = " AND ";
                condition = "";
            }

            if ((!paramsHolder.point_max.equals("")) && !paramsHolder.point_max.isEmpty()) {
                baseSql += condition + start + "point <=" + paramsHolder.point_max;
            }

            if ((!paramsHolder.orderBy.equals("")) && !paramsHolder.orderBy.isEmpty()) {
                baseSql += order + paramsHolder.orderBy + " ";
            }

            if ((!paramsHolder.orderType.equals("")) && !paramsHolder.orderType.isEmpty()) {
                baseSql += paramsHolder.orderType;
            }

            if ((!limit.equals("")) && (!offset.equals("")) && (!limit.isEmpty()) && (!offset.isEmpty())) {

                baseSql += " LIMIT " + limit + " OFFSET " + offset;

            }

        } else {

            switch (paramsHolder.queryTypeFlag) {

                case Constants.DONWLOADQUERY:

                    baseSql = "SELECT w.* FROM DOWNLOADEDWALLPAPER dw, WALLPAPER w WHERE dw.ID = w.WALLPAPER_ID ORDER BY dw.sorting asc";

                    break;

                case Constants.FAVQUERY:

                    baseSql = "SELECT w.* FROM FAVORITEWALLPAPER fw, WALLPAPER w WHERE fw.ID = w.WALLPAPER_ID ORDER BY fw.sorting asc";

                    break;
            }

        }

        Utils.psLog("The query is " + baseSql);

        return getAllWallpaperListData(new SimpleSQLiteQuery(baseSql));
    }

}
