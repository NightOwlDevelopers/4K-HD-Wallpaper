package com.panaceasoft.pswallpaper.di;


import com.panaceasoft.pswallpaper.MainActivity;
import com.panaceasoft.pswallpaper.ui.aboutus.AboutUsFragment;
import com.panaceasoft.pswallpaper.ui.apploading.AppLoadingActivity;
import com.panaceasoft.pswallpaper.ui.apploading.AppLoadingFragment;
import com.panaceasoft.pswallpaper.ui.category.list.CategoryListActivity;
import com.panaceasoft.pswallpaper.ui.category.list.CategoryListFragment;
import com.panaceasoft.pswallpaper.ui.claimpoint.ClaimPointActivity;
import com.panaceasoft.pswallpaper.ui.claimpoint.ClaimPointFragment;
import com.panaceasoft.pswallpaper.ui.contactus.ContactUsFragment;
import com.panaceasoft.pswallpaper.ui.dashboard.DashboardFragment;
import com.panaceasoft.pswallpaper.ui.dashboard.free.WallpaperContainerFragment;
import com.panaceasoft.pswallpaper.ui.dashboard.gif.GifContainerFragment;
import com.panaceasoft.pswallpaper.ui.dashboard.livewallpaper.LiveWallpaperContainerFragment;
import com.panaceasoft.pswallpaper.ui.dashboard.premium.PremiumContainerFragment;
import com.panaceasoft.pswallpaper.ui.dashboard.search.DashboardSearchActivity;
import com.panaceasoft.pswallpaper.ui.dashboard.search.DashboardSearchFragment;
import com.panaceasoft.pswallpaper.ui.download.DownloadLiveWallpaperListFragment;
import com.panaceasoft.pswallpaper.ui.download.DownloadedListFragment;
import com.panaceasoft.pswallpaper.ui.favorite.FavoriteListFragment;
import com.panaceasoft.pswallpaper.ui.favorite.FavoriteLiveWallpaperFragment;
import com.panaceasoft.pswallpaper.ui.forceupdate.ForceUpdateActivity;
import com.panaceasoft.pswallpaper.ui.forceupdate.ForceUpdateFragment;
import com.panaceasoft.pswallpaper.ui.language.LanguageFragment;
import com.panaceasoft.pswallpaper.ui.livewallpaper.detail.LiveWallpaperListFragment;
import com.panaceasoft.pswallpaper.ui.livewallpaper.detail.VideoPlayActivity;
import com.panaceasoft.pswallpaper.ui.livewallpaper.detail.VideoPlayFragment;
import com.panaceasoft.pswallpaper.ui.notification.NotificationSettingActivity;
import com.panaceasoft.pswallpaper.ui.notification.NotificationSettingFragment;
import com.panaceasoft.pswallpaper.ui.privacy.PrivacyActivity;
import com.panaceasoft.pswallpaper.ui.privacy.PrivacyFragment;
import com.panaceasoft.pswallpaper.ui.privacypolicy.PrivacyPolicyActivity;
import com.panaceasoft.pswallpaper.ui.privacypolicy.PrivacyPolicyFragment;
import com.panaceasoft.pswallpaper.ui.search.SearchActivity;
import com.panaceasoft.pswallpaper.ui.search.SearchFragment;
import com.panaceasoft.pswallpaper.ui.search.selection.categoryselection.CategorySelectionListActivity;
import com.panaceasoft.pswallpaper.ui.search.selection.categoryselection.CategorySelectionListFragment;
import com.panaceasoft.pswallpaper.ui.search.selection.colorselection.ColorSelectionListActivity;
import com.panaceasoft.pswallpaper.ui.search.selection.colorselection.ColorSelectionListFragment;
import com.panaceasoft.pswallpaper.ui.search.selection.wallpapertypesselection.WallpaperTypesSelectionListActivity;
import com.panaceasoft.pswallpaper.ui.search.selection.wallpapertypesselection.WallpaperTypesSelectionListFragment;
import com.panaceasoft.pswallpaper.ui.setting.SettingFragment;
import com.panaceasoft.pswallpaper.ui.upload.list.UploadedWallpaperListFragment;
import com.panaceasoft.pswallpaper.ui.upload.upload.UploadWallpaperActivity;
import com.panaceasoft.pswallpaper.ui.upload.upload.UploadWallpaperFragment;
import com.panaceasoft.pswallpaper.ui.user.PasswordChangeActivity;
import com.panaceasoft.pswallpaper.ui.user.PasswordChangeFragment;
import com.panaceasoft.pswallpaper.ui.user.ProfileEditActivity;
import com.panaceasoft.pswallpaper.ui.user.ProfileEditFragment;
import com.panaceasoft.pswallpaper.ui.user.ProfileFragment;
import com.panaceasoft.pswallpaper.ui.user.UserForgotPasswordActivity;
import com.panaceasoft.pswallpaper.ui.user.UserForgotPasswordFragment;
import com.panaceasoft.pswallpaper.ui.user.UserLoginActivity;
import com.panaceasoft.pswallpaper.ui.user.UserLoginFragment;
import com.panaceasoft.pswallpaper.ui.user.UserRegisterActivity;
import com.panaceasoft.pswallpaper.ui.user.UserRegisterFragment;
import com.panaceasoft.pswallpaper.ui.user.phonelogin.PhoneLoginActivity;
import com.panaceasoft.pswallpaper.ui.user.phonelogin.PhoneLoginFragment;
import com.panaceasoft.pswallpaper.ui.user.verifyemail.VerifyEmailActivity;
import com.panaceasoft.pswallpaper.ui.user.verifyemail.VerifyEmailFragment;
import com.panaceasoft.pswallpaper.ui.user.verifyphone.VerifyMobileActivity;
import com.panaceasoft.pswallpaper.ui.user.verifyphone.VerifyMobileFragment;
import com.panaceasoft.pswallpaper.ui.wallpaper.detail.WallpaperDetailActivity;
import com.panaceasoft.pswallpaper.ui.wallpaper.detail.WallpaperDetailFragment;
import com.panaceasoft.pswallpaper.ui.wallpaper.list.WallpaperListFragment;
import com.panaceasoft.pswallpaper.ui.wallpaper.listwithfilter.WallpaperListWithFilterActivity;
import com.panaceasoft.pswallpaper.ui.wallpaper.listwithfilter.WallpaperListWithFilterFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Panacea-Soft on 11/15/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = ProfileEditModule.class)
    abstract ProfileEditActivity contributeProfileEditActivity();

    @ContributesAndroidInjector(modules = PrivacyModule.class)
    abstract PrivacyActivity contributePrivacyActivity();

    @ContributesAndroidInjector(modules = NotificationSettingModule.class)
    abstract NotificationSettingActivity contributeNotificationSettingActivity();

    @ContributesAndroidInjector(modules = UserRegisterModule.class)
    abstract UserRegisterActivity contributeUserRegisterActivity();

    @ContributesAndroidInjector(modules = PrivacyPolicyActivityModule.class)
    abstract PrivacyPolicyActivity contributePrivacyPolicyActivity();

    @ContributesAndroidInjector(modules = VideoPlayActivityModule.class)
    abstract VideoPlayActivity contributeVideoPlayActivity();

    @ContributesAndroidInjector(modules = UserForgotPasswordModule.class)
    abstract UserForgotPasswordActivity contributeUserForgotPasswordActivity();

    @ContributesAndroidInjector(modules = UserLoginModule.class)
    abstract UserLoginActivity contributeUserLoginActivity();

    @ContributesAndroidInjector(modules = PasswordChangeModule.class)
    abstract PasswordChangeActivity contributePasswordChangeActivity();

    @ContributesAndroidInjector(modules = WallpaperByCategoryModule.class)
    abstract CategoryListActivity contributeCategoryListActivity();

    @ContributesAndroidInjector(modules = LatestWallpaperModule.class)
    abstract WallpaperListWithFilterActivity contributeLatestWallpaperActivity();

    @ContributesAndroidInjector(modules = SearchListActivityModule.class)
    abstract DashboardSearchActivity contributeSearchListActivity();

    @ContributesAndroidInjector(modules = WallpaperDetailModule.class)
    abstract WallpaperDetailActivity contributeWallpaperDetailActivity();

    @ContributesAndroidInjector(modules = SearchModule.class)
    abstract SearchActivity contributeSearchActivity();

    @ContributesAndroidInjector(modules = CategorySelectionModule.class)
    abstract CategorySelectionListActivity contributeCategorySelectionListActivity();

    @ContributesAndroidInjector(modules = ColorSelectionModule.class)
    abstract ColorSelectionListActivity contributeColorSelectionListActivity();

    @ContributesAndroidInjector(modules = ClaimPointModule.class)
    abstract ClaimPointActivity contributeClaimPointActivity();

    @ContributesAndroidInjector(modules = ImageUploadModule.class)
    abstract UploadWallpaperActivity contributeImageUploadActivity();

    @ContributesAndroidInjector(modules = ForceUpdateModule.class)
    abstract ForceUpdateActivity contributeForceUpdateActivity();

    @ContributesAndroidInjector(modules = AppLoadingModule.class)
    abstract AppLoadingActivity contributeAppLoadingActivity();

    @ContributesAndroidInjector(modules = VerifyEmailActivityModule.class)
    abstract VerifyEmailActivity contributeVerifyEmailActivity();

    @ContributesAndroidInjector(modules = ImageTypesSelectionModule.class)
    abstract WallpaperTypesSelectionListActivity contributeImageTypesSelectionListActivity();

    @ContributesAndroidInjector(modules = VerifyMobileModule.class)
    abstract VerifyMobileActivity contributeVerifyMobileActivity();

    @ContributesAndroidInjector(modules = PhoneLoginActivityModule.class)
    abstract PhoneLoginActivity contributePhoneLoginActivity();


}

