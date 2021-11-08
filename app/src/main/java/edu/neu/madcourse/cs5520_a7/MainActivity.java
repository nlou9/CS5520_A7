package edu.neu.madcourse.cs5520_a7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.neu.madcourse.cs5520_a7.stickerService.models.Event;
import edu.neu.madcourse.cs5520_a7.stickerService.models.User;
import edu.neu.madcourse.cs5520_a7.utils.Utils;

public class MainActivity extends AppCompatActivity {

  private static final String EVENT_TABLE = "Events";
  private static final String EVENT_RECEIVER = "receiver";
  private static final String USER_TABLE = "Users";
  private DatabaseReference mDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Connect with firebase
    //
    mDatabase = FirebaseDatabase.getInstance().getReference();


    Event event =
      new Event("1", UUID.randomUUID().toString(), "test1", "test2", Instant.now().toEpochMilli(),
        true);
    mDatabase.child(EVENT_TABLE).child(event.eventId).setValue(event);

//    mDatabase.child("Users").orderByChild("username").addValueEventListener(
//      new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot snapshot) {
//          System.out.println(snapshot.hasChildren());
//          if (snapshot.hasChildren()) {
//            Iterator<DataSnapshot> snapshotIterator = snapshot.getChildren().iterator();
//            while (snapshotIterator.hasNext()) {
//              DataSnapshot dataSnapshot = snapshotIterator.next();
//              User user = dataSnapshot.getValue(User.class);
//              System.out.println("user name is: " + user.username);
//            }
//          }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//          // ...
//        }
//      });
    getHistoryOfReceivedStickers("test2");
    getHistoryOfSentStickers("test1");


    FirebaseMessaging.getInstance().getToken()
      .addOnCompleteListener(new OnCompleteListener<String>() {
        @Override
        public void onComplete(@NonNull Task<String> task) {
          if (!task.isSuccessful()) {
            Log.w("Fetching FCM registration token failed", task.getException());
            return;
          }

          // Get new FCM registration token
          String token = task.getResult();
          System.out.println("Fetched token is: " + token);
          User user = new User("test1", token);
          mDatabase.child("Users").child(user.username).setValue(user);
          sendSticker("test2", "test1", "1");


        }
      });



  }

  public List<Event> getHistoryOfReceivedStickers(String userName) {
    List<Event> eventHistory = new ArrayList<>();
    mDatabase.child(EVENT_TABLE).orderByChild(EVENT_RECEIVER).equalTo(
      userName).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        System.out.println(snapshot.hasChildren());
        if (snapshot.hasChildren()) {
          Iterator<DataSnapshot> snapshotIterator = snapshot.getChildren().iterator();
          while (snapshotIterator.hasNext()) {
            DataSnapshot dataSnapshot = snapshotIterator.next();
            Event event = dataSnapshot.getValue(Event.class);
            System.out.println("Event receiver is: " + event.receiver);
            eventHistory.add(event);
          }
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        // ...
      }
    });
    eventHistory.sort((o1, o2) -> Long.compare(o2.timestampInMillis, o1.timestampInMillis));
    return eventHistory;
  }

  public void getHistoryOfSentStickers(String userName) {

    mDatabase.child(EVENT_TABLE).orderByChild("sender").equalTo(userName).addValueEventListener(
      new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          List<Event> eventHistory = new ArrayList<>();
          System.out.println(snapshot.hasChildren());
          if (snapshot.hasChildren()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
              Event event = dataSnapshot.getValue(Event.class);
              System.out.println("Event sender is: " + event.sender);
              eventHistory.add(event);
            }
            updateStatisticsView(eventHistory);
          }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
          // ...
        }
      });

  }

  private void updateStatisticsView(List<Event> eventHistory) {
    System.out.println("Event history size is : " + eventHistory.size());
    Map<String, Integer> countByStickerId = new HashMap<>();
    for (Event event : eventHistory) {
      System.out.println("Event id: " + event.eventId + ", sticker id: " + event.stickerId);
      countByStickerId.put(event.stickerId, countByStickerId.getOrDefault(event.stickerId, 0) + 1);
    }
    System.out.println("Map size is : " + countByStickerId.size());
  }

  public void sendSticker(String senderUserName, String receiverUserName, String stickerId) {

    mDatabase.child(USER_TABLE).child(receiverUserName).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
//        System.out.println(snapshot.hasChildren());
//        if (snapshot.hasChildren()) {
          User receiver =  snapshot.getValue(User.class);
          System.out.println("Receiver username: " + receiver.username + ", token" + receiver.fcmToken);
          String eventId = "2";
          Event event =
            new Event(eventId, stickerId, senderUserName, receiverUserName, Instant.now().toEpochMilli(),
              true);

          mDatabase.child(EVENT_TABLE).child(eventId).setValue(event);
          // TODO(add FCM)
          sendMessageToDevice(receiver.fcmToken);
//        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        // ...
      }
    });

  }

  /**
   * Pushes a notification to a given device-- in particular, this device,
   * because that's what the instanceID token is defined to be.
   */
  private void sendMessageToDevice(String targetToken) {
    System.out.println("Target token: " + targetToken);
    String key = "key=";
    // Prepare data
    JSONObject jPayload = new JSONObject();
    JSONObject jNotification = new JSONObject();
    JSONObject jdata = new JSONObject();
    try {
      jNotification.put("title", "Message Title from 'SEND MESSAGE TO CLIENT BUTTON'");
      jNotification.put("body", "Message body from 'SEND MESSAGE TO CLIENT BUTTON'");
      jNotification.put("sound", "default");
      jNotification.put("badge", "1");
            /*
            // We can add more details into the notification if we want.
            // We happen to be ignoring them for this demo.
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            */
      jdata.put("title", "data title from 'SEND MESSAGE TO CLIENT BUTTON'");
      jdata.put("content", "data content from 'SEND MESSAGE TO CLIENT BUTTON'");

      /***
       * The Notification object is now populated.
       * Next, build the Payload that we send to the server.
       */

      // If sending to a single client
      jPayload.put("to", targetToken); // CLIENT_REGISTRATION_TOKEN);

            /*
            // If sending to multiple clients (must be more than 1 and less than 1000)
            JSONArray ja = new JSONArray();
            ja.put(CLIENT_REGISTRATION_TOKEN);
            // Add Other client tokens
            ja.put(FirebaseInstanceId.getInstance().getToken());
            jPayload.put("registration_ids", ja);
            */

      jPayload.put("priority", "high");
      jPayload.put("notification", jNotification);
      jPayload.put("data", jdata);

    } catch (JSONException e) {
      e.printStackTrace();
    }
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        final String resp = Utils.fcmHttpConnection(key, jPayload);
        System.out.println("FCM response: " + resp);
        Utils.postToastMessage("Status from Server: " + resp, getApplicationContext());
      }
    });
    t.start();
  }
}
