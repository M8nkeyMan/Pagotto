package com.pag.socialz.Managers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.pag.socialz.ApplicationHelper;
import com.pag.socialz.Enums.ProfileStatus;
import com.pag.socialz.Enums.UploadImagePrefix;
import com.pag.socialz.Listeners.OnObjectChangedListener;
import com.pag.socialz.Listeners.OnObjectExistListener;
import com.pag.socialz.Listeners.OnProfileCreatedListener;
import com.pag.socialz.Models.Profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileManager extends FirebaseListenerManager {

    private static final String TAG = ProfileManager.class.getSimpleName();
    private static ProfileManager instance;

    private Context context;
    private DatabaseManager databaseManager;
    private Map<Context, List<ValueEventListener>> activeListeners = new HashMap<>();

    public static ProfileManager getInstance(Context context){
        if(instance == null){
            instance = new ProfileManager(context);
        }
        return instance;
    }

    private ProfileManager(Context context){
        this.context = context;
        this.databaseManager = ApplicationHelper.getDatabaseManager();
    }

    public void isProfileExisting(String id, final OnObjectExistListener<Profile> onObjectExistListener){
        DatabaseReference databaseReference = databaseManager.getDatabaseReference().child("profiles").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onObjectExistListener.onDataChanged(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Profile buildProfile(FirebaseUser firebaseUser, String largeAvatarURL){
        Profile profile = new Profile(firebaseUser.getUid());
        profile.setEmail(firebaseUser.getEmail());
        profile.setUsername(firebaseUser.getDisplayName());
        profile.setPhotoUrl(largeAvatarURL != null?largeAvatarURL:firebaseUser.getPhotoUrl().toString());
        return profile;
    }

    public void createOrUpdateProfile(Profile profile, OnProfileCreatedListener onProfileCreatedListener){
        createOrUpdateProfile(profile, null, onProfileCreatedListener);
    }

    public void createOrUpdateProfile(final Profile profile, Uri imageUri, final OnProfileCreatedListener onProfileCreatedListener){
        if(imageUri == null){
            databaseManager.createOrUpdateProfile(profile, onProfileCreatedListener);
            return;
        }

        String imageTitle = ImageUtil.generateImageTitle(UploadImagePrefix.PROFILE, profile.getId());
        UploadTask uploadTask = databaseManager.uploadImage(imageUri,imageTitle);

        if(uploadTask != null){
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        //Uri testDownloadUrl = task.getResult().getMetadata()
                        Uri downloadUrl = task.getResult().getDownloadUrl();
                        LogUtil.logDebug(TAG,"caricamento completo, url: "+String.valueOf(downloadUrl));
                        profile.setPhotoUrl(downloadUrl.toString());
                        databaseManager.createOrUpdateProfile(profile, onProfileCreatedListener);
                    }
                }
            });
        }
    }

    public void getProfileValue(Context activityContext, String id, final OnObjectChangedListener<Profile> listener){
        ValueEventListener valueEventListener = databaseManager.getProfile(id, listener);
        addListenerToMap(activityContext,valueEventListener);
    }

    public void getProfileSingleValue(String id, final OnObjectChangedListener<Profile> listener){
        databaseManager.getProfileSingleValue(id, listener);
    }

    public ProfileStatus checkProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return ProfileStatus.NOT_AUTHORIZED;
        }else if(!PreferencesUtil.isProfileCreated(context)){
            return ProfileStatus.NO_PROFILE;
        }else{
            return ProfileStatus.PROFILE_CREATED;
        }
    }
}

