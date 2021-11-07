package edu.neu.madcourse.cs5520_a7.stickerService.models;
public class User {

    public String username;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username) {
        this.username = username;
    }

}
