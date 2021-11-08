package edu.neu.madcourse.cs5520_a7.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.stickerService.HistoryActivity;
import edu.neu.madcourse.cs5520_a7.stickerService.SendStickerActivity;
import edu.neu.madcourse.cs5520_a7.stickerService.models.User;

public class UserActivity extends AppCompatActivity {

  private static final String TAG = UserActivity.class.getSimpleName();
  private String loginUsername = "";
  private DatabaseReference mDatabase;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user);
    loginUsername = getIntent().getStringExtra("login_username");
    System.out.println("Login user name: " + loginUsername);
  }

  public void goToSendSticker(View view) {
    Intent intent = new Intent(getBaseContext(), SendStickerActivity.class);
    intent.putExtra("login_username", loginUsername);
    startActivity(intent);
  }

  public void goToHistory(View view) {
    Intent intent = new Intent(getBaseContext(), HistoryActivity.class);
    intent.putExtra("login_username", loginUsername);
    startActivity(intent);
  }
}
