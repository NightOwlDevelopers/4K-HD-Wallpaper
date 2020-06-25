package com.panaceasoft.pswallpaper.utils;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.like.LikeButton;
import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.MainActivity;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.ui.common.NavigationController;
import com.panaceasoft.pswallpaper.viewobject.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Panacea-Soft on 7/15/15.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class Utils {

    private static Typeface fromAsset;
    private static SpannableString spannableString;
    private static Fonts currentTypeface;

    public static final String IS_NOTI_EXISTS_TO_SHOW = "is_noti_exists_to_show";
    public static final String NOTI_MSG = "noti_msg";
    public static final String NOTI_SETTING = "noti_setting";
    public static final String NOTI_TOKEN = "noti_token";

    public static final int RESULT_LOAD_IMAGE = 1;
    public static final int RESULT_OK = -1;

    public static final int RESULT_LOAD_IMAGE_CATEGORY = 10;

    public static final int REQUEST_TO_SEARCH = 1111;
    public static final int RESULT_FROM_SEARCH = 1112;

    public static final String NO_USER = "nologinuser";

    public static final String START_OF_OFFSET = "0"; // Please don't change!

    public static final String PLATFORM = "android"; // Please don't change!

    public static final String setToHome = "0";
    public static final String setToLock = "1";
    public static final String setToBoth = "2";

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String numberFormat(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return numberFormat(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + numberFormat(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    /*For WallpaperParamsHolder*/

    public static String active = "1";
    public static String inactive = "";
    public static String trending = "touch_count";
    public static String download = "download_count";
    public static String latest = "added_date";
    public static String rating = "rating_count";
    public static String point = "point";
    public static String asc = "asc";
    public static String desc = "desc";

    public static String FREE = "free";
    public static String PREMIUM_NOT_PURCHASED = "not Purchased";
    public static String PREMIUM_PURCHASED = "purchased";

    /*For WallpaperParamsHolder*/

    public enum LoadingDirection {
        top,
        bottom,
        none
    }

    public static String checkUserId(String loginUserId) {
        if (loginUserId.trim().equals("")) {
            loginUserId = NO_USER;
        }
        return loginUserId;
    }

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getSize(size);
        } catch (NoSuchMethodError e) {
            // For lower than api 11
            size.x = display.getWidth();
            size.y = display.getHeight();
        }
        return size;
    }

    public static boolean isAndroid_7_0() {
        String version = Build.VERSION.RELEASE;
        if (version != null && !version.equals("")) {
            String[] versionDetail = version.split("\\.");
            Log.d("TEAMPS", "0 : " + versionDetail[0] + " 1 : " + versionDetail[1]);
            if (versionDetail[0].equals("7")) {
                if (versionDetail[1].equals("0") || versionDetail[1].equals("00")) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void psLog(String log) {
        if (Config.IS_DEVELOPMENT) {
            Log.d("TEAMPS", log);
        }
    }

    public static View getCurrentView(ViewPager viewPager) {
        final int currentItem = viewPager.getCurrentItem();
        for (int i = 0; i < viewPager.getChildCount(); i++) {
            final View child = viewPager.getChildAt(i);
            final ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) child.getLayoutParams();

            int position = 0;
            try {
                Field f = layoutParams.getClass().getDeclaredField("position");
                f.setAccessible(true);
                position = f.getInt(layoutParams); //IllegalAccessException
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (!layoutParams.isDecor && currentItem == position) {
                return child;
            }
        }
        return null;
    }

    public static boolean isGooglePlayServicesOK(Activity activity) {

        final int GPS_ERRORDIALOG_REQUEST = 9001;

        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, activity, GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(activity, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }


    public static boolean isEmailFormatValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void saveBitmapImage(Context context, Bitmap b, String picName) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(picName, Context.MODE_APPEND);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("TEAMPS", "file not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("TEAMPS", "io exception");
            e.printStackTrace();
        }

    }

    public static void setDatesToShared(String start_date, String end_date, SharedPreferences pref) {

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.APPINFO_START_DATE, start_date);
        editor.putString(Constants.APPINFO_END_DATE, end_date);
        editor.apply();

    }

    public static void updateUserLoginData(SharedPreferences pref, User user){
        addUserLoginData(pref,user,user.user_password);
        deleteUserVerifyData(pref);
    }

    public static void registerUserLoginData(SharedPreferences pref, User user, String password){
        addUserLoginData(pref,user,password);
        addUserVerifyData(pref,user,password);
    }

    public static void addUserLoginData(SharedPreferences pref, User user, String password){
        pref.edit().putString(Constants.FACEBOOK_ID, user.facebook_id).apply();
//        pref.edit().putString(Constants.PHONE_ID, user.phoneId).apply();
        pref.edit().putString(Constants.GOOGLE_ID, user.google_id).apply();
        pref.edit().putString(Constants.USER_PHONE, user.user_phone).apply();
        pref.edit().putString(Constants.USER_ID, user.user_id).apply();
        pref.edit().putString(Constants.USER_NAME, user.user_name).apply();
        pref.edit().putString(Constants.USER_EMAIL, user.user_email).apply();
        pref.edit().putString(Constants.USER_PASSWORD, password).apply();

    }

    private static void deleteUserVerifyData(SharedPreferences pref){
        pref.edit().putString(Constants.USER_EMAIL_TO_VERIFY, Constants.EMPTY_STRING).apply();
        pref.edit().putString(Constants.USER_PASSWORD_TO_VERIFY, Constants.EMPTY_STRING).apply();
        pref.edit().putString(Constants.USER_NAME_TO_VERIFY, Constants.EMPTY_STRING).apply();
        pref.edit().putString(Constants.USER_ID_TO_VERIFY, Constants.EMPTY_STRING).apply();
    }

    private static void addUserVerifyData(SharedPreferences pref,User user,String password){
        pref.edit().putString(Constants.USER_EMAIL_TO_VERIFY, user.user_email).apply();
        pref.edit().putString(Constants.USER_PASSWORD_TO_VERIFY, password).apply();
        pref.edit().putString(Constants.USER_NAME_TO_VERIFY, user.user_name).apply();
        pref.edit().putString(Constants.USER_ID_TO_VERIFY, user.user_id).apply();
    }

    public static void navigateAfterUserLogin(Activity activity, NavigationController navigationController){
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).setToolbarText(((MainActivity) activity).binding.toolbar, activity.getString(R.string.profile__title));
            navigationController.navigateToUserProfile((MainActivity) activity);

        } else {
            try {
                if (activity != null) {
                    activity.finish();
                }
            } catch (Exception e) {
                Utils.psErrorLog("Error in closing parent activity.", e);
            }
        }
    }

    public static void navigateAfterUserRegister(Activity activity, NavigationController navigationController){
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).setToolbarText(((MainActivity) activity).binding.toolbar, activity.getString(R.string.verify_email__title));

            navigationController.navigateToVerifyEmail((MainActivity) activity);

        } else {
            navigationController.navigateToVerifyEmailActivity(activity);
            try {
                if (activity != null) {
                    activity.finish();
                }
            } catch (Exception e) {
                Utils.psErrorLog("Error in closing parent activity.", e);
            }
        }
    }

    public static void navigateAfterForgotPassword(Activity activity, NavigationController navigationController){
        if (activity instanceof MainActivity) {
            navigationController.navigateToUserForgotPassword((MainActivity) activity);
        } else {

            navigationController.navigateToUserForgotPasswordActivity(activity);

            try {
                if (activity != null) {
                    activity.finish();
                }
            } catch (Exception e) {
                Utils.psErrorLog("Error in closing activity.", e);
            }
        }

    }

    public static void navigateAfterLogin(Activity activity, NavigationController navigationController){
        if (activity instanceof MainActivity) {
            navigationController.navigateToUserLogin((MainActivity) activity);
        } else {

            navigationController.navigateToUserLoginActivity(activity);

            try {
                if (activity != null) {
                    activity.finish();
                }
            } catch (Exception e) {
                Utils.psErrorLog("Error in closing activity.", e);
            }
        }

    }

    public static void navigateAfterPhoneVerify(Activity activity, NavigationController navigationController,String number,String username){
        if(activity instanceof  MainActivity) {
            navigationController.navigateToPhoneVerifyFragment((MainActivity) activity,number, username);
        }else {

            navigationController.navigateToPhoneVerifyActivity(activity,number,username);

            try {
                if (activity != null) {
                    activity.finish();
                }
            } catch (Exception e) {
                Utils.psErrorLog("Error in closing activity.", e);
            }
        }

    }

    public static void navigateAfterPhoneLogin(Activity activity, NavigationController navigationController){
        if(activity instanceof  MainActivity) {
            navigationController.navigateToPhoneLoginFragment((MainActivity) activity);
        }else {

            navigationController.navigateToPhoneLoginActivity(activity);

            try {
                if (activity != null) {
                    activity.finish();
                }
            } catch (Exception e) {
                Utils.psErrorLog("Error in closing activity.", e);
            }
        }

    }

    public static void navigateAfterRegister(Activity activity, NavigationController navigationController){
        if (activity instanceof MainActivity) {
            navigationController.navigateToUserRegister((MainActivity) activity);
        } else {

            navigationController.navigateToUserRegisterActivity(activity);

            try {
                if (activity != null) {
                    activity.finish();
                }
            } catch (Exception e) {
                Utils.psErrorLog("Error in closing activity.", e);
            }
        }

    }

    public static void navigateOnUserVerificationFragment(SharedPreferences pref,User user,NavigationController navigationController,MainActivity activity) {
        String fragmentType = pref.getString(Constants.USER_ID_TO_VERIFY, Constants.EMPTY_STRING);

        if (fragmentType.isEmpty()) {
            if (user == null) {

                activity.setToolbarText(activity.binding.toolbar, activity.getString(R.string.login__login));
                navigationController.navigateToUserLogin(activity);
            } else {

                activity.setToolbarText(activity.binding.toolbar, activity.getString(R.string.profile__title));
                navigationController.navigateToUserProfile(activity);
            }
        } else {
            navigationController.navigateToVerifyEmail(activity);
        }

    }

    public interface NavigateOnUserVerificationActivityCallback {
        void onSuccess();
    }

    public static void navigateOnUserVerificationActivity(String userIdToVerify,String loginUserId,
                                                          PSDialogMsg psDialogMsg,Activity activity,
                                                          NavigationController navigationController,
                                                          NavigateOnUserVerificationActivityCallback callback
    ) {
        if (userIdToVerify.isEmpty()) {
            if (loginUserId.equals("")) {

                psDialogMsg.showInfoDialog(activity.getString(R.string.error_message__login_first), activity.getString(R.string.app__ok));
                psDialogMsg.show();
                psDialogMsg.okButton.setOnClickListener(v1 -> {
                    psDialogMsg.cancel();
                    navigationController.navigateToUserLoginActivity(activity);
                });

            } else {
                callback.onSuccess();
            }
        }else {

            navigationController.navigateToVerifyEmailActivity(activity);
        }


    }

    public static void navigateOnUserVerificationActivityFromFav(String userIdToVerify,String loginUserId,
                                                                 PSDialogMsg psDialogMsg,Activity activity,
                                                                 NavigationController navigationController,
                                                                 LikeButton likeButton,
                                                                 NavigateOnUserVerificationActivityCallback callback

    ) {
        if (userIdToVerify.isEmpty()) {
            if (loginUserId.equals("")) {

                likeButton.setLiked(false);

                psDialogMsg.showInfoDialog(activity.getString(R.string.error_message__login_first), activity.getString(R.string.app__ok));
                psDialogMsg.show();
                psDialogMsg.okButton.setOnClickListener(v1 -> {
                    psDialogMsg.cancel();
                    navigationController.navigateToUserLoginActivity(activity);
                });

            } else {
                callback.onSuccess();
            }
        }else {
            likeButton.setLiked(false);
            navigationController.navigateToVerifyEmailActivity(activity);
        }
    }

    public static void navigateOnUserVerificationActivityFromFavDetail(String userIdToVerify,String loginUserId,
                                                                 PSDialogMsg psDialogMsg,Activity activity,
                                                                 NavigationController navigationController,
                                                                 LikeButton likeButton,
                                                                 BottomSheetDialog bottomSheetDialog,
                                                                 NavigateOnUserVerificationActivityCallback callback

    ) {
        if (userIdToVerify.isEmpty()) {
            if (loginUserId.equals("")) {

                likeButton.setLiked(false);

                psDialogMsg.showInfoDialog(activity.getString(R.string.error_message__login_first), activity.getString(R.string.app__ok));
                psDialogMsg.show();
                psDialogMsg.okButton.setOnClickListener(v1 -> {
                    psDialogMsg.cancel();
                    bottomSheetDialog.cancel();
                    navigationController.navigateToUserLoginActivity(activity);
                });

            } else {
                callback.onSuccess();
            }
        }else {
            likeButton.setLiked(false);
            navigationController.navigateToVerifyEmailActivity(activity);
        }
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static Bitmap loadBitmapImage(Context context, String picName) {
        Bitmap b = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
            fis.close();

        } catch (FileNotFoundException e) {
            Log.d("TEAMPS", "file not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("TEAMPS", "io exception");
            e.printStackTrace();
        }
        return b;
    }

    public static Typeface getTypeFace(Context context, Fonts fonts) {

        if (currentTypeface == fonts) {
            if (fromAsset == null) {
                if (fonts == Fonts.NOTO_SANS) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSans-Regular.ttf");
                } else if (fonts == Fonts.ROBOTO) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
                } else if (fonts == Fonts.ROBOTO_MEDIUM) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
                } else if (fonts == Fonts.ROBOTO_LIGHT) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
                }
            }
        } else {
            if (fonts == Fonts.NOTO_SANS) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSans-Regular.ttf");
            } else if (fonts == Fonts.ROBOTO) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
            } else if (fonts == Fonts.ROBOTO_MEDIUM) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
            } else if (fonts == Fonts.ROBOTO_LIGHT) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
            } else {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
            }

            //fromAsset = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Italic.ttf");
            currentTypeface = fonts;
        }
        return fromAsset;
    }

    public static SpannableString getSpannableString(Context context, String str, Fonts font) {
        spannableString = new SpannableString(str);
        spannableString.setSpan(new PSTypefaceSpan("", Utils.getTypeFace(context, font)), 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public enum Fonts {
        ROBOTO,
        NOTO_SANS,
        ROBOTO_LIGHT,
        ROBOTO_MEDIUM
    }

    public static Bitmap getUnRotatedImage(String imagePath, Bitmap rotatedBitmap) {
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        return Bitmap.createBitmap(rotatedBitmap, 0, 0, rotatedBitmap.getWidth(), rotatedBitmap.getHeight(), matrix,
                true);
    }

    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[4].getLineNumber();
    }

    public static String getClassName(Object obj) {
        return "" + ((Object) obj).getClass();
    }

    public static void psErrorLog(String log, Object obj) {
        try {
            Log.d("TEAMPS", log);
            Log.d("TEAMPS", "Line : " + getLineNumber());
            Log.d("TEAMPS", "Class : " + getClassName(obj));
        } catch (Exception ee) {
            Log.d("TEAMPS", "Error in psErrorLog");
        }
    }

    public static void psErrorLog(String log, Exception e) {
        try {
            StackTraceElement l = e.getStackTrace()[0];
            Log.d("TEAMPS", log);
            Log.d("TEAMPS", "Line : " + l.getLineNumber());
            Log.d("TEAMPS", "Method : " + l.getMethodName());
            Log.d("TEAMPS", "Class : " + l.getClassName());
        } catch (Exception ee) {
            Log.d("TEAMPS", "Error in psErrorLogE");
        }

    }


    public static void unbindDrawables(View view) {
        try {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }

                if (!(view instanceof AdapterView)) {
                    ((ViewGroup) view).removeAllViews();
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("Error in Unbind", e);
        }
    }

    public static boolean isStoragePermissionGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Utils.psLog("Permission is granted");
                return true;
            } else {
                Utils.psLog("Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Utils.psLog("Permission is granted");
            return true;
        }
    }

    // Sleep Me
    public static void sleepMe(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Utils.psErrorLog("InterruptedException", e);
        } catch (Exception e) {
            Utils.psErrorLog("Exception", e);
        }
    }


    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);

            if (imm != null) {
                if (activity.getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("Error in hide keyboard.", e);
        }
    }

    //Ad
    public static void initInterstitialAd(Context context, String adKey) {
        //load ad
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd interstitial;
        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(context);

        // Insert the Ad Unit ID
        interstitial.setAdUnitId(adKey);

        interstitial.loadAd(adRequest);

        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial(interstitial);
            }
        });
    }

    public static void displayInterstitial(InterstitialAd interstitial) {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    public static int getDrawableInt(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    public static void hideFirstFab(final View v) {
        v.setVisibility(View.GONE);
        v.setTranslationY(v.getHeight());
        v.setAlpha(0f);
    }

    public static void setImageToImageView(Context context, ImageView imageView, int drawable) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true);

        Glide.with(context)
                .load(drawable)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    public static void showFab(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0f);
        v.setTranslationY(v.getHeight());
        v.animate()
                .setDuration(300)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(1f)
                .start();
    }

    public static void hideFab(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(1f);
        v.setTranslationY(0);
        v.animate()
                .setDuration(300)
                .translationY(v.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.GONE);
                        super.onAnimationEnd(animation);
                    }
                }).alpha(0f)
                .start();
    }

    public static boolean twistFab(final View v, boolean rotate) {
        v.animate().setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .rotation(rotate ? 165f : 0f);
        return rotate;
    }
}
