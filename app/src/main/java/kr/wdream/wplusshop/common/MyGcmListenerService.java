package kr.wdream.wplusshop.common;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.app.PushWebView;


/**
 * Created by SEO on 2015-12-10.
 */
public class MyGcmListenerService extends GcmListenerService {

    public Intent intent = null;
    public String DBName = "WPLUSSHOP.db";
    public String tableName = "USER_SETTING";
    public String txtDomain = "";
    public String messageYN = "Y";
    public DBManager dbManager;

    String title        = "";
    String message      = "";
    String type         = "";
    String bigicon      = "";
    String bigtitle     = "";
    String bigtext      = "";
    String intent_uri   = "";
    String summary      = "";

    private static final String TAG = "MyGcmListenerService";

    /**
     *
     * @param from SenderID 값을 받아온다.
     * @param data Set형태로 GCM으로 받은 데이터 payload이다.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.d(TAG, "data : "+ data);
        title        = data.getString("TITLE");
        message      = data.getString("MESSAGE");
        type         = data.getString("TYPE");
        bigicon      = data.getString("BIGICON");
        bigtitle     = data.getString("BIGTITLE");
        bigtext      = data.getString("BIGTEXT");
        intent_uri   = data.getString("INTENT_URI");
        summary      = data.getString("SUMMARY");

        dbManager = new DBManager(getApplicationContext(), DBName, null, 4);
        String[] selectParams = {"MSG_YN", "LOCATION_DOMAIN"};    // select columns
        HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
        HashMap<String, String> userSetRst = null;
        try {
            userSetRst = dbManager.select(tableName, selectParams, whereParams);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (userSetRst.size() > 0) {
            messageYN = userSetRst.get("MSG_YN");
            txtDomain = userSetRst.get("LOCATION_DOMAIN");
        }

        if(messageYN.equals("Y")) {
            // GCM으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.
            sendNotification(title, message, bigicon, type, bigtitle, intent_uri, summary, bigtext);
        }
    }


    /**
     * 실제 디바에스에 GCM으로부터 받은 메세지를 알려주는 함수이다. 디바이스 Notification Center에 나타난다.
     * @param title
     * @param message
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(String title, String message, String bigicon, String type, String bigtitle, String intent_uri, String summary, String bigtext) {

        Bitmap remote_picture = null;
        if(!bigicon.equals("")) {
            try {
                remote_picture = BitmapFactory.decodeStream(
                        (InputStream) new URL(bigicon).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(intent_uri.contains("http://") || intent_uri.contains("https://")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intent_uri));
        } else {
            String targetURL = "";

            if (intent_uri.contains("http://")) {
                targetURL = intent_uri;
            } else {
                if (txtDomain.equals(""))
                    targetURL = "http://www.wpoint.co.kr" + intent_uri;
                else
                    targetURL = "http://" + txtDomain + intent_uri;
            }

            intent = new Intent(this, PushWebView.class);
            intent.putExtra("LOCATION_URL", targetURL);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUMMARY", summary);
            intent.putExtra("PICTURE", bigicon);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (remote_picture != null) {
                notification = new Notification.BigPictureStyle(
                        new Notification.Builder(getApplicationContext())
                                .setContentTitle(title)
                                .setContentText(message)
                                .setSmallIcon(R.drawable.icon_push)
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setColor(Color.parseColor("#ff123456"))
                                .setLargeIcon(remote_picture))
                        .bigPicture(remote_picture)
                        .setSummaryText(summary)
                        .build();
            } else if (!bigtext.equals("")) {
                notification = new Notification.BigTextStyle(
                        new Notification.Builder(getApplicationContext())
                                .setContentTitle(title)
                                .setContentText(message)
                                .setSound(defaultSoundUri)
                                .setSmallIcon(R.drawable.icon_push)
                                .setAutoCancel(true)
                                .setColor(Color.parseColor("#ff123456"))
                                .setContentIntent(pendingIntent))
                        .setSummaryText(summary)
                        .bigText(bigtext)
                        .build();
            } else {
                notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSound(defaultSoundUri)
                        .setSmallIcon(R.drawable.icon_push)
                        .setAutoCancel(true)
                        .setColor(Color.parseColor("#ff123456"))
                        .setContentIntent(pendingIntent)
                        .build();
            }
        }
        else {
            if (remote_picture != null) {
                notification = new Notification.BigPictureStyle(
                        new Notification.Builder(getApplicationContext())
                                .setContentTitle(title)
                                .setContentText(message)
                                .setSmallIcon(R.drawable.icon)
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setLargeIcon(remote_picture))
                        .bigPicture(remote_picture)
                        .setSummaryText(summary)
                        .build();
            } else if (!bigtext.equals("")) {
                notification = new Notification.BigTextStyle(
                        new Notification.Builder(getApplicationContext())
                                .setContentTitle(title)
                                .setContentText(message)
                                .setSound(defaultSoundUri)
                                .setSmallIcon(R.drawable.icon)
                                .setAutoCancel(true)
                                .setContentIntent(pendingIntent))
                        .setSummaryText(summary)
                        .bigText(bigtext)
                        .build();
            } else {
                notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSound(defaultSoundUri)
                        .setSmallIcon(R.drawable.icon)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .build();
            }
        }
        manager.notify(0, notification);


//

//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

//        notification = new Notification.BigPictureStyle(
//                new Notification.Builder(getApplicationContext())
//                        .setContentTitle(title)
//                        .setContentText(message)
//                        .setSmallIcon(R.drawable.icon)
//                        .setLargeIcon(remote_picture)
//                        .setTicker("BigPicture!"))
//                .bigPicture(remote_picture)
//                .setBigContentTitle("BigNotification")
//                .setSummaryText("BigNotification summary")
//                .build();

//                NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

//    private int getNotificationIcon() {
//        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
//        return useWhiteIcon ? R.drawable.icon_silhouette : R.drawable.icon;
//    }

}
