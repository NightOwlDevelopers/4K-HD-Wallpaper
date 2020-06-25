package com.panaceasoft.pswallpaper.ui.dashboard;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.like.LikeButton;
import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.MainActivity;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.FragmentDashboardBinding;
import com.panaceasoft.pswallpaper.ui.common.PSFragment;
import com.panaceasoft.pswallpaper.ui.dashboard.adapter.DashboardCategoryAdapter;
import com.panaceasoft.pswallpaper.ui.dashboard.adapter.DashboardPagerAdapter;
import com.panaceasoft.pswallpaper.ui.dashboard.adapter.landscape.LandscapeAdapter;
import com.panaceasoft.pswallpaper.ui.dashboard.adapter.portrait.PortraitAdapter;
import com.panaceasoft.pswallpaper.ui.dashboard.adapter.square.SquareAdapter;
import com.panaceasoft.pswallpaper.ui.dashboard.adapter.wallpaper.WallpaperAdapter;
import com.panaceasoft.pswallpaper.utils.AutoClearedValue;
import com.panaceasoft.pswallpaper.utils.Constants;
import com.panaceasoft.pswallpaper.utils.PSDialogMsg;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewmodel.category.CategoryViewModel;
import com.panaceasoft.pswallpaper.viewmodel.clearalldata.ClearAllDataViewModel;
import com.panaceasoft.pswallpaper.viewmodel.favourite.FavouriteViewModel;
import com.panaceasoft.pswallpaper.viewmodel.livewallpaper.latest.LatestLiveWallpaperViewModel;
import com.panaceasoft.pswallpaper.viewmodel.user.UserViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.WallpaperViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.feature.FeatureViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.gif.GifWallpaperViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.landscape.LandscapeWallpaperViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.latest.LatestWallpaperViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.portrait.PortraitWallpaperViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.square.SquareWallpaperViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.trending.TrendingViewModel;
import com.panaceasoft.pswallpaper.viewobject.Category;
import com.panaceasoft.pswallpaper.viewobject.PSAppInfo;
import com.panaceasoft.pswallpaper.viewobject.Wallpaper;
import com.panaceasoft.pswallpaper.viewobject.common.Resource;
import com.panaceasoft.pswallpaper.viewobject.holder.WallpaperParamsHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import static com.panaceasoft.pswallpaper.viewobject.common.Status.ERROR;
import static com.panaceasoft.pswallpaper.viewobject.common.Status.SUCCESS;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends PSFragment {

    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private WallpaperViewModel wallpaperViewModel;
    private CategoryViewModel categoryViewModel;
    private FavouriteViewModel favouriteViewModel;
    private PortraitWallpaperViewModel portraitWallpaperViewModel;
    private LandscapeWallpaperViewModel landScapeWallpaperViewModel;
    private SquareWallpaperViewModel squareWallpaperViewModel;
    private LatestWallpaperViewModel latestWallpaperViewModel;
    private GifWallpaperViewModel gifWallpaperViewModel;
    private UserViewModel userViewModel;
    private TrendingViewModel trendingViewModel;
    private FeatureViewModel featureViewModel;

    private LatestLiveWallpaperViewModel latestLiveWallpaperViewModel;
    private ClearAllDataViewModel clearAllDataViewModel;
    private PSDialogMsg psDialogMsg;

    private ImageView[] dots;
    private Handler handler = new Handler();
    private Runnable update;
    private int NUM_PAGES = 10;
    private int currentPage = 0;
    private boolean touched = false;
    private Timer swipeTimer;
    @Inject
    protected SharedPreferences pref;

    private String startDate = Constants.ZERO;
    private String endDate = Constants.ZERO;

    @VisibleForTesting
    private AutoClearedValue<FragmentDashboardBinding> binding;
    private AutoClearedValue<DashboardCategoryAdapter> categoryAdapter;
    private AutoClearedValue<LandscapeAdapter> landScape2Adapter;
    private AutoClearedValue<PortraitAdapter> portrait2Adapter;
    private AutoClearedValue<SquareAdapter> square2Adapter;
    private AutoClearedValue<WallpaperAdapter> latestAdapter;
    private AutoClearedValue<WallpaperAdapter> gifAdapter;
    private AutoClearedValue<WallpaperAdapter> trending2Adapter;
    private AutoClearedValue<WallpaperAdapter> latestLiveWallpaperAdapter;
    private AutoClearedValue<ViewPager> imageViewPager;
    private AutoClearedValue<LinearLayout> pageIndicatorLayout;
    private AutoClearedValue<DashboardPagerAdapter> pagerAdapter;
//    private AppLoadingViewModel psAppInfoViewModel;

    //endregion

    //    private SearchView searchView;
    private MenuItem pointMenuItem;

    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        FragmentDashboardBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        imageViewPager = new AutoClearedValue<>(this, binding.get().imageViewPager);
        pageIndicatorLayout = new AutoClearedValue<>(this, binding.get().pagerIndicator);
        setHasOptionsMenu(true);

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            binding.get().adView.loadAd(adRequest);
            binding.get().adView2.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
            binding.get().adView2.setVisibility(View.GONE);
        }
        binding.get().setLoadingMore(connectivity.isConnected());



        return binding.get().getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (Config.ENABLE_PREMIUM) {
            inflater.inflate(R.menu.point_menu, menu);
            pointMenuItem = menu.findItem(R.id.pointItem);

            super.onCreateOptionsMenu(menu, inflater);
        }
        if (userViewModel != null) {
            userViewModel.setUser(loginUserId);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.pointItem) {

            navigationController.navigateToClaimPointActivity(getActivity());
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        if (Config.ENABLE_UPLOAD_WALLPAPER) {
            if (getActivity() != null) {
                binding.get().floatingActionButton2.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).hideItem(R.id.nav_upload_photo_login, true);
            }
        } else if (!Config.ENABLE_UPLOAD_WALLPAPER) {
            if (getActivity() != null) {
                binding.get().floatingActionButton2.setVisibility(View.GONE);
                ((MainActivity) getActivity()).hideItem(R.id.nav_upload_photo_login, false);
            }
        }


        startPagerAutoSwipe();

        binding.get().floatingActionButton2.setOnClickListener(v -> {

            Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, () -> {
                navigationController.navigateToImageUploadActivity(getActivity());
            });

        });

        binding.get().latestWallpaperViewAllTextView.setOnClickListener(v -> navigationController.navigateToLatestWallpaperList(DashboardFragment.this.getActivity(), latestWallpaperViewModel.wallpaperParamsHolder, Constants.WALLPAPER));

        binding.get().latestLiveWallpaperViewAllTextView.setOnClickListener(v -> navigationController.navigateToLatestWallpaperList(DashboardFragment.this.getActivity(), latestLiveWallpaperViewModel.latestLiveWallpaperParamsHolder, Constants.LIVE_WALLPAPER));

        binding.get().trendingViewAllTextView.setOnClickListener(v -> navigationController.navigateToLatestWallpaperList(DashboardFragment.this.getActivity(), trendingViewModel.wallpaperParamsHolder, Constants.WALLPAPER));

        binding.get().portraitViewAllTextView.setOnClickListener(v -> navigationController.navigateToLatestWallpaperList(DashboardFragment.this.getActivity(), portraitWallpaperViewModel.wallpaperParamsHolder, Constants.WALLPAPER));

        binding.get().landScapeViewAllTextView.setOnClickListener(v -> navigationController.navigateToLatestWallpaperList(DashboardFragment.this.getActivity(), landScapeWallpaperViewModel.wallpaperParamsHolder, Constants.WALLPAPER));

        binding.get().squareViewAllTextView.setOnClickListener(v -> navigationController.navigateToLatestWallpaperList(DashboardFragment.this.getActivity(), squareWallpaperViewModel.wallpaperParamsHolder, Constants.WALLPAPER));

        binding.get().categoryViewAllTextView.setOnClickListener(v -> navigationController.navigateToCategoryList(getActivity()));

        binding.get().textView8.setOnClickListener(v -> navigationController.navigateToLatestWallpaperList(DashboardFragment.this.getActivity(), gifWallpaperViewModel.wallpaperParamsHolder, Constants.WALLPAPER));
        //pager
        if (imageViewPager != null && imageViewPager.get() != null && pageIndicatorLayout != null) {
            imageViewPager.get().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    currentPage = position;

                    if (pageIndicatorLayout != null) {
                        setupSliderPagination();
                    }

                    for (ImageView dot : dots) {
                        if (dots != null) {
                            dot.setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                        }
                    }

                    if (dots != null && dots.length > position) {
                        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        if (force_update) {
            navigationController.navigateToForceUpdateActivity(this.getActivity(), force_update_msg, force_update_title);
        }

        //endregion

        if(Config.ENABLE_GIF){
            binding.get().gifConstraintLayout.setVisibility(View.VISIBLE);
            binding.get().gifRecyclerView.setVisibility(View.VISIBLE);
        }else{
            binding.get().gifConstraintLayout.setVisibility(View.GONE);
            binding.get().gifRecyclerView.setVisibility(View.GONE);
        }

        if(Config.ENABLE_LIVE_WALLPAPER){
            binding.get().LatestLiveWallpaperConstraintLayout.setVisibility(View.VISIBLE);
            binding.get().latestLiveWallpaperRecyclerView.setVisibility(View.VISIBLE);
        }else{
            binding.get().LatestLiveWallpaperConstraintLayout.setVisibility(View.GONE);
            binding.get().latestLiveWallpaperRecyclerView.setVisibility(View.GONE);
        }
    }

    private void setupSliderPagination() {

        int dotsCount = pagerAdapter.get().getCount();


        if (dotsCount > 0) {

            dots = new ImageView[dotsCount];

            if (pageIndicatorLayout != null) {
                if (pageIndicatorLayout.get().getChildCount() > 0) {
                    pageIndicatorLayout.get().removeAllViewsInLayout();
                }
            }

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(getContext());
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                pageIndicatorLayout.get().addView(dots[i], params);
            }

            int currentItem = imageViewPager.get().getCurrentItem();
            if (currentItem > 0 && currentItem < dots.length) {
                dots[currentItem].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            } else {
                dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            }

        }

    }

    private void replacePager(List<Wallpaper> wallpaperList) {
        pagerAdapter.get().setWallpaperList(wallpaperList);
        setupSliderPagination();
    }

    @Override
    protected void initViewModels() {

        wallpaperViewModel = new ViewModelProvider(this, viewModelFactory).get(WallpaperViewModel.class);
        categoryViewModel = new ViewModelProvider(this, viewModelFactory).get(CategoryViewModel.class);
        favouriteViewModel = new ViewModelProvider(this, viewModelFactory).get(FavouriteViewModel.class);
        portraitWallpaperViewModel = new ViewModelProvider(this, viewModelFactory).get(PortraitWallpaperViewModel.class);
        landScapeWallpaperViewModel = new ViewModelProvider(this, viewModelFactory).get(LandscapeWallpaperViewModel.class);
        squareWallpaperViewModel = new ViewModelProvider(this, viewModelFactory).get(SquareWallpaperViewModel.class);
        latestWallpaperViewModel = new ViewModelProvider(this, viewModelFactory).get(LatestWallpaperViewModel.class);
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
        trendingViewModel = new ViewModelProvider(this, viewModelFactory).get(TrendingViewModel.class);
        featureViewModel = new ViewModelProvider(this, viewModelFactory).get(FeatureViewModel.class);
//        psAppInfoViewModel = new ViewModelProvider(this, viewModelFactory).get(AppLoadingViewModel.class);
        clearAllDataViewModel = new ViewModelProvider(this, viewModelFactory).get(ClearAllDataViewModel.class);
        gifWallpaperViewModel = new ViewModelProvider(this, viewModelFactory).get(GifWallpaperViewModel.class);
        latestLiveWallpaperViewModel = new ViewModelProvider(this, viewModelFactory).get(LatestLiveWallpaperViewModel.class);


    }

    @Override
    protected void initAdapters() {

        DashboardPagerAdapter pagerAdapter = new DashboardPagerAdapter(dataBindingComponent, new DashboardPagerAdapter.WallpaperClickCallback() {
            @Override
            public void onItemClick(View view, Wallpaper wallpaper, int position) {

            }

            @Override
            public void onPagerClick(Wallpaper wallpaper) {

                WallpaperParamsHolder wallpaperParamsHolder = new WallpaperParamsHolder().getRecommendedHolder();

                navigationController.navigateToWallpaperDetail(DashboardFragment.this.getActivity(), wallpaper, wallpaperParamsHolder);

            }
        });
        this.pagerAdapter = new AutoClearedValue<>(this, pagerAdapter);
        imageViewPager.get().setAdapter(this.pagerAdapter.get());

        //latest
        WallpaperAdapter latest2Adapter = new WallpaperAdapter(dataBindingComponent, new WallpaperAdapter.AllWallpapersClickCallback() {
            @Override
            public void onClick(Wallpaper wallpaper) {

                navigationController.navigateToWallpaperDetail(DashboardFragment.this.getActivity(), wallpaper, latestWallpaperViewModel.wallpaperParamsHolder);
            }

            @Override
            public void onFavLikeClick(Wallpaper wallpaper, LikeButton likeButton) {
                favFunction(wallpaper, likeButton);
            }

            @Override
            public void onFavUnlikeClick(Wallpaper wallpaper, LikeButton likeButton) {
                unFavFunction(wallpaper, likeButton);
            }
        });
        this.latestAdapter = new AutoClearedValue<>(this, latest2Adapter);
        binding.get().latestRecyclerView.setAdapter(latest2Adapter);

        //latest live wallpaper
        WallpaperAdapter latestLiveAdapter = new WallpaperAdapter(dataBindingComponent, new WallpaperAdapter.AllWallpapersClickCallback() {
            @Override
            public void onClick(Wallpaper liveWallpaper) {

                navigationController.navigateToLiveWallpaperDetail(DashboardFragment.this.getActivity(), liveWallpaper, latestLiveWallpaperViewModel.latestLiveWallpaperParamsHolder);
            }

            @Override
            public void onFavLikeClick(Wallpaper wallpaper, LikeButton likeButton) {
                favFunction(wallpaper, likeButton);
            }

            @Override
            public void onFavUnlikeClick(Wallpaper wallpaper, LikeButton likeButton) {
                unFavFunction(wallpaper, likeButton);
            }
        });
        this.latestLiveWallpaperAdapter = new AutoClearedValue<>(this, latestLiveAdapter);
        binding.get().latestLiveWallpaperRecyclerView.setAdapter(latestLiveAdapter);

        //Gif
        WallpaperAdapter gifAdapter = new WallpaperAdapter(dataBindingComponent, new WallpaperAdapter.AllWallpapersClickCallback() {
            @Override
            public void onClick(Wallpaper wallpaper) {

                navigationController.navigateToWallpaperDetail(DashboardFragment.this.getActivity(), wallpaper, gifWallpaperViewModel.wallpaperParamsHolder);
            }

            @Override
            public void onFavLikeClick(Wallpaper wallpaper, LikeButton likeButton) {
                favFunction(wallpaper, likeButton);
            }

            @Override
            public void onFavUnlikeClick(Wallpaper wallpaper, LikeButton likeButton) {
                unFavFunction(wallpaper, likeButton);
            }
        });
        this.gifAdapter = new AutoClearedValue<>(this, gifAdapter);
        binding.get().gifRecyclerView.setAdapter(gifAdapter);

        //trending

        WallpaperAdapter trending2Adapter = new WallpaperAdapter(dataBindingComponent, new WallpaperAdapter.AllWallpapersClickCallback() {
            @Override
            public void onClick(Wallpaper wallpaper) {

                navigationController.navigateToWallpaperDetail(DashboardFragment.this.getActivity(), wallpaper, trendingViewModel.wallpaperParamsHolder);
            }

            @Override
            public void onFavLikeClick(Wallpaper wallpaper, LikeButton likeButton) {
                favFunction(wallpaper, likeButton);
            }

            @Override
            public void onFavUnlikeClick(Wallpaper wallpaper, LikeButton likeButton) {
                unFavFunction(wallpaper, likeButton);
            }
        });
        this.trending2Adapter = new AutoClearedValue<>(this, trending2Adapter);
        binding.get().trendingRecyclerView.setAdapter(trending2Adapter);


        //grid landscape wallpaper

        LandscapeAdapter landScape2Adapter = new LandscapeAdapter(dataBindingComponent, new LandscapeAdapter.AllWallpapersClickCallback() {
            @Override
            public void onClick(Wallpaper wallpaper) {

                navigationController.navigateToWallpaperDetail(DashboardFragment.this.getActivity(), wallpaper, landScapeWallpaperViewModel.wallpaperParamsHolder);
            }

            @Override
            public void onFavLikeClick(Wallpaper wallpaper, LikeButton likeButton) {
                favFunction(wallpaper, likeButton);
            }

            @Override
            public void onFavUnlikeClick(Wallpaper wallpaper, LikeButton likeButton) {
                unFavFunction(wallpaper, likeButton);
            }
        });
        this.landScape2Adapter = new AutoClearedValue<>(this, landScape2Adapter);
        binding.get().landScapeRecyclerView.setAdapter(landScape2Adapter);

        PortraitAdapter portrait2Adapter = new PortraitAdapter(dataBindingComponent, new PortraitAdapter.AllWallpapersClickCallback() {
            @Override
            public void onClick(Wallpaper wallpaper) {

                navigationController.navigateToWallpaperDetail(DashboardFragment.this.getActivity(), wallpaper, portraitWallpaperViewModel.wallpaperParamsHolder);
            }

            @Override
            public void onFavLikeClick(Wallpaper wallpaper, LikeButton likeButton) {
                favFunction(wallpaper, likeButton);
            }

            @Override
            public void onFavUnlikeClick(Wallpaper wallpaper, LikeButton likeButton) {
                unFavFunction(wallpaper, likeButton);
            }
        });
        this.portrait2Adapter = new AutoClearedValue<>(this, portrait2Adapter);
        binding.get().portraitRecyclerView.setAdapter(portrait2Adapter);

        SquareAdapter square2Adapter = new SquareAdapter(dataBindingComponent, new SquareAdapter.AllWallpapersClickCallback() {
            @Override
            public void onClick(Wallpaper wallpaper) {

                navigationController.navigateToWallpaperDetail(DashboardFragment.this.getActivity(), wallpaper, squareWallpaperViewModel.wallpaperParamsHolder);
            }

            @Override
            public void onFavLikeClick(Wallpaper wallpaper, LikeButton likeButton) {
                favFunction(wallpaper, likeButton);
            }

            @Override
            public void onFavUnlikeClick(Wallpaper wallpaper, LikeButton likeButton) {
                unFavFunction(wallpaper, likeButton);
            }
        });
        this.square2Adapter = new AutoClearedValue<>(this, square2Adapter);
        binding.get().squareRecyclerView.setAdapter(square2Adapter);


        //category list
        DashboardCategoryAdapter categoryAdapter = new DashboardCategoryAdapter(dataBindingComponent, category
                -> navigationController.navigateToLatestWallpaperList(getActivity(), categoryViewModel.wallpaperParamsHolder.getWallpaperByCategory(category.cat_id, category.cat_name), Constants.WALLPAPER));

        this.categoryAdapter = new AutoClearedValue<>(this, categoryAdapter);
        binding.get().categoryRecycler.setAdapter(categoryAdapter);

        //endregion

    }

    private void unFavFunction(Wallpaper wallpaper, LikeButton likeButton) {

        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController,likeButton, () -> {

            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(wallpaper.wallpaper_id, loginUserId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_off, null));
            }

        });
    }

    private void favFunction(Wallpaper wallpaper, LikeButton likeButton) {

        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController,likeButton, () -> {

            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(wallpaper.wallpaper_id, loginUserId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_on, null));
            }

        });
    }



    @Override
    protected void initData() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        Date date = new Date();

        String formateedDateTime = String.valueOf(formatter.format(date));

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.LAST_APP_OPENED_TIME , formateedDateTime);
        editor.apply();

        if (connectivity.isConnected()) {
            if (startDate.equals(Constants.ZERO)) {

                startDate = Utils.getDateTime();
                Utils.setDatesToShared(startDate, endDate, pref);
            }

//            endDate = Utils.getDateTime();
//            psAppInfoViewModel.setDeleteHistoryObj(startDate, endDate);

        } else {

            if (!Config.APP_VERSION.equals(versionNo) && !force_update) {
                psDialogMsg.showInfoDialog(getString(R.string.force_update_true), getString(R.string.app__ok));
                psDialogMsg.show();
            }
        }

        clearAllDataViewModel.getDeleteAllDataData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {

                    case ERROR:
                        break;

                    case SUCCESS:

                        break;
                }
            }
        });


