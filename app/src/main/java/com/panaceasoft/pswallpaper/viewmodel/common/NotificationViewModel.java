package com.panaceasoft.pswallpaper.viewmodel.common;

import android.content.Context;

import com.panaceasoft.pswallpaper.repository.aboutus.AboutUsRepository;
import com.panaceasoft.pswallpaper.ui.common.BackgroundTaskHandler;
import com.panaceasoft.pswallpaper.ui.common.NotificationTaskHandler;
import com.panaceasoft.pswallpaper.utils.Utils;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;

/**
 * Created by Panacea-Soft on 1/4/18.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class NotificationViewModel extends PSViewModel {

    private final NotificationTaskHandler backgroundTaskHandler;

    public boolean pushNotificationSetting = false;
    public boolean isLoading = false;

    @Inject
    NotificationViewModel(AboutUsRepository repository) {
        Utils.psLog("Inside NewsViewModel");

        backgroundTaskHandler = new NotificationTaskHandler(repository);
    }

    public void registerNotification(Context context, String platform, String token) {

        if(token == null || platform == null) return;

        if(platform.equals("")) return;

        backgroundTaskHandler.registerNotification(context, platform, token);
    }

    public void unregisterNotification(Context context, String platform, String token) {

        if(token == null || platform == null) return;

        if(platform.equals("")) return;

        backgroundTaskHandler.unregisterNotification(context, platform, token);
    }

    public LiveData<BackgroundTaskHandler.LoadingState> getLoadingStatus() {
        return backgroundTaskHandler.getLoadingState();
    }



}
