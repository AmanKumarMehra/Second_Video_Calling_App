package com.example.videomeeting.firebase;

import android.content.Intent;
import android.util.Log;

import com.example.videomeeting.activities.IncomingInvitationActivity;
import com.example.videomeeting.utilities.Constants;
import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.lang.reflect.Type;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM", "Token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String type = remoteMessage.getData().get(Constants.REMOTE_MSG_TYPE);

        if(type != null){
            if(type.equals(Constants.REMOTE_MSG_INVITATION)){
                Intent intent = new Intent(getApplicationContext(), IncomingInvitationActivity.class);
                intent.putExtra(
                        Constants.REMOTE_MSG_MEETING_TYPE,
                        remoteMessage.getData().get(Constants.REMOTE_MSG_MEETING_TYPE)
                );
                intent.putExtra(
                        Constants.Key_First_Name,
                        remoteMessage.getData().get(Constants.Key_First_Name)
                );
                intent.putExtra(
                        Constants.Key_Last_Name,
                        remoteMessage.getData().get(Constants.Key_Last_Name)
                );
                intent.putExtra(
                        Constants.Key_Email,
                        remoteMessage.getData().get(Constants.Key_Email)
                );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

    }
}