//        psAppInfoViewModel.getDeleteHistoryData().observe(this, result -> {
//
//            if (result != null) {
//                switch (result.status) {
//
//                    case SUCCESS:
//
//                        if (result.data != null) {
//                            psAppInfoViewModel.psAppInfo = result.data;
//                            checkVersionNumber(result.data);
//                            startDate = endDate;
//                        }
//
//                        break;
//
//                    case ERROR:
//
//                        break;
//                }
//            }
//        });
//        psAppInfoViewModel.getDeleteHistoryData().observe(this, result -> {
//
//            if (result != null) {
//                switch (result.status) {
//
//                    case SUCCESS:
//
//                        if (result.data != null) {
//                            checkVersionNumber(result.data);
//                            startDate = endDate;
//                        }
//
//                        break;
//
//                    case ERROR:
//
//                        break;
//                }
//            }
//        });


        wallpaperViewModel.setDeleteExtraRowsObj(Config.MAXIMUMWALLPAPERCOUNT);

        wallpaperViewModel.getDeleteExtraRowsData().observe(this, result -> {

            switch (result.status) {
                case SUCCESS:
//                    Toast.makeText(getActivity(), result.message, Toast.LENGTH_SHORT).show();
                    break;

                case ERROR:
                    break;
            }
        });

        if (loginUserId != null && (!loginUserId.equals(""))) {
            userViewModel.getUser(loginUserId).observe(this, listResource -> {

                if (listResource != null) {

                    Utils.psLog("Got Data" + listResource.message + listResource.toString());

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                if (pointMenuItem != null && getContext() != null) {
                                    pointMenuItem.setTitle(getContext().getString(R.string.dashboard__pts, Utils.numberFormat(Long.parseLong(listResource.data.total_point))));
                                }

                            }

                            break;
                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                //fadeIn Animation
                                //fadeIn(binding.get().getRoot());

                                if (pointMenuItem != null && getContext() != null) {
                                    pointMenuItem.setTitle(getContext().getString(R.string.dashboard__pts, Utils.numberFormat(Long.parseLong(listResource.data.total_point))));
                                }

                            }

                            break;
                        case ERROR:
                            // Error State

                            Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();

                            userViewModel.isLoading = false;

                            break;
                        default:
                            // Default
                            userViewModel.isLoading = false;

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                }


                // we don't need any null checks here for the adapter since LiveData guarantees that
                // it won't call us if fragment is stopped or not started.
                if (listResource != null && listResource.data != null) {
                    Utils.psLog("Got Data");


                } else {
                    //noinspection Constant Conditions
                    Utils.psLog("Empty Data");

                }
            });

        } else {
            if (pointMenuItem != null && getContext() != null) {
                pointMenuItem.setTitle(getContext().getString(R.string.dashboard__pts, Constants.ZERO));
            }
        }
        bindTitleText();

        //feature
        featureViewModel.setAllFeatureWallpaperObj(featureViewModel.wallpaperParamsHolder.getRecommendedHolder(), String.valueOf(Config.FEATURE_COUNT), String.valueOf(featureViewModel.offset), loginUserId);

        LiveData<Resource<List<Wallpaper>>> discount = featureViewModel.getAllFeatureWallpaperData();

        if (discount != null) {

            discount.observe(this, listResource -> {
                if (listResource.data != null) {

                    DashboardFragment.this.replacePager(listResource.data);

                    if (listResource.data.size() < 10) {
                        NUM_PAGES = listResource.data.size();
                    } else {
                        NUM_PAGES = Config.PAGER_COUNT;
                    }

                    if (listResource.data.size() > 0) {
                        replacePager(listResource.data);
                    }

                    Utils.psLog(listResource.data.size() + "feature size");
                    featureViewModel.setLoadingState(false);


                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (featureViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        featureViewModel.forceEndLoading = true;
                    }

                }

            });
        }

