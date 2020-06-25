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
import com.panaceasoft.pswallpaper.databinding.FragmentUserRegisterBinding;
import com.panaceasoft.pswallpaper.ui.common.PSFragment;
import com.panaceasoft.pswallpaper.utils.AutoClearedValue;
import com.panaceasoft.pswallpaper.utils.PSDialogMsg;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewmodel.user.UserViewModel;
import com.panaceasoft.pswallpaper.viewobject.User;

/**
 * UserRegisterFragment
 */
public class UserRegisterFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    private PSDialogMsg psDialogMsg;

    private boolean checkFlag;

    @VisibleForTesting
    AutoClearedValue<FragmentUserRegisterBinding> binding;

    AutoClearedValue<ProgressDialog> prgDialog;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentUserRegisterBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_register, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }


    @Override
    protected void initUIAndActions() {

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        prgDialog.get().setMessage(getString(R.string.message__please_wait));
        prgDialog.get().setCancelable(false);

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        dataBindingComponent.getFragmentBindingAdapters().bindFullImageDrawbale(binding.get().bgImageView, getResources().getDrawable(R.drawable.wallpaper_login_bg));

        //fadeIn Animation
        fadeIn(binding.get().getRoot());

        binding.get().loginButton.setOnClickListener(view -> {

            if (connectivity.isConnected()) {

                Utils.navigateAfterLogin(UserRegisterFragment.this.getActivity(), navigationController);

            } else {
                psDialogMsg.showWarningDialog(getString(R.string.error_message__no_internet), getString(R.string.app__ok));

                psDialogMsg.show();
            }

        });

        binding.get().registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFlag) {
                    UserRegisterFragment.this.registerUser();
                } else {

                    psDialogMsg.showWarningDialog(getString(R.string.error_message__to_check_agreement), getString(R.string.app__ok));
                    psDialogMsg.show();

                }

            }
        });

        binding.get().policyAndPrivacyCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.get().policyAndPrivacyCheckBox.isChecked()) {
                    navigationController.navigateToPrivacyPolicyActivity(getActivity());
                    checkFlag = true;
                } else {
                    checkFlag = false;
                }
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
        getDeviceToken();
    }

    private void getDeviceToken() {
        userViewModel.deviceToken = pref.getString(Utils.NOTI_TOKEN, "");
    }
    //endregion


    //region Private Methods

    private void updateRegisterBtnStatus() {
        if (userViewModel.isLoading) {
            binding.get().registerButton.setText(getResources().getString(R.string.message__loading));
        } else {
            binding.get().registerButton.setText(getResources().getString(R.string.register__register));
        }
    }

    private void registerUser() {

        Utils.hideKeyboard(getActivity());

        String userName = binding.get().nameEditText.getText().toString().trim();
        if (userName.equals("")) {
            Toast.makeText(getContext(), getString(R.string.error_message__blank_name), Toast.LENGTH_SHORT).show();
            return;
        }

        String userEmail = binding.get().emailEditText.getText().toString().trim();
        if (userEmail.equals("")) {
            Toast.makeText(getContext(), getString(R.string.error_message__blank_email), Toast.LENGTH_SHORT).show();
            return;
        }

        String userPassword = binding.get().passwordEditText.getText().toString().trim();
        if (userPassword.equals("")) {
            Toast.makeText(getContext(), getString(R.string.error_message__blank_password), Toast.LENGTH_SHORT).show();
            return;
        }


        userViewModel.isLoading = true;
        updateRegisterBtnStatus();

        userViewModel.registerUser(new User("",
                "",
                "",
                "",
                userName,
                userEmail,
                "",
                userPassword,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""), userViewModel.deviceToken).observe(this, listResource -> {


            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        prgDialog.get().show();

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            try {
                                if (getActivity() != null) {

                                    Utils.registerUserLoginData(pref,listResource.data,binding.get().passwordEditText.getText().toString());
                                    Utils.navigateAfterUserRegister(getActivity(),navigationController);

                                }

                            } catch (NullPointerException ne) {
                                Utils.psErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Utils.psErrorLog("Error in getting notification flag data.", e);
                            }

                            userViewModel.isLoading = false;
                            prgDialog.get().cancel();
                            updateRegisterBtnStatus();

                        }

                        break;
                    case ERROR:
                        // Error State

                        Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
                        binding.get().registerButton.setText(getResources().getString(R.string.register__register));

                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();

                        break;
                    default:
                        // Default
                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();
                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }
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
//                    Toast.makeText(getContext(), getString(R.string.message__register_success), Toast.LENGTH_SHORT).show();
//
//
//
//
//
//                }
//
//            } else if (listResource != null && listResource.message != null) {
//                Utils.psLog("Message from server.");
//
//
//                userViewModel.isLoading = false;
//                prgDialog.get().cancel();
//            } else {
//
//                Utils.psLog("Empty Data");
//
//            }

//            updateRegisterBtnStatus();

        });

    }

    //endregion


}

