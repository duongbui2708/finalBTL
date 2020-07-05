package com.example.baitaplon.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.baitaplon.MessageActivity;
import com.example.baitaplon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    String title,message,icon,user;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sented= remoteMessage.getData().get("sented");
        String user= remoteMessage.getData().get("user");
        SharedPreferences preferences=getSharedPreferences("PREFS", MODE_PRIVATE);
        String currentUser= preferences.getString("currentUser","none");
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(!currentUser.equals(user))
        if(firebaseUser!=null && sented.equals(firebaseUser.getUid())){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                sendOreoNotification(remoteMessage);
            }else {
                sendNotification(remoteMessage);
            }
        }

    }

    private void sendOreoNotification(RemoteMessage remoteMessage) {
        user= remoteMessage.getData().get("user");
        //icon = remoteMessage.getData().get("icon");
        title=remoteMessage.getData().get("title");
        message=remoteMessage.getData().get("body");
        RemoteMessage.Notification notification= remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent= new Intent(this, MessageActivity.class);
        Bundle bundle= new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,j,intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        OreoNotification oreoNotification=new OreoNotification(this);
        Notification.Builder builder= oreoNotification.getOreoNotification(title,message,pendingIntent,defaultSound);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int i=0;
        if(j>0){
            i=j;
        }
        oreoNotification.getManager().notify(i, builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        user= remoteMessage.getData().get("user");
        //icon = remoteMessage.getData().get("icon");
        title=remoteMessage.getData().get("title");
        message=remoteMessage.getData().get("body");
        RemoteMessage.Notification notification= remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent= new Intent(this, MessageActivity.class);
        Bundle bundle= new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,j,intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSound(defaultSound)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int i=0;
        if(j>0){
            i=j;
        }
        manager.notify(i, builder.build());
    }
}