@Module
abstract class MainModule {

    @ContributesAndroidInjector
    abstract AboutUsFragment contributeAboutUsFragmentFragment();

    @ContributesAndroidInjector
    abstract ContactUsFragment contributeContactUsFragment();

    @ContributesAndroidInjector
    abstract VerifyEmailFragment contributeVerifyEmailFragment();

    @ContributesAndroidInjector
    abstract LiveWallpaperContainerFragment contributeLiveWallpaperContainerFragment();

    @ContributesAndroidInjector
    abstract DownloadLiveWallpaperListFragment contributeDownloadLiveWallpaperListFragment();

    @ContributesAndroidInjector
    abstract SettingFragment contributeSettingFragment();

    @ContributesAndroidInjector
    abstract FavoriteLiveWallpaperFragment contributeFavoriteLiveWallpaperFragment();

    @ContributesAndroidInjector
    abstract DashboardSearchFragment contributeSearchListFragment();

    @ContributesAndroidInjector
    abstract UserLoginFragment contributeUserLoginFragment();

    @ContributesAndroidInjector
    abstract DashboardFragment contributeDashboard1Fragment();

    @ContributesAndroidInjector
    abstract UserForgotPasswordFragment contributeUserForgotPasswordFragment();

    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();

    @ContributesAndroidInjector
    abstract NotificationSettingFragment contributeNotificationSettingFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract LanguageFragment contributeLanguageFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment contributeWallpaperByCategoryListFragment();

