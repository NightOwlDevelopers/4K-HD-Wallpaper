package com.panaceasoft.pswallpaper.viewmodel.user;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.repository.user.UserRepository;
import com.panaceasoft.pswallpaper.utils.AbsentLiveData;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewmodel.common.PSViewModel;
import com.panaceasoft.pswallpaper.viewobject.ApiStatus;
import com.panaceasoft.pswallpaper.viewobject.User;
import com.panaceasoft.pswallpaper.viewobject.UserLogin;
import com.panaceasoft.pswallpaper.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Panacea-Soft on 12/12/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class UserViewModel extends PSViewModel {


    //region Variables

    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    public boolean isLoading = false;
    public String profileImagePath = "";
    public User user;
    public String deviceToken = "";
    public String appHome, appGrid, appDetail;

    private final UserRepository repository;

    // for Login
    private final LiveData<Resource<UserLogin>> doUserLoginData;
    private MutableLiveData<LoginDataHolder> doUserLoginObj = new MutableLiveData<>();

    private final LiveData<Resource<User>> getUserPointByIdViewModelData;
    private MutableLiveData<String> getUserPointByIdObj = new MutableLiveData<>();

    // for get User
    private final LiveData<Resource<User>> userData;
    private MutableLiveData<String> userObj = new MutableLiveData<>();

    //for resent verification code
    private final LiveData<Resource<Boolean>> resentVerifyCodeData;
    private MutableLiveData<UserViewModel.resentCodeTmpDataHolder> resentVerifyCodeObj = new MutableLiveData<>();

    // for get User from DB
    private final LiveData<User> localUserData;
    private MutableLiveData<String> localUserObj = new MutableLiveData<>();

    // for register
    private final LiveData<Resource<User>> registerUserData;
    private MutableLiveData<TmpDataHolder> registerUserObj = new MutableLiveData<>();

    // for getting login user from db
    private final LiveData<List<UserLogin>> userLoginData;
    private MutableLiveData<String> userLoginObj = new MutableLiveData<>();

    //for verification code
    private final LiveData<Resource<UserLogin>> verificationEmailData;
    private MutableLiveData<TmpDataHolder> verificationEmailObj = new MutableLiveData<>();


    // for update user
    private final LiveData<Resource<ApiStatus>> updateUserData;
    private MutableLiveData<TmpDataHolder> updateUserObj = new MutableLiveData<>();

    // for forgot password
    private final LiveData<Resource<ApiStatus>> forgotpasswordData;
    private MutableLiveData<String> forgotPasswordObj = new MutableLiveData<>();

    // for password update
    private final LiveData<Resource<ApiStatus>> passwordUpdateData;
    private MutableLiveData<TmpDataHolder> passwordUpdateObj = new MutableLiveData<>();

    // for image upload
    private MutableLiveData<String> imgObj = new MutableLiveData<>();

    // for register FB
    private final LiveData<Resource<UserLogin>> registerFBUserData;
    private MutableLiveData<TmpDataHolder> registerFBUserObj = new MutableLiveData<>();

    // for register Google
    private final LiveData<Resource<UserLogin>> registerGoogleUserData;
    private MutableLiveData<TmpDataHolder> registerGoogleUserObj = new MutableLiveData<>();

    // for phone login
    private final LiveData<Resource<UserLogin>> phoneLoginData;
    private MutableLiveData<TmpDataHolder> phoneLoginObj = new MutableLiveData<>();

    //endregion


    //region Constructor

    @Inject
    public UserViewModel(UserRepository repository) {

        this.repository = repository;

        // Login User
        doUserLoginData = Transformations.switchMap(doUserLoginObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : doUserLoginData");
            return repository.doLogin(Config.API_KEY, obj.email, obj.password);
        });

        // Register User
        registerUserData = Transformations.switchMap(registerUserObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : registerUserData");
            return repository.registerUser(Config.API_KEY, obj.user.user_name, obj.user.user_email, obj.user.user_password, obj.deviceToken);
        });

        verificationEmailData = Transformations.switchMap(verificationEmailObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.verificationCodeForUser(obj.loginUserId, obj.code);
        });

        resentVerifyCodeData = Transformations.switchMap(resentVerifyCodeObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.resentCodeForUser(obj.userEmail);

        });

        // Get User Data
        userLoginData = Transformations.switchMap(userLoginObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : userLoginData");
            return repository.getLoginUser();
        });

        // Get User Data
        userData = Transformations.switchMap(userObj, userId -> {
            if (userId == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : userLoginData");
            return repository.getUser(Config.API_KEY, userId);
        });

        // Get Local User Data
        localUserData = Transformations.switchMap(localUserObj, userId -> {
            if (userId == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : userLoginData");
            return repository.getLocalUser(userId);
        });

        // Update User
        updateUserData = Transformations.switchMap(updateUserObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : updateUserData");
            return repository.updateUser(Config.API_KEY, obj.user, obj.deviceToken);
        });

        // Forgot Password
        forgotpasswordData = Transformations.switchMap(forgotPasswordObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : forgotPasswordData");
            return repository.forgotPassword(Config.API_KEY, obj);
        });

        // Password Update
        passwordUpdateData = Transformations.switchMap(passwordUpdateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : passwordUpdateData");
            return repository.passwordUpdate(Config.API_KEY, obj.loginUserId, obj.password);
        });

        getUserPointByIdViewModelData = Transformations.switchMap(getUserPointByIdObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : passwordUpdateData");
            return repository.getTotalPointByUserId(obj);
        });

        // Register FB User
        registerFBUserData = Transformations.switchMap(registerFBUserObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : registerFBUserData");
            return repository.registerFBUser(Config.API_KEY, obj.fbId, obj.name, obj.email, obj.imageUrl);
        });

        // Register Google User
        registerGoogleUserData = Transformations.switchMap(registerGoogleUserObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : registerFBUserData");
            return repository.registerGoogleUser(Config.API_KEY, obj.googleId, obj.name, obj.email, obj.imageUrl, obj.deviceToken);
        });

        // phone login User
        phoneLoginData = Transformations.switchMap(phoneLoginObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : phoneLoginData");
            return repository.postPhoneLogin(Config.API_KEY, obj.phoneId, obj.name, obj.phone, obj.deviceToken);
        });
    }

    //endregion


    //region Methods

    // For loading status
    public void setLoadingState(Boolean state) {
        isLoading = state;
        loadingState.setValue(state);
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    // For Login User
    public void setUserLogin(String email, String password) {
        loadingState.setValue(true);
        LoginDataHolder dataHolder = new LoginDataHolder();
        dataHolder.email = email;
        dataHolder.password = password;
        this.doUserLoginObj.setValue(dataHolder);
    }

    public LiveData<Resource<UserLogin>> getUserLoginStatus() {
        return doUserLoginData;
    }


    // For Getting Login User Data
    public LiveData<List<UserLogin>> getLoginUser() {
        userLoginObj.setValue("load");

        return userLoginData;
    }

    // For User Data
    public void setUser(String userId) {
        userObj.setValue(userId);
    }

    public LiveData<Resource<User>> getUser(String userId) {
        userObj.setValue(userId);

        return userData;
    }

    // For Local User Data
    public void setLocalUser(String userId) {

        if (userId != null && (!userId.equals(""))) {
            localUserObj.setValue(userId);
        }

    }

    public LiveData<User> getLocalUser(String userId) {

        if (userId != null && (!userId.equals(""))) {
            localUserObj.setValue(userId);
        }

        return localUserData;
    }

    public void setEmailVerificationUser(String loginUserId, String code) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.loginUserId = loginUserId;
        tmpDataHolder.code = code;
        this.verificationEmailObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<UserLogin>> getEmailVerificationUser() {
        return verificationEmailData;
    }

    public void setResentVerifyCodeObj(String userEmail) {
        resentCodeTmpDataHolder tmpDataHolder = new resentCodeTmpDataHolder(userEmail);

        this.resentVerifyCodeObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getResentVerifyCodeData() {
        return resentVerifyCodeData;
    }


    // For Delete Login User
    public LiveData<Resource<Boolean>> deleteUserLogin(User user) {

        if (user == null) {
            return AbsentLiveData.create();
        }

        return this.repository.deleteUser(user);
    }


    // Upload Image
    public LiveData<Resource<User>> uploadImage(String filePath, String userId) {

        imgObj.setValue("PS");

        return Transformations.switchMap(imgObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return this.repository.uploadImage(filePath, userId, Utils.PLATFORM);
        });

    }


    // Update User
    public LiveData<Resource<ApiStatus>> updateUser(User user, String deviceToken) {
        TmpDataHolder holder = new TmpDataHolder();
        holder.user = user;
        holder.deviceToken = deviceToken;
        updateUserObj.setValue(holder);

        return updateUserData;
    }


    // Register User
    public LiveData<Resource<User>> registerUser(User user, String deviceToken) {
        TmpDataHolder holder = new TmpDataHolder();
        holder.user = user;
        holder.deviceToken = deviceToken;
        registerUserObj.setValue(holder);
        return registerUserData;
    }


    // Forgot password
    public LiveData<Resource<ApiStatus>> forgotPassword(String email) {
        forgotPasswordObj.setValue(email);
        return forgotpasswordData;
    }

    // Forgot password
    public LiveData<Resource<ApiStatus>> passwordUpdate(String loginUserId, String password) {

        TmpDataHolder holder = new TmpDataHolder();
        holder.loginUserId = loginUserId;
        holder.password = password;

        passwordUpdateObj.setValue(holder);
        return passwordUpdateData;
    }

    //endregion

    public void setGetUserPointByIdObj(String loginUserId) {
        this.getUserPointByIdObj.setValue(loginUserId);
    }

    public LiveData<Resource<User>> getGetUserPointByIdData() {
        return getUserPointByIdViewModelData;
    }


    // Register User
    public void registerFBUser(String fbId, String name, String email, String imageUrl) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.fbId = fbId;
        tmpDataHolder.name = name;
        tmpDataHolder.email = email;
        tmpDataHolder.imageUrl = imageUrl;

        registerFBUserObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<UserLogin>> getRegisterFBUserData() {
        return registerFBUserData;
    }

    // Register Google User
    public void setRegisterGoogleUser(String googleId, String name, String email,String imageUrl, String deviceToken) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.googleId = googleId;
        tmpDataHolder.name = name;
        tmpDataHolder.email = email;
        tmpDataHolder.imageUrl = imageUrl;
        tmpDataHolder.deviceToken = deviceToken;
        registerGoogleUserObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<UserLogin>> getGoogleLoginData() {
        return registerGoogleUserData;
    }
    //region Tmp Holder

    class TmpDataHolder {

        public String fbId = "";
        public String googleId = "";
        public String name = "";
        public String email = "";
        public String imageUrl = "";
        public String loginUserId = "";
        public String password = "";
        public String deviceToken = "";
        public String code = "";
        public String phoneId = "";
        public String phone = "";
        public User user = new User("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");

    }

    class LoginDataHolder {
        public String email = "";
        public String password = "";
    }

    private class resentCodeTmpDataHolder {

        public String userEmail;

        private resentCodeTmpDataHolder(String userEmail) {
            this.userEmail = userEmail;
        }
    }


    // phone login User
    public void setPhoneLoginUser(String phoneId, String name, String phone, String deviceToken) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.phoneId = phoneId;
        tmpDataHolder.name = name;
        tmpDataHolder.phone = phone;
        tmpDataHolder.deviceToken = deviceToken;
        phoneLoginObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<UserLogin>> getPhoneLoginData() {
        return phoneLoginData;
    }

    //endregion

}
