package com.pag.socialz;

import com.pag.socialz.Managers.DatabaseManager;

public class Application extends android.app.Application {

    public static final String TAG = Application.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationHelper.initDatabaseManager(this);
        DatabaseManager.getInstance(this).subscribeToNewPosts();
    }
}
