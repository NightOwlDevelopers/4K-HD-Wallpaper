package com.panaceasoft.pswallpaper.api;


import androidx.lifecycle.LiveData;

import com.panaceasoft.pswallpaper.viewobject.AboutUs;
import com.panaceasoft.pswallpaper.viewobject.ApiStatus;
import com.panaceasoft.pswallpaper.viewobject.Category;
import com.panaceasoft.pswallpaper.viewobject.Color;
import com.panaceasoft.pswallpaper.viewobject.Image;
import com.panaceasoft.pswallpaper.viewobject.PSAppInfo;
import com.panaceasoft.pswallpaper.viewobject.User;
import com.panaceasoft.pswallpaper.viewobject.Wallpaper;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * REST API access points
 */
public interface PSApiService {

    //region POST Notification

    // Submit Notification Token
    @FormUrlEncoded
    @POST("rest/notis/register/api_key/{API_KEY}")
    Call<ApiStatus> rawRegisterNotiToken(@Path("API_KEY") String apiKey, @Field("platform_name") String platform, @Field("device_id") String deviceId);


    @FormUrlEncoded
    @POST("rest/notis/unregister/api_key/{API_KEY}")
    Call<ApiStatus> rawUnregisterNotiToken(@Path("API_KEY") String apiKey, @Field("platform_name") String platform, @Field("device_id") String deviceId);

    //endregion

    //region User Related

    //region GET User
    @GET("rest/users/get/api_key/{API_KEY}/id/{user_id}")
    LiveData<ApiResponse<User>> getUser(@Path("API_KEY") String apiKey, @Path("user_id") String userId);
    //endregion

    //region POST Upload Image
    @Multipart
    @POST("rest/images/upload/api_key/{API_KEY}")
    LiveData<ApiResponse<User>> doUploadImage(@Path("API_KEY") String apiKey, @Part("user_id") RequestBody userId, @Part("file") RequestBody name, @Part MultipartBody.Part file, @Part("platform_name") RequestBody platformName);
    //endregion

    //region POST User for Login
    @FormUrlEncoded
    @POST("rest/users/login/api_key/{API_KEY}")
    LiveData<ApiResponse<User>> postUserLogin(@Path("API_KEY") String apiKey, @Field("user_email") String userEmail, @Field("user_password") String userPassword);
    //endregion

    //region POST User for Register
    @FormUrlEncoded
    @POST("rest/users/add/api_key/{API_KEY}")
    Call<User> postUser(@Path("API_KEY") String apiKey, @Field("user_name") String userName, @Field("user_email") String userEmail, @Field("user_password") String userPassword,@Field("device_token") String deviceToken);
    //endregion

    //region POST Forgot Password
    @FormUrlEncoded
    @POST("rest/users/reset/api_key/{API_KEY}")
    LiveData<ApiResponse<ApiStatus>> postForgotPassword(@Path("API_KEY") String apiKey, @Field("user_email") String userEmail);
    //endregion

    //region PUT User for User Update
    @FormUrlEncoded
    @POST("rest/users/profile_update/api_key/{API_KEY}")
    LiveData<ApiResponse<ApiStatus>> putUser(@Path("API_KEY") String apiKey, @Field("user_id") String loginUserId, @Field("user_name") String userName,
                                             @Field("user_email") String userEmail, @Field("user_phone") String userPhone, @Field("user_about_me") String userAboutMe,@Field("device_token") String deviceToken);
    //endregion

    //region PUT for Password Update
    @FormUrlEncoded
    @PUT("rest/users/password_update/api_key/{API_KEY}")
    LiveData<ApiResponse<ApiStatus>> postPasswordUpdate(@Path("API_KEY") String apiKey, @Field("login_user_id") String loginUserId, @Field("user_password") String password);
    //endregion

    //endregion

    //region About Us
    @GET("rest/abouts/get/api_key/{API_KEY}")
    LiveData<ApiResponse<List<AboutUs>>> getAboutUs(@Path("API_KEY") String apiKey);
    //endregion

    //region Contact Us

    @FormUrlEncoded
    @POST("rest/contacts/add/api_key/{API_KEY}")
    Call<ApiStatus> rawPostContact(@Path("API_KEY") String apiKey, @Field("contact_name") String contactName, @Field("contact_email") String contactEmail, @Field("contact_message") String contactMessage, @Field("contact_phone") String contactPhone);

    //endregion

