package com.panaceasoft.pswallpaper.ui.dashboard.gif;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.FragmentGifContainerBinding;
import com.panaceasoft.pswallpaper.ui.common.PSFragment;
import com.panaceasoft.pswallpaper.ui.dashboard.adapter.TabViewPagerAdapter;
import com.panaceasoft.pswallpaper.utils.AutoClearedValue;
import com.panaceasoft.pswallpaper.utils.Constants;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewmodel.user.UserViewModel;

public class GifContainerFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private String gifOrNot;
    private MenuItem pointMenuItem;
    private UserViewModel userViewModel;

    @androidx.annotation.VisibleForTesting
    private AutoClearedValue<FragmentGifContainerBinding> binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentGifContainerBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gif_container, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        gifOrNot = getArguments() != null ? getArguments().getString(Constants.GIF) : Constants.ZERO;
        Utils.psLog(gifOrNot + "gif wallpaper");
        setHasOptionsMenu(true);
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


    }

    @Override
    protected void initViewModels() {
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

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

        tabViewPager();

    }


    private void tabViewPager() {
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getFragmentManager(), binding.get().tabLayout.getTabCount(), "", gifOrNot);
        binding.get().tabViewPager.setAdapter(tabViewPagerAdapter);

        //tabViewPagerAdapter.setPremiumOrNot(gifOrNot);
        Utils.psLog(gifOrNot + "premium wallpaper");

        binding.get().tabViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.get().tabLayout));
        binding.get().tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.get().tabViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

}
