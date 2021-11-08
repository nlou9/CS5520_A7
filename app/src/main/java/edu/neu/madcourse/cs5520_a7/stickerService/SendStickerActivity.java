package edu.neu.madcourse.cs5520_a7.stickerService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Event;
import edu.neu.madcourse.cs5520_a7.stickerService.models.User;
import edu.neu.madcourse.cs5520_a7.utils.Utils;

public class SendStickerActivity extends AppCompatActivity {

  private DatabaseReference mDatabase;
  private static final String EVENT_TABLE = "Events";
  private static final String EVENT_SENDER = "sender";
  private static final String USER_TABLE = "Users";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_send_sticker);

    // Connects firebase
    mDatabase = FirebaseDatabase.getInstance().getReference();
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

      jPayload.put("priority", "high");
      jPayload.put("notification", jNotification);
      jPayload.put("data", jdata);

    } catch (JSONException e) {
      e.printStackTrace();
    }
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        final String resp = edu.neu.madcourse.cs5520_a7.utils.Utils.fcmHttpConnection(key, jPayload);
        System.out.println("FCM response: " + resp);
        Utils.postToastMessage("Status from Server: " + resp, getApplicationContext());
      }
    });
    t.start();
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
}