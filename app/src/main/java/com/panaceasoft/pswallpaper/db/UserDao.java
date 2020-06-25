package com.panaceasoft.pswallpaper.db;

import com.panaceasoft.pswallpaper.viewobject.User;
import com.panaceasoft.pswallpaper.viewobject.UserLogin;
import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Created by Panacea-Soft on 12/6/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> userList);

    @Query("SELECT * FROM User order by added_date desc ")
    LiveData<List<User>> getAll();

    @Query("SELECT * FROM User WHERE user_id = :user_id order by added_date desc ")
    LiveData<User> getUserData(String user_id);

    @Query("SELECT * FROM User WHERE user_id = :user_id order by added_date desc ")
    User getUserRawData(String user_id);

    @Query("SELECT * FROM User order by added_date desc LIMIT :limit")
    LiveData<List<User>> getWithLimit(String limit);

    @Query("SELECT * FROM User WHERE user_id = :user_id order by added_date desc ")
    LiveData<User> findById(String user_id);

    @Query("SELECT total_point FROM USER WHERE user_id = :user_id")
    String getPointByUserId(String user_id);

    @Query("SELECT * FROM USER WHERE user_id = :user_id")
    LiveData<User> getPointByUserIdLiveData(String user_id);

    @Query("UPDATE User SET total_point = :total_point WHERE user_id = :user_id ")
    void updatePointByUserId(String total_point, String user_id);

    @Query("DELETE FROM User WHERE user_id = :user_id")
    void deleteById(String user_id);

    @Query("DELETE FROM User")
    void deleteTable();


    //region User Login Related

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserLogin userLogin);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(UserLogin userLogin);

    @Query("SELECT * FROM UserLogin WHERE user_id = :user_id")
    LiveData<UserLogin> getUserLoginData(String user_id);

    @Query("SELECT * FROM UserLogin")
    LiveData<List<UserLogin>> getUserLoginData();

    @Query("DELETE FROM UserLogin")
    void deleteUserLogin();

    //endregion
}
