package com.panaceasoft.pswallpaper.viewmodel.favourite;

import com.panaceasoft.pswallpaper.repository.wallpaper.WallpaperRepository;
import com.panaceasoft.pswallpaper.utils.AbsentLiveData;
import com.panaceasoft.pswallpaper.viewmodel.common.PSViewModel;
import com.panaceasoft.pswallpaper.viewobject.common.Resource;
import com.panaceasoft.pswallpaper.viewobject.holder.WallpaperParamsHolder;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;


public class FavouriteViewModel extends PSViewModel {

    private final LiveData<Resource<Boolean>> sendFavouritePostData;
    private MutableLiveData<FavouriteViewModel.TmpDataHolder> sendFavouriteDataPostObj = new MutableLiveData<>();
    public WallpaperParamsHolder favQueryListWallpaperParamsHolder = new WallpaperParamsHolder().getFavQueryHolder();

    @Inject
    public FavouriteViewModel(WallpaperRepository wallpaperRepository) {
        sendFavouritePostData = Transformations.switchMap(sendFavouriteDataPostObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }
            return wallpaperRepository.uploadFavouritePostToServer(obj.wallpaperId, obj.loginUserId);
        });
    }

    public void setFavouritePostDataObj(String wallpaperId, String loginUserId) {

        FavouriteViewModel.TmpDataHolder tmpDataHolder = new FavouriteViewModel.TmpDataHolder();
        tmpDataHolder.wallpaperId = wallpaperId;
        tmpDataHolder.loginUserId = loginUserId;

        sendFavouriteDataPostObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<Boolean>> getFavouritePostData() {
        return sendFavouritePostData;
    }

    class TmpDataHolder {
        public String wallpaperId = "";
        public String loginUserId = "";
    }
}