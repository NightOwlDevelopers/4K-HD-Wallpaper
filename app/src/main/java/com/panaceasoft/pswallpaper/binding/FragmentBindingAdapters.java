package com.panaceasoft.pswallpaper.binding;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewobject.common.SyncStatus;

import javax.inject.Inject;

/**
 * Binding adapters that work with a fragment instance.
 */
public class FragmentBindingAdapters {
    private Fragment fragment;
    private Activity activity;

    @Inject
    public FragmentBindingAdapters(Fragment fragment, Activity activity) {
        if (fragment != null) {
            this.fragment = fragment;
        }

        if (activity != null) {
            this.activity = activity;
        }
    }

    @BindingAdapter("imageUrl")
    public void bindImage(ImageView imageView, String imageUrl) {

        if (isValid(imageView, imageUrl)) {
            String fullUrl = Config.APP_IMAGES_URL + imageUrl;
            imageUrl = Config.APP_IMAGES_THUMB_URL + imageUrl;
            Utils.psLog("Image : " + imageUrl);

            if (Config.PRE_LOAD_FULL_IMAGE) {
                Glide.with(fragment).load(fullUrl).thumbnail(Glide.with(fragment).load(imageUrl)).into(imageView);
            } else {
                Glide.with(fragment).load(imageUrl).thumbnail(0.08f).into(imageView);
            }

        } else {

            if (imageView != null) {
                imageView.setImageResource(R.drawable.placeholder_image);
            }

        }
    }

    @BindingAdapter("bindFullImage")
    public void bindFullImage(ImageView imageView, String imageUrl) {

        if (isValid(imageView, imageUrl)) {
            String fullUrl = Config.APP_IMAGES_URL + imageUrl;
            imageUrl = Config.APP_IMAGES_THUMB_URL + imageUrl;
            Utils.psLog("Image : " + imageUrl);

            Glide.with(fragment).load(fullUrl).thumbnail(Glide.with(fragment).load(imageUrl)).into(imageView);

        } else {

            if (imageView != null) {
                imageView.setImageResource(R.drawable.placeholder_image);
            }

        }
    }

    @BindingAdapter("bindGif")
    public void bindGif(ImageView imageView, String imageUrl, String gifUrl) {

        if (isValid(imageView, imageUrl)) {
            String fullUrl = Config.APP_IMAGES_URL + gifUrl;
            imageUrl = Config.APP_IMAGES_THUMB_URL + imageUrl;
            Utils.psLog("Image GIF : " + imageUrl);
            Utils.psLog("Image GIF : " + fullUrl);

            Glide.with(fragment).load(fullUrl).thumbnail(Glide.with(fragment).load(imageUrl)).into(imageView);

        } else {

            if (imageView != null) {
                imageView.setImageResource(R.drawable.placeholder_image);
            }

        }
    }

    @BindingAdapter("bindFullImageDrawable")
    public void bindFullImageDrawbale(ImageView imageView, Drawable drawable) {

        if (imageView != null && drawable != null) {

            Glide.with(fragment).load(drawable).thumbnail(Glide.with(fragment).load(drawable)).into(imageView);

        } else {

            if (imageView != null) {
                imageView.setImageResource(R.drawable.placeholder_image);
            }

        }
    }

    @BindingAdapter("bindFullImageUri")
    public void bindFullImageUri(ImageView imageView, Uri uri) {

        if (imageView != null && uri != null) {

            Glide.with(fragment).load(uri).thumbnail(Glide.with(fragment).load(uri)).into(imageView);

        } else {

            if (imageView != null) {
                imageView.setImageResource(R.drawable.placeholder_image);
            }

        }
    }

    @BindingAdapter("imageCircleUrl")
    public void bindCircleImage(ImageView imageView, String url) {

        if (isValid(imageView, url)) {

            url = Config.APP_IMAGES_URL + url;

            Glide.with(fragment).load(url).apply(RequestOptions.circleCropTransform()
                    .placeholder(R.drawable.placeholder_circle_image)
            ).into(imageView);

        } else {

            if (imageView != null) {
                imageView.setImageResource(R.drawable.placeholder_circle_image);
            }
        }
    }

    @BindingAdapter("favImage")
    public void bindLikeFav(FloatingActionButton imageView, String isFavourite) {

        if (isValid(imageView, isFavourite)) {

            switch (isFavourite) {
                case SyncStatus.SERVER_SELECTED:
                    imageView.setImageResource(R.drawable.liked);
                    break;
                case SyncStatus.SERVER_NOT_SELECTED:
                    imageView.setImageResource(R.drawable.like);
                    break;
                default:
                    imageView.setImageResource(R.drawable.like);
                    break;
            }

        } else {
            if (imageView != null) {
                imageView.setImageResource(R.drawable.like);
            }
        }
    }

    @BindingAdapter("imageGifUrl")
    public void bindGifImage(ImageView imageView, String imageUrl) {


        if (imageView != null && imageUrl != null) {
            String fullUrl = imageUrl;
            imageUrl = Config.APP_IMAGES_THUMB_URL + "IMG-20171115-WA0001.jpg";
            Utils.psLog("Image : " + imageUrl);

            Glide.with(fragment).load(fullUrl).thumbnail(Glide.with(fragment).load(imageUrl)).into(imageView);

        } else {

            if (imageView != null) {
                imageView.setImageResource(R.drawable.placeholder_image);
            }

        }
//        if(imageView !=null && url !=null) {
//
//            url = Config.APP_IMAGES_THUMB_URL + url;
//
//            Glide.with(fragment).load(url).thumbnail("http://www.panacea-soft.com/pswallpapers-admin/uploads/thumbnail/IMG-20171115-WA0001.jpg").into(
//                    imageView);
//        }else {
//            if(imageView != null) {
//                imageView.setImageResource(R.drawable.placeholder_circle_image);
//            }
//        }
    }

