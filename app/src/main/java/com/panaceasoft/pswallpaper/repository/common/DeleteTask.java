package com.panaceasoft.pswallpaper.repository.common;

import com.panaceasoft.pswallpaper.api.PSApiService;
import com.panaceasoft.pswallpaper.db.PSCoreDb;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewobject.User;
import com.panaceasoft.pswallpaper.viewobject.common.Resource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.BindingAdapter;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;


/**
 * General Delete Task Sample
 * Created by Panacea-Soft on 12/14/17.
 * Contact Email : teamps.is.cool@gmail.com
 */
public class DeleteTask implements Runnable {


    //region Variables

    private final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

    private final PSApiService service;
    private final PSCoreDb db;
    private final Object obj;

    //endregion


    //region Constructor

    public DeleteTask(PSApiService service, PSCoreDb db, Object obj) {
        this.service = service;
        this.db = db;
        this.obj = obj;
    }

    //endregion


    //region Override Methods

    @Override
    public void run() {
        try {
//            try {
//                db.beginTransaction();
//
//                if (obj instanceof User) {
//                    db.userDao().deleteUserLogin();
//
//                    db.setTransactionSuccessful();
//                }
//
//            } finally {
//                db.endTransaction();
//            }

            try {
                db.runInTransaction(() -> {
                    if (obj instanceof User) {
                        db.userDao().deleteUserLogin();

                    }
                });
            } catch (Exception ex) {
                Utils.psErrorLog("Error at ", ex);
            }

            statusLiveData.postValue(Resource.success(true));
        } catch (Exception e) {
            statusLiveData.postValue(Resource.error(e.getMessage(), true));
        }
    }

    //endregion


    //region public SyncCategory Methods

    /**
     * This function will return Status of Process
     *
     * @return statusLiveData
     */
    public LiveData<Resource<Boolean>> getStatusLiveData() {
        return statusLiveData;
    }

    //endregion
}