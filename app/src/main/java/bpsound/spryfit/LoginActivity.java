package bpsound.spryfit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import bpsound.spryfit.data.UserInfo;
import bpsound.spryfit.utils.AppInfoUtils;
import bpsound.spryfit.utils.KLib;

/**
 * Created by elegantuniv on 2016. 5. 10..
 */
public class LoginActivity extends Activity{
    public static final int RC_FACEBOOK_LOGIN = 0;

    private Context mContext;
    private TextView mTvVersion;

    private Firebase mFirebaseRef;
    private AuthData mAuthData;
    private ProgressDialog mAuthProgressDialog;

    /* *************************************
     *              FACEBOOK               *
     ***************************************/
    /* The login button for Facebook */
    private LoginButton mFacebookLoginButton;
    /* The callback manager for Facebook */
    private CallbackManager mFacebookCallbackManager;
    /* Used to track user logging in/out off Facebook */
    private AccessTokenTracker mFacebookAccessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_login);

        bindViews();
        setListener();

         /* *************************************
         *              FACEBOOK               *
         ***************************************/
        /* Load the Facebook login button and set up the tracker to monitor access token changes */
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginButton = (LoginButton) findViewById(R.id.login_with_facebook);
        mFacebookAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.i("anq", "Facebook.AccessTokenTracker.OnCurrentAccessTokenChanged");
                LoginActivity.this.onFacebookAccessTokenChange(currentAccessToken);
            }
        };

        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        /* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
         * user and hide hide any login buttons */
        mFirebaseRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mAuthProgressDialog.hide();

                setAuthenticatedUser(authData);
            }
        });
    }

    private void bindViews() {
        mTvVersion = (TextView)findViewById(R.id.tvVersion);

    }

    private void setListener() {

    }

    @Override
    public void onResume() {
        super.onResume();

        String app_version = AppInfoUtils.getVersionName();
        mTvVersion.setText(app_version + " v.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // if user logged in with Facebook, stop tracking their token
        if (mFacebookAccessTokenTracker != null) {
            mFacebookAccessTokenTracker.stopTracking();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {
            /* Hide all the login buttons */
            mFacebookLoginButton.setVisibility(View.GONE);
        } else {
            /* No authenticated user show all the login buttons */
            mFacebookLoginButton.setVisibility(View.VISIBLE);

        }
        this.mAuthData = authData;

        /* go to main activity */
        if(authData != null){
            Log.d("anq", authData.toString());
            String uid = authData.getUid();
            String provider = authData.getProvider();

            ///only for facebook
            String userName = authData.getProviderData().get("displayName").toString();
            LinkedHashMap pfData = (LinkedHashMap)authData.getProviderData().get("cachedUserProfile");
            String gender = pfData.get("gender").toString();
            String link = pfData.get("link").toString();

            LinkedHashMap lmPicture = (LinkedHashMap)pfData.get("picture");
            LinkedHashMap lmPData = (LinkedHashMap) lmPicture.get("data");
            String picpath = lmPData.get("url").toString();

            String locale = pfData.get("locale").toString();

            //storing data to server
            Map<String, String> map = new HashMap<>();
            map.put("provider", provider);
            map.put("username", userName);
            map.put("gender", gender);
            map.put("link", link);
            map.put("picpath", picpath);
            map.put("locale", locale);
            mFirebaseRef.child("users").child(uid).setValue(map);

            UserInfo.setUid(uid);
            UserInfo.setProvider(provider);
            UserInfo.setUserName(userName);
            UserInfo.setGender(gender);
            UserInfo.setLink(link);
            UserInfo.setMyPicPath(picpath);
            UserInfo.setLocale(locale);

            redirectMainActivity();
        }
    }

    protected void redirectMainActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /* ************************************
     *             FACEBOOK               *
     **************************************
     */
    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            mAuthProgressDialog.show();
            mFirebaseRef.authWithOAuthToken("facebook", token.getToken(), new AuthResultHandler("facebook"));
        } else {
            // Logged out of Facebook and currently authenticated with Firebase using Facebook, so do a logout
            if (this.mAuthData != null && this.mAuthData.getProvider().equals("facebook")) {
                mFirebaseRef.unauth();
                setAuthenticatedUser(null);
            }
        }
    }

    /**
     * Utility class for authentication results
     */
    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.hide();
            Log.i("anq", provider + " auth successful");
            setAuthenticatedUser(authData);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();
            KLib.showErrorDialog(mContext, firebaseError.toString());
        }
    }
}
