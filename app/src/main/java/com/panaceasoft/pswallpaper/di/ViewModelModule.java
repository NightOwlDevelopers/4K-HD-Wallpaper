package com.panaceasoft.pswallpaper.di;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.panaceasoft.pswallpaper.viewmodel.aboutus.AboutUsViewModel;
import com.panaceasoft.pswallpaper.viewmodel.apploading.AppLoadingViewModel;
import com.panaceasoft.pswallpaper.viewmodel.category.CategoryViewModel;
import com.panaceasoft.pswallpaper.viewmodel.clearalldata.ClearAllDataViewModel;
import com.panaceasoft.pswallpaper.viewmodel.color.ColorViewModel;
import com.panaceasoft.pswallpaper.viewmodel.common.NotificationViewModel;
import com.panaceasoft.pswallpaper.viewmodel.common.PSNewsViewModelFactory;
import com.panaceasoft.pswallpaper.viewmodel.contactus.ContactUsViewModel;
import com.panaceasoft.pswallpaper.viewmodel.favourite.FavouriteViewModel;
import com.panaceasoft.pswallpaper.viewmodel.livewallpaper.latest.LatestLiveWallpaperViewModel;
import com.panaceasoft.pswallpaper.viewmodel.point.PointViewModel;
import com.panaceasoft.pswallpaper.viewmodel.user.UserViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.WallpaperViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.downloadcount.DownloadCountViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.feature.FeatureViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.gif.GifWallpaperViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.landscape.LandscapeWallpaperViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.latest.LatestWallpaperViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.portrait.PortraitWallpaperViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.square.SquareWallpaperViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.touchcount.TouchCountViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.trending.TrendingViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by Panacea-Soft on 11/16/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Module
abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PSNewsViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AboutUsViewModel.class)
    abstract ViewModel bindAboutUsViewModel(AboutUsViewModel aboutUsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PortraitWallpaperViewModel.class)
    abstract ViewModel bindPortraitWallpaperViewModel(PortraitWallpaperViewModel portraitWallpaperViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LandscapeWallpaperViewModel.class)
    abstract ViewModel bindLandScapeWallpaperViewModel(LandscapeWallpaperViewModel landScapeWallpaperViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SquareWallpaperViewModel.class)
    abstract ViewModel bindSquareWallpaperViewModel(SquareWallpaperViewModel squareWallpaperViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LatestWallpaperViewModel.class)
    abstract ViewModel bindLatestWallpaperViewModel(LatestWallpaperViewModel latestWallpaperViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel.class)
    abstract ViewModel bindNotificationViewModel(NotificationViewModel notificationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ContactUsViewModel.class)
    abstract ViewModel bindContactUsViewModel(ContactUsViewModel contactUsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FeatureViewModel.class)
    abstract ViewModel bindFeatureViewModel(FeatureViewModel featureViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TrendingViewModel.class)
    abstract ViewModel bindTrendingViewModel(TrendingViewModel trendingViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(WallpaperViewModel.class)
    abstract ViewModel bindDashboardViewModel(WallpaperViewModel wallpaperViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel.class)
    abstract ViewModel bindCategoryViewModel(CategoryViewModel categoryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TouchCountViewModel.class)
    abstract ViewModel bindTouchCountViewModel(TouchCountViewModel touchCountViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FavouriteViewModel.class)
    abstract ViewModel bindFavouriteViewModel(FavouriteViewModel favouriteViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ColorViewModel.class)
    abstract ViewModel bindColorViewModel(ColorViewModel colorViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PointViewModel.class)
    abstract ViewModel bindPointViewModel(PointViewModel pointViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DownloadCountViewModel.class)
    abstract ViewModel binddDownloadCountViewModel(DownloadCountViewModel downloadCountViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ClearAllDataViewModel.class)
    abstract ViewModel binddClearAllDataViewModel(ClearAllDataViewModel clearAllDataViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AppLoadingViewModel.class)
    abstract ViewModel binddPSAppInfoViewModel(AppLoadingViewModel psAppInfoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GifWallpaperViewModel.class)
    abstract ViewModel binddPSGifWallpaperViewModel(GifWallpaperViewModel psGifWallpaperViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LatestLiveWallpaperViewModel.class)
    abstract ViewModel binddLatestLiveWallpaperViewModel(LatestLiveWallpaperViewModel latestLiveWallpaperViewModel);
}