    @ContributesAndroidInjector
    abstract FavoriteListFragment contributeFavoriteListFragment();

    @ContributesAndroidInjector
    abstract DownloadedListFragment contributeDownloadedListFragment();

    @ContributesAndroidInjector
    abstract PremiumContainerFragment contributePremiumFragment();

    @ContributesAndroidInjector
    abstract WallpaperListFragment contributeLatestFragment();

    @ContributesAndroidInjector
    abstract LiveWallpaperListFragment contributeLiveWallpaperListFragment();

    @ContributesAndroidInjector
    abstract WallpaperContainerFragment contributeWallpaperFragment();

    @ContributesAndroidInjector
    abstract WallpaperListWithFilterFragment contributeWallpaperListWithFilterFragment();

    @ContributesAndroidInjector
    abstract PrivacyFragment contributePrivacyFragment();

    @ContributesAndroidInjector
    abstract UploadedWallpaperListFragment contributeUploadPhotoListFragment();

    @ContributesAndroidInjector
    abstract ClaimPointFragment contributeClaimPointFragment();

    @ContributesAndroidInjector
    abstract GifContainerFragment contributeGifContainerFragment();

    @ContributesAndroidInjector
    abstract PhoneLoginFragment contributePhoneLoginFragment();

    @ContributesAndroidInjector
    abstract VerifyMobileFragment contributeVerifyMobileFragment();
}

