package com.panaceasoft.pswallpaper.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.panaceasoft.pswallpaper.Config;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.binding.FragmentDataBindingComponent;
import com.panaceasoft.pswallpaper.databinding.FragmentUserLoginBinding;
import com.panaceasoft.pswallpaper.ui.common.PSFragment;
import com.panaceasoft.pswallpaper.utils.AutoClearedValue;
import com.panaceasoft.pswallpaper.utils.Constants;
import com.panaceasoft.pswallpaper.utils.PSDialogMsg;
import com.panaceasoft.pswallpaper.utils.Utils;
import com.panaceasoft.pswallpaper.viewmodel.user.UserViewModel;

import org.json.JSONException;

import java.util.Arrays;


/**
 * UserLoginFragment
 */
public class UserLoginFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    @VisibleForTesting
    AutoClearedValue<FragmentUserLoginBinding> binding;

    AutoClearedValue<ProgressDialog> prgDialog;

    private PSDialogMsg psDialogMsg;

    private CallbackManager callbackManager;

    private boolean checkFlag;

    private String token;

    //Firebase test
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleSignInClient mGoogleSignInClient;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);


        callbackManager = CallbackManager.Factory.create();
        // Inflate the layout for this fragment
        FragmentUserLoginBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_login, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }

        return binding.get().getRoot();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constants.REQUEST_CODE__GOOGLE_SIGN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Utils.psLog("firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        if (getActivity() != null) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Utils.psLog("signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                userViewModel.setRegisterGoogleUser(user.getUid(), user.getDisplayName(), user.getEmail(), String.valueOf(user.getPhotoUrl()), token);

                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Utils.psErrorLog("signInWithCredential:failure", task.getException());
                            Toast.makeText(UserLoginFragment.this.getActivity(), "SignIn Failed", Toast.LENGTH_LONG).show();

                        }
                    });
        }
    }

    //firebase
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void initUIAndActions() {

//        Glide.with(this).load(getResources().getDrawable(R.drawable.wallpaper_login_bg)).into(binding.get().bgImageView);

        dataBindingComponent.getFragmentBindingAdapters().bindFullImageDrawbale(binding.get().bgImageView, getResources().getDrawable(R.drawable.wallpaper_login_bg));

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        prgDialog.get().setMessage(getString(R.string.message__please_wait));
        prgDialog.get().setCancelable(false);

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        //fadeIn Animation
        fadeIn(binding.get().getRoot());

        binding.get().loginButton.setOnClickListener(view -> {

            Utils.hideKeyboard(getActivity());

            if (connectivity.isConnected()) {
                String userEmail = binding.get().emailEditText.getText().toString().trim();
                String userPassword = binding.get().passwordEditText.getText().toString().trim();

                Utils.psLog("Email " + userEmail);
                Utils.psLog("Password " + userPassword);

                if (userEmail.equals("")) {
                    Toast.makeText(getContext(), R.string.error_message__blank_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userPassword.equals("")) {
                    Toast.makeText(getContext(), R.string.error_message__blank_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!userViewModel.isLoading) {

                    updateLoginBtnStatus();

                    doSubmit(userEmail, userPassword);

                }
            } else {
                Toast.makeText(getContext(), R.string.error_message__no_internet, Toast.LENGTH_SHORT).show();
            }

        });

        binding.get().registerButton.setOnClickListener(view ->
                Utils.navigateAfterRegister(UserLoginFragment.this.getActivity(), navigationController));

        binding.get().forgotPasswordButton.setOnClickListener(view ->
                Utils.navigateAfterForgotPassword(UserLoginFragment.this.getActivity(), navigationController));

        binding.get().phoneLoginButton.setOnClickListener(v -> Utils.navigateAfterPhoneLogin(getActivity(),navigationController));

        if (Config.ENABLE_FACEBOOK_LOGIN) {
            binding.get().fbLoginButton.setVisibility(View.VISIBLE);
        } else {
            binding.get().fbLoginButton.setVisibility(View.GONE);
        }
        if (Config.ENABLE_GOOGLE_LOGIN) {
            binding.get().googleLoginButton.setVisibility(View.VISIBLE);
        } else {
            binding.get().googleLoginButton.setVisibility(View.GONE);
        }
        if (Config.ENABLE_PHONE_LOGIN) {
            binding.get().phoneLoginButton.setVisibility(View.VISIBLE);
        } else {
            binding.get().phoneLoginButton.setVisibility(View.GONE);
        }
        if (Config.ENABLE_FACEBOOK_LOGIN || Config.ENABLE_GOOGLE_LOGIN || Config.ENABLE_PHONE_LOGIN) {
            binding.get().privacyPolicyCheckbox.setVisibility(View.VISIBLE);
        } else {
            binding.get().privacyPolicyCheckbox.setVisibility(View.GONE);
        }

        View.OnClickListener onClickListener = v -> {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__to_check_agreement), getString(R.string.app__ok));
            psDialogMsg.show();

        };

        binding.get().facebookSignInView.setOnClickListener(onClickListener);

        binding.get().googleSignInView.setOnClickListener(onClickListener);

        binding.get().googleSignInView.setOnClickListener(onClickListener);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = firebaseAuth -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                int a;
//                Toast.makeText(UserLoginFragment.this.getContext(), "Successfully singned in with" + currentUser.getUid(), Toast.LENGTH_SHORT).show();
            }
        };

        //end

        // Configure Google Sign In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        if (getActivity() != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);
        }
        //end

        //google login
        binding.get().googleLoginButton.setOnClickListener(v -> signIn());

        //for check privacy and policy
        binding.get().privacyPolicyCheckbox.setOnClickListener(v -> {
            if (binding.get().privacyPolicyCheckbox.isChecked()) {

                navigationController.navigateToPrivacyPolicyActivity(getActivity());
                checkFlag = true;
                binding.get().googleSignInView.setVisibility(View.GONE);
                binding.get().facebookSignInView.setVisibility(View.GONE);
                binding.get().phoneSignInView.setVisibility(View.GONE);
                binding.get().fbLoginButton.setEnabled(true);
                binding.get().googleLoginButton.setEnabled(true);
                binding.get().phoneLoginButton.setEnabled(true);
            } else {

                checkFlag = false;
                binding.get().googleSignInView.setVisibility(View.VISIBLE);
                binding.get().facebookSignInView.setVisibility(View.VISIBLE);
                binding.get().phoneSignInView.setVisibility(View.VISIBLE);
                binding.get().fbLoginButton.setEnabled(false);
                binding.get().googleLoginButton.setEnabled(false);
                binding.get().phoneLoginButton.setEnabled(false);
            }
        });

        // For First Time Loading
        if (!checkFlag) {
            binding.get().googleSignInView.setVisibility(View.VISIBLE);
            binding.get().facebookSignInView.setVisibility(View.VISIBLE);
            binding.get().phoneSignInView.setVisibility(View.VISIBLE);
            binding.get().fbLoginButton.setEnabled(false);
            binding.get().googleLoginButton.setEnabled(false);
            binding.get().phoneLoginButton.setEnabled(false);
        } else {
            binding.get().googleSignInView.setVisibility(View.GONE);
            binding.get().facebookSignInView.setVisibility(View.GONE);
            binding.get().phoneSignInView.setVisibility(View.GONE);
            binding.get().fbLoginButton.setEnabled(true);
            binding.get().googleLoginButton.setEnabled(true);
            binding.get().phoneLoginButton.setEnabled(true);
        }
    }

    private void updateLoginBtnStatus() {
        if (userViewModel.isLoading) {
            binding.get().loginButton.setText(getResources().getString(R.string.message__loading));
        } else {
            binding.get().loginButton.setText(getResources().getString(R.string.login__login));
        }
    }

    private void doSubmit(String email, String password) {

        //prgDialog.get().show();
        userViewModel.setUserLogin(email, password);

        userViewModel.isLoading = true;

    }


    @Override
    protected void initViewModels() {
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        token = pref.getString(Constants.NOTI_TOKEN, Constants.USER_NO_DEVICE_TOKEN);

        userViewModel.getLoadingState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loadingState) {

                if (loadingState != null && loadingState) {
                    prgDialog.get().show();
                } else {
                    prgDialog.get().cancel();
                }

                UserLoginFragment.this.updateLoginBtnStatus();

            }
        });

        userViewModel.getUserLoginStatus().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            try {

                                Utils.updateUserLoginData(pref, listResource.data.user);
                                Utils.navigateAfterUserLogin(getActivity(), navigationController);

                            } catch (NullPointerException ne) {
                                Utils.psErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Utils.psErrorLog("Error in getting notification flag data.", e);
                            }

                            userViewModel.setLoadingState(false);

                        }

                        break;
                    case ERROR:
                        // Error State

                        Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();

                        userViewModel.setLoadingState(false);

                        break;
                    default:
                        // Default

                        userViewModel.setLoadingState(false);

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }


            // we don't need any null checks here for the adapter since LiveData guarantees that
