package com.panaceasoft.pswallpaper.ui.dashboard.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.databinding.ItemTagAdapterBinding;
import com.panaceasoft.pswallpaper.ui.common.DataBoundListAdapter;
import com.panaceasoft.pswallpaper.utils.Objects;
import com.panaceasoft.pswallpaper.viewobject.Wallpaper;

import androidx.databinding.DataBindingUtil;

public class TagAdapter extends DataBoundListAdapter<String,ItemTagAdapterBinding>{

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    public TagAdapter.AllWallpapersByCategoryClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;

    public TagAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                      TagAdapter.AllWallpapersByCategoryClickCallback callback) {

        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;

    }

    @Override
    protected ItemTagAdapterBinding createBinding(ViewGroup parent) {
        ItemTagAdapterBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_tag_adapter, parent, false,
                dataBindingComponent);


        binding.getRoot().setOnClickListener(v -> {
            String tag = binding.getTags();
            if (tag != null && callback != null) {
                callback.onClick(tag);
            }
        });
        return  binding;
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemTagAdapterBinding binding, String item) {
        binding.setTags(item);
    }

    @Override
    protected boolean areItemsTheSame(String oldItem, String newItem) {
        return Objects.equals(oldItem, newItem);
    }

    @Override
    protected boolean areContentsTheSame(String oldItem, String newItem) {
        return Objects.equals(oldItem, newItem);
    }

    public interface AllWallpapersByCategoryClickCallback {
        void onClick(String tag);
    }
}

