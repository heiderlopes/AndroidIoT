package br.com.heiderlopes.androidfirebasecloudmessage.services;

/**
 * Created by heider on 12/07/16.
 */

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import br.com.heiderlopes.androidfirebasecloudmessage.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static final int ID_NOTIFICACAO = 1000;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setSound(defaultSoundUri)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText(remoteMessage.getNotification().getBody());

        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(ID_NOTIFICACAO, mBuilder.build());
    }
}