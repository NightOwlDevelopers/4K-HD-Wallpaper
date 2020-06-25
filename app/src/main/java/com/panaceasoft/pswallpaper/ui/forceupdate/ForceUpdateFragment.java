package com.panaceasoft.pswallpaper.ui.forceupdate;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.FragmentForceUpdateBinding;
import com.panaceasoft.pswallpaper.ui.common.PSFragment;
import com.panaceasoft.pswallpaper.utils.AutoClearedValue;
import com.panaceasoft.pswallpaper.utils.Constants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;

public class ForceUpdateFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    @VisibleForTesting
    private AutoClearedValue<FragmentForceUpdateBinding> binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentForceUpdateBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_force_update, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        if (getActivity() != null) {
            if (getActivity().getIntent().hasExtra(Constants.APPINFO_FORCE_UPDATE_MSG)) {
                binding.get().titleTextView.setText(getActivity().getIntent().getStringExtra(Constants.APPINFO_FORCE_UPDATE_TITLE));
                binding.get().descriptionTextView.setText(getActivity().getIntent().getStringExtra(Constants.APPINFO_FORCE_UPDATE_MSG));

                binding.get().descriptionTextView.setMovementMethod(new ScrollingMovementMethod());
            }
        }

        binding.get().button4.setOnClickListener(v -> navigationController.navigateToPlayStore(getActivity()));
    }

    @Override
    protected void initViewModels() {

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

    }
}
