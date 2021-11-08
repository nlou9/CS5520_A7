package edu.neu.madcourse.cs5520_a7.user;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.cs5520_a7.stickerService.models.User;

public class DAOUser {
    private DatabaseReference mDatabase;

    public DAOUser() {
        mDatabase = FirebaseDatabase.getInstance().getReference(User.class.getSimpleName());
    }

    public Task<Void> add(User user) {
        return mDatabase.push().setValue(user);
    }
}
