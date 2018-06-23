package com.pag.socialz.Managers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pag.socialz.Util.GoogleApiUtil;
import com.pag.socialz.Util.LogUtil;

public class LogoutManager {

    private static final String TAG = LogoutManager.class.getSimpleName();
    private static ClearImageCache clearImageCache;

    public static void signOut(GoogleApiClient mGoogleApiClient, FragmentActivity fragmentActivity){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            DatabaseManager.getInstance(fragmentActivity.getApplicationContext())
                    .removeRegistrationToken(FirebaseInstanceId.getInstance().getToken(),user.getUid());
            for(UserInfo profile: user.getProviderData()){
                String providerId = profile.getProviderId();
                logoutByProvider(providerId,mGoogleApiClient,fragmentActivity);
            }
            logoutFirebase(fragmentActivity.getApplicationContext());
        }
    }

    private static void logoutByProvider(String providerId, GoogleApiClient mGoogleApiClient, FragmentActivity fragmentActivity){
        switch (providerId){
            case GoogleAuthProvider.PROVIDER_ID:
                logoutGoogle(mGoogleApiClient, fragmentActivity);
                break;
        }
    }

    private static void logoutFirebase(Context context){
        FirebaseAuth.getInstance().signOut();
        PreferencesUtil.setProfileCreated(context, false);
    }

    private static void logoutGoogle(GoogleApiClient mGoogleApiClient, FragmentActivity fragmentActivity){
        if(mGoogleApiClient == null) mGoogleApiClient = GoogleApiUtil.createGoogleApiClient(fragmentActivity);
        if(!mGoogleApiClient.isConnected()) mGoogleApiClient.connect();

        final GoogleApiClient finalmGoogleApiClient = mGoogleApiClient;
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if(finalmGoogleApiClient.isConnected()){
                    Auth.GoogleSignInApi.signOut(finalmGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if(status.isSuccess()){
                                LogUtil.logDebug(TAG,"User logged out from google");
                            }else{
                                LogUtil.logDebug(TAG,"Error logging out from google");
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                LogUtil.logDebug(TAG, "Google API client has suspended the connection");
            }
        });
    }

    private static class ClearImageCache extends AsyncTask<Void, Void, Void>{

        private Context context;

        public ClearImageCache(Context context){
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Glide.get(context.getApplicationContext()).clearDiskCache();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            clearImageCache = null;
            Glide.get(context.getApplicationContext()).clearMemory();
        }
    }
}
