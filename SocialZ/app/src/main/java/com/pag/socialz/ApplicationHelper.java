package com.pag.socialz;

import com.pag.socialz.Managers.DatabaseManager;

public class ApplicationHelper {

    private static final String TAG = ApplicationHelper.class.getSimpleName();
    private static DatabaseManager databaseHelper;

    public static DatabaseManager getDatabaseManager() {
        return databaseHelper;
    }

    public static void initDatabaseManager(android.app.Application application) {
        databaseHelper = DatabaseManager.getInstance(application);
        databaseHelper.init();
    }
}
