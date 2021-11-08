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
import java.util.List;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Event;

public class HistoryActivity extends AppCompatActivity {

  private static final String EVENT_TABLE = "Events";
  private static final String EVENT_RECEIVER = "receiver";

  private DatabaseReference mDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_history);

    // Connects firebase
    mDatabase = FirebaseDatabase.getInstance().getReference();
  }

  private void getHistoryOfReceivedStickers(String userName) {

    mDatabase.child(EVENT_TABLE).orderByChild(EVENT_RECEIVER).equalTo(
      userName).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        List<Event> eventHistory = new ArrayList<>();
        if (snapshot.hasChildren()) {
          for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            Event event = dataSnapshot.getValue(Event.class);
            eventHistory.add(event);
          }
        }
        updateHistoryView(eventHistory);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        // ...
      }
    });
  }

  private void updateHistoryView(List<Event> events) {
    // Sort the event by timestamp desc.
    events.sort((o1, o2) -> Long.compare(o2.timestampInMillis, o1.timestampInMillis));
    // TODO: show the list on the UI

  }
}