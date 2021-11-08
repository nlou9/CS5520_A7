package edu.neu.madcourse.cs5520_a7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.cs5520_a7.stickerService.HistoryActivity;
import edu.neu.madcourse.cs5520_a7.stickerService.SendStickerActivity;
import edu.neu.madcourse.cs5520_a7.user.LoginActivity;

public class MainActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button btnLogin = findViewById(R.id.login_main);


    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
      }
    });

  }

  public void goToLogin(View view) {
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
  }
  public void goToSendSticker(View view) {
    Intent intent = new Intent(this, SendStickerActivity.class);
    startActivity(intent);
  }
  public void goToHistory(View view) {
    Intent intent = new Intent(this, HistoryActivity.class);
    startActivity(intent);
  }
}
