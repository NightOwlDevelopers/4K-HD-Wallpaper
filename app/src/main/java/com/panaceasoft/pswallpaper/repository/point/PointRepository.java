package com.panaceasoft.pswallpaper.repository.point;


import android.database.Cursor;

import com.panaceasoft.pswallpaper.AppExecutors;
import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.api.ApiResponse;
import com.panaceasoft.pswallpaper.api.PSApiService;
import com.panaceasoft.pswallpaper.db.PSCoreDb;
import com.panaceasoft.pswallpaper.db.PointDao;
import com.panaceasoft.pswallpaper.repository.common.PSRepository;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewobject.DailyPoint;
import com.panaceasoft.pswallpaper.viewobject.User;
import com.panaceasoft.pswallpaper.viewobject.common.Resource;

import java.io.IOException;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Response;

public class PointRepository extends PSRepository {

    private Cursor data;
    private PointDao pointDao;
    private String oldPoint;

    @Inject
    PointRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, PointDao pointDao) {
        super(psApiService, appExecutors, db);

        this.pointDao = pointDao;
    }

    public LiveData<Resource<Boolean>> insertDailyPoint(DailyPoint dailyPoint) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();

        appExecutors.diskIO().execute(() -> {

//            try {
//                db.beginTransaction();
//
//                pointDao.insert(dailyPoint);
//
//                db.setTransactionSuccessful();
//
//                statusLiveData.postValue(Resource.success(true));
//
//            } catch (NullPointerException ne) {
//                Utils.psErrorLog("Null Pointer Exception : ", ne);
//                statusLiveData.postValue(Resource.error(ne.getMessage(), false));
//
//            } catch (Exception e) {
//                Utils.psErrorLog("Exception : ", e);
//
//                statusLiveData.postValue(Resource.error(e.getMessage(), false));
//            } finally {
//                db.endTransaction();
//            }

            try {
                db.runInTransaction(() -> {
                    pointDao.insert(dailyPoint);

                    statusLiveData.postValue(Resource.success(true));
                });
            } catch (Exception ex) {
                Utils.psErrorLog("Error at ", ex);
                statusLiveData.postValue(Resource.error(ex.getMessage(), false));
            }

        });

        return statusLiveData;
    }

    public LiveData<Resource<Boolean>> checkClaimStatus(DailyPoint dailyPoint) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();

        appExecutors.diskIO().execute(() -> {

//            try {
//                db.beginTransaction();
//
//                data = pointDao.getClaimedStatusByDate(dailyPoint.date);
//
//                db.setTransactionSuccessful();
//
//            } catch (NullPointerException ne) {
//                Utils.psErrorLog("Null Pointer Exception : ", ne);
//                statusLiveData.postValue(Resource.error(ne.getMessage(), false));
//
//            } catch (Exception e) {
//                Utils.psErrorLog("Exception : ", e);
//
//                statusLiveData.postValue(Resource.error(e.getMessage(), false));
//            } finally {
//                db.endTransaction();
//            }

            try {
                db.runInTransaction(() -> {
                    data = pointDao.getClaimedStatusByDate(dailyPoint.date);
                });
            } catch (Exception ex) {
                Utils.psErrorLog("Error at ", ex);
                statusLiveData.postValue(Resource.error(ex.getMessage(), false));
            }

            if (!(data.moveToFirst()) || data.getCount() == 0) {
                statusLiveData.postValue(Resource.success(true));
            } else {
                statusLiveData.postValue(Resource.error("Already claimed for today", false));
            }
        });

        return statusLiveData;

    }

    public LiveData<Resource<Boolean>> uploadClaimedPointToServer(String loginUserId, String claimedPoint) {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            try {
//                try {
//                    db.beginTransaction();
//
//                    oldPoint = db.userDao().getPointByUserId(loginUserId);
//
//                    db.setTransactionSuccessful();
//                } catch (NullPointerException ne) {
//                    Utils.psErrorLog("Null Pointer Exception : ", ne);
//                } catch (Exception e) {
//                    Utils.psErrorLog("Exception : ", e);
//                } finally {
//                    db.endTransaction();
//                }

                try {
                    db.runInTransaction(() -> {
                        oldPoint = db.userDao().getPointByUserId(loginUserId);
                    });
                } catch (Exception ex) {
                    Utils.psErrorLog("Error at ", ex);
                }

                // Call the API Service
                Response<User> response;

                response = psApiService.setPointToServer(Config.API_KEY, loginUserId, claimedPoint).execute();

                // Wrap with APIResponse Class
                ApiResponse<User> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

//                    try {
//                        db.beginTransaction();
//
//                        if (apiResponse.body != null) {
//
//                            db.userDao().updatePointByUserId(apiResponse.body.total_point, loginUserId);
//
//                        }
//
//                        db.setTransactionSuccessful();
//                    } catch (NullPointerException ne) {
//                        Utils.psErrorLog("Null Pointer Exception : ", ne);
//                    } catch (Exception e) {
//                        Utils.psErrorLog("Exception : ", e);
//                    } finally {
//                        db.endTransaction();
//                    }

                    try {
                        db.runInTransaction(() -> {
                            if (apiResponse.body != null) {

                                db.userDao().updatePointByUserId(apiResponse.body.total_point, loginUserId);

                            }
                        });
                    } catch (Exception ex) {
                        Utils.psErrorLog("Error at ", ex);
                    }

                    statusLiveData.postValue(Resource.success(apiResponse.getNextPage() != null));

                } else {

//                    try {
//                        db.beginTransaction();
//
//                        db.userDao().updatePointByUserId(oldPoint, loginUserId);
//
//                        db.setTransactionSuccessful();
//                    } catch (NullPointerException ne) {
//                        Utils.psErrorLog("Null Pointer Exception : ", ne);
//                    } catch (Exception e) {
//                        Utils.psErrorLog("Exception : ", e);
//                    } finally {
//                        db.endTransaction();
//                    }

                    try {
                        db.runInTransaction(() -> db.userDao().updatePointByUserId(oldPoint, loginUserId));
                    } catch (Exception ex) {
                        Utils.psErrorLog("Error at ", ex);
                    }

                    statusLiveData.postValue(Resource.error(apiResponse.errorMessage, false));
                }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), false));
            }
        });

        return statusLiveData;
    }
}
