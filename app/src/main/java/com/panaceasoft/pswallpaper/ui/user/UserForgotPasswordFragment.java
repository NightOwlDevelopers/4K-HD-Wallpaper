package com.panaceasoft.pswallpaper.ui.user;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.FragmentUserForgotPasswordBinding;
import com.panaceasoft.pswallpaper.ui.common.PSFragment;
import com.panaceasoft.pswallpaper.utils.AutoClearedValue;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewmodel.user.UserViewModel;

/**
 * UserForgotPasswordFragment
 */
public class UserForgotPasswordFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    @VisibleForTesting
    AutoClearedValue<FragmentUserForgotPasswordBinding> binding;

    AutoClearedValue<ProgressDialog> prgDialog;


    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentUserForgotPasswordBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_forgot_password, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        prgDialog.get().setMessage(getString(R.string.message__please_wait));
        prgDialog.get().setCancelable(false);


        dataBindingComponent.getFragmentBindingAdapters().bindFullImageDrawbale(binding.get().bgImageView, getResources().getDrawable(R.drawable.wallpaper_login_bg));

        //fadeIn Animation
        fadeIn(binding.get().getRoot());


        binding.get().loginButton.setOnClickListener(view ->
                Utils.navigateAfterLogin(UserForgotPasswordFragment.this.getActivity(), navigationController));

        binding.get().forgotPasswordButton.setOnClickListener(view -> {
            if (connectivity.isConnected()) {
                forgotPassword();
            } else {
                Toast.makeText(getContext(), R.string.error_message__no_internet, Toast.LENGTH_SHORT).show();
            }
        });

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

    }

    //endregion


    //region Private Methods

    private void updateForgotBtnStatus() {
        if (userViewModel.isLoading) {
            binding.get().forgotPasswordButton.setText(getResources().getString(R.string.message__loading));
        } else {
            binding.get().forgotPasswordButton.setText(getResources().getString(R.string.forgot_password__title));
        }
    }

    private void forgotPassword() {

        Utils.hideKeyboard(getActivity());

        String email = binding.get().emailEditText.getText().toString().trim();
        if (email.equals("")) {
            Toast.makeText(getContext(), getString(R.string.error_message__blank_email), Toast.LENGTH_SHORT).show();
            return;
        }

        userViewModel.isLoading = true;

        userViewModel.forgotPassword(email).observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        prgDialog.get().show();
                        updateForgotBtnStatus();

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            Toast.makeText(getContext(), listResource.data.message, Toast.LENGTH_SHORT).show();
                            userViewModel.isLoading = false;
                            prgDialog.get().cancel();

                            updateForgotBtnStatus();
                        }

                        break;
                    case ERROR:
                        // Error State

                        Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();

                        userViewModel.isLoading = false;

                        prgDialog.get().cancel();

                        break;
                    default:

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }
        });
    }

    //endregion


}
