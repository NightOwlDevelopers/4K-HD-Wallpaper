package com.panaceasoft.pswallpaper.viewmodel.color;

import com.panaceasoft.pswallpaper.repository.color.ColorRepository;
import com.panaceasoft.pswallpaper.utils.AbsentLiveData;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewmodel.common.PSViewModel;
import com.panaceasoft.pswallpaper.viewobject.Color;
import com.panaceasoft.pswallpaper.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/**
 * Created by Panacea-Soft on 2/10/19.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class ColorViewModel extends PSViewModel {

    private LiveData<Resource<List<Color>>> allColorData;
    private MutableLiveData<TmpDataHolder> allColorObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageColorData;
    private MutableLiveData<TmpDataHolder> nextPageColorObj = new MutableLiveData<>();


    @Inject
    ColorViewModel(ColorRepository repository) {
        Utils.psLog("DashBoard ViewModel...");

        allColorData = Transformations.switchMap(allColorObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getColorList(obj.limit, obj.offset);
        });

        nextPageColorData = Transformations.switchMap(nextPageColorObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("ItemInquiry List.");
            return repository.getNextPageColorList(obj.limit, obj.offset);
        });
    }

    public void setAllColorObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.limit = limit;
        tmpDataHolder.offset = offset;

        allColorObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Color>>> getAllColorData() {
        return allColorData;
    }

    public void setNextPageColorObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.limit = limit;
        tmpDataHolder.offset = offset;

        nextPageColorObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageColorData() {
        return nextPageColorData;
    }

    class TmpDataHolder {
        public String loginUserId = "";
        public String limit = "";
        public String offset = "";

    }
}