@Module
abstract class PrivacyModule {
    @ContributesAndroidInjector
    abstract PrivacyFragment contributePrivacyFragment();
}
@Module
abstract class NotificationSettingModule {
    @ContributesAndroidInjector
    abstract NotificationSettingFragment contributeNotificationSettingFragment();
}

@Module
abstract class ProfileEditModule {
    @ContributesAndroidInjector
    abstract ProfileEditFragment contributeProfileEditFragment();
}

@Module
abstract class UserRegisterModule {
    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();
}

@Module
abstract class PrivacyPolicyActivityModule {
    @ContributesAndroidInjector
    abstract PrivacyPolicyFragment contributePrivacyPolicyFragment();
}

@Module
abstract class VideoPlayActivityModule {
    @ContributesAndroidInjector
    abstract VideoPlayFragment contributeVideoPlayFragment();
}

@Module
abstract class UserForgotPasswordModule {
    @ContributesAndroidInjector
    abstract UserForgotPasswordFragment contributeUserForgotPasswordFragment();
}


@Module
abstract class UserLoginModule {
    @ContributesAndroidInjector
    abstract UserLoginFragment contributeUserLoginFragment();
}

@Module
abstract class PasswordChangeModule {
    @ContributesAndroidInjector
    abstract PasswordChangeFragment contributePasswordChangeFragment();
}

@Module
abstract class WallpaperByCategoryModule {
    @ContributesAndroidInjector
    abstract CategoryListFragment contributeWallpaperByCategoryFragment();

}

@Module
abstract class LatestWallpaperModule {
    @ContributesAndroidInjector
    abstract WallpaperListWithFilterFragment contributeLatestWallpaperFragment();
}


@Module
abstract class SearchListActivityModule {
    @ContributesAndroidInjector
    abstract DashboardSearchFragment contributeSearchListFragment();
}


@Module
abstract class WallpaperDetailModule {
    @ContributesAndroidInjector
    abstract WallpaperDetailFragment contributeWallpaperDetailFragment();
}


@Module
abstract class SearchModule {
    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();
}

@Module
abstract class CategorySelectionModule{
    @ContributesAndroidInjector
    abstract CategorySelectionListFragment contributeCategoryFragment();
}


@Module
abstract class ColorSelectionModule{
    @ContributesAndroidInjector
    abstract ColorSelectionListFragment contributeColorFragment();
}

@Module
abstract class ClaimPointModule{
    @ContributesAndroidInjector
    abstract ClaimPointFragment contributeClaimPointFragment();
}


@Module
abstract class ImageUploadModule{
    @ContributesAndroidInjector
    abstract UploadWallpaperFragment contributeImageUploadFragment();
}

@Module
abstract class ForceUpdateModule{
    @ContributesAndroidInjector
    abstract ForceUpdateFragment contributeForceUpdateFragment();
}

@Module
abstract class AppLoadingModule{
    @ContributesAndroidInjector
    abstract AppLoadingFragment contributeAppLoadingFragment();
}
@Module
abstract class VerifyEmailActivityModule{
    @ContributesAndroidInjector
    abstract VerifyEmailFragment contributeVerifyEmailFragment();
}

@Module
abstract class ImageTypesSelectionModule{
    @ContributesAndroidInjector
    abstract WallpaperTypesSelectionListFragment contributeImageTypesSelectionListFragment();
}

@Module
abstract class VerifyMobileModule {
    @ContributesAndroidInjector
    abstract VerifyMobileFragment contributeVerifyMobileFragment();
}

@Module
abstract class PhoneLoginActivityModule {
    @ContributesAndroidInjector
    abstract PhoneLoginFragment cameraPhoneLoginFragment();
}