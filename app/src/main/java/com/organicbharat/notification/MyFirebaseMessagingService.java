package com.organicbharat.notification;

import android.app.PendingIntent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.organicbharat.R;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    AppPref appPref;

    @Override
    public void onCreate() {
        super.onCreate();

        AppLog.e(TAG, "OnCreate");
        appPref = AppPref.getInstance(getApplicationContext());
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        AppLog.e(TAG, "From: " + remoteMessage.getFrom());



        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            AppLog.e(TAG, "Message data payload: " + remoteMessage.getData());

            JSONObject jsonObject = new JSONObject(remoteMessage.getData());
            //JSONObject jsonNotification = new JSONObject(jsonObject.getString("message"));
            NotificationHelper.manageNotification(getApplicationContext(), jsonObject);

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            AppLog.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN", s);

        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        AppLog.e(TAG, "RegisterToken: " + token);
        appPref.set(AppPref.FCM_TOKEN, token);
        //SessionManager.getInstance(getApplicationContext()).setString(Constants.FCM_TOKEN,token);
    }
}
