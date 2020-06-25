package com.panaceasoft.pswallpaper.binding;


import android.app.Activity;

import androidx.fragment.app.Fragment;

/**
 * A Data Binding Component implementation for fragments.
 */
public class FragmentDataBindingComponent implements androidx.databinding.DataBindingComponent {
    private final FragmentBindingAdapters adapter;

    public FragmentDataBindingComponent(Fragment fragment) {
        this.adapter = new FragmentBindingAdapters(fragment, null);
    }

    public FragmentDataBindingComponent(Activity fragment) {
        this.adapter = new FragmentBindingAdapters(null, fragment);
    }

    @Override
    public FragmentBindingAdapters getFragmentBindingAdapters() {
        return adapter;
    }
}
