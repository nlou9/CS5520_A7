package edu.neu.madcourse.cs5520_a7.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import edu.neu.madcourse.cs5520_a7.MainActivity;
import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.stickerService.ReceiveHistoryActivity;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Event;

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
    System.out.println("StickerFirebaseMessagingService on create");
  }

  @Override
  public void onNewToken(String newToken) {
    super.onNewToken(newToken);

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
    super.onMessageReceived(remoteMessage);

    Log.i(TAG, "msgId:" + remoteMessage.getMessageId());
    Log.i(TAG, "senderId:" + remoteMessage.getSenderId());
    Log.i(TAG, "messageData:" + remoteMessage.getData());

    myClassifier(remoteMessage);

  }

  private void myClassifier(RemoteMessage remoteMessage) {

    if (remoteMessage.getData().size() > 0) {
      String stickerId = remoteMessage.getData().get("stickerId");
      String title = remoteMessage.getData().get("title");
      String receiver = remoteMessage.getData().get("receiver");
      showNotification(title, stickerId, receiver);
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
    }
  }

  /**
   * Create and show a simple notification containing the received FCM message.
   */
  private void showNotification(String title, String stickerId, String receiver) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = CHANNEL_NAME;
      String description = CHANNEL_DESCRIPTION;
      int importance = NotificationManager.IMPORTANCE_HIGH;
      NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
      channel.setDescription(description);
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }

    Intent intent = new Intent(getApplicationContext(), ReceiveHistoryActivity.class);
    intent.putExtra("login_username", receiver);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
      PendingIntent.FLAG_UPDATE_CURRENT);


    NotificationCompat.Builder builder =
      new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID).setSmallIcon(
        Integer.parseInt(stickerId)).setLargeIcon(
        BitmapFactory.decodeResource(getResources(), Integer.parseInt(stickerId))).setContentTitle(
        title).setPriority(NotificationCompat.PRIORITY_MAX)
        // Set the intent that will fire when the user taps the notification
        .setContentIntent(pendingIntent).setAutoCancel(true);

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

    Random random = new Random();
    // notificationId is a unique int for each notification that you must define
    notificationManager.notify(random.nextInt(), builder.build());

  }

}