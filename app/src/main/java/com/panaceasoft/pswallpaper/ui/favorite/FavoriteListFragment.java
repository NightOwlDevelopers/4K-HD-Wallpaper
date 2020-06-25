package com.panaceasoft.pswallpaper.ui.favorite;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.like.LikeButton;
import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.FragmentFavoriteListBinding;
import com.panaceasoft.pswallpaper.ui.common.PSFragment;
import com.panaceasoft.pswallpaper.ui.wallpaper.listwithfilter.adapter.WallpaperListAdapter;
import com.panaceasoft.pswallpaper.utils.AutoClearedValue;
import com.panaceasoft.pswallpaper.utils.Constants;
import com.panaceasoft.pswallpaper.utils.PSDialogMsg;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewmodel.favourite.FavouriteViewModel;
import com.panaceasoft.pswallpaper.viewmodel.user.UserViewModel;
import com.panaceasoft.pswallpaper.viewmodel.wallpaper.WallpaperViewModel;
import com.panaceasoft.pswallpaper.viewobject.Wallpaper;
import com.panaceasoft.pswallpaper.viewobject.common.Status;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteListFragment extends PSFragment {
    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private WallpaperViewModel wallpaperViewModel;
    private FavouriteViewModel favouriteViewModel;

    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private int[] firstVisibleItems = null;
    private PSDialogMsg psDialogMsg;

    @androidx.annotation.VisibleForTesting
    private AutoClearedValue<FragmentFavoriteListBinding> binding;
    private AutoClearedValue<WallpaperListAdapter> adapter;
    private MenuItem pointMenuItem;
    private UserViewModel userViewModel;
    //endregion

    //region Override Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentFavoriteListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_list, container, false, dataBindingComponent);

        setHasOptionsMenu(true);
        binding = new AutoClearedValue<>(this, dataBinding);

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
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
            userViewModel.setLocalUser(loginUserId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.pointItem) {
            // Open Claim Activity

            navigationController.navigateToClaimPointActivity(getActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        binding.get().scrollFloatingButton.setOnClickListener(v ->
                binding.get().favoriteWallpaperView.smoothScrollToPosition(0));

        if (Config.APP_GRID_LAYOUT == Constants.LAYOUT_TYPE.GRID_LAYOUT) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), Config.MIN_COLUMN_COUNT, RecyclerView.VERTICAL, false);
            binding.get().favoriteWallpaperView.setLayoutManager(gridLayoutManager);
        } else {
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(Config.MIN_COLUMN_COUNT, RecyclerView.VERTICAL);
            binding.get().favoriteWallpaperView.setLayoutManager(staggeredGridLayoutManager);
        }
        binding.get().favoriteWallpaperView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager mLayoutManager = (StaggeredGridLayoutManager)
                            recyclerView.getLayoutManager();


                    if (mLayoutManager != null) {

                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();

                        firstVisibleItems = mLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);

                        if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                            pastVisibleItems = firstVisibleItems[0];
                        }


                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            //                if (firstVisibleItems[firstVisibleItems.length - 1] == count - 1) {
                            Utils.psLog("Status : " + binding.get().getLoadingMore());
                            if (!binding.get().getLoadingMore() && !wallpaperViewModel.forceEndLoading) {

                                if (connectivity.isConnected()) {

                                    wallpaperViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                                    int limit = Config.FAVORITE_COUNT;
                                    wallpaperViewModel.offset = wallpaperViewModel.offset + limit;

                                    wallpaperViewModel.setNextPageFavLoadingStateObj(loginUserId, String.valueOf(Config.FAVORITE_COUNT), String.valueOf(wallpaperViewModel.offset),Constants.WALLPAPER);
                                }

                            }
                        }
                    }
                }else{
                    GridLayoutManager layoutManager = (GridLayoutManager)
                            recyclerView.getLayoutManager();

                    if (layoutManager != null) {

                        int lastPosition = layoutManager
                                .findLastVisibleItemPosition();

                        if (lastPosition == adapter.get().getItemCount() - 1) {

                            if (!binding.get().getLoadingMore() && !wallpaperViewModel.forceEndLoading) {

                                wallpaperViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                                int limit = Config.ALL_WALLPAPERS_COUNT;

                                wallpaperViewModel.offset = wallpaperViewModel.offset + limit;

                                wallpaperViewModel.setNextPageFavLoadingStateObj(loginUserId, String.valueOf(Config.FAVORITE_COUNT), String.valueOf(wallpaperViewModel.offset),Constants.WALLPAPER);

                            }
                        }

                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            wallpaperViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset reservationViewModel.offset
            wallpaperViewModel.offset = 0;

            // reset reservationViewModel.forceEndLoading
            wallpaperViewModel.forceEndLoading = false;

            // update live data
            wallpaperViewModel.setAllFavoriteWallpaperObj(loginUserId, String.valueOf(Config.FAVORITE_COUNT), String.valueOf(wallpaperViewModel.offset),Constants.WALLPAPER);

        });
    }

    @Override
    protected void initViewModels() {
        wallpaperViewModel = new ViewModelProvider(this, viewModelFactory).get(WallpaperViewModel.class);
        favouriteViewModel = new ViewModelProvider(this, viewModelFactory).get(FavouriteViewModel.class);
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);

    }

    @Override
    protected void initAdapters() {

        WallpaperListAdapter latestWallpaperAdapter = new WallpaperListAdapter(dataBindingComponent, new WallpaperListAdapter.AllWallpapersClickCallback() {
            @Override
            public void onClick(Wallpaper wallpaper) {
                navigationController.navigateToFavWallpaperDetail(FavoriteListFragment.this.getActivity(), wallpaper, favouriteViewModel.favQueryListWallpaperParamsHolder);
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

        this.adapter = new AutoClearedValue<>(this, latestWallpaperAdapter);
        binding.get().favoriteWallpaperView.setAdapter(latestWallpaperAdapter);

        showHideWhenScroll();
    }

    private void showHideWhenScroll() {
        binding.get().favoriteWallpaperView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) binding.get().scrollFloatingButton.show();
                else binding.get().scrollFloatingButton.hide();
                super.onScrolled(recyclerView, dx, dy);
            }
        });
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

        //get favourite post method
        favouriteViewModel.getFavouritePostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (FavoriteListFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }

                } else if (result.status == Status.ERROR) {
                    if (FavoriteListFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }
                }
            }
        });
        //endregion

        wallpaperViewModel.setAllFavoriteWallpaperObj(loginUserId, String.valueOf(Config.FAVORITE_COUNT), String.valueOf(wallpaperViewModel.offset),Constants.WALLPAPER);
        wallpaperViewModel.getAllFavoriteWallpaperData().observe(this, resource -> {

            if (resource != null) {

                Utils.psLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

//                            if (resource.data.size() == 0) {
//
//                                if (!binding.get().getLoadingMore()) {
//                                    binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);
//                                }
//
//                            } else {
//                                binding.get().noItemConstraintLayout.setVisibility(View.INVISIBLE);
//
//                            }

                            fadeIn(binding.get().getRoot());

//                            replaceGrid1Data(resource.data);
                        }

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null) {

                            if (resource.data.size() == 0) {

                                if (!binding.get().getLoadingMore()) {
                                    binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);
                                }

                            } else {
                                binding.get().noItemConstraintLayout.setVisibility(View.INVISIBLE);

                            }

                            replaceGrid1Data(resource.data);

                        }

                        wallpaperViewModel.setLoadingState(false);
                        break;
                    case ERROR:
                        // Error State
                        wallpaperViewModel.setLoadingState(false);

                        if (wallpaperViewModel.getAllFavoriteWallpaperData() != null) {
                            if (wallpaperViewModel.getAllFavoriteWallpaperData().getValue() != null) {
                                if (wallpaperViewModel.getAllFavoriteWallpaperData().getValue().data != null) {
                                    if (!binding.get().getLoadingMore() && wallpaperViewModel.getAllFavoriteWallpaperData().getValue().data.size() == 0) {
                                        binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }

                        break;
                    default:
                        // Default
                        break;
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }

            // we don't need any null checks here for the adapterGrid2 since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (resource != null) {
                Utils.psLog("Got Data Of all wallpapers.");

            } else {

                Utils.psLog("No Data of all wallpapers.");
            }
        });

        wallpaperViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(wallpaperViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });

        wallpaperViewModel.getNextPageFavLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    wallpaperViewModel.setLoadingState(false);//hide
                    wallpaperViewModel.forceEndLoading = true;//stop
                }
            }
        });


    }
    //endregion

    private void replaceGrid1Data(List<Wallpaper> wallpapers) {
        adapter.get().replace(wallpapers);
        binding.get().executePendingBindings();

    }

    //endregion

}