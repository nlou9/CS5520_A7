package edu.neu.madcourse.cs5520_a7.stickerService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

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

  private static final String TAG = SendStickerActivity.class.getSimpleName();
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
  private void sendMessageToDevice(String sender, String stickerId, String targetToken) {
    String key = "key=";
    // Prepare data
    JSONObject jPayload = new JSONObject();
    JSONObject jNotification = new JSONObject();
    JSONObject jdata = new JSONObject();
    try {
      jNotification.put("title", String.format("%s sends you a new message", sender));
      jNotification.put("body", stickerId);

      jdata.put("title",  String.format("%s sends you a new message", sender));
      jdata.put("content", stickerId);
      
      // If sending to a single client
      jPayload.put("to", targetToken);
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
        Log.i(TAG, String.format("FCM Server response: %s", resp));
        Utils.postToastMessage("Status from Server: " + resp, getApplicationContext());
      }
    });
    t.start();
  }


  public void getHistoryOfSentStickers(String userName) {
    mDatabase.child(EVENT_TABLE).orderByChild(EVENT_SENDER).equalTo(userName).addValueEventListener(
      new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          List<Event> eventHistory = new ArrayList<>();
          System.out.println(snapshot.hasChildren());
          if (snapshot.hasChildren()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
              Event event = dataSnapshot.getValue(Event.class);
              eventHistory.add(event);
            }
            updateStatisticsView(eventHistory);
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
          // ...
        }
      });

  }

  private void updateStatisticsView(List<Event> eventHistory) {
    Map<String, Integer> countByStickerId = new HashMap<>();
    for (Event event : eventHistory) {
      countByStickerId.put(event.stickerId, countByStickerId.getOrDefault(event.stickerId, 0) + 1);
    }
    // TODO: show the statistics map on UI
  }
}