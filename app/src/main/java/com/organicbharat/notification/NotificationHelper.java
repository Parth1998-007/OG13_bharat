package com.organicbharat.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import android.util.Log;

import com.organicbharat.R;
import com.organicbharat.ui.my_orders.MyOrders;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by frenzin05 on 12/21/2017.
 */

public class NotificationHelper {

    private static final String TAG = "Noti_Helper";

    private Ringtone ringtone1;

    public static void manageNotification(Context context, JSONObject jsonNotification) {

        AppLog.e(TAG + "TEST12", "notification_data->" + jsonNotification.toString());

        String data = jsonNotification.optString("message");
        AppLog.e(TAG, "data is.." + data);
        if (data != null) {
            AppPref appPref = AppPref.getInstance(context);
            AppLog.e(TAG, "login is..." + appPref.getBoolean(AppPref.IS_LOGIN));
            if (appPref.getBoolean(AppPref.IS_LOGIN)) {
                JSONObject object = null;
                try {
                    object = new JSONObject(data);
                    Intent notificationIntent = new Intent(context, MyOrders.class);
                    notificationIntent.putExtra("orderid", object.getString("order_id"));
                    notificationIntent.putExtra("from", "notification");
                    //context,FitnessLoopDetailActivity.class
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    showAlertNotification(context, object, notificationIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void showAlertNotification(Context context, JSONObject jsonNotification, Intent intent) {
        AppLog.e(TAG, " hreerererere: " + jsonNotification.toString());
        AppLog.e(TAG, " hreerererere: " + jsonNotification.optString("notification_type") + " ; " + jsonNotification.optString("message"));
        String CHANNEL_ID = "organicbharatrider_01";// The id of the channel.

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.appicon)
                        .setContentTitle(jsonNotification.optString("Title"))//context.getResources().getString(R.string.app_name)
                        .setAutoCancel(true)
                        .setContentText(jsonNotification.optString("message"))
                        .setChannelId(CHANNEL_ID)
                        .setContentIntent(resultPendingIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getResources().getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            assert mNotifyMgr != null;
            mNotifyMgr.createNotificationChannel(mChannel);
        }

        assert mNotifyMgr != null;
        mNotifyMgr.notify(001, mBuilder.build());
       /* if(jsonNotification.optInt("is_sound")==1)
            playSound();*/

        openActivity(context, jsonNotification);
    }

    private static void openActivity(Context context, JSONObject jsonObject) {


        AppPref appPref = AppPref.getInstance(context);


        Log.e("APP NOTIFICATION", appPref.getBoolean("APP") + " ");


        // if(!appPref.getBoolean("APP")){
        Log.e("Start", "FROm Notification" + " " + appPref.getBoolean("APP"));

//            Intent intent = new Intent (context, MainActivity.class);
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//            stackBuilder.addParentStack(MainActivity.class);
//            stackBuilder.addNextIntent(intent);

        Intent i = new Intent(new Intent(context, MyOrders.class));
        AppLog.e(TAG, " orderidddddd: " + jsonObject.optString("order_id"));
        // try {
        i.putExtra("orderid", jsonObject.optString("order_id"));
        /*} catch (JSONException e) {
            e.printStackTrace();
        }*/
        i.putExtra("from", "notification");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("fromNotification", true);

        //        Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
        if (context == null) {
            Log.e("Context: ", "NULL");
        }

        try {
            context.startActivity(i);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

    }

}
