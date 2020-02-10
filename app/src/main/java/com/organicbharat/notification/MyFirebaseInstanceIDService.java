package com.organicbharat.notification;


import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;

public class MyFirebaseInstanceIDService  {
    private static final String TAG = "MyFirebaseIIDService";
    AppPref appPref;
  /*  @Override
    public void onCreate() {
        super.onCreate();
        AppLog.e(TAG,"OnCreate");

        appPref = AppPref.getInstance(getApplicationContext());
    }

    *//**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     *//*
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        AppLog.d(TAG, "Refreshed token: " + refreshedToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    *//**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        AppLog.e(TAG, "RegisterToken: " + token);
        appPref.set(AppPref.FCM_TOKEN,token);
        //SessionManager.getInstance(getApplicationContext()).setString(Constants.FCM_TOKEN,token);
    }
}
