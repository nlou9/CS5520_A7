package edu.neu.madcourse.cs5520_a7.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import com.google.firebase.database.DatabaseReference;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.stickerService.ReceiveHistoryActivity;
import edu.neu.madcourse.cs5520_a7.stickerService.SendHistoryActivity;
import edu.neu.madcourse.cs5520_a7.stickerService.SendStickerActivity;

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

  public void goToSendHistory(View view) {
    Intent intent = new Intent(getBaseContext(), SendHistoryActivity.class);
    intent.putExtra("login_username", loginUsername);
    startActivity(intent);
  }

  public void goToHistory(View view) {
    Intent intent = new Intent(getBaseContext(), ReceiveHistoryActivity.class);
    intent.putExtra("login_username", loginUsername);
    startActivity(intent);
  }
}
