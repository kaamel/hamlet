package com.genesis.hamlet.notifiations;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.genesis.hamlet.R;
import com.genesis.hamlet.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        //FirebaseMessaging.getInstance().subscribeToTopic("DQmvYVIQC8R4rTGFWesxCSPPhND2");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        /* There are two types of messages data messages and notification messages. Data messages are handled here in onMessageReceived whether the app is in the foreground or background. Data messages are the type traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app is in the foreground. When the app is in the background an automatically generated notification is displayed. */

        String uid = null;
        String name = null;
        String action = null;
        String title = null;
        String message = null;

        String notificationTitle = null;
        String notificationBody = null;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("message"));
            //uid, Interests.getInstance().getNickName(), action, title, message)
            uid = remoteMessage.getData().get("uid");
            name = remoteMessage.getData().get("name");
            action = remoteMessage.getData().get("action");
            title = remoteMessage.getData().get("title");
            message = remoteMessage.getData().get("message");
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        sendNotification(uid, name, action,title, message, notificationTitle, notificationBody);
        FirebaseDatabase.getInstance()
                .getReference("/notifications/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(remoteMessage.getData().get("uid")).setValue(null);
    }

    /**
     //     * Create and show a simple notification containing the received FCM message.
     //     */
    private void sendNotification(String uid, String name, String action, String title, String message,
                                  String notificationTitle, String notificationBody) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("uid", uid);
        intent.putExtra("action", action);
        intent.putExtra("name", name);
        intent.putExtra("message", message);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
