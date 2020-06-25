package com.panaceasoft.pswallpaper.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.firebase.messaging.RemoteMessage;
import com.panaceasoft.pswallpaper.R;
import com.panaceasoft.pswallpaper.MainActivity;

import androidx.core.app.NotificationCompat;


/**
 * Created by Panacea-Soft on 8/11/16.
 * Contact Email : teamps.is.cool@gmail.com
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        /* Important : Please don't change this "message" because if you change this, need to update at PHP.  */

        String msg = remoteMessage.getData().get("message");

        if(msg!= null && msg.equals("")) {
            msg = remoteMessage.getNotification().getBody();
        }
        showNotification(msg);

    }


    private void showNotification(String message) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Utils.NOTI_MSG, message);
        editor.putBoolean( Utils.IS_NOTI_EXISTS_TO_SHOW, true);
        editor.apply();

        Intent i = new Intent(this,MainActivity.class);
        i.putExtra(Utils.NOTI_MSG, message);
        String displayMessage ;
        Utils.psLog("Message From Server : " + message);
        if(message == null || message.equals("")) {
            i.putExtra(Utils.IS_NOTI_EXISTS_TO_SHOW, false);
            displayMessage = "Welcome from PSFoundation";
        } else {
            i.putExtra(Utils.IS_NOTI_EXISTS_TO_SHOW, true);
            displayMessage = message; //"You've received new message.";

        }

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "0")
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.notification__alert))
                .setContentText(displayMessage)
                .setSmallIcon(R.drawable.ic_notifications_white)
                .setContentIntent(pendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        builder.setDefaults(defaults);
        // Set the content for Notification
        builder.setContentText(displayMessage);
        // Set autocancel
        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(manager != null) {
            manager.notify(0, builder.build());
        }
    }


}
