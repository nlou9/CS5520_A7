package edu.neu.madcourse.cs5520_a7.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import edu.neu.madcourse.cs5520_a7.stickerService.models.User;

public class LoginActivity extends AppCompatActivity {

  private static final String TAG = LoginActivity.class.getSimpleName();
  private static final String USER_TABLE = "Users";
  private String deviceFcmToken = "";
  private DatabaseReference mDatabase;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(
      new OnCompleteListener<String>() {
        @Override
        public void onComplete(@NonNull Task<String> task) {
          if (!task.isSuccessful()) {
            Log.w("Fetching FCM registration token failed", task.getException());
            return;
          }
          // Get new FCM registration token
          deviceFcmToken = task.getResult();
          Log.i(TAG, "Fetched token is: " + deviceFcmToken);
        }
      });

    EditText etName = findViewById(R.id.et);
    Button btnRegister = findViewById(R.id.btnRegister);
    btnRegister.setOnClickListener(view -> {
      String name = etName.getText().toString();
      if (name.isEmpty()) {
        Toast.makeText(LoginActivity.this, "Please enter a name!", Toast.LENGTH_SHORT).show();
      } else {
        login(name);
      }
    });

  }

  private void login(String username) {
    User user = new User(username, deviceFcmToken);
    Task<Void> loginTask = mDatabase.child(USER_TABLE).child(user.username).setValue(user);
    loginTask.addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        Log.i(TAG,
          String.format("Login user %s, success: %s ", user.username, loginTask.isSuccessful()));
        onLoginSuccess();
      }
    });
  }

  private void onLoginSuccess() {
    Toast.makeText(LoginActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
  }

  public void loginAndGoBackToMain(View view) {
    this.finish();
  }
}
