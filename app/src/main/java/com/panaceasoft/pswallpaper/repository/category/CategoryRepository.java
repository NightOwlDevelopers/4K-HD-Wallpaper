package com.panaceasoft.pswallpaper.repository.category;

import com.panaceasoft.pswallpaper.AppExecutors;
import com.panaceasoft.pswallpaper.api.ApiResponse;
import com.panaceasoft.pswallpaper.api.PSApiService;
import com.panaceasoft.pswallpaper.db.CategoryDao;
import com.panaceasoft.pswallpaper.db.PSCoreDb;
import com.panaceasoft.pswallpaper.repository.common.NetworkBoundResource;
import com.panaceasoft.pswallpaper.repository.common.PSRepository;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewobject.Category;
import com.panaceasoft.pswallpaper.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

/**
 * Created by Panacea-Soft on 2/10/19.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class CategoryRepository extends PSRepository {

    private final CategoryDao categoryDao;

    @Inject
    CategoryRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, CategoryDao categoryDao) {
        super(psApiService, appExecutors, db);
        this.categoryDao = categoryDao;

    }

    public LiveData<Resource<List<Category>>> getCategoryList(String apiKey, String limit, String offset) {

        String functionKey = "getAllWallpaperById";

        return new NetworkBoundResource<List<Category>, List<Category>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Category> item) {
                Utils.psLog("SaveCallResult of get all wallpapers by category.");

//                db.beginTransaction();
//                try {
//
//                    categoryDao.deleteTable();
//
//                    categoryDao.insertAll(item);
//
//                    db.setTransactionSuccessful();
//
//                } catch (Exception e) {
//                    Utils.psErrorLog("Error in inserting category", e);
//                } finally {
//                    db.endTransaction();
//                }

                try {
                    db.runInTransaction(() -> {
                        categoryDao.deleteTable();

                        categoryDao.insertAll(item);
                    });
                } catch (Exception ex) {
                    Utils.psErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Category> data) {
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<Category>> loadFromDb() {
                Utils.psLog("Load wallpaper by category id From DB.");

                return categoryDao.getCategoryList();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Category>>> createCall() {
                Utils.psLog("Call all wallpaper by category webservice.");
                return psApiService.getCategoryList(apiKey, limit, offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of all wallpapers by category");
                rateLimiter.reset(functionKey);
            }

        }.asLiveData();
    }


    //Get Next Page Category LIst
    public LiveData<Resource<Boolean>> getNextPageCategoryList(String apiKey, String limit, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();

        LiveData<ApiResponse<List<Category>>> apiResponse = psApiService.getCategoryList(apiKey, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);


            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {


//                    try {
//                        db.beginTransaction();
//
//                        categoryDao.insertAll(response.body);
//
//
//                        db.setTransactionSuccessful();
//
//                    } catch (NullPointerException ne) {
//                        Utils.psErrorLog("Null Pointer Exception : ", ne);
//                    } catch (Exception e) {
//                        Utils.psErrorLog("Exception : ", e);
//                    } finally {
//                        db.endTransaction();
//                    }

                    try {
                        db.runInTransaction(() -> categoryDao.insertAll(response.body));
                    } catch (Exception ex) {
                        Utils.psErrorLog("Error at ", ex);
                    }

                    statusLiveData.postValue(Resource.success(true));
                });

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }

        });

        return statusLiveData;

    }
}