//            // it won't call us if fragment is stopped or not started.
//            if (listResource != null && listResource.data != null) {
//                Utils.psLog("Got Data" + listResource.message + listResource.toString());
//
//
//                if (listResource.message != null && !listResource.message.equals("")) {
//                    Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
//                    userViewModel.isLoading = false;
//                    prgDialog.get().cancel();
//                } else {
//                    // Update the data
//
//
//                }
//
//
//            } else if (listResource != null && listResource.message != null) {
//                Utils.psLog("Message from server.");
//                Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
//
//                userViewModel.isLoading = false;
//                prgDialog.get().cancel();
//            } else {
//
//                Utils.psLog("Empty Data");
//
//            }

        });

        //For Facebook button click
        binding.get().fbLoginButton.setFragment(this);
        binding.get().fbLoginButton.setPermissions(Arrays.asList("email"));
        binding.get().fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        (object, response) -> {

                            String name = "";
                            String email = "";
                            String id = "";
                            String imageURL = "";
                            try {
                                if (object != null) {

                                    name = object.getString("name");

                                }
                                //link.setText(object.getString("link"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                if (object != null) {

                                    email = object.getString("email");

                                }
                                //link.setText(object.getString("link"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                if (object != null) {

                                    id = object.getString("id");

                                }
                                //link.setText(object.getString("link"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            if (!id.equals("")) {
                                prgDialog.get().show();
                                userViewModel.registerFBUser(id, name, email, imageURL);
                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email,name,id");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

                Utils.psLog("OnCancel.");
            }

            @Override
            public void onError(FacebookException e) {
                Utils.psLog("OnError." + e.getMessage());
            }


        });


        userViewModel.getRegisterFBUserData().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        prgDialog.get().show();

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            try {

                                Utils.updateUserLoginData(pref, listResource.data.user);
                                Utils.navigateAfterUserLogin(getActivity(), navigationController);

                            } catch (NullPointerException ne) {
                                Utils.psErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Utils.psErrorLog("Error in getting notification flag data.", e);
                            }

                            userViewModel.isLoading = false;
                            prgDialog.get().cancel();

                        }

                        break;
                    case ERROR:
                        // Error State

                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();

                        break;
                    default:
                        // Default
                        //userViewModel.isLoading = false;
                        //prgDialog.get().cancel();
                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }

        });

        userViewModel.getGoogleLoginData().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        prgDialog.get().show();

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            try {

                                Utils.updateUserLoginData(pref, listResource.data.user);
                                Utils.navigateAfterUserLogin(getActivity(), navigationController);

                            } catch (NullPointerException ne) {
                                Utils.psErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Utils.psErrorLog("Error in getting notification flag data.", e);
                            }

                            userViewModel.isLoading = false;
                            prgDialog.get().cancel();

                        }

                        break;
                    case ERROR:
                        // Error State

                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }

        });

    }

    //endregion

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Constants.REQUEST_CODE__GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Utils.psErrorLog("Google sign in failed", e);
                // ...
            }
        }
    }


}
