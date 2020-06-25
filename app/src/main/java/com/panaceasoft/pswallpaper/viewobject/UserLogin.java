package com.panaceasoft.pswallpaper.viewobject;


import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
/**
 * Created by Panacea-Soft on 12/12/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


@Entity(primaryKeys = "user_id")
public class UserLogin {

    @NonNull
    public final String user_id;

    public final Boolean login;

    @Embedded(prefix = "user_")
    public final User user;

    public UserLogin(@NonNull String user_id, Boolean login, User user) {
        this.user_id = user_id;
        this.login = login;
        this.user = user;
    }
}
