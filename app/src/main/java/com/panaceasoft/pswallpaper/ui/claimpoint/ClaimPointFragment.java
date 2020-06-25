package com.panaceasoft.pswallpaper.ui.claimpoint;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.FragmentClaimPointBinding;
import com.panaceasoft.pswallpaper.ui.common.PSFragment;
import com.panaceasoft.pswallpaper.utils.AutoClearedValue;
import com.panaceasoft.pswallpaper.utils.Constants;
import com.panaceasoft.pswallpaper.utils.PSDialogMsg;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewmodel.point.PointViewModel;
import com.panaceasoft.pswallpaper.viewmodel.user.UserViewModel;
import com.panaceasoft.pswallpaper.viewobject.DailyPoint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClaimPointFragment extends PSFragment implements RewardedVideoAdListener {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private RewardedVideoAd mRewardedVideoAd;
    private PointViewModel pointViewModel;
    private DailyPoint dailyPoint;
    private InterstitialAd mInterstitialAd;
    private CardView lastCardView;
    private PSDialogMsg psDialogMsg;
    private View lastView;
    private MenuItem pointMenuItem;
    private UserViewModel userViewModel;
    private int currentRewardPoint1DailyCount = 0;
    private int currentRewardPoint2DailyCount = 0;

    private String currentRewardPoint1DailyDate = "";
    private String currentRewardPoint2DailyDate = "";

    private SharedPreferences.Editor editor;

    @VisibleForTesting
    private AutoClearedValue<FragmentClaimPointBinding> binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentClaimPointBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_claim_point, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        setHasOptionsMenu(true);


        // Load Control
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();

        // Get Current Touch Count
        currentRewardPoint1DailyCount = preferences.getInt(Constants.CURRENT_REWARD_POINT_1_DAILY_COUNT_KEY, Config.REWARD_INIT);
        currentRewardPoint2DailyCount = preferences.getInt(Constants.CURRENT_REWARD_POINT_2_DAILY_COUNT_KEY, Config.REWARD_INIT);
        currentRewardPoint1DailyDate = preferences.getString(Constants.CURRENT_REWARD_POINT_1_DAILY_DATE_KEY, "");
        currentRewardPoint2DailyDate = preferences.getString(Constants.CURRENT_REWARD_POINT_2_DAILY_DATE_KEY, "");

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }

        return binding.get().getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.point_menu, menu);
        pointMenuItem = menu.findItem(R.id.pointItem);
        super.onCreateOptionsMenu(menu, inflater);

        if (userViewModel != null) {
            userViewModel.setLocalUser(loginUserId);
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat, Locale.US);
        String formattedDate = dateFormat.format(date);

        dailyPoint = new DailyPoint(formattedDate, "0", loginUserId);

        if (!currentRewardPoint1DailyDate.equals(dailyPoint.date)) {
            // reset Data
            currentRewardPoint1DailyCount = 0;
            currentRewardPoint1DailyDate = dailyPoint.date;

            editor.putString(Constants.CURRENT_REWARD_POINT_1_DAILY_DATE_KEY, dailyPoint.date);
            editor.putInt(Constants.CURRENT_REWARD_POINT_1_DAILY_COUNT_KEY, Config.REWARD_INIT);

        }

        if (!currentRewardPoint2DailyDate.equals(dailyPoint.date)) {
            // reset Data
            currentRewardPoint2DailyCount = 0;
            currentRewardPoint2DailyDate = dailyPoint.date;

            editor.putString(Constants.CURRENT_REWARD_POINT_2_DAILY_DATE_KEY, dailyPoint.date);
            editor.putInt(Constants.CURRENT_REWARD_POINT_2_DAILY_COUNT_KEY, Config.REWARD_INIT);

        }
        editor.apply();

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this.getActivity());
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        binding.get().watchCardView.setOnClickListener(v -> {

            Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, new Utils.NavigateOnUserVerificationActivityCallback() {
                @Override
                public void onSuccess() {
                    if (mRewardedVideoAd.isLoaded()) {
                        mRewardedVideoAd.show();
                    }

                    if (binding != null) {
                        lastCardView = binding.get().watchCardView;
                        lastView = binding.get().rewardView;
                    }
                }
            });

        });

        binding.get().dailyCardView.setOnClickListener(v -> {

            Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, new Utils.NavigateOnUserVerificationActivityCallback() {
                @Override
                public void onSuccess() {
                    pointViewModel.setSendClaimedPointToServerObj(loginUserId, String.valueOf(Config.DAILY_POINT));
                    pointViewModel.setInsertObj(dailyPoint);

                    if (binding != null) {
                        lastCardView = binding.get().dailyCardView;
                        lastView = binding.get().dailyView;
                    }
                }
            });

        });

        binding.get().fullScreenCardView.setOnClickListener(v -> {

            Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, new Utils.NavigateOnUserVerificationActivityCallback() {
                @Override
                public void onSuccess() {
                    showFullScreenAds();

                    if (binding != null) {
                        lastCardView = binding.get().fullScreenCardView;
                        lastView = binding.get().fullScreenView;
                    }
                }
            });

        });

        if (Config.SHOW_DAILY_POINT) {
            binding.get().dailyPointTextView.setText(getString(R.string.dashboard__pts, Config.DAILY_POINT));
        } else {
            binding.get().dailyCardView.setVisibility(View.GONE);
        }

        if (Config.SHOW_REWARD_POINT_1) {
            binding.get().reward1TextView.setText(getString(R.string.dashboard__pts, Config.REWARD_POINT_1));
            prepareFullScreenAds();
        } else {
            binding.get().fullScreenCardView.setVisibility(View.GONE);
        }

        if (Config.SHOW_REWARD_POINT_2) {
            binding.get().reward2TextView.setText(getString(R.string.dashboard__pts, Config.REWARD_POINT_2));
            loadRewardedVideoAd();
        } else {
            binding.get().watchCardView.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initViewModels() {
        pointViewModel = new ViewModelProvider(this, viewModelFactory).get(PointViewModel.class);
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

        pointViewModel.setClaimStatusObj(dailyPoint);

    }

    @Override
    protected void initData() {

        // get local user from database
        userViewModel.getLocalUser(loginUserId).observe(this, localUserData -> {

            if (localUserData != null) {

                if (pointMenuItem != null && getContext() != null) {
                    pointMenuItem.setTitle(getContext().getString(R.string.dashboard__pts, Utils.numberFormat(Long.parseLong(localUserData.total_point))));
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }
        });

        pointViewModel.getInsertData().observe(this, result ->
        {
            switch (result.status) {
                case SUCCESS:

                    changeTheBackgroundOfTheCardView(binding.get().dailyView, binding.get().dailyCardView, false);

                    break;

                case ERROR:
                    Toast.makeText(getActivity(), "Insertion Unsuccessful", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        pointViewModel.getClaimStatusData().observe(this, result -> {
            switch (result.status) {

                case SUCCESS:

                    changeTheBackgroundOfTheCardView(binding.get().dailyView, binding.get().dailyCardView, true);

                    break;

                case ERROR:

                    changeTheBackgroundOfTheCardView(binding.get().dailyView, binding.get().dailyCardView, false);

                    break;
            }
        });

        pointViewModel.getSendClaimedPointToServerData().observe(this, result -> {

            switch (result.status) {

                case SUCCESS:

                    Toast.makeText(getActivity(), "Uploaded To Server", Toast.LENGTH_SHORT).show();

                    changeTheBackgroundOfTheCardView(lastView, lastCardView, false);

                    break;

                case ERROR:

                    Toast.makeText(getActivity(), "failed To upload, Please reclaim", Toast.LENGTH_SHORT).show();

                    changeTheBackgroundOfTheCardView(lastView, lastCardView, true);

                    break;
            }
        });
    }

    @Override
    public void onRewardedVideoAdLoaded() {

        changeTheBackgroundOfTheCardView(binding.get().rewardView, binding.get().watchCardView, true);
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        currentRewardPoint2DailyCount++;
        editor.putInt(Constants.CURRENT_REWARD_POINT_2_DAILY_COUNT_KEY, currentRewardPoint2DailyCount);
        editor.apply();
        pointViewModel.setSendClaimedPointToServerObj(loginUserId, String.valueOf(Config.REWARD_POINT_2));
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {
        int i = 0;
    }

    private void loadRewardedVideoAd() {

        if (currentRewardPoint2DailyCount < Config.REWARD_POINT_2_DAILY_LIMIT) {

            mRewardedVideoAd.setRewardedVideoAdListener(this);
            mRewardedVideoAd.loadAd(getString(R.string.adview_reward_ad_key),
                    new AdRequest.Builder().build());

        }
    }

    private void prepareFullScreenAds() {
        if (getContext() != null) {

            if (currentRewardPoint1DailyCount < Config.REWARD_POINT_1_DAILY_LIMIT) {

                // load AD
                mInterstitialAd = new InterstitialAd(getContext());

                // set the ad unit ID
                mInterstitialAd.setAdUnitId(getString(R.string.adview_interstitial_ad_key));

                AdRequest adRequest = new AdRequest.Builder().build();

                // Load ads into Interstitial Ads
                mInterstitialAd.loadAd(adRequest);

                mInterstitialAd.setAdListener(new AdListener() {

                    @Override
                    public void onAdFailedToLoad(int i) {
                        Log.d("TEAMPS", "Failed to load." + i);
                        super.onAdFailedToLoad(i);
                    }

                    @Override
                    public void onAdClosed() {
                        //prepareFullScreenAds();
                        super.onAdClosed();

                        pointViewModel.setSendClaimedPointToServerObj(loginUserId, String.valueOf(Config.REWARD_POINT_1));
                        currentRewardPoint1DailyCount++;
                        editor.putInt(Constants.CURRENT_REWARD_POINT_1_DAILY_COUNT_KEY, currentRewardPoint1DailyCount);
                        editor.apply();
                    }

                    public void onAdLoaded() {

                        if (binding.get() != null && binding.get().fullScreenView != null) {
                            changeTheBackgroundOfTheCardView(binding.get().fullScreenView, binding.get().fullScreenCardView, true);
                        }
                    }
                });

            }
        }
    }

    private void showFullScreenAds() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void changeTheBackgroundOfTheCardView(View v, CardView cardView, boolean status) {

        if (v != null && cardView != null) {
            if (status) {
                Drawable drawable = getResources().getDrawable(R.drawable.gradient_background);

                v.setBackground(drawable);

                cardView.setClickable(true);
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.gradient_background_grey);

                v.setBackground(drawable);

                cardView.setClickable(false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.psLog("On Resume");

        loadLoginUserId();

        if (userViewModel != null) {
            userViewModel.setLocalUser(loginUserId);
        }
    }
}
