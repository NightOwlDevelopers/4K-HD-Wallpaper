package com.panaceasoft.pswallpaper.repository.aboutus;


import android.content.Context;

import com.panaceasoft.pswallpaper.AppExecutors;
import com.panaceasoft.pswallpaper.api.ApiResponse;
import com.panaceasoft.pswallpaper.api.PSApiService;
import com.panaceasoft.pswallpaper.db.AboutUsDao;
import com.panaceasoft.pswallpaper.db.PSCoreDb;
import com.panaceasoft.pswallpaper.repository.common.NetworkBoundResource;
import com.panaceasoft.pswallpaper.repository.common.NotificationTask;
import com.panaceasoft.pswallpaper.repository.common.PSRepository;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewobject.AboutUs;
import com.panaceasoft.pswallpaper.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


/**
 * Created by Panacea-Soft on 12/8/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Singleton
public class AboutUsRepository extends PSRepository {


    //region Variables

    private final AboutUsDao aboutUsDao;

    //endregion


    //region Constructor

    @Inject
    AboutUsRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, AboutUsDao aboutUsDao) {
        super(psApiService, appExecutors, db);

        this.aboutUsDao = aboutUsDao;
    }

    //endregion


    //region About Us Repository Functions for ViewModel

    /**
     * Load About Us
     */
    public LiveData<Resource<AboutUs>> getAboutUs(String apiKey) {

        String functionKey = "getAboutUs";

        return new NetworkBoundResource<AboutUs, List<AboutUs>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<AboutUs> item) {
                Utils.psLog("SaveCallResult of get about us.");

//*                db.beginTransaction();
//                try {
//
//                    aboutUsDao.deleteTable();
//
//                    aboutUsDao.insertAll(item);
//
//                    db.setTransactionSuccessful();
//
//                } catch (Exception e) {
//                    Utils.psErrorLog("Error in inserting about us.", e);
//                } finally {
//                    db.endTransaction();
//                }

                try {
                    db.runInTransaction(() -> {
                        aboutUsDao.deleteTable();

                        aboutUsDao.insertAll(item);

                    });
                } catch (Exception ex) {
                    Utils.psErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable AboutUs data) {
                // API level cache
                //if (connectivity.isConnected())
                //if (data == null || rateLimiter.shouldFetch(functionKey)) return true;
                //return false;

                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<AboutUs> loadFromDb() {
                Utils.psLog("Load AboutUs From DB.");

                return aboutUsDao.get();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<AboutUs>>> createCall() {
                Utils.psLog("Call About us webservice.");
                return psApiService.getAboutUs(apiKey);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of About Us");
                rateLimiter.reset(functionKey);
            }

        }.asLiveData();
    }


    //endregion

    /**
     * Function to call background task to register Notification
     *
     * @param platform Current Platform
     * @return Status of Process
     */
    public LiveData<Resource<Boolean>> registerNotification(Context context, String platform, String token) {
        NotificationTask notificationTask = new NotificationTask(context,
                psApiService, platform, true, token);

        Utils.psLog("Register Notification : News repository.");
        appExecutors.networkIO().execute(notificationTask);

        return notificationTask.getStatusLiveData();
    }


    /**
     * Function to call background task to un-register notification.
     *
     * @param platform Current Platform
     * @return Status of Process
     */
    public LiveData<Resource<Boolean>> unregisterNotification(Context context, String platform, String token) {
        NotificationTask notificationTask = new NotificationTask(context,
                psApiService, platform, false, token);

        Utils.psLog("Unregister Notification : News repository.");
        appExecutors.networkIO().execute(notificationTask);

        return notificationTask.getStatusLiveData();
    }
}
