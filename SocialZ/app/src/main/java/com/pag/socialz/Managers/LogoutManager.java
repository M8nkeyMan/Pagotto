package com.pag.socialz.Managers;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.auth.GoogleAuthProvider;

public class LogoutManager {

    private static final String tag = LogoutManager.class.getSimpleName();
    private static ClearImageCache clearImageCache;

    public static void signOut(GoogleApiClient mGoogleApiClient, FragmentActivity fragmentActivity){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            DatabaseManager.getInstance(fragmentActivity.getApplicationContext())
                    .removeRegistrationToken(FirebaseInstanceId.getInstance().getToken(),user.getUid());
            for(UserInfo profile: user.getProviderData()){
                String providerId = profile.getProviderId();

            }
        }
    }

    private static void logout(String providerId, GoogleApiClient mGoogleApiClient, FragmentActivity fragmentActivity){
        switch (providerId){
            case GoogleAuthProvider.PROVIDER_ID:
                break;
        }
    }

    private static void logoutFirebase(Context context){
        FirebaseAuth.getInstance().signOut();
        PreferencesUtil.setProfileCreated(context, false);
    }

    private static void looutGoogle(GoogleApiClient mGoogleApiClient, FragmentActivity fragmentActivity){
        //if(mGoogleApiClient != null) mGoogleApiClient = GoogleApiHelper
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
