package com.panaceasoft.pswallpaper.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.FragmentProfileEditBinding;
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
 * ProfileEditFragment
 */
public class ProfileEditFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentProfileEditBinding> binding;
    private AutoClearedValue<ProgressDialog> prgDialog;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        FragmentProfileEditBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_edit, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
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

        userViewModel.getLoginUser().observe(this, listResource -> {
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.size() > 0) {
                Utils.psLog("Got Data");

                //fadeIn Animation
                fadeIn(binding.get().getRoot());

                binding.get().setUser(listResource.get(0).user);
                userViewModel.user = listResource.get(0).user;
                Utils.psLog("Photo : " + listResource.get(0).user.user_profile_photo);
            } else {

                Utils.psLog("Empty Data");
                //Toast.makeText(getContext(), "Empty Data", Toast.LENGTH_SHORT).show();

            }
        });

        getDeviceToken();

    }

    private void getDeviceToken() {
        userViewModel.deviceToken = pref.getString(Utils.NOTI_TOKEN, "");
    }

    @Override
    protected void initUIAndActions() {

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        prgDialog.get().setMessage(getString(R.string.message__please_wait));
        prgDialog.get().setCancelable(false);

        binding.get().profileImageview.setOnClickListener(view -> {

            if (connectivity.isConnected()) {
                try {

                    if (Utils.isStoragePermissionGranted(getActivity())) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, Utils.RESULT_LOAD_IMAGE);
                    }
                } catch (Exception e) {
                    Utils.psErrorLog("Error in Image Gallery.", e);
                }
            } else {
                Toast.makeText(getContext(), R.string.error_message__no_internet, Toast.LENGTH_SHORT).show();
            }

        });

        binding.get().saveButton.setOnClickListener(view -> editProfileData());

        binding.get().passwordChangeButton.setOnClickListener(view -> navigationController.navigateToPasswordChangeActivity(getActivity()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == Utils.RESULT_LOAD_IMAGE && resultCode == Utils.RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                if (getActivity() != null && selectedImage != null) {
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    if (cursor != null) {
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        userViewModel.profileImagePath = cursor.getString(columnIndex);
                        cursor.close();

                        uploadImage();
                    }
                }

            }

        } catch (Exception e) {
            Utils.psErrorLog("Error in load image.", e);
        }


    }


    //endregion


    //region Private Methods


    private void editProfileData() {

        if (!connectivity.isConnected()) {
            Toast.makeText(getContext(), R.string.error_message__no_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        String userName = binding.get().nameEditText.getText().toString();
        if (userName.equals("")) {
            Toast.makeText(getContext(), R.string.error_message__blank_name, Toast.LENGTH_SHORT).show();
            return;
        }

        String userEmail = binding.get().emailEditText.getText().toString();
        if (userEmail.equals("")) {
            Toast.makeText(getContext(), R.string.error_message__blank_email, Toast.LENGTH_SHORT).show();
            return;
        }

        String userPhone = binding.get().phoneEditText.getText().toString();
        String userAboutMe = binding.get().aboutMeEditText.getText().toString();

        if (userViewModel.user != null) {
            prgDialog.get().show();
            userViewModel.user.user_name = userName;
            userViewModel.user.user_email = userEmail;
            userViewModel.user.user_phone = userPhone;
            userViewModel.user.user_about_me = userAboutMe;

            userViewModel.updateUser(userViewModel.user, userViewModel.deviceToken).observe(this, listResource -> {

                if (listResource != null) {

                    Utils.psLog("Got Data" + listResource.message + listResource.toString());

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            break;
                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                Toast.makeText(getContext(), listResource.data.message, Toast.LENGTH_SHORT).show();
                                prgDialog.get().cancel();

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


//                // we don't need any null checks here for the adapter since LiveData guarantees that
//                // it won't call us if fragment is stopped or not started.
//                if (listResource != null && listResource.data != null) {
//                    Utils.psLog("Got Data" + listResource.message + listResource.toString());
//
//
//                    if (listResource.message != null && !listResource.message.equals("")) {
//                        Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
//                        prgDialog.get().cancel();
//                    } else {
//                        // Update the data
//                        Toast.makeText(getContext(), listResource.data.message, Toast.LENGTH_SHORT).show();
//                        prgDialog.get().cancel();
//                    }
//
//                } else if (listResource != null && listResource.message != null) {
//                    Utils.psLog("Message from server.");
//                    Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
//
//                    prgDialog.get().cancel();
//                } else {
//
//                    Utils.psLog("Empty Data");
//
//                }

            });
        }
    }

    private void uploadImage() {

        prgDialog.get().show();
        Utils.psLog("Uploading Image.");

        userViewModel.uploadImage(userViewModel.profileImagePath, loginUserId).observe(this, listResource -> {
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString());


                if (listResource.message != null && !listResource.message.equals("")) {
                    Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
                    prgDialog.get().cancel();
                } else {
                    // Update the data
                    Toast.makeText(getContext(), listResource.data.user_profile_photo, Toast.LENGTH_SHORT).show();
                    prgDialog.get().cancel();
                }

            } else if (listResource != null && listResource.message != null) {
                Utils.psLog("Message from server.");
                Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();

                prgDialog.get().cancel();
            } else {

                Utils.psLog("Empty Data");

            }

        });
    }


    //endregion
}
