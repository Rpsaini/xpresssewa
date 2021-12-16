package transfer.money.com.xpresssewa.fcm;


import android.app.LauncherActivity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;


import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.util.UtilClass;


public class MessagingService extends FirebaseMessagingService {

    SaveImpPrefrences savePreferences = new SaveImpPrefrences();

    @Override
    public void onNewToken(String token) {
        savePreferences.savePrefrencesData(getApplicationContext(), token, "device_token");

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("notification data===" + remoteMessage.getData());
        int count = Integer.parseInt(savePreferences.reterivePrefrence(getApplicationContext(), DefaultConstatnts.notification_count).toString());
        savePreferences.savePrefrencesData(getApplicationContext(), (count + 1), DefaultConstatnts.notification_count);
        Intent intent = new Intent(UtilClass.notificationBroadCast);
        sendBroadcast(intent);
        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData());
                showNotification(jsonObject.getString("title"), jsonObject.getString("text"));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }


    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, LauncherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);


        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/");
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "xsewa";// The id of the channel.
            CharSequence name = "expresssewa";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
            mChannel.setSound(defaultSoundUri, attributes); // This is IMPORTANT

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Notification notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(this.getResources().getString(R.string.app_name)).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(pendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setSound(defaultSoundUri)
                    .setSmallIcon(R.mipmap.ic_launcher)

                    .setContentText(message).build();

            nm.createNotificationChannel(mChannel);
            nm.notify(401, notification);
        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this);
            Notification notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(getResources().getString(R.string.app_name)).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setSound(defaultSoundUri)
                    .setContentText(message).build();

            nm.notify(401, notification);
        }
    }


}
