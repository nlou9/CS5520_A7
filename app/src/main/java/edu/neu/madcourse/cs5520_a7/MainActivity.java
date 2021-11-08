package edu.neu.madcourse.cs5520_a7;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.cs5520_a7.stickerService.models.User;
import edu.neu.madcourse.cs5520_a7.user.DAOUser;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Connect with firebase
        //
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").setValue(true);

        EditText etName = findViewById(R.id.et);
        Button btnRegister = findViewById(R.id.btnRegister);
        DAOUser dao = new DAOUser();

        btnRegister.setOnClickListener(view -> {
            User user = new User(etName.getText().toString());
            dao.add(user).addOnSuccessListener(success -> {
                Toast.makeText(this, "Register successfully!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(error -> {
                Toast.makeText(this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }
}