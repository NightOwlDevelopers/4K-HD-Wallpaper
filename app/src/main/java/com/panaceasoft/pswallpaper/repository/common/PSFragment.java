package com.panaceasoft.pswallpaper.repository.common;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.di.Injectable;
import com.panaceasoft.pswallpaper.ui.common.NavigationController;
import com.panaceasoft.pswallpaper.utils.Connectivity;
import com.panaceasoft.pswallpaper.utils.Utils;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.BindingAdapter;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.VisibleForTesting;
import javax.inject.Inject;

/**
 * Parent class for all fragment in this project.
 * Created by Panacea-Soft on 12/2/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

public abstract class PSFragment extends Fragment implements Injectable {

    //region Variables

    @Inject
    protected ViewModelProvider.Factory viewModelFactory;

    @Inject
    protected NavigationController navigationController;

    @Inject
    protected Connectivity connectivity;

    @Inject
    protected SharedPreferences pref;

    protected String loginUserId;

    private boolean isFadeIn = false;

    //endregion


    //region Override Methods
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadLoginUserId();

        initViewModels();

        initUIAndActions();

        initAdapters();

        initData();

    }

    //endregion


    //region Methods

    protected void loadLoginUserId(){
        try {

            if(getActivity() != null && getActivity().getBaseContext() != null) {
                loginUserId = pref.getString("user_id", "");
            }

        }catch (NullPointerException ne){
            Utils.psErrorLog("Null Pointer Exception.", ne);
        }catch(Exception e){
            Utils.psErrorLog("Error in getting notification flag data.", e);
        }
    }

    protected abstract void initUIAndActions();

    protected abstract void initViewModels();

    protected abstract void initAdapters();

    protected abstract void initData();

    protected void fadeIn(View view) {

        if(!isFadeIn) {
            view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
            isFadeIn = true; // Fade in will do only one time.
        }
    }
    //endregion

}
