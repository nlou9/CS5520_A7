package edu.neu.madcourse.cs5520_a7.stickerService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Event;

public class SendStickerActivity extends AppCompatActivity {

  private DatabaseReference mDatabase;
  private static final String EVENT_TABLE = "Events";
  private static final String EVENT_SENDER = "sender";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_send_sticker);

    // Connects firebase
    mDatabase = FirebaseDatabase.getInstance().getReference();
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
      countByStickerId.put(event.stickerId,
        countByStickerId.getOrDefault(event.stickerId, 0) + 1);
    }
    System.out.println("Map size is : " + countByStickerId.size());
  }
}