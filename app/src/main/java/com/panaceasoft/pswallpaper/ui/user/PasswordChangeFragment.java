package com.panaceasoft.pswallpaper.ui.user;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.FragmentPasswordChangeBinding;
import com.panaceasoft.pswallpaper.ui.common.PSFragment;
import com.panaceasoft.pswallpaper.utils.AutoClearedValue;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewmodel.user.UserViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

/**
 * PasswordChangeFragment
 */
public class PasswordChangeFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    @VisibleForTesting
    AutoClearedValue<FragmentPasswordChangeBinding> binding;

    AutoClearedValue<ProgressDialog> prgDialog;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentPasswordChangeBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_password_change, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        prgDialog.get().setMessage(getString(R.string.message__please_wait));
        prgDialog.get().setCancelable(false);

        //fadeIn Animation
        fadeIn(binding.get().getRoot());

        binding.get().saveButton.setOnClickListener(view -> passwordUpdate());


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
            binding.get().saveButton.setText(getResources().getString(R.string.message__loading));
        } else {
            binding.get().saveButton.setText(getResources().getString(R.string.password_change__save));
        }

    }

    private void passwordUpdate() {

        Utils.hideKeyboard(getActivity());

        if (!connectivity.isConnected()) {

            Toast.makeText(getContext(), R.string.error_message__no_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        String password = binding.get().passwordEditText.getText().toString().trim();
        String confirmPassword = binding.get().confirmPasswordEditText.getText().toString().trim();
        if (password.equals("") || confirmPassword.equals("")) {
            Toast.makeText(getContext(), getString(R.string.error_message__blank_password), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!(password.equals(confirmPassword))) {
            Toast.makeText(getContext(), getString(R.string.error_message__password_not_equal), Toast.LENGTH_SHORT).show();
            return;
        }


        userViewModel.isLoading = true;

        userViewModel.passwordUpdate(loginUserId, password).observe(this, listResource -> {

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
//
//            // we don't need any null checks here for the adapter since LiveData guarantees that
//            // it won't call us if fragment is stopped or not started.
//            if (listResource != null && listResource.data != null) {
//                Utils.psLog("Got Data" + listResource.message + listResource.toString());
//
//
//                if (listResource.message != null && !listResource.message.equals("")) {
//                    Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
//                    userViewModel.isLoading = false;
//                    prgDialog.get().cancel();
//                } else {
//                    // Update the data
//                    Toast.makeText(getContext(), listResource.data.message, Toast.LENGTH_SHORT).show();
//                    userViewModel.isLoading = false;
//                    prgDialog.get().cancel();
//                }
//
//            } else if (listResource != null && listResource.message != null) {
//                Utils.psLog("Message from server.");
//                Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
//
//                userViewModel.isLoading = false;
//                prgDialog.get().cancel();
//            } else {
//
//                Utils.psLog("Empty Data");
//            }
//
//            updateForgotBtnStatus();

        });
    }

    //endregion

}