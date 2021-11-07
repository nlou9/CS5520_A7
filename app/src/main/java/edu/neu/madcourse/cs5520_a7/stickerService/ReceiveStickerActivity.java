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
import java.util.Iterator;
import java.util.List;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Event;

public class ReceiveStickerActivity extends AppCompatActivity {

  private static final String EVENT_TABLE = "Events";
  private static final String EVENT_RECEIVER = "receiver";

  private DatabaseReference mDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_receive_sticker);

    // Connects firebase
    mDatabase = FirebaseDatabase.getInstance().getReference();
  }

  public void getHistoryOfReceivedStickers(String userName) {

    mDatabase.child(EVENT_TABLE).orderByChild(EVENT_RECEIVER).equalTo(
      userName).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        System.out.println(snapshot.hasChildren());
        List<Event> eventHistory = new ArrayList<>();
        if (snapshot.hasChildren()) {
          for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            Event event = dataSnapshot.getValue(Event.class);
            System.out.println("Event receiver is: " + event.receiver);
            eventHistory.add(event);
          }
        }
        updateHistoryView(eventHistory);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        // ...
      }
    });
  }

  private void updateHistoryView(List<Event> events) {
    events.sort((o1, o2) -> Long.compare(o2.timestampInMillis, o1.timestampInMillis));

  }
}