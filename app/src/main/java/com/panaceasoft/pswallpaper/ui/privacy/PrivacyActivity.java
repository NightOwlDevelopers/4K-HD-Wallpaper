package com.panaceasoft.pswallpaper.ui.privacy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.databinding.ActivityPrivacyBinding;
import com.panaceasoft.pswallpaper.ui.common.PSAppCompactActivity;
import com.panaceasoft.pswallpaper.utils.Constants;
import com.panaceasoft.pswallpaper.utils.MyContextWrapper;

public class PrivacyActivity extends PSAppCompactActivity {


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPrivacyBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy);

        // Init all UI
        initUI(binding);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);

        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, CURRENT_LANG_COUNTRY_CODE, true));

    }

    //endregion


    //region Private Methods

    private void initUI(ActivityPrivacyBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.setting__privacy_policy));

        // setup Fragment
        setupFragment(new PrivacyFragment());
        // Or you can call like this
    }

    //endregion


}