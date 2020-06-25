package com.panaceasoft.pswallpaper.ui.language;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.MainActivity;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.FragmentLanguageBinding;
import com.panaceasoft.pswallpaper.ui.apploading.AppLoadingActivity;
import com.panaceasoft.pswallpaper.ui.common.PSFragment;
import com.panaceasoft.pswallpaper.utils.AutoClearedValue;
import com.panaceasoft.pswallpaper.utils.Constants;
import com.panaceasoft.pswallpaper.utils.LanguageData;
import com.panaceasoft.pswallpaper.utils.PSDialogMsg;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class LanguageFragment extends PSFragment {

    //region Variables

    @Inject
    SharedPreferences sharedPreferences;

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private String LANG_CURRENT,CURRENT_COUNTRY_CODE;
    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentLanguageBinding> binding;
    private int selectedPosition = 0;

    private List<LanguageData> languageDataList = Arrays.asList(
            new LanguageData("English", "en", ""),
            new LanguageData("Arabic", "ar", ""),
            new LanguageData("Chinese (Mandarin)", "zh", ""),
            new LanguageData("French", "fr", ""),
            new LanguageData("German", "de", ""),
            new LanguageData("India (Hindi)", "hi", "rIN"),
            new LanguageData("Indonesian", "in", ""),
            new LanguageData("Italian", "it", ""),
            new LanguageData("Japanese", "ja", ""),
            new LanguageData("Korean", "ko", ""),
            new LanguageData("Malay", "ms", ""),
            new LanguageData("Portuguese", "pt", ""),
            new LanguageData("Russian", "ru", ""),
            new LanguageData("Spanish", "es", ""),
            new LanguageData("Thai", "th", ""),
            new LanguageData("Turkish", "tr", ""));

    //endregion

    //region Override Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentLanguageBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_language, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        LANG_CURRENT = sharedPreferences.getString(Constants.LANGUAGE, Config.DEFAULT_LANGUAGE);
        CURRENT_COUNTRY_CODE = sharedPreferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);

        checkSelectedLanguage(LANG_CURRENT,CURRENT_COUNTRY_CODE);

        if (selectedPosition == 0) {
            if (languageDataList != null && languageDataList.size() > 0) {
                binding.get().languageTextView.setText(languageDataList.get(0).languageName);
            }
        }
        return binding.get().getRoot();
    }

    private void checkSelectedLanguage(String LANG_CURRENT, String CURRENT_COUNTRY_CODE){

        for (int i = 0; i < languageDataList.size(); i++) {
            LanguageData languageData = languageDataList.get(i);
            if (LANG_CURRENT.equals(languageData.languageLocalCode) && CURRENT_COUNTRY_CODE.equals(languageData.languageCountry)) {
                selectedPosition = i;
                binding.get().languageTextView.setText(languageData.languageName);
                break;
            }

        }
    }

    @Override
    protected void initUIAndActions() {
        if(getActivity() instanceof MainActivity)  {
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        //fadeIn Animation
        fadeIn(binding.get().getRoot());

        binding.get().cardLanguageView.setOnClickListener(v -> {
            LanguageSelectionListAdapter adapter = new LanguageSelectionListAdapter(languageDataList, getContext(), selectedPosition);

            if (getContext() != null) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setTitle(getString(R.string.language__title));
                mBuilder.setSingleChoiceItems(adapter, -1, (dialogInterface, i) -> {

                    psDialogMsg.showConfirmDialog(getString(R.string.language__language_change, languageDataList.get(i).languageName), getString(R.string.app__ok), getString(R.string.app__cancel));

                    psDialogMsg.show();

                    psDialogMsg.okButton.setOnClickListener(v1 -> {
                        changeLang(languageDataList.get(i).languageLocalCode, languageDataList.get(i).languageCountry);
                        if (getActivity() != null) {
                            getActivity().finish();
                            startActivity(new Intent(getActivity(), AppLoadingActivity.class));
                            psDialogMsg.cancel();
                        }
                    });

                    psDialogMsg.cancelButton.setOnClickListener(v1 -> {
                        psDialogMsg.cancel();
                        checkSelectedLanguage(LANG_CURRENT,CURRENT_COUNTRY_CODE);
                    });
                    binding.get().languageTextView.setText(languageDataList.get(i).languageName);
                    dialogInterface.dismiss();
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

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

    private void changeLang(String lang, String countryCode) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.LANGUAGE_CODE, lang);
        editor.putString(Constants.LANGUAGE_COUNTRY_CODE, countryCode);
        editor.apply();
    }

}
