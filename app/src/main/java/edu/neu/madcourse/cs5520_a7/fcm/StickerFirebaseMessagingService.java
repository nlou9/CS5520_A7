package edu.neu.madcourse.cs5520_a7.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import edu.neu.madcourse.cs5520_a7.stickerService.models.Event;
import edu.neu.madcourse.cs5520_a7.stickerService.models.User;
import edu.neu.madcourse.cs5520_a7.utils.Utils;

public class StickerFirebaseMessagingService extends FirebaseMessagingService {
  private static final String TAG = StickerFirebaseMessagingService.class.getSimpleName();
  private static final String EVENT_TABLE = "Events";
  private DatabaseReference mDatabase;
  private static final String CHANNEL_ID = "STICKER_CHANNEL_ID";
  private static final String CHANNEL_NAME = "STICKER_CHANNEL_NAME";
  private static final String CHANNEL_DESCRIPTION = "STICKER_CHANNEL_DESCRIPTION";

  @Override
  public void onCreate() {
    super.onCreate();
    mDatabase = FirebaseDatabase.getInstance().getReference();
  }

  @Override
  public void onNewToken(String newToken) {
    //super.onNewToken(newToken);

    Log.d(TAG, "Refreshed token: " + newToken);

    // If you want to send messages to this application instance or
    // manage this apps subscriptions on the server side, send the
    // Instance ID token to your app server.
    // sendRegistrationToServer(newToken);
  }


  /**
   * Called when message is received.
   * Mainly what you need to implement
   *
   * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
   */
  @Override
  public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

    Log.i(TAG, "msgId:" + remoteMessage.getMessageId());
    Log.i(TAG, "senderId:" + remoteMessage.getSenderId());
    Log.i(TAG, "messageData:" + remoteMessage.getData());

    myClassifier(remoteMessage);

  }

  private void myClassifier(RemoteMessage remoteMessage) {

    if (remoteMessage.getData().size() > 0) {
      // TODO: show notification on UI
      String stickerId = remoteMessage.getData().get("stickerId");
      String eventId = remoteMessage.getData().get("eventId");
      if (eventId != null) {
        // Update the notifyStatus as true when receiving the event.
        mDatabase.child(EVENT_TABLE).child(eventId).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            Event event = snapshot.getValue(Event.class);
            if (event == null) {
              return;
            }
            event.notifyStatus = true;
            mDatabase.child(EVENT_TABLE).child(eventId).setValue(event);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
        });
      }
      Utils.postToastMessage(remoteMessage.getData().get("title"), getApplicationContext());
    }
  }


//  /**
//   * Create and show a simple notification containing the received FCM message.
//   *
//   * @param remoteMessage FCM message  received.
//   */
//  @Deprecated
//  private void showNotification(RemoteMessage remoteMessage) {
//
//    Intent intent = new Intent(this, MainActivity.class);
//    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//      PendingIntent.FLAG_ONE_SHOT);
//
//    Notification notification;
//    NotificationCompat.Builder builder;
//    NotificationManager notificationManager = getSystemService(NotificationManager.class);
//
//    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//      NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
//      // Configure the notification channel
//      notificationChannel.setDescription(CHANNEL_DESCRIPTION);
//      notificationManager.createNotificationChannel(notificationChannel);
//      builder = new NotificationCompat.Builder(this, CHANNEL_ID);
//
//    } else {
//      builder = new NotificationCompat.Builder(this);
//    }
//
//
//    notification = builder.setContentTitle(remoteMessage.getNotification().getTitle())
//      .setContentText(remoteMessage.getNotification().getBody())
//      .setSmallIcon(R.mipmap.ic_launcher)
//      .setAutoCancel(true)
//      .setContentIntent(pendingIntent)
//      .build();
//    notificationManager.notify(0, notification);
//
//  }
//
//  /**
//   * Create and show a simple notification containing the received FCM message.
//   *
//   * @param remoteMessageNotification FCM message  received.
//   */
//  private void showNotification(RemoteMessage.Notification remoteMessageNotification) {
//
//    Intent intent = new Intent(this, MainActivity.class);
//    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//      PendingIntent.FLAG_ONE_SHOT);
//
//    Notification notification;
//    NotificationCompat.Builder builder;
//    NotificationManager notificationManager = getSystemService(NotificationManager.class);
//
//    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//      NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
//      // Configure the notification channel
//      notificationChannel.setDescription(CHANNEL_DESCRIPTION);
//      notificationManager.createNotificationChannel(notificationChannel);
//      builder = new NotificationCompat.Builder(this, CHANNEL_ID);
//
//    } else {
//      builder = new NotificationCompat.Builder(this);
//    }
//
//
//    notification = builder.setContentTitle(remoteMessageNotification.getTitle())
//      .setContentText(remoteMessageNotification.getBody())
//      .setSmallIcon(R.mipmap.ic_launcher)
//      .setAutoCancel(true)
//      .setContentIntent(pendingIntent)
//      .build();
//    notificationManager.notify(0, notification);
//
//  }

}