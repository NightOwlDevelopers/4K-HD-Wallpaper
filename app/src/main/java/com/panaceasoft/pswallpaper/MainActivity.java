package com.panaceasoft.pswallpaper;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.login.LoginManager;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.panaceasoft.pswallpaper.databinding.ActivityMainBinding;
import com.panaceasoft.pswallpaper.ui.common.NavigationController;
import com.panaceasoft.pswallpaper.ui.common.PSAppCompactActivity;
import com.panaceasoft.pswallpaper.ui.dashboard.DashboardFragment;
import com.panaceasoft.pswallpaper.utils.Constants;
import com.panaceasoft.pswallpaper.utils.MyContextWrapper;
import com.panaceasoft.pswallpaper.utils.PSDialogMsg;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewmodel.common.NotificationViewModel;
import com.panaceasoft.pswallpaper.viewmodel.user.UserViewModel;
import com.panaceasoft.pswallpaper.viewobject.User;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

/**
 * MainActivity of Panacea-Soft
 * Contact Email : teamps.is.cool@gmail.com
 *
 * @author Panacea-soft
 * @version 1.0
 * @since 11/15/17.
 */

public class MainActivity extends PSAppCompactActivity {


    //region Variables

    @Inject
    SharedPreferences pref;

    private Boolean notiSetting = false;
    private String token = "";
    private UserViewModel userViewModel;

    private NotificationViewModel notificationViewModel;
    private User user;

    private ConsentForm form;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    public ActivityMainBinding binding;
    private BroadcastReceiver broadcastReceiver = null;

    //    public String appHome;
    public String LANG_CURRENT;
    private boolean isLogout = false;

    //endregion


    //region Override Methods

