package com.panaceasoft.pswallpaper.ui.contactus;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.FragmentContactUsBinding;
import com.panaceasoft.pswallpaper.ui.common.PSFragment;
import com.panaceasoft.pswallpaper.utils.AutoClearedValue;
import com.panaceasoft.pswallpaper.viewmodel.contactus.ContactUsViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class ContactUsFragment extends PSFragment {
    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ContactUsViewModel contactUsViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentContactUsBinding> binding;

    private AutoClearedValue<ProgressDialog> prgDialog;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentContactUsBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_us, container, false, dataBindingComponent);

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

        binding.get().btnSubmit.setOnClickListener(view -> {

            if (connectivity.isConnected()) {
                String contactName = binding.get().contactNameTextInput.getText().toString().trim();
                String contactEmail = binding.get().contactEmailTextInput.getText().toString().trim();
                String contactDesc = binding.get().contactDescEditText.getText().toString().trim();
                String contactPhone = binding.get().contactPhoneTextInput.getText().toString().trim();

                if (contactName.equals("")) {
                    Toast.makeText(getContext(), getString(R.string.error_message__blank_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (contactEmail.equals("")) {
                    Toast.makeText(getContext(), getString(R.string.error_message__blank_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (contactDesc.equals("")) {
                    Toast.makeText(getContext(), getString(R.string.error_message__blank_contact_message), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!contactUsViewModel.isLoading) {
                    contactUsViewModel.contactName = contactName;
                    contactUsViewModel.contactEmail = contactEmail;
                    contactUsViewModel.contactDesc = contactDesc;
                    contactUsViewModel.contactPhone = contactPhone;
                    doSubmit();
                }

            } else {
                Toast.makeText(getContext(), R.string.error_message__no_internet, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initViewModels() {
        contactUsViewModel = new ViewModelProvider(this, viewModelFactory).get(ContactUsViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

    }

    //endregion

    //region Methods

    private void doSubmit() {

        prgDialog.get().show();

        contactUsViewModel.postContactUs(Config.API_KEY, contactUsViewModel.contactName, contactUsViewModel.contactEmail, contactUsViewModel.contactDesc, contactUsViewModel.contactPhone);

        contactUsViewModel.getLoadingStatus().observe(this, submitStatus -> {
            if (submitStatus != null) {

                String error = submitStatus.getErrorMessageIfNotHandled();
                if (error != null) {

                    //Error Case
                    if (getView() != null) {
                        Snackbar.make(getView(), error, Snackbar.LENGTH_LONG).show();
                    }
                    prgDialog.get().cancel();
                } else {

                    if (!submitStatus.isRunning()) {
                        //Success
                        binding.get().contactNameTextInput.setText("");
                        binding.get().contactEmailTextInput.setText("");
                        binding.get().contactPhoneTextInput.setText("");
                        binding.get().contactDescEditText.setText("");

                        Toast.makeText(getContext(), "Your message has been sent.", Toast.LENGTH_SHORT).show();


                        prgDialog.get().cancel();
                    }

                }

            }
            binding.get().executePendingBindings();
        });

    }

    //endregion

}
