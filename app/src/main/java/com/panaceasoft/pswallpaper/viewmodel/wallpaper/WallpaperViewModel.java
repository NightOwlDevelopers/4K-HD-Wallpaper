package com.panaceasoft.pswallpaper.viewmodel.wallpaper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.repository.wallpaper.WallpaperRepository;
import com.panaceasoft.pswallpaper.utils.AbsentLiveData;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewmodel.common.PSViewModel;
import com.panaceasoft.pswallpaper.viewobject.Wallpaper;
import com.panaceasoft.pswallpaper.viewobject.common.Resource;
import com.panaceasoft.pswallpaper.viewobject.holder.WallpaperParamsHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class WallpaperViewModel extends PSViewModel {

    private LiveData<Resource<List<Wallpaper>>> wallpaperListLiveData;
    private MutableLiveData<TmpDataHolder> wallpaperListObj = new MutableLiveData<>();

    private LiveData<Resource<Boolean>> nextPageWallpaperListLiveData;
    private MutableLiveData<TmpDataHolder> nextPageWallpaperListObj = new MutableLiveData<>();

    private LiveData<Resource<List<Wallpaper>>> allDownloadedWallpaperData;
    private MutableLiveData<TmpDataHolder> allDownloadedWallpapersObj = new MutableLiveData<>();

    private LiveData<Resource<List<Wallpaper>>> allFavoriteWallpaperData;
    private MutableLiveData<TmpDataHolder> allFavoriteWallpapersObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageFavListData;
    private MutableLiveData<WallpaperViewModel.TmpDataHolder> nextPageFavLoadingStateObj = new MutableLiveData<>();

    private LiveData<List<Wallpaper>> getWallpaperListFromDatabaseData;
    private MutableLiveData<WallpaperParamsHolder> getWallpaperListFromDatabaseObj = new MutableLiveData<>();

    private LiveData<Resource<List<Wallpaper>>> allUploadedWallpaperData;
    private MutableLiveData<TmpDataHolder> allUploadedWallpapersObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageUploadedListData;
    private MutableLiveData<WallpaperViewModel.TmpDataHolder> nextPageUploadedLoadingStateObj = new MutableLiveData<>();

    private final LiveData<Resource<Wallpaper>> ratingData;
    private MutableLiveData<TmpDataHolder> ratingObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> buyWallpaperData;
    private MutableLiveData<TmpDataHolder> buyWallpaperObj = new MutableLiveData<>();

    private LiveData<Resource<Wallpaper>> uploadWallpaperData;
    private MutableLiveData<UploadWallpaperTmpDataHolder> uploadWallpaperObj = new MutableLiveData<>();

    private LiveData<Resource<Wallpaper>> uploadWallpaperImageData;
    private MutableLiveData<UploadWallpaperImageTmpDataHolder> uploadWallpaperImageObj = new MutableLiveData<>();

    private LiveData<Resource<Boolean>> deleteUserWallpaperByIdData;
    private MutableLiveData<DeleteUserWallPaperByIdTmpDataHolder> deleteUserWallpaperByIdObj = new MutableLiveData<>();

    private LiveData<Resource<Wallpaper>> wallpaperByIdData;
    private MutableLiveData<DeleteUserWallPaperByIdTmpDataHolder> wallpaperByIdObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageDownloadListData;
    private MutableLiveData<WallpaperViewModel.TmpDataHolder> nextPageDownloadLoadingStateObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> deleteExtraRowsData;
    private MutableLiveData<Integer> deleteExtraRowsObj = new MutableLiveData<>();

    //endregion

    public String imageUri;
    public WallpaperParamsHolder wallpaperParamsHolder;
    public WallpaperParamsHolder downloadListWallpaperParamsHolder = new WallpaperParamsHolder().getDownloadQueryHolder();

    public String wallpaperName, catId, flag;
    public int limit;
    public String deviceToken = "";
    public String type = "";
    public String videoUri = "";
    public String videoId = "";
    public String videoPath = "";
    public boolean callFromDBVideo = true;
    public boolean callFromDBWallpaper = true;

    public List<Wallpaper> wallpapers = new ArrayList<>();
    public Wallpaper wallpaperContainer;

    @Inject
    WallpaperViewModel(WallpaperRepository repository) {
        Utils.psLog("DashBoard ViewModel...");

        wallpaperListLiveData = Transformations.switchMap(wallpaperListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getWallpaperListByKey(obj.wallpaperParamsHolder, obj.limit, obj.offset, obj.loginUserId);
        });

        nextPageWallpaperListLiveData = Transformations.switchMap(nextPageWallpaperListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getNextWallpaperListByKey(Config.API_KEY, obj.loginUserId, obj.wallpaperParamsHolder, obj.limit, obj.offset);
        });

        allDownloadedWallpaperData = Transformations.switchMap(allDownloadedWallpapersObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getAllDownloadedWallpapers(Config.API_KEY, obj.loginUserId, obj.limit, obj.offset, obj.wallpaperType);
        });

        allFavoriteWallpaperData = Transformations.switchMap(allFavoriteWallpapersObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getAllFavoriteWallpapers(Config.API_KEY, obj.loginUserId, obj.limit, obj.offset, obj.wallpaperType);
        });

        nextPageFavListData = Transformations.switchMap(nextPageFavLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("ItemInquiry List.");
            return repository.getNextPageFavouriteList(Config.API_KEY, obj.loginUserId, obj.limit, obj.offset, obj.wallpaperType);
        });

        getWallpaperListFromDatabaseData = Transformations.switchMap(getWallpaperListFromDatabaseObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getWallpaperListByKeyOnlyFromDatabase(obj);
        });


        allUploadedWallpaperData = Transformations.switchMap(allUploadedWallpapersObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getAllUploadedWallpapers(Config.API_KEY, obj.loginUserId, obj.limit, obj.offset);
        });

        nextPageUploadedListData = Transformations.switchMap(nextPageUploadedLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("ItemInquiry List.");
            return repository.getNextPageUploadedWallpaperList(Config.API_KEY, obj.loginUserId, obj.limit, obj.offset, obj.wallpaperType);
        });

        ratingData = Transformations.switchMap(ratingObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Transaction detail List.");
            return repository.uploadRatingToServer(obj.wallpaperId, obj.loginUserId, obj.rating);
        });

        buyWallpaperData = Transformations.switchMap(buyWallpaperObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Transaction detail List.");
            return repository.uploadBuyingStatusToServer(obj.loginUserId, obj.point, obj.symbol, obj.wallpaperId);
        });

        uploadWallpaperImageData = Transformations.switchMap(uploadWallpaperImageObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.uploadWallpaperImage(obj.filePath, obj.wallpaperId, obj.imgId);

        });

        uploadWallpaperData = Transformations.switchMap(uploadWallpaperObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.uploadWallpaper(obj.catId, obj.colorId, obj.wallpaperName, obj.types, obj.is_portrait, obj.is_landscape, obj.is_square, obj.point,
                    obj.search_tags, obj.userId, obj.wallpaperid, obj.token, obj.credit, obj.isGif, obj.isWallpaper);

        });


        deleteUserWallpaperByIdData = Transformations.switchMap(deleteUserWallpaperByIdObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.deleteUserWallpaperById(obj.wallpaperId, obj.userId);
        });

        wallpaperByIdData = Transformations.switchMap(wallpaperByIdObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getWallpaperById(obj.wallpaperId, obj.userId);
        });

        nextPageDownloadListData = Transformations.switchMap(nextPageDownloadLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("ItemInquiry List.");
            return repository.getNextPageDownloadList(Config.API_KEY,obj.loginUserId, obj.limit, obj.offset, obj.wallpaperType);
        });

        deleteExtraRowsData = Transformations.switchMap(deleteExtraRowsObj, obj -> {
            if(obj == null)
            {
                return AbsentLiveData.create();
            }

            return repository.deleteExtraWallpapers(obj);
        });
    }
    //endregion


    //local data
    public void setGetWallpaperListObj(WallpaperParamsHolder paramsHolder, String limit, String offset, String loginUserId) {

        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.wallpaperParamsHolder = paramsHolder;
        tmpDataHolder.limit = limit;
        tmpDataHolder.offset = offset;
        tmpDataHolder.loginUserId = loginUserId;

        wallpaperListObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<List<Wallpaper>>> getWallpaperListLiveData() {
        return wallpaperListLiveData;
    }
    //endregion

    //local Wallpaper Only from Database
    public void setGetWallpaperListFromDatabaseObj(WallpaperParamsHolder paramsHolder) {

        getWallpaperListFromDatabaseObj.setValue(paramsHolder);

    }

    public LiveData<List<Wallpaper>> getGetWallpaperListFromDatabaseData() {
        return getWallpaperListFromDatabaseData;
    }
    //endregion

    //next page


    public LiveData<Resource<Boolean>> getNextPageDownloadLoadingStateData() {
        return nextPageDownloadListData;
    }

    public void setNextPageDownloadLoadingStateObj(String loginUserId, String limit, String offset, String wallpaperType) {
        if (!isLoading) {
            WallpaperViewModel.TmpDataHolder tmpDataHolder = new WallpaperViewModel.TmpDataHolder();
            tmpDataHolder.loginUserId = loginUserId;
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.wallpaperType = wallpaperType;
            nextPageDownloadLoadingStateObj.setValue(tmpDataHolder);
            // start loading
            setLoadingState(true);
        }
    }
    //endregion


    // region all trending wallpapers add network data

    public void setGetNextPageWallpaperList(String loginUserId,
                                            WallpaperParamsHolder wallpaperParamsHolder, String limit, String offset) {

        if (!isLoading) {

            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.loginUserId = loginUserId;
            tmpDataHolder.wallpaperParamsHolder = wallpaperParamsHolder;
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;

            Utils.psLog("Going to call API");
            nextPageWallpaperListObj.setValue(tmpDataHolder);

            setLoadingState(true);
        }
    }


    public LiveData<Resource<Boolean>> getNextPageWallpaperListLiveData() {
        return nextPageWallpaperListLiveData;
    }

    //endregion


    // region all downloaded wallpapers
    public void setAllDownloadedWallpaperObj(String loginUserId, String limit, String offset, String wallpaperType) {
        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.loginUserId = loginUserId;
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.wallpaperType = wallpaperType;
            allDownloadedWallpapersObj.setValue(tmpDataHolder);

            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<Wallpaper>>> getAllDownloadedWallpaperData() {
        return allDownloadedWallpaperData;
    }

    // region all favorite wallpapers
    public void setAllFavoriteWallpaperObj(String loginUserId, String limit, String offset, String wallpaperType) {
        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.loginUserId = loginUserId;
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.wallpaperType = wallpaperType;
            allFavoriteWallpapersObj.setValue(tmpDataHolder);

            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<Wallpaper>>> getAllFavoriteWallpaperData() {
        return allFavoriteWallpaperData;
    }

    public LiveData<Resource<Boolean>> getNextPageFavLoadingStateData() {
        return nextPageFavListData;
    }

    public void setNextPageFavLoadingStateObj(String loginUserId, String limit, String offset, String wallpaperType) {
        if (!isLoading) {
            WallpaperViewModel.TmpDataHolder tmpDataHolder = new WallpaperViewModel.TmpDataHolder();
            tmpDataHolder.loginUserId = loginUserId;
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.wallpaperType = wallpaperType;
            nextPageFavLoadingStateObj.setValue(tmpDataHolder);
            // start loading
            setLoadingState(true);
        }
    }

    public void setAllUploadedWallpaperObj(String loginUserId, String limit, String offset) {
        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.loginUserId = loginUserId;
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            allUploadedWallpapersObj.setValue(tmpDataHolder);

            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<Wallpaper>>> getAllUploadedWallpaperData() {
        return allUploadedWallpaperData;
    }

    public LiveData<Resource<Boolean>> getNextPageUploadedLoadingStateData() {
        return nextPageUploadedListData;
    }

    public void setNextPageUploadedLoadingStateObj(String loginUserId, String limit, String offset, String wallpaperType) {
        if (!isLoading) {
            WallpaperViewModel.TmpDataHolder tmpDataHolder = new WallpaperViewModel.TmpDataHolder();
            tmpDataHolder.loginUserId = loginUserId;
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.wallpaperType = wallpaperType;
            nextPageUploadedLoadingStateObj.setValue(tmpDataHolder);
            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Wallpaper>> getRatingData() {

        return ratingData;
    }

    public void setRatingObj(String wallpaperId, String loginUserId, float rating) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.wallpaperId = wallpaperId;
        tmpDataHolder.loginUserId = loginUserId;
        tmpDataHolder.rating = rating;
        ratingObj.setValue(tmpDataHolder);

        // start loading
        setLoadingState(true);

    }

    public void setBuyWallpaperObj(String userId, int point, String symbol, String wallpaperId) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.loginUserId = userId;
        tmpDataHolder.point = point;
        tmpDataHolder.symbol = symbol;
        tmpDataHolder.wallpaperId = wallpaperId;

        this.buyWallpaperObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getBuyWallpaperData() {
        return buyWallpaperData;
    }

    public void setUploadWallpaperobj(String catId, String colorId, String wallpaperName, String types, String is_portrait, String is_landscape,

                                      String is_square, String point, String searchTags, String userId, String wallpaperId, String token, String credit, String isGif, String isWallpaper) {
        UploadWallpaperTmpDataHolder tmpDataHolder = new UploadWallpaperTmpDataHolder(catId, colorId, wallpaperName, types, is_portrait, is_landscape, is_square,
                point, searchTags, userId, wallpaperId, token, credit, isGif, isWallpaper);

        this.uploadWallpaperObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Wallpaper>> getUploadWallpaperData() {
        return uploadWallpaperData;
    }

    public void setUploadWallpaperImageObj(String filePath, String wallpaperId, String imgId) {
        UploadWallpaperImageTmpDataHolder tmpDataHolder = new UploadWallpaperImageTmpDataHolder(filePath, wallpaperId, imgId);

        this.uploadWallpaperImageObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Wallpaper>> getUploadWallpaperImageData() {
        return uploadWallpaperImageData;
    }

    public void setDeleteUserWallpaperByIdObj(String wallpaperId, String userId)
    {
        DeleteUserWallPaperByIdTmpDataHolder tmpDataHolder = new DeleteUserWallPaperByIdTmpDataHolder(wallpaperId, userId);

        this.deleteUserWallpaperByIdObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getDeleteUserWallpaperByIdData()
    {
        return deleteUserWallpaperByIdData;
    }

    public void setWallpaperByIdObj(String wallpaperId, String userId)
    {
        DeleteUserWallPaperByIdTmpDataHolder tmpDataHolder = new DeleteUserWallPaperByIdTmpDataHolder(wallpaperId, userId);

        this.wallpaperByIdObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Wallpaper>> getWallpaperById()
    {
        return wallpaperByIdData;
    }

    public void setDeleteExtraRowsObj(int totalWallpaperCount)
    {
        this.deleteExtraRowsObj.setValue(totalWallpaperCount);
    }

    public LiveData<Resource<Boolean>> getDeleteExtraRowsData()
    {
        return deleteExtraRowsData;
    }

    //endregion

    //Color

    class TmpDataHolder {
        public WallpaperParamsHolder wallpaperParamsHolder = new WallpaperParamsHolder();
        public String loginUserId = "";
        public String wallpaperId = "";
        public String wallpaperName = "";
        public String cat_id = "";
        public float rating = 0.0f;
        public int point = 0;
        public String symbol = "";
        public String limit = "";
        public String offset = "";
        public String startDate = "";
        public String endDate = "";
        public String wallpaperType = "";


    }

    class UploadWallpaperTmpDataHolder {

        String catId, colorId, wallpaperName, types, is_portrait, is_landscape, is_square, point, search_tags, userId, wallpaperid, token, credit, isGif, isWallpaper;

        public UploadWallpaperTmpDataHolder(String catId, String colorId, String wallpaperName, String types, String is_portrait, String is_landscape, String is_square, String point, String search_tags, String userId, String wallpaperid, String token, String credit, String isGif, String isWallpaper) {
            this.catId = catId;
            this.colorId = colorId;
            this.wallpaperName = wallpaperName;
            this.types = types;
            this.is_portrait = is_portrait;
            this.is_landscape = is_landscape;
            this.is_square = is_square;
            this.point = point;
            this.search_tags = search_tags;
            this.userId = userId;
            this.wallpaperid = wallpaperid;
            this.token = token;
            this.credit = credit;
            this.isGif = isGif;
            this.isWallpaper = isWallpaper;
        }
    }

    class UploadWallpaperImageTmpDataHolder {

        String filePath, wallpaperId, imgId;

        public UploadWallpaperImageTmpDataHolder(String filePath, String wallpaperId, String imgId) {
            this.filePath = filePath;
            this.wallpaperId = wallpaperId;
            this.imgId = imgId;
        }
    }

    class DeleteUserWallPaperByIdTmpDataHolder

    {
        String wallpaperId, userId;

        public DeleteUserWallPaperByIdTmpDataHolder(String wallpaperId, String userId) {
            this.wallpaperId = wallpaperId;
            this.userId = userId;
        }
    }
}
