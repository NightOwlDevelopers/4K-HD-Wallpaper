package com.panaceasoft.pswallpaper.binding;


import android.view.View;

import androidx.databinding.BindingAdapter;

/**
 * Data Binding adapters specific to the app.
 */
public class BindingAdapters {
    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