    private void launchAppIntro() {
        Intent intent = new Intent(this, MainWalkThroughtActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Base_PSTheme);

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initModels();

        initUIAndActions();

        initData();

        checkConsentStatus();

        FirebaseMessaging.getInstance().subscribeToTopic("ps_foundation");

//        ConsentInformation a = ConsentInformation.getInstance(getBaseContext());


    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);

        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, CURRENT_LANG_COUNTRY_CODE, true));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.psLog("Inside Result MainActivity");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onPause() {

        try {
            if (broadcastReceiver != null) {
                unregisterReceiver(broadcastReceiver);
            }
        } catch (Exception e) {
            Utils.psErrorLog("Error in unregister.", e);
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();


        Utils.psLog(">>>> On Resume.");

        try {

            if (pref.getBoolean(Utils.IS_NOTI_EXISTS_TO_SHOW, false) ||
                    getIntent().getBooleanExtra(Utils.IS_NOTI_EXISTS_TO_SHOW, false)) {

                String message = pref.getString(Utils.NOTI_MSG, "");

                if (!message.equals("")) {

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(Utils.IS_NOTI_EXISTS_TO_SHOW, false).apply();

                    showAlertMessage(message);

                }
            }
        } catch (NullPointerException ne) {
            Utils.psErrorLog("Null Pointer Exception.", ne);
        } catch (Exception e) {
            Utils.psErrorLog("Error in getting notification flag data.", e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);

            if(fragment != null)
            {
                if(fragment instanceof DashboardFragment)
                {
                    new AlertDialog.Builder(this)
                            .setTitle(getResources().getString(R.string.app__app_name))
                            .setMessage(R.string.message__want_to_quit)
                            .setIcon(R.drawable.ic_about_us)
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, (DialogInterface dialog, int whichButton) -> {
                                finish();
                                System.exit(0);
                            }).show();
                }else {
                    setSelectMenu(R.id.nav_home);
                    showBottomNavigation();
                    binding.bottomNavigationView.setSelectedItemId(R.id.home_menu);
                    navigationController.navigateToDashBoardSetting2(this, true);
                    setToolbarText(binding.toolbar, getString(R.string.app__app_name));
                }

            }

        }
        return false;
    }

    //endregion


    //region Private Methods

    /**
     * Initialize Models
     */
    private void initModels() {
        //MobileAds.initialize(this, getResources().getString(R.string.banner_home_footer));

        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
        notificationViewModel = new ViewModelProvider(this, viewModelFactory).get(NotificationViewModel.class);

    }


    /**
     * Show alert message to user.
     *
     * @param msg Message to show to user
     */
    private void showAlertMessage(String msg) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.ps_dialog, null);

        builder.setView(view)
                .setPositiveButton(getString(R.string.app__ok), null);

        TextView message = view.findViewById(R.id.messageTextView);

        message.setText(msg);

        builder.create();

        builder.show();

    }

    /**
     * This function will initialize UI and Event Listeners
     */
    private void initUIAndActions() {

        Menu navViewMenu = binding.navView.getMenu();
        Menu bottomNavigationViewMenu = binding.bottomNavigationView.getMenu();

        if(!Config.ENABLE_GIF) {
            navViewMenu.findItem(R.id.nav_gif_wallpapers_login).setVisible(false);
            navViewMenu.findItem(R.id.nav_gif_wallpapers).setVisible(false);
        }

        if(!Config.ENABLE_LIVE_WALLPAPER) {
            navViewMenu.findItem(R.id.nav_live_wallpapers_login).setVisible(false);
            navViewMenu.findItem(R.id.nav_live_wallpapers).setVisible(false);

            navViewMenu.findItem(R.id.nav_fav_live_wallpaper_login).setVisible(false);

            navViewMenu.findItem(R.id.nav_downloaded_live_wallpapers_login).setVisible(false);

        }

        if(!Config.ENABLE_PREMIUM){
            navViewMenu.findItem(R.id.nav_premium_wallpapers).setVisible(false);
            navViewMenu.findItem(R.id.nav_premium_wallpapers_login).setVisible(false);
            navViewMenu.findItem(R.id.nav_claimPoint).setVisible(false);
            navViewMenu.findItem(R.id.nav_claimPoint_login).setVisible(false);
            bottomNavigationViewMenu.findItem(R.id.premium_menu).setVisible(false);
        }


        initToolbar(binding.toolbar, getResources().getString(R.string.app__app_name));

        initDrawerLayout();

        initNavigationView();

        navigationController.navigateToDashBoardSetting2(this, false);

        setSelectMenu(R.id.nav_home);

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home_menu:

                    navigationController.navigateToDashBoardSetting2(this, true);
                    setToolbarText(binding.toolbar, getString(R.string.app__app_name));

                    break;
                case R.id.wallpaper_menu:

                    navigationController.navigateToWallpaperFragment(this, Config.ZERO_COUNT);
                    setToolbarText(binding.toolbar, getString(R.string.menu__wallpapers_list));

                    break;
                case R.id.category_menu:

                    setToolbarText(binding.toolbar, getString(R.string.menu__category));
                    navigationController.navigateToCategoryLists(this);

                    break;
                case R.id.premium_menu:

                    navigationController.navigateToPremiumFragment(this, Constants.PREMIUM);
                    setToolbarText(binding.toolbar, getString(R.string.menu__premium));

                    break;
                case R.id.search_menu:

                    navigationController.navigateToSearchLists(this);
                    setToolbarText(binding.toolbar, getString(R.string.menu__search));

                    break;

                default:
                    navigationController.navigateToSearchLists(this);
                    setToolbarText(binding.toolbar, getString(R.string.menu__search));

                    break;
            }

            return true;
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.contains("checked")) {

            if (preferences.getBoolean("checked", true)) {
                launchAppIntro();
            }
        } else {
            launchAppIntro();
        }


    }


    private void initDrawerLayout() {

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.app__drawer_open, R.string.app__drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        binding.drawerLayout.addDrawerListener(drawerToggle);
        binding.drawerLayout.post(drawerToggle::syncState);


    }

    private void initNavigationView() {

        if (binding.navView != null) {

            // Updating Custom Fonts
            Menu m = binding.navView.getMenu();
            try {
                if (m != null) {
                    for (int i = 0; i < m.size(); i++) {
                        MenuItem mi = m.getItem(i);

                        //for applying a font to subMenu ...
                        SubMenu subMenu = mi.getSubMenu();
                        if (subMenu != null && subMenu.size() > 0) {
                            for (int j = 0; j < subMenu.size(); j++) {
                                MenuItem subMenuItem = subMenu.getItem(j);

                                // update font
                                subMenuItem.setTitle(Utils.getSpannableString(getBaseContext(), subMenuItem.getTitle().toString(), Utils.Fonts.ROBOTO_LIGHT));
                            }
                        }

                        // update font
                        mi.setTitle(Utils.getSpannableString(getBaseContext(), mi.getTitle().toString(), Utils.Fonts.ROBOTO_LIGHT));
                    }
                }
            } catch (Exception e) {
                Utils.psErrorLog("Error in Setting Custom Font", e);
            }

            binding.navView.setNavigationItemSelectedListener(menuItem -> {
                navigationMenuChanged(menuItem);
                return true;
            });

        }


    }

    private void showBottomNavigation() {
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void hideBottomNavigation() {
        binding.bottomNavigationView.setVisibility(View.GONE);
    }

    public void hideItem(int menuId, boolean visibility) {
        Menu menu = binding.navView.getMenu();

        menu.findItem(menuId).setVisible(visibility);
    }

    private void selectBottomNavigationView(@NonNull int itemId) {
        binding.bottomNavigationView.setSelectedItemId(itemId);
    }

    private void navigationMenuChanged(MenuItem menuItem) {
        openFragment(menuItem.getItemId());
        menuItem.setChecked(true);
        binding.drawerLayout.closeDrawers();
    }

    public void setSelectMenu(int id) {
        binding.navView.setCheckedItem(id);
    }

    private int menuId = 0;

    /**
     * Open Fragment
     *
     * @param menuId To know which fragment to open.
     */

    private void openFragment(int menuId) {

        this.menuId = menuId;
        switch (menuId) {
            case R.id.nav_home:
            case R.id.nav_home_login:

                setToolbarText(binding.toolbar, getString(R.string.app__app_name));
                //navigationController.navigateToDashBoardSetting2(this);
                selectBottomNavigationView(R.id.home_menu);
                showBottomNavigation();
                Utils.psLog("nav_home");

                break;

            case R.id.nav_profile:
            case R.id.nav_profile_login:

                Utils.navigateOnUserVerificationFragment(pref,user,navigationController,this);

                hideBottomNavigation();
                Utils.psLog("nav_profile");

                break;

            case R.id.nav_logout_login:

                PSDialogMsg psDialogMsg = new PSDialogMsg(this, false);
                psDialogMsg.showConfirmDialog(getString(R.string.message__want_to_logout), getString(R.string.message__logout), getString(R.string.message__cancel_close));
                psDialogMsg.show();

                psDialogMsg.okButton.setOnClickListener(view -> {

                    psDialogMsg.cancel();

                    userViewModel.deleteUserLogin(user).observe(this, status -> {
                        if (status != null) {

                            Utils.psLog("User is Status : " + status);

                            this.menuId = 0;

                            isLogout = true;
//                            FacebookSdk.sdkInitialize(getApplicationContext());
                            LoginManager.getInstance().logOut();
                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestEmail()
                                    .build();
                            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
                            googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });

                    Utils.psLog("nav_logout_login");

                });

                psDialogMsg.cancelButton.setOnClickListener(view -> psDialogMsg.cancel());

                Utils.psLog("nav_logout_login");

                break;

            case R.id.nav_about_us:
            case R.id.nav_about_us_login:

                Utils.psLog("nav_about_us");
                setToolbarText(binding.toolbar, getString(R.string.menu__about_app));
                navigationController.navigateToAboutUs(this);
                hideBottomNavigation();
                break;


            case R.id.nav_contact_us:
            case R.id.nav_contact_us_login:

                Utils.psLog("nav_contact_us");

                setToolbarText(binding.toolbar, getString(R.string.menu__contact_us));
                navigationController.navigateToContactUs(this);
                hideBottomNavigation();
                break;


            case R.id.nav_language:
            case R.id.nav_language_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__language));
                navigationController.navigateToLanguageSetting(this);
                Utils.psLog("nav_language");
                hideBottomNavigation();
                break;

            case R.id.nav_setting:
            case R.id.nav_setting_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__setting));
                navigationController.navigateToSetting(this);
                Utils.psLog("nav_setting");
                hideBottomNavigation();
                break;

            case R.id.nav_premium_wallpapers:
            case R.id.nav_premium_wallpapers_login:

                navigationController.navigateToPremiumFragment(this, Constants.PREMIUM);
                setToolbarText(binding.toolbar, getString(R.string.menu__premium));
                hideBottomNavigation();
                break;

            case R.id.nav_fav_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__fav));
                navigationController.navigateToFavoriteLists(this);
                Utils.psLog("nav_favorite");
                hideBottomNavigation();
                break;

            case R.id.nav_fav_live_wallpaper_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__fav_live_wallpaper));
                navigationController.navigateToFavoriteLiveWallpaperLists(this);
                Utils.psLog("nav_favorite_live_wallpaper");
                hideBottomNavigation();
                break;

            case R.id.nav_upload_photo_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__upload_photo));
                navigationController.navigateToUploadPhoto(this);
                Utils.psLog("nav_upload_photo");
                hideBottomNavigation();
                break;

            case R.id.nav_category:
            case R.id.nav_category_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__category));
                navigationController.navigateToCategoryLists(this);
                Utils.psLog("nav_category");
                hideBottomNavigation();
                break;

            case R.id.nav_free_wallpapers:
            case R.id.nav_free_wallpapers_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__free_wallpapers));
