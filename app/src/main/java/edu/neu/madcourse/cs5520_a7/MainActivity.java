package edu.neu.madcourse.cs5520_a7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

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

public class MainActivity extends AppCompatActivity {

  private static final String EVENT_TABLE = "Events";
  private static final String EVENT_RECEIVER = "receiver";
  private DatabaseReference mDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Connect with firebase
    //
    mDatabase = FirebaseDatabase.getInstance().getReference();
    User user = new User("test1");
    mDatabase.child("Users").child(user.username).setValue(user);

    Event event =
      new Event("1", UUID.randomUUID().toString(), "test1", "test2", Instant.now().toEpochMilli(),
        true);
    mDatabase.child(EVENT_TABLE).child(event.eventId).setValue(event);

    mDatabase.child("Users").orderByChild("username").addValueEventListener(
      new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
          System.out.println(snapshot.hasChildren());
          if (snapshot.hasChildren()) {
            Iterator<DataSnapshot> snapshotIterator = snapshot.getChildren().iterator();
            while (snapshotIterator.hasNext()) {
              DataSnapshot dataSnapshot = snapshotIterator.next();
              User user = dataSnapshot.getValue(User.class);
              System.out.println("user name is: " + user.username);
            }
          }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
          // ...
        }
      });
    getHistoryOfReceivedStickers("test2");
    getHistoryOfSentStickers("test1");
    //        for (String stickerId : map.keySet()) {
    //            System.out.println("StickerId: " + stickerId + ", count: " + map.get(stickerId));
    //        }
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
}