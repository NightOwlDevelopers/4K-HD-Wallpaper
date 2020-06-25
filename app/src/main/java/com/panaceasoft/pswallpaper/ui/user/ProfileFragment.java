package com.panaceasoft.pswallpaper.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.FragmentProfileBinding;
import com.panaceasoft.pswallpaper.ui.common.PSFragment;
import com.panaceasoft.pswallpaper.utils.AutoClearedValue;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewmodel.user.UserViewModel;

/**
 * ProfileFragment
 */
public class ProfileFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;
    private MenuItem pointMenuItem;
    @VisibleForTesting
    AutoClearedValue<FragmentProfileBinding> binding;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentProfileBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        setHasOptionsMenu(true);
        return binding.get().getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu,@NonNull MenuInflater inflater) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pointItem) {
            // Open Claim Activity

            navigationController.navigateToClaimPointActivity(getActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initUIAndActions() {

        binding.get().fab.setOnClickListener(view ->
                navigationController.navigateToProfileEditActivity(getActivity())
        );
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

                            binding.get().setUser(listResource.data);
                            Utils.psLog("Photo : " + listResource.data.user_profile_photo);

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

                            binding.get().setUser(listResource.data);
                            Utils.psLog("Photo : " + listResource.data.user_profile_photo);

                            if (pointMenuItem != null && getContext() != null) {
                                pointMenuItem.setTitle(getContext().getString(R.string.dashboard__pts, Utils.numberFormat(Long.parseLong(listResource.data.total_point))));
                            }

                        }

                        break;
                    case ERROR:
                        // Error State

                        Toast.makeText(getContext(), listResource.message, Toast.LENGTH_LONG).show();
                        Utils.psLog("*****************" + listResource.message);
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

                Utils.psLog("Empty Data");
                //Toast.makeText(getContext(), "Empty Data", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //endregion

}
