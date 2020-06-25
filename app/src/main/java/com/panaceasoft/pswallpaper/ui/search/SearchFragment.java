package com.panaceasoft.pswallpaper.ui.search;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;

import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.FragmentSearchBinding;
import com.panaceasoft.pswallpaper.ui.common.PSFragment;
import com.panaceasoft.pswallpaper.utils.AutoClearedValue;
import com.panaceasoft.pswallpaper.utils.Constants;
import com.panaceasoft.pswallpaper.utils.RangeSeekBar;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewobject.holder.WallpaperParamsHolder;

import java.util.Objects;

public class SearchFragment extends PSFragment {


    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private WallpaperParamsHolder wallpaperParamsHolder;
    private RangeSeekBar seekBar;
    private Drawable drawable;

    @VisibleForTesting
    private AutoClearedValue<FragmentSearchBinding> binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentSearchBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        drawable = Objects.requireNonNull(getActivity()).getResources().getDrawable(R.drawable.circular_shape);

        setHasOptionsMenu(true);

        return binding.get().getRoot();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ok_button, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.clearButton) {

            binding.get().setKeyWord.setText("");
            binding.get().setCategoryEditText.setText("");
            binding.get().colorEditText.setText("");
            binding.get().freeSwitch.setChecked(false);
            binding.get().premiumSwitch.setChecked(false);
            binding.get().maximumEditText.setText("");
            binding.get().minimumEditText.setText("");
            binding.get().isRecommendSwitch.setChecked(false);
            binding.get().isPortraitSwitch.setChecked(false);
            binding.get().isLandscapeSwitch.setChecked(false);
            binding.get().isSquareSwitch.setChecked(false);
            binding.get().isGifSwitch.setChecked(false);
            binding.get().isLiveWallpaperSwitch.setChecked(false);

            wallpaperParamsHolder = new WallpaperParamsHolder();
            wallpaperParamsHolder.orderType = "";
            wallpaperParamsHolder.orderBy = "";

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_CATEGORY) {
            wallpaperParamsHolder.catId = data.getStringExtra(Constants.INTENT__CAT_ID);
            binding.get().setCategoryEditText.setText(data.getStringExtra(Constants.INTENT__CAT_NAME));
        } else if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_COLOR) {
            wallpaperParamsHolder.colorId = data.getStringExtra(Constants.INTENT__COLOR_ID);
            wallpaperParamsHolder.colorName = data.getStringExtra(Constants.INTENT__COLOR_NAME);
            wallpaperParamsHolder.colorCode = data.getStringExtra(Constants.INTENT__COLOR_CODE);

            if (!wallpaperParamsHolder.colorCode.equals("")) {
                drawable.setColorFilter(Color.parseColor(data.getStringExtra(Constants.INTENT__COLOR_CODE)), PorterDuff.Mode.SRC_ATOP);

                binding.get().view11.setBackground(drawable);
            }
            binding.get().colorEditText.setText(data.getStringExtra(Constants.INTENT__COLOR_NAME));

        }

    }

    @Override
    protected void initUIAndActions() {

        seekBar = new RangeSeekBar<>(0, 5, this.getContext());

        LinearLayout linearLayout = binding.get().priceRangeBarContainer;
        linearLayout.addView(seekBar);

        seekBar.setSelectedMinValue(0);
        seekBar.setSelectedMaxValue(5);

        wallpaperParamsHolder = new WallpaperParamsHolder();

        seekBar.onStartTrackingTouch(binding.get().setKeyWord);

        seekBar.setOnRangeSeekBarChangeListener((bar, minValue, maxValue) -> {

            wallpaperParamsHolder.rating_max = maxValue.toString();
            wallpaperParamsHolder.rating_min = minValue.toString();

            binding.get().minRatingTextView.setText(wallpaperParamsHolder.rating_min);
            binding.get().maxRatingTextView.setText(wallpaperParamsHolder.rating_max);

        });

        binding.get().setCategoryEditText.setOnClickListener(v -> navigationController.navigateToCategorySelectionActivity(getActivity(), wallpaperParamsHolder.catId));

        binding.get().colorEditText.setOnClickListener(v -> navigationController.navigateToColorSelectionActivity(getActivity(), wallpaperParamsHolder.colorId));

        binding.get().view11.setOnClickListener(v -> navigationController.navigateToColorSelectionActivity(getActivity(), wallpaperParamsHolder.colorId));

        binding.get().filter.setOnClickListener(v -> {

            wallpaperParamsHolder.wallpaperName = binding.get().setKeyWord.getText().toString();
            wallpaperParamsHolder.keyword = binding.get().setKeyWord.getText().toString();

            wallpaperParamsHolder.catName = binding.get().setCategoryEditText.getText().toString();

            if (binding.get().freeSwitch.isChecked()) {
                wallpaperParamsHolder.type = Constants.ONE;
            }

            if (binding.get().premiumSwitch.isChecked()) {
                wallpaperParamsHolder.type = Constants.TWO;
            }

            if (binding.get().freeSwitch.isChecked() && binding.get().premiumSwitch.isChecked()) {
                wallpaperParamsHolder.type = Constants.THREE;
            } else if (!binding.get().freeSwitch.isChecked() && !binding.get().premiumSwitch.isChecked()) {
                wallpaperParamsHolder.type = "";
            }

            if (binding.get().minimumEditText.getText().toString().equals(getResources().getString(R.string.sf__notSet))) {
                wallpaperParamsHolder.point_min = "";
            } else {
                wallpaperParamsHolder.point_min = binding.get().minimumEditText.getText().toString();
            }

            if (binding.get().maximumEditText.getText().toString().equals(getResources().getString(R.string.sf__notSet))) {
                wallpaperParamsHolder.point_max = "";
            } else {
                wallpaperParamsHolder.point_max = binding.get().maximumEditText.getText().toString();
            }


            if (binding.get().isRecommendSwitch.isChecked()) {
                wallpaperParamsHolder.isRecommended = Constants.ONE;
            } else {
                wallpaperParamsHolder.isRecommended = "";
            }

            Utils.psLog(wallpaperParamsHolder.isRecommended + "recommended");

            if (binding.get().isPortraitSwitch.isChecked()) {
                wallpaperParamsHolder.isPortrait = Constants.ONE;
            } else {
                wallpaperParamsHolder.isPortrait = "";
            }

            if (binding.get().isLandscapeSwitch.isChecked()) {
                wallpaperParamsHolder.isLandscape = Constants.ONE;
            } else {
                wallpaperParamsHolder.isLandscape = "";
            }

            if (binding.get().isSquareSwitch.isChecked()) {
                wallpaperParamsHolder.isSquare = Constants.ONE;
            } else {
                wallpaperParamsHolder.isSquare = "";
            }

            if (binding.get().isGifSwitch.isChecked()) {
                wallpaperParamsHolder.isGif = Constants.ONE;
                wallpaperParamsHolder.isWallpaper = Constants.ZERO;
                wallpaperParamsHolder.isLiveWallpaper = Constants.ZERO;
            } else {
                wallpaperParamsHolder.isGif = "";
            }

            if (binding.get().isLiveWallpaperSwitch.isChecked()) {
                wallpaperParamsHolder.isLiveWallpaper = Constants.ONE;
                wallpaperParamsHolder.isWallpaper = Constants.ZERO;
                wallpaperParamsHolder.isGif = Constants.ZERO;
            } else {
                wallpaperParamsHolder.isLiveWallpaper = "";
            }

            if(!binding.get().isGifSwitch.isChecked() && !binding.get().isLiveWallpaperSwitch.isChecked()){
                wallpaperParamsHolder.isWallpaper = Constants.ONE;
            }

            Utils.psLog(wallpaperParamsHolder.isRecommended + "recommended");

            navigationController.navigateBackFromSearchActivity(getActivity(), wallpaperParamsHolder);

            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        binding.get().isGifSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.get().isRecommendSwitch.setChecked(false);
                binding.get().isPortraitSwitch.setChecked(false);
                binding.get().isLandscapeSwitch.setChecked(false);
                binding.get().isSquareSwitch.setChecked(false);
                binding.get().isLiveWallpaperSwitch.setChecked(false);
            }
        });

        binding.get().isLiveWallpaperSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.get().isRecommendSwitch.setChecked(false);
                binding.get().isPortraitSwitch.setChecked(false);
                binding.get().isLandscapeSwitch.setChecked(false);
                binding.get().isSquareSwitch.setChecked(false);
                binding.get().isGifSwitch.setChecked(false);
            }
        });

        binding.get().isRecommendSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.get().isGifSwitch.setChecked(false);
                binding.get().isLiveWallpaperSwitch.setChecked(false);
            }
        });

        binding.get().isPortraitSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.get().isGifSwitch.setChecked(false);
                binding.get().isLiveWallpaperSwitch.setChecked(false);
            }
        });

        binding.get().isLandscapeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.get().isGifSwitch.setChecked(false);
                binding.get().isLiveWallpaperSwitch.setChecked(false);
            }
        });

        binding.get().isSquareSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.get().isGifSwitch.setChecked(false);
                binding.get().isLiveWallpaperSwitch.setChecked(false);
            }
        });

        //Show or Hide Gif by EnableGif in Config file
        if(Config.ENABLE_GIF){
            binding.get().isGifButton.setVisibility(View.VISIBLE);
            binding.get().isGifSwitch.setVisibility(View.VISIBLE);
        }else{
            binding.get().isGifButton.setVisibility(View.GONE);
            binding.get().isGifSwitch.setVisibility(View.GONE);
        }

        if(Config.ENABLE_LIVE_WALLPAPER){
            binding.get().isLiveWallpaperButton.setVisibility(View.VISIBLE);
            binding.get().isLiveWallpaperSwitch.setVisibility(View.VISIBLE);
        }else{
            binding.get().isLiveWallpaperButton.setVisibility(View.GONE);
            binding.get().isLiveWallpaperSwitch.setVisibility(View.GONE);
        }

        if (Config.ENABLE_PREMIUM){
            binding.get().premiumButton.setVisibility(View.VISIBLE);
            binding.get().premiumSwitch.setVisibility(View.VISIBLE);
            binding.get().pointTextView.setVisibility(View.VISIBLE);
            binding.get().pointRangeConstraintLayout.setVisibility(View.VISIBLE);
        }else{
            binding.get().premiumButton.setVisibility(View.GONE);
            binding.get().premiumSwitch.setVisibility(View.GONE);
            binding.get().pointTextView.setVisibility(View.GONE);
            binding.get().pointRangeConstraintLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initViewModels() {

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        if (getActivity() != null) {
            wallpaperParamsHolder = (WallpaperParamsHolder) getActivity().getIntent().getSerializableExtra(Constants.INTENT__WALLPAPER_PARAM_HOLDER);

            if ((!wallpaperParamsHolder.keyword.equals("")) && !wallpaperParamsHolder.keyword.isEmpty()) {
                binding.get().setKeyWord.setText(wallpaperParamsHolder.keyword);
            }

            if ((!wallpaperParamsHolder.catName.equals("")) && !wallpaperParamsHolder.catName.isEmpty()) {
                binding.get().setCategoryEditText.setText(wallpaperParamsHolder.catName);
            }

            if ((!wallpaperParamsHolder.colorName.equals("")) && !wallpaperParamsHolder.colorName.isEmpty()) {
                binding.get().colorEditText.setText(wallpaperParamsHolder.colorName);
            }

            if ((!wallpaperParamsHolder.colorCode.equals("")) && !wallpaperParamsHolder.colorCode.isEmpty()) {

                drawable.setColorFilter(Color.parseColor(wallpaperParamsHolder.colorCode), PorterDuff.Mode.SRC_ATOP);

                binding.get().view11.setBackground(drawable);
            }

            if ((!wallpaperParamsHolder.catName.equals("")) && !wallpaperParamsHolder.catName.isEmpty()) {
                binding.get().setCategoryEditText.setText(wallpaperParamsHolder.catName);
            }

            if ((!wallpaperParamsHolder.type.equals("")) && !wallpaperParamsHolder.type.isEmpty()) {
                switch (wallpaperParamsHolder.type) {
                    case Constants.ONE:
                        binding.get().freeSwitch.setChecked(true);
                        break;
                    case Constants.TWO:
                        binding.get().premiumSwitch.setChecked(true);
                        break;
                    case Constants.THREE:
                        binding.get().freeSwitch.setChecked(true);
                        binding.get().premiumSwitch.setChecked(true);
                        break;
                }
            }

            if ((!wallpaperParamsHolder.point_min.equals("")) && !wallpaperParamsHolder.point_min.isEmpty()) {

                binding.get().minimumEditText.setText(wallpaperParamsHolder.point_min);
            }

            if ((!wallpaperParamsHolder.point_max.equals("")) && !wallpaperParamsHolder.point_max.isEmpty()) {

                binding.get().maximumEditText.setText(wallpaperParamsHolder.point_max);
            }

            if ((!wallpaperParamsHolder.rating_min.equals("")) && !wallpaperParamsHolder.rating_min.isEmpty()) {
                binding.get().minRatingTextView.setText(wallpaperParamsHolder.rating_min);
                seekBar.setSelectedMinValue(Integer.parseInt(wallpaperParamsHolder.rating_min));
            }

            if ((!wallpaperParamsHolder.rating_max.equals("")) && !wallpaperParamsHolder.rating_max.isEmpty()) {
                binding.get().maxRatingTextView.setText(wallpaperParamsHolder.rating_max);
                seekBar.setSelectedMaxValue(Integer.parseInt(wallpaperParamsHolder.rating_max));
            }


            if ((!wallpaperParamsHolder.isRecommended.equals("")) && !wallpaperParamsHolder.isRecommended.isEmpty()) {
                if (wallpaperParamsHolder.isRecommended.equals(Constants.ONE)) {
                    binding.get().isRecommendSwitch.setChecked(true);
                } else {
                    binding.get().isRecommendSwitch.setChecked(false);
                }
            }

            if ((!wallpaperParamsHolder.isPortrait.equals("")) && !wallpaperParamsHolder.isPortrait.isEmpty()) {
                if (wallpaperParamsHolder.isPortrait.equals(Constants.ONE)) {
                    binding.get().isPortraitSwitch.setChecked(true);
                } else {
                    binding.get().isPortraitSwitch.setChecked(false);
                }
            }

            if ((!wallpaperParamsHolder.isLandscape.equals("")) && !wallpaperParamsHolder.isLandscape.isEmpty()) {
                if (wallpaperParamsHolder.isLandscape.equals(Constants.ONE)) {
                    binding.get().isLandscapeSwitch.setChecked(true);
                } else {
                    binding.get().isLandscapeSwitch.setChecked(false);
                }
            }

            if ((!wallpaperParamsHolder.isSquare.equals("")) && !wallpaperParamsHolder.isSquare.isEmpty()) {
                if (wallpaperParamsHolder.isSquare.equals(Constants.ONE)) {
                    binding.get().isSquareSwitch.setChecked(true);
                } else {
                    binding.get().isSquareSwitch.setChecked(false);
                }
            }

            if ((!wallpaperParamsHolder.isGif.equals("")) && !wallpaperParamsHolder.isGif.isEmpty()) {
                if (wallpaperParamsHolder.isGif.equals(Constants.ONE)) {
                    binding.get().isGifSwitch.setChecked(true);
                } else {
                    binding.get().isGifSwitch.setChecked(false);
                }
            }

            if ((!wallpaperParamsHolder.isLiveWallpaper.equals("")) && !wallpaperParamsHolder.isLiveWallpaper.isEmpty()) {
                if (wallpaperParamsHolder.isLiveWallpaper.equals(Constants.ONE)) {
                    binding.get().isLiveWallpaperSwitch.setChecked(true);
                } else {
                    binding.get().isLiveWallpaperSwitch.setChecked(false);
                }
            }
        }
    }
}
