package com.pag.socialz.Managers;

import android.content.Context;

import com.google.firebase.database.ValueEventListener;
import com.pag.socialz.ApplicationHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseListenerManager {

    Map<Context, List<ValueEventListener>> activeListeners = new HashMap<>();

    public void addListenerToMap(Context context, ValueEventListener valueEventListener){
        if(activeListeners.containsKey(context)){
            activeListeners.get(context).add(valueEventListener);
        }else{
            List<ValueEventListener> valueEventListeners = new ArrayList<>();
            valueEventListeners.add(valueEventListener);
            activeListeners.put(context,valueEventListeners);
        }
    }

    public void closeListeners(Context context){
        DatabaseManager databaseManager = ApplicationHelper.getDatabaseManager();
        if(activeListeners.containsKey(context)){
            for(ValueEventListener listener:activeListeners.get(context)){
                databaseManager.closeListener(listener);
            }
            activeListeners.remove(context);
        }
    }
}
