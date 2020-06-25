package com.panaceasoft.pswallpaper.ui.setting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentStatus;
import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.FragmentSettingBinding;
import com.panaceasoft.pswallpaper.ui.common.PSFragment;
import com.panaceasoft.pswallpaper.utils.AutoClearedValue;
import com.panaceasoft.pswallpaper.utils.GetSizeTaskForGlide;
import com.panaceasoft.pswallpaper.utils.PSDialogMsg;
import com.panaceasoft.pswallpaper.utils.Utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;

public class SettingFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private PSDialogMsg psDialogMsg;
    private ConsentForm form;

    @VisibleForTesting
    private AutoClearedValue<FragmentSettingBinding> binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentSettingBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);


        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(),false);

        if(getContext() != null)
        {
            new GetSizeTaskForGlide(binding.get().cacheValueTextViewDesc).execute(new File(getContext().getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR));
        }

        binding.get().appVersionTextView.setText(Config.APP_VERSION);

        binding.get().privacyPolicyImage.setOnClickListener(v -> navigationController.navigateToPrivacy(getActivity()));
        binding.get().privacyPolicyTextview.setOnClickListener(v -> navigationController.navigateToPrivacy(getActivity()));
        binding.get().privacyPolicyTextViewDesc.setOnClickListener(v -> navigationController.navigateToPrivacy(getActivity()));

        binding.get().notificationSettingImage.setOnClickListener(v -> navigationController.navigateToNotificationSetting(getActivity()));
        binding.get().notificationSettingText.setOnClickListener(v -> navigationController.navigateToNotificationSetting(getActivity()));
        binding.get().notificationSettingTextViewDesc.setOnClickListener(v -> navigationController.navigateToNotificationSetting(getActivity()));

        binding.get().clearCacheTextView.setOnClickListener(v -> {
            psDialogMsg.showConfirmDialog(getString(R.string.setting__clear_cache_confirm), getString(R.string.app__ok), getString(R.string.message__cancel_close));
            psDialogMsg.show();

            psDialogMsg.okButton.setOnClickListener(v12 -> {
                new ClearCacheAsync().execute();

                if(getActivity() != null)
                {
                    Glide.get(getActivity()).clearMemory();
                }

                psDialogMsg.cancel();
            });

            psDialogMsg.cancelButton.setOnClickListener(v1 -> psDialogMsg.cancel());
        });

        binding.get().gdprTextView.setOnClickListener(v -> collectConsent());
    }

    @Override
    protected void initViewModels() {

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {
        boolean consentStatusIsReady = pref.getBoolean(Config.CONSENTSTATUS_IS_READY_KEY, false);
        if(consentStatusIsReady) {
            binding.get().gdprTextView.setVisibility(View.VISIBLE);
        }else {
            binding.get().gdprTextView.setVisibility(View.GONE);
        }

    }

    class ClearCacheAsync extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(getActivity() != null)
            {
                Glide glide = Glide.get(getActivity().getApplicationContext());
                glide.clearDiskCache();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(getContext() != null)
            {
                new GetSizeTaskForGlide(binding.get().cacheValueTextViewDesc).execute(new File(getContext().getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR));
            }

            Toast.makeText(getActivity(), getString(R.string.success), Toast.LENGTH_SHORT).show();
        }
    }

    private void collectConsent(){
        URL privacyUrl = null;
        try {
            // TODO: Replace with your app's privacy policy URL.
            privacyUrl = new URL(Config.POLICY_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }

        form = new ConsentForm.Builder(this.getContext(), privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.

                        Utils.psLog("Form loaded");

                        if(form != null)
                        {
                            form.show();
                        }
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.

                        Utils.psLog("Form Opened");
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.

                        pref.edit().putString(Config.CONSENTSTATUS_CURRENT_STATUS, consentStatus.name()).apply();
                        pref.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, true).apply();
                        Utils.psLog("Form Closed");
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.

                        pref.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, false).apply();
                        Utils.psLog("Form Error " + errorDescription);
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build();

        form.load();

    }

}
