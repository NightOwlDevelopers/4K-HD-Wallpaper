package com.panaceasoft.pswallpaper.repository.common;

import com.panaceasoft.pswallpaper.api.PSApiService;
import com.panaceasoft.pswallpaper.db.PSCoreDb;
import com.panaceasoft.pswallpaper.viewobject.Category;
import com.panaceasoft.pswallpaper.viewobject.common.Resource;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * General Save Task Sample
 * Created by Panacea-Soft on 12/6/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class SaveTask implements Runnable {


    //region Variables

    private final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

    private final PSApiService service;
    private final PSCoreDb db;
    private final Object obj;

    //endregion


    //region Constructor

    /**
     * Constructor of SaveTask.
     *
     * @param service Panacea-Soft API Service Instance
     * @param db      Panacea-Soft DB Instance
     * @param obj     Object to Save
     */
    public SaveTask(PSApiService service, PSCoreDb db, Object obj) {
        this.service = service;
        this.db = db;
        this.obj = obj;
    }

    //endregion


    //region Override Methods

    @Override
    public void run() {
        try {

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
