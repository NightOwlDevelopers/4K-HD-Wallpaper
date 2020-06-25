package com.panaceasoft.pswallpaper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.ActivityMainWalkthroughBinding;
import com.panaceasoft.pswallpaper.ui.mainwalkthrough.MainWalkthroughAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

public class MainWalkThroughtActivity extends AppCompatActivity {

    private int[] welcomeScreen;
    private androidx.databinding.DataBindingComponent dataBindingComponent;
    private ActivityMainWalkthroughBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBindingComponent = new FragmentDataBindingComponent(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_walkthrough, dataBindingComponent);

        initUI();

        initActions();

    }

    private void initUI() {

        welcomeScreen = new int[]{
                R.layout.main_walkthrough_slider_1,
                R.layout.main_walkthrough_slider_2,
                R.layout.main_walkthrough_slider_3,
                R.layout.main_walkthrough_slider_4

        };

        addPagination(0);

    }

    private void initActions() {

        ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                addPagination(position);

                if (position == welcomeScreen.length - 1) {
                    binding.nextButton.setVisibility(View.GONE);
                    binding.skipButton.setVisibility(View.GONE);
                    binding.dotsLinearLayout.setVisibility(View.GONE);
                    binding.futureCheckbox.setVisibility(View.VISIBLE);
                    binding.explorerConstraintLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.nextButton.setText(getString(R.string.walk_through__next));
                    binding.nextButton.setVisibility(View.VISIBLE);
                    binding.skipButton.setVisibility(View.VISIBLE);
                    binding.dotsLinearLayout.setVisibility(View.VISIBLE);
                    binding.futureCheckbox.setVisibility(View.GONE);
                    binding.explorerConstraintLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        };


        //    private ViewPager viewPager;
        MainWalkthroughAdapter mainWalkthroughtAdapter = new MainWalkthroughAdapter(welcomeScreen, getApplicationContext(), dataBindingComponent);
        binding.viewPager.setAdapter(mainWalkthroughtAdapter);
        binding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        binding.skipButton.setOnClickListener(v -> finish());

        binding.nextButton.setOnClickListener(v -> {

            int current = getItem(+1);
            if (current < welcomeScreen.length) {
                binding.viewPager.setCurrentItem(current);
            }
        });

        binding.letExploreTextView.setOnClickListener(v -> {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("checked", !binding.futureCheckbox.isChecked());
            editor.apply();
            //launchApp();

            finish();
        });

    }

    private void addPagination(int currentPage) {
        TextView[] pages = new TextView[welcomeScreen.length];

        binding.dotsLinearLayout.removeAllViews();
        for (int i = 0; i < pages.length; i++) {
            pages[i] = new TextView(this);
            pages[i].setText(Html.fromHtml("&#8226;"));
            pages[i].setTextSize(35);
            pages[i].setTextColor(getResources().getColor(R.color.md_grey_300));
            binding.dotsLinearLayout.addView(pages[i]);
        }

        if (pages.length > 0) {
            pages[currentPage].setTextColor(getResources().getColor(R.color.md_yellow_600));
        }
    }

    private int getItem(int i) {
        return binding.viewPager.getCurrentItem() + i;
    }

}
