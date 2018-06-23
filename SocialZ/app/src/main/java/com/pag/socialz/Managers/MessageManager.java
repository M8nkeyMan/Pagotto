package com.pag.socialz.Managers;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pag.socialz.Util.LogUtil;

public class MessageManager extends FirebaseMessagingService{

    private static final String TAG = MessageManager.class.getSimpleName();

    public MessageManager() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        LogUtil.logDebug(TAG, "From: "+remoteMessage.getFrom());
        if(remoteMessage.getData().size() > 0){
            LogUtil.logDebug(TAG, "Payload: "+remoteMessage.getData());
            //process data
            //else
            //handle now
        }
    }
}