    //GET All Category List
    @GET("rest/categories/get/api_key/{API_KEY}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<Category>>> getCategoryList(@Path("API_KEY") String apiKey, @Path("limit") String limit, @Path("offset") String offset);

    // GET All Color List
    @GET("rest/colors/get/api_key/{API_KEY}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<Color>>> getColorList(@Path("API_KEY") String api_key, @Path("limit") String limit, @Path("offset") String offset);

    //region wallpaper
    @FormUrlEncoded
    @POST("rest/wallpapers/search/api_key/{API_KEY}/login_user_id/{login_user_id}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<Wallpaper>>> getWallpaperList(@Path("API_KEY") String apiKey, @Path("login_user_id") String login_user_id, @Path("limit") String limit, @Path("offset") String offset,
                                                            @Field("wallpaper_name") String wallpaper_name, @Field("cat_id") String cat_id, @Field("added_user_id") String added_user_id, @Field("type") String type,
                                                            @Field("is_recommended") String is_recommended, @Field("is_portrait") String is_portrait, @Field("is_landscape") String is_landscape,
                                                            @Field("is_square") String is_square, @Field("color_id") String color_id, @Field("rating_max") String rating_max, @Field("rating_min") String rating_min,
                                                            @Field("point_max") String point_max,@Field("point_min") String point_min, @Field("is_wallpaper") String is_wallpaper, @Field("is_gif") String is_gif,
                                                            @Field("is_video_wallpaper") String is_video_wallpaper, @Field("order_by") String order_by, @Field("order_type") String order_type);
    //endregion

    //GET All wallpaper List
    @GET("rest/wallpapers/get_favourite/api_key/{API_KEY}/login_user_id/{login_user_id}/wallpaper_type/{wallpaper_type}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<Wallpaper>>> getFavoriteWallpaper(@Path("API_KEY") String apiKey,
                                                                @Path("login_user_id") String login_user_id,
                                                                @Path("wallpaper_type") String wallpaper_type,
                                                                @Path("limit") String limit,
                                                                @Path("offset") String offset);

    //GET Downloaded wallpaper List
    @GET("rest/wallpapers/get_download/api_key/{API_KEY}/login_user_id/{login_user_id}/wallpaper_type/{wallpaper_type}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<Wallpaper>>> getDownloadedWallpaper(@Path("API_KEY") String apiKey,
                                                                  @Path("login_user_id") String login_user_id,
                                                                  @Path("wallpaper_type") String wallpaper_type,
                                                                  @Path("limit") String limit,
                                                                  @Path("offset") String offset
                                                                  );

    //GET Uploaded wallpaper List
    @GET("rest/wallpapers/get_wallpaper_user/api_key/{API_KEY}/login_user_id/{login_user_id}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<Wallpaper>>> getUploadedWallpaper(@Path("API_KEY") String apiKey,
                                                                @Path("login_user_id") String login_user_id,
                                                                @Path("limit") String limit,
                                                                @Path("offset") String offset);


    //region download count
    @FormUrlEncoded
    @POST("rest/downloads/download_wallpaper/api_key/{API_KEY}/login_user_id/{login_user_id}")
    Call<Wallpaper> setRawPostDownloadCount(
            @Path("API_KEY") String apiKey,
            @Path("login_user_id") String login_user_id,
            @Field("wallpaper_id") String wallpaper_id,
            @Field("user_id") String user_id);

    //region touch count
    @FormUrlEncoded
    @POST("rest/touches/touch_wallpaper/api_key/{API_KEY}/login_user_id/{login_user_id}")
    Call<Wallpaper> setRawPostTouchCount(
            @Path("API_KEY") String apiKey,
            @Path("login_user_id") String login_user_id,
            @Field("wallpaper_id") String wallpaper_id,
            @Field("user_id") String user_id);

    //region favourite
    @FormUrlEncoded
    @POST("rest/favourites/press/api_key/{API_KEY}/login_user_id/{login_user_id}")
    Call<Wallpaper> setRawPostFavourite(
            @Path("API_KEY") String apiKey,
            @Path("login_user_id") String login_user_id,
            @Field("wallpaper_id") String wallpaper_id,
            @Field("user_id") String user_id);
    //endregion

    @FormUrlEncoded
    @POST("rest/rates/add_rating/api_key/{API_KEY}/login_user_id/{login_user_id}")
    Call<Wallpaper> setRawPostRating(
            @Path("API_KEY") String apiKey,
            @Path("login_user_id") String login_user_id,
            @Field("wallpaper_id") String wallpaper_id,
            @Field("user_id") String user_id,
            @Field("rating") String rating);

    @FormUrlEncoded
    @POST("rest/users/claim_point/api_key/{API_KEY}")
    Call<User> setPointToServer(
            @Path("API_KEY") String apiKey,
            @Field("user_id") String user_id,
            @Field("point") String claimed_point
    );

    @GET("rest/users/get/api_key/{API_KEY}/id/{user_id}")
    LiveData<ApiResponse<User>> getPointByUserId(
            @Path("API_KEY") String apiKey,
            @Path("user_id") String user_Id
    );

    @FormUrlEncoded
    @POST("rest/earningpoints/buy_wallpaper/api_key/{API_KEY}/login_user_id/{login_user_id}")
    Call<Wallpaper> buyWallpaperRepo(
            @Path("API_KEY") String apiKey,
            @Path("login_user_id") String login_user_id,
            @Field("user_id") String user_id,
            @Field("earn_point") int earn_point,
            @Field("currency_symbol") String currency_symbol,
            @Field("wallpaper_id") String wallpaper_id
    );

    @FormUrlEncoded
    @POST("rest/wallpapers/add_wallpaper/api_key/{API_KEY}")
    Call<Wallpaper> uploadWallpaper(
            @Path("API_KEY") String apiKey,
            @Field("cat_id") String cat_id,
            @Field("color_id") String color_id,
            @Field("wallpaper_name") String wallpaper_name,
            @Field("types") String types,
            @Field("is_portrait") String is_portrait,
            @Field("is_landscape") String is_landscape,
            @Field("is_square") String is_square,
            @Field("point") String point,
            @Field("wallpaper_search_tags") String wallpaper_search_tags,
            @Field("added_user_id") String added_user_id,
            @Field("wallpaper_id") String wallpaper_id,
            @Field("device_token") String device_token,
            @Field("credit") String credit,
            @Field("is_gif") String isGif,
            @Field("is_wallpaper") String isWallpaper
    );

    @Multipart
    @POST("rest/images/upload_wallpaper/api_key/{API_KEY}")
    LiveData<ApiResponse<Image>> uploadWallpaperImage(
            @Path("API_KEY") String apiKey,
            @Part("wallpaper_id") RequestBody wallpaper_id,
            @Part("img_id") RequestBody img_id,
            @Part MultipartBody.Part file
    );

    @FormUrlEncoded
    @POST("rest/wallpapers/wallpaper_delete/api_key/{API_KEY}")
    Call<ApiStatus> deleteWallpaperById(
            @Path("API_KEY") String apiKey,
            @Field("wallpaper_id") String wallpaper_id,
            @Field("added_user_id") String added_user_id
    );

    @GET("rest/wallpapers/get/api_key/{API_KEY}/id/{id}/login_user_id/{userId}")
    LiveData<ApiResponse<Wallpaper>> getWallpaperById(
            @Path("API_KEY") String apiKey,
            @Path("id") String id,
            @Path("userId") String userId
    );

    //region Delete Wallpaper list by date

    @FormUrlEncoded
    @POST("rest/appinfo/get_delete_history/api_key/{API_KEY}")
    Call<PSAppInfo> getDeletedHistory(
            @Path("API_KEY") String apiKey,
            @Field("start_date") String start_date,
            @Field("end_date") String end_date);


    //Login with facebook
    @FormUrlEncoded
    @POST("rest/users/register/api_key/{API_KEY}")
    Call<User> postFBUser(@Path("API_KEY") String apiKey, @Field("facebook_id") String facebookId, @Field("user_name") String userName, @Field("user_email") String userEmail, @Field("profile_photo_url") String profilePhotoUrl);


    //Login with google
    @FormUrlEncoded
    @POST("rest/users/google_register/api_key/{API_KEY}")
    Call<User> postGoogleUser(
            @Path("API_KEY") String API_KEY,
            @Field("google_id") String googleId,
            @Field("user_name") String userName,
            @Field("user_email") String userEmail,
            @Field("profile_photo_url") String profilePhotoUrl,
            @Field("device_token") String deviceToken
    );

    @FormUrlEncoded
    @POST("rest/users/verify/api_key/{API_KEY}")
    Call<User> verifyEmail(
            @Path("API_KEY") String API_KEY,
            @Field("user_id") String userId,
            @Field("code") String code);

    @FormUrlEncoded
    @POST("rest/users/request_code/api_key/{API_KEY}")
    Call<ApiStatus> resentCodeAgain(
            @Path("API_KEY") String API_KEY,
            @Field("user_email") String user_email
    );

    @FormUrlEncoded
    @POST("rest/users/phone_register/api_key/{API_KEY}")
    Call<User> postPhoneLogin(
            @Path("API_KEY") String API_KEY,
            @Field("phone_id") String phoneId,
            @Field("user_name") String userName,
            @Field("user_phone") String userPhone,
            @Field("device_token") String deviceToken
    );

    //endregion

}