//        featureViewModel.getAllFeatureWallpaperNetworkData().observe(this, state -> {
//            if (state != null) {
//                if (state.status == ERROR) {
//                    Utils.psLog("Next Page State : " + state.data);
//
//                    featureViewModel.setLoadingState(false);
//                    featureViewModel.forceEndLoading = true;
//                }
//            }
//        });

        //trending
//        trendingViewModel.setAllTrendingWallpaperNetworkObj(loginUserId, trendingViewModel.wallpaperParamsHolder.getTrendingHolder(), String.valueOf(Config.TRENDING_COUNT), String.valueOf(trendingViewModel.offset));
        trendingViewModel.setAllTrendingWallpaperObj(trendingViewModel.wallpaperParamsHolder.getTrendingHolder(), String.valueOf(Config.TRENDING_COUNT), String.valueOf(trendingViewModel.offset), loginUserId);

        LiveData<Resource<List<Wallpaper>>> wallpaperList = trendingViewModel.getAllTrendingWallpaperData();

        if (wallpaperList != null) {

            wallpaperList.observe(this, listResource -> {
                if (listResource.data != null) {

                    if (listResource.data.size() > 0) {
                        replaceTrendingData(listResource.data);
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (trendingViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        trendingViewModel.forceEndLoading = true;
                    }

                }

            });
        }

//        trendingViewModel.getAllTrendingWallpaperNetworkData().observe(this, state -> {
//            if (state != null) {
//                if (state.status == ERROR) {
//                    Utils.psLog("Next Page State : " + state.data);
//
//                    trendingViewModel.setLoadingState(false);
//                    trendingViewModel.forceEndLoading = true;
//                }
//            }
//        });
        //endregion

        //potrait list
//        portraitWallpaperViewModel.setAllPortraitWallpaperNetworkObj(loginUserId, portraitWallpaperViewModel.wallpaperParamsHolder.getPortraitHolder(), String.valueOf(Config.PORTRAIT_COUNT), String.valueOf(portraitWallpaperViewModel.offset));
        portraitWallpaperViewModel.setAllPortraitWallpaperObj(portraitWallpaperViewModel.wallpaperParamsHolder.getPortraitHolder(), String.valueOf(Config.PORTRAIT_COUNT), String.valueOf(portraitWallpaperViewModel.offset));

        LiveData<Resource<List<Wallpaper>>> portraitWallpaperList = portraitWallpaperViewModel.getAllPortraitWallpaperData();

        if (portraitWallpaperList != null) {

            portraitWallpaperList.observe(this, listResource -> {
                if (listResource.data != null) {

                    if (listResource.data.size() > 0) {
                        replacePortraitData(listResource.data);
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (portraitWallpaperViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        portraitWallpaperViewModel.forceEndLoading = true;
                    }

                }

            });
        }

//        portraitWallpaperViewModel.getAllPortraitWallpaperNetworkData().observe(this, state -> {
//            if (state != null) {
//                if (state.status == ERROR) {
//                    Utils.psLog("Next Page State : " + state.data);
//
//                    portraitWallpaperViewModel.setLoadingState(false);
//                    portraitWallpaperViewModel.forceEndLoading = true;
//                }
//            }
//        });
        //endregion

        //landscape
//        landScapeWallpaperViewModel.setAllLandScapeWallpaperNetworkObj(loginUserId, landScapeWallpaperViewModel.wallpaperParamsHolder.getLandscapeHolder(), String.valueOf(Config.LANDSCAPE_COUNT), String.valueOf(landScapeWallpaperViewModel.offset));
        landScapeWallpaperViewModel.setAllLandScapeWallpaperObj(landScapeWallpaperViewModel.wallpaperParamsHolder.getLandscapeHolder(), String.valueOf(Config.LANDSCAPE_COUNT), String.valueOf(landScapeWallpaperViewModel.offset), loginUserId);

        LiveData<Resource<List<Wallpaper>>> landScapeList = landScapeWallpaperViewModel.getAllLandscapeWallpaperData();

        if (landScapeList != null) {

            landScapeList.observe(this, listResource -> {
                if (listResource.data != null) {

                    if (listResource.data.size() > 0) {
                        replaceLandScapeData(listResource.data);
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (landScapeWallpaperViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        landScapeWallpaperViewModel.forceEndLoading = true;
                    }

                }

            });
        }

//        landScapeWallpaperViewModel.getAllLandscapeWallpaperNetworkData().observe(this, state -> {
//            if (state != null) {
//                if (state.status == ERROR) {
//                    Utils.psLog("Next Page State : " + state.data);
//
//                    landScapeWallpaperViewModel.setLoadingState(false);
//                    landScapeWallpaperViewModel.forceEndLoading = true;
//                }
//            }
//        });
        //endregion

        //square list
//        squareWallpaperViewModel.setAllSquareWallpaperNetworkObj(loginUserId, squareWallpaperViewModel.wallpaperParamsHolder.getSquareHolder(), String.valueOf(Config.SQUARE_COUNT), String.valueOf(squareWallpaperViewModel.offset));
        squareWallpaperViewModel.setAllSquareWallpaperObj(squareWallpaperViewModel.wallpaperParamsHolder.getSquareHolder(), String.valueOf(Config.SQUARE_COUNT), String.valueOf(squareWallpaperViewModel.offset));

        LiveData<Resource<List<Wallpaper>>> squareWallpaperData = squareWallpaperViewModel.getAllSquareWallpaperData();

        if (squareWallpaperData != null) {

            squareWallpaperData.observe(this, listResource -> {
                if (listResource.data != null) {

                    if (listResource.data.size() > 0) {
                        replaceSquareData(listResource.data);
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (squareWallpaperViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        squareWallpaperViewModel.forceEndLoading = true;
                    }

                }

            });
        }

//        squareWallpaperViewModel.getAllSquareWallpaperNetworkData().observe(this, state -> {
//            if (state != null) {
//                if (state.status == ERROR) {
//                    Utils.psLog("Next Page State : " + state.data);
//
//                    squareWallpaperViewModel.setLoadingState(false);
//                    squareWallpaperViewModel.forceEndLoading = true;
//                }
//            }
//        });
        //endregion
        //endregion

        //latest
//        latestWallpaperViewModel.setAllLatestWallpaperNetworkObj(loginUserId, latestWallpaperViewModel.wallpaperParamsHolder.getLatestHolder(), String.valueOf(Config.LATEST_COUNT), String.valueOf(latestWallpaperViewModel.offset));


        latestWallpaperViewModel.setAllLatestWallpaperObj(latestWallpaperViewModel.wallpaperParamsHolder.getLatestHolder(), String.valueOf(Config.LATEST_COUNT), String.valueOf(latestWallpaperViewModel.offset), loginUserId);

        LiveData<Resource<List<Wallpaper>>> latestWallpaperData = latestWallpaperViewModel.getAllLatestWallpaperData();

        if (latestWallpaperData != null) {

            latestWallpaperData.observe(this, listResource -> {
                if (listResource.data != null) {

                    if (listResource.data.size() > 0) {
                        replaceLatestData(listResource.data);
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (latestWallpaperViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        latestWallpaperViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        //video

        latestLiveWallpaperViewModel.setAllLatestLiveWallpaperObj(latestLiveWallpaperViewModel.latestLiveWallpaperParamsHolder.getLatestLiveWallpaperHolder(), String.valueOf(Config.LATEST_COUNT), String.valueOf(latestWallpaperViewModel.offset), loginUserId);

        LiveData<Resource<List<Wallpaper>>> latestVideoData = latestLiveWallpaperViewModel.getAllLatestLiveWallpaperData();

        if (latestVideoData != null) {

            latestVideoData.observe(this, listResource -> {
                if (listResource.data != null) {

                    if (listResource.data.size() > 0) {
                        replaceLatestVideoData(listResource.data);
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (latestLiveWallpaperViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        latestLiveWallpaperViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        //endregion

        // Gif
        gifWallpaperViewModel.setAllGifWallpaperObj(gifWallpaperViewModel.wallpaperParamsHolder.getGifHolder(), String.valueOf(Config.GIF_COUNT), String.valueOf(gifWallpaperViewModel.offset), loginUserId);

        LiveData<Resource<List<Wallpaper>>> gifWallpaperData = gifWallpaperViewModel.getAllGifWallpaperData();

        if (gifWallpaperData != null) {

            gifWallpaperData.observe(this, listResource -> {
                if (listResource.data != null) {

                    if (listResource.data.size() > 0) {
                        replaceGifData(listResource.data);
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (latestWallpaperViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        latestWallpaperViewModel.forceEndLoading = true;
                    }

                }

            });
        }

//        latestWallpaperViewModel.getAllLatestWallpaperNetworkData().observe(this, state -> {
//            if (state != null) {
//                if (state.status == ERROR) {
//                    Utils.psLog("Next Page State : " + state.data);
//
//                    latestWallpaperViewModel.setLoadingState(false);
//                    latestWallpaperViewModel.forceEndLoading = true;
//                }
//            }
//        });
        //endregion

        //category
        categoryViewModel.setCategoryList(String.valueOf(Config.ALL_WALLPAPERS_CATEGORY), String.valueOf(categoryViewModel.offset));
        categoryViewModel.getAllCategoryList().observe(this, resource -> {

            if (resource != null) {

                Utils.psLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            fadeIn(binding.get().getRoot());

                            if (resource.data.size() > 0) {
                                replaceDataByCategories(resource.data);
                            }

                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null && resource.data.size() > 0) {

                            replaceDataByCategories(resource.data);
                        }

                        categoryViewModel.setLoadingState(false);
                        break;
                    case ERROR:
                        // Error State
                        categoryViewModel.setLoadingState(false);
                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }


            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (resource != null) {
                Utils.psLog("Got Data Of all wallpapers.");


            } else {
                //noinspection Constant Conditions
                Utils.psLog("No Data of all wallpapers.");
            }
        });

        //get favourite post method
        favouriteViewModel.getFavouritePostData().observe(this, result -> {
            if (result != null) {
                if (result.status == SUCCESS) {
                    if (DashboardFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }

                } else if (result.status == ERROR) {
                    if (DashboardFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }
                }
            }
        });
        //endregion
    }

    private void bindTitleText() {
        //binding.get().categoryTextView.setText(R.string.category__category);
    }

    //endregion

    private void replaceTrendingData(List<Wallpaper> wallpapers) {

        trending2Adapter.get().replace(wallpapers);
        binding.get().executePendingBindings();

    }


    private void replacePortraitData(List<Wallpaper> wallpapers) {

        portrait2Adapter.get().replace(wallpapers);
        binding.get().executePendingBindings();

    }

    private void replaceLandScapeData(List<Wallpaper> wallpapers) {

        landScape2Adapter.get().replace(wallpapers);
        binding.get().executePendingBindings();

    }

    private void replaceLatestData(List<Wallpaper> wallpapers) {

        latestAdapter.get().replace(wallpapers);
        binding.get().executePendingBindings();

    }

    private void replaceLatestVideoData(List<Wallpaper> wallpapers) {

        latestLiveWallpaperAdapter.get().replace(wallpapers);
        binding.get().executePendingBindings();

    }

    private void replaceGifData(List<Wallpaper> wallpapers) {

        gifAdapter.get().replace(wallpapers);
        binding.get().executePendingBindings();

    }

    private void replaceSquareData(List<Wallpaper> wallpapers) {

        square2Adapter.get().replace(wallpapers);
        binding.get().executePendingBindings();

    }


    private void replaceDataByCategories(List<Category> categories) {

        categoryAdapter.get().replace(categories);
        binding.get().executePendingBindings();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (swipeTimer != null) {
            swipeTimer.cancel();
            swipeTimer.purge();
            swipeTimer = null;
        }
    }

    private void startPagerAutoSwipe() {
        update = () -> {
            if (!touched) {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                if (imageViewPager != null) {
                    if (imageViewPager.get() != null) {
                        imageViewPager.get().setCurrentItem(currentPage++, true);
                    }
                }

            }
        };
        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 1000, 3000);
    }


    @Override
    public void onResume() {
        super.onResume();
        Utils.psLog("On Resume");

        loadLoginUserId();

        if (userViewModel != null) {
            userViewModel.setUser(loginUserId);
        }

    }



//    private void checkVersionNumber(PSAppInfo psAppInfo) {
//        if (!Config.APP_VERSION.equals(psAppInfo.psAppVersion.versionNo)) {
//
//            if (psAppInfo.psAppVersion.versionNeedClearData.equals(Constants.ONE)) {
//                clearAllDataViewModel.setDeleteAllDataObj();
//            } else {
//                checkForceUpdate(psAppInfoViewModel.psAppInfo);
//            }
//        } else {
//            pref.edit().putBoolean(Constants.APPINFO_PREF_FORCE_UPDATE, false).apply();
//        }
//
//    }

//    private void checkVersionNumber(PSAppInfo psAppInfo) {
//        if (!Config.APP_VERSION.equals(psAppInfo.psAppVersion.versionNo)) {
//            if (psAppInfo.psAppVersion.versionForceUpdate.equals(Config.ONE)) {
//
//                pref.edit().putString(Config.APPINFO_PREF_VERSION_NO, psAppInfo.psAppVersion.versionNo).apply();
//                pref.edit().putBoolean(Config.APPINFO_PREF_FORCE_UPDATE, true).apply();
//                pref.edit().putString(Config.APPINFO_FORCE_UPDATE_MSG, psAppInfo.psAppVersion.versionMessage).apply();
//                pref.edit().putString(Config.APPINFO_FORCE_UPDATE_TITLE, psAppInfo.psAppVersion.versionTitle).apply();
//
//                navigationController.navigateToForceUpdateActivity(this.getActivity(), psAppInfo.psAppVersion.versionMessage, psAppInfo.psAppVersion.versionTitle);
//
//            } else if (psAppInfo.psAppVersion.versionForceUpdate.equals(Config.ZERO)) {
//
//                pref.edit().putBoolean(Config.APPINFO_PREF_FORCE_UPDATE, false).apply();
//
//                psDialogMsg.showAppInfoDialog(getString(R.string.force_update__button_update), getString(R.string.message__cancel_close), psAppInfo.psAppVersion.versionTitle, psAppInfo.psAppVersion.versionMessage);
//                psDialogMsg.show();
//
//
//                psDialogMsg.okButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        navigationController.navigateToPlayStore(getActivity());
//                    }
//                });
//            }
//
//            if (psAppInfo.psAppVersion.versionNeedClearData.equals(Config.ONE)) {
//                clearAllDataViewModel.setDeleteAllDataObj();
//            }
//        } else {
//            pref.edit().putBoolean(Config.APPINFO_PREF_FORCE_UPDATE, false).apply();
//        }
//
//    }

}