    @BindingAdapter("favImage")
    public void bindFavouriteImage(ImageView imageView, String isFavourite) {

        if (isValid(imageView, isFavourite)) {

            switch (isFavourite) {
                case SyncStatus.SERVER_SELECTED:
                    imageView.setImageResource(R.drawable.liked);
                    break;
                case SyncStatus.SERVER_NOT_SELECTED:
                    imageView.setImageResource(R.drawable.like);
                    break;
                default:
                    imageView.setImageResource(R.drawable.like);
                    break;
            }

        } else {
            if (imageView != null) {
                imageView.setImageResource(R.drawable.like);
            }
        }
    }

    @BindingAdapter("imageProfileUrl")
    public void bindProfileImage(ImageView imageView, String url) {

        if (isValid(imageView, url)) {

            url = Config.APP_IMAGES_URL + url;
            Utils.psLog("Image Url : " + url);

            Glide.with(fragment).load(url).apply(RequestOptions.circleCropTransform()
                    .placeholder(R.drawable.default_profile)
            ).into(imageView);

        } else {

            if (imageView != null) {
                imageView.setImageResource(R.drawable.default_profile);
            }
        }
    }

    @BindingAdapter("font")
    public void setFont(TextView textView, String type) {
        switch (type) {
            case "normal":
                textView.setTypeface(Utils.getTypeFace(textView.getContext(), Utils.Fonts.ROBOTO));
                break;
            case "bold":
                textView.setTypeface(Utils.getTypeFace(textView.getContext(), Utils.Fonts.ROBOTO), Typeface.BOLD);
                break;
            case "bold_italic":
                textView.setTypeface(Utils.getTypeFace(textView.getContext(), Utils.Fonts.ROBOTO), Typeface.BOLD_ITALIC);
                break;
            case "italic":
                textView.setTypeface(Utils.getTypeFace(textView.getContext(), Utils.Fonts.ROBOTO), Typeface.ITALIC);
                break;
            case "medium":
                textView.setTypeface(Utils.getTypeFace(textView.getContext(), Utils.Fonts.ROBOTO_MEDIUM));
                break;
            case "light":
                textView.setTypeface(Utils.getTypeFace(textView.getContext(), Utils.Fonts.ROBOTO_LIGHT));
                break;
            default:
                textView.setTypeface(Utils.getTypeFace(textView.getContext(), Utils.Fonts.ROBOTO));
                break;
        }

    }

    @BindingAdapter("font")
    public void setFont(Button button, String type) {
        switch (type) {
            case "normal":
                button.setTypeface(Utils.getTypeFace(button.getContext(), Utils.Fonts.ROBOTO));
                break;
            case "medium":
                button.setTypeface(Utils.getTypeFace(button.getContext(), Utils.Fonts.ROBOTO_MEDIUM));
                break;
            case "light":
                button.setTypeface(Utils.getTypeFace(button.getContext(), Utils.Fonts.ROBOTO_LIGHT));
                break;
            default:
                button.setTypeface(Utils.getTypeFace(button.getContext(), Utils.Fonts.ROBOTO));
                break;
        }

    }

    @BindingAdapter("textSize")
    public void setTextSize(TextView textView, String dimenType) {

        float dimenPix = 0;
        Utils.psLog("dimenType " + dimenType);
        switch (dimenType) {

            case "font_h1_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(R.dimen.font_h1_size);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h2_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(R.dimen.font_h2_size);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h3_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(R.dimen.font_h3_size);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h4_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(R.dimen.font_h4_size);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h5_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(R.dimen.font_h5_size);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h6_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(R.dimen.font_h6_size);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h7_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(R.dimen.font_h7_size);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_title_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(R.dimen.font_title_size);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_body_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(R.dimen.font_body_size);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_body_s_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(R.dimen.font_body_s_size);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_body_xs_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(R.dimen.font_body_xs_size);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;
        }
    }

    @BindingAdapter("textSize")
    public void setTextSize(EditText editText, String dimenType) {

        float dimenPix = 0;
        switch (dimenType) {
            case "edit_text":

                dimenPix = editText.getResources().getDimensionPixelOffset(R.dimen.edit_text__size);
                editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);

                break;
        }
    }

    @BindingAdapter("textSize")
    public void setTextSize(Button button, String dimenType) {

        float dimenPix = 0;
        switch (dimenType) {
            case "button_text":

                dimenPix = button.getResources().getDimensionPixelOffset(R.dimen.button__text_size);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);

                break;
        }
    }

//    @BindingAdapter("youTubeImage")
//    public void bindYouTubeImage(ImageView imageView, String youTubeId) {
//
//        if(isValid(imageView, youTubeId)) {
//
//            String url = String.format(Config.YOUTUBE_IMAGE_BASE_URL, youTubeId);
//            Glide.with(fragment).load(url).apply(new RequestOptions()
//                    .placeholder(R.drawable.placeholder_image)
//                    .centerCrop()
//                    .dontAnimate()
//                    .dontTransform()).into(imageView);
//
//        } else {
//
//            if(imageView != null) {
//                imageView.setImageResource(R.drawable.placeholder_image);
//            }
//        }
//    }

    private Boolean isValid(ImageView imageView, String url) {
        return !(url == null
                || imageView == null
                || fragment == null
                || url.equals(""));
    }


}