//                navigationController.navigateToLatestWallpaper(this);

                navigationController.navigateToWallpaperFragment(this, Config.ZERO_COUNT);

                Utils.psLog("nav_free_wallpapers");
                hideBottomNavigation();
                break;
            case R.id.nav_gif_wallpapers:
            case R.id.nav_gif_wallpapers_login:

                navigationController.navigateToGifFragment(this, Constants.GIF);
                setToolbarText(binding.toolbar, getString(R.string.menu__gif_wallpapers));
                hideBottomNavigation();
                break;

            case R.id.nav_live_wallpapers:
            case R.id.nav_live_wallpapers_login:

                navigationController.navigateToLiveWallpaper(this);
                setToolbarText(binding.toolbar, getString(R.string.menu__live_wallpapers));
                hideBottomNavigation();
                break;
            case R.id.nav_claimPoint:
            case R.id.nav_claimPoint_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__claimPoint));
                navigationController.navigateToClaimPoint(this);
                Utils.psLog("nav_claimPoint");
                hideBottomNavigation();

                break;

            case R.id.nav_search:
            case R.id.nav_search_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__search));
                navigationController.navigateToSearchLists(this);
                hideBottomNavigation();

                break;

            case R.id.nav_downloaded_wallpapers_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__downloaded));
                navigationController.navigateToDownloadedLists(this);
                hideBottomNavigation();
                break;

            case R.id.nav_downloaded_live_wallpapers_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__downloaded_live_wallpaper));
                navigationController.navigateToDownloadLiveWallpaperLists(this);
                hideBottomNavigation();
                break;

            case R.id.nav_rate_this_app:
            case R.id.nav_rate_this_app_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__rate));
                navigationController.navigateToPlayStore(this);
                hideBottomNavigation();

                break;
        }
    }

    /**
     * Initialize Data
     */
    private void initData() {

        try {
            Utils.psLog(">>>> On initData.");
            notiSetting = pref.getBoolean(Utils.NOTI_SETTING, false);
            token = pref.getString(Utils.NOTI_TOKEN, "");

        } catch (NullPointerException ne) {
            Utils.psErrorLog("Null Pointer Exception.", ne);
        } catch (Exception e) {
            Utils.psErrorLog("Error in getting notification flag data.", e);
        }
        /*
         * Get Login User and Update Menu
         */
        userViewModel.getLoginUser().observe(this, data -> {

            if (data != null) {

                if (data.size() > 0) {
                    user = data.get(0).user;

                    pref.edit().putString(Constants.USER_ID, user.user_id).apply();
                    pref.edit().putString(Constants.USER_NAME, user.user_name).apply();
                    pref.edit().putString(Constants.USER_EMAIL, user.user_email).apply();
                    pref.edit().putString(Constants.USER_PASSWORD, user.user_password).apply();

                } else {
                    user = null;

                    pref.edit().remove(Constants.USER_ID).apply();
                    pref.edit().remove(Constants.USER_NAME).apply();
                    pref.edit().remove(Constants.USER_EMAIL).apply();
                    pref.edit().remove(Constants.USER_PASSWORD).apply();
                }

            } else {

                user = null;
                pref.edit().remove(Constants.USER_ID).apply();
                pref.edit().remove(Constants.USER_NAME).apply();
                pref.edit().remove(Constants.USER_EMAIL).apply();
                pref.edit().remove(Constants.USER_PASSWORD).apply();

                Utils.psLog("Empty Data");

            }
            updateMenu();

            if (isLogout) {
                setToolbarText(binding.toolbar, getString(R.string.app__app_name));
                showBottomNavigation();
                binding.bottomNavigationView.setSelectedItemId(R.id.home_menu);
                isLogout = false;
            }

        });


        registerNotificationToken(); // Just send "" because don't have token to sent. It will get token itself.
    }


    /**
     * This function will change the menu based on the user is logged in or not.
     */
    private void updateMenu() {

        if (user == null) {

            Utils.psLog("User is null");

            binding.navView.getMenu().setGroupVisible(R.id.group_before_login, true);
            binding.navView.getMenu().setGroupVisible(R.id.group_after_login, false);

            setSelectMenu(R.id.nav_home);

        } else {

            Utils.psLog("User is not null");

            binding.navView.getMenu().setGroupVisible(R.id.group_after_login, true);
            binding.navView.getMenu().setGroupVisible(R.id.group_before_login, false);

            if (menuId == R.id.nav_profile) {
                setSelectMenu(R.id.nav_profile_login);
            } else if (menuId == R.id.nav_profile_login) {
                setSelectMenu(R.id.nav_profile_login);
            } else {
                setSelectMenu(R.id.nav_home_login);
            }

        }

    }

    private void registerNotificationToken() {
        /*
         * Register Notification
         */

        // Check already submit or not
        // If haven't, submit to server
        if (!notiSetting) {

            if (this.token.equals("")) {
                Utils.psLog("Registering Notification Token...");

                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {

                                Utils.psErrorLog("getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token1 = task.getResult().getToken();

                            notificationViewModel.registerNotification(getBaseContext(), Utils.PLATFORM, token1);
                        });


            }
        } else {
            Utils.psLog("Notification Token is already registered. Notification Setting : true.");
        }
    }

    //endregion

    private void checkConsentStatus() {

        // For Testing Open this
//        ConsentInformation.getInstance(this).
//                setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);

        ConsentInformation consentInformation = ConsentInformation.getInstance(this);
        String[] publisherIds = {getString(R.string.adview_publisher_key)};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.

                Utils.psLog(consentStatus.name());

                if (!consentStatus.name().equals(pref.getString(Config.CONSENTSTATUS_CURRENT_STATUS, Config.CONSENTSTATUS_CURRENT_STATUS)) || consentStatus.name().equals(Config.CONSENTSTATUS_UNKNOWN)) {
                    collectConsent();
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.

                Utils.psLog("Failed to updateeee");
            }
        });
    }

    private void collectConsent() {
        URL privacyUrl = null;
        try {
            // TODO: Replace with your app's privacy policy URL.
            privacyUrl = new URL(Config.POLICY_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }

        form = new ConsentForm.Builder(this, privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.

                        Utils.psLog("Form loaded");

                        if (form != null) {
                            form.show();
                        }
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.

                        Utils.psLog("Form Opened");
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.

                        pref.edit().putString(Config.CONSENTSTATUS_CURRENT_STATUS, consentStatus.name()).apply();
                        pref.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, true).apply();
                        Utils.psLog("Form Closed");
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.

                        pref.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, false).apply();
                        Utils.psLog("Form Error " + errorDescription);
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build();

        form.load();

    }

}
