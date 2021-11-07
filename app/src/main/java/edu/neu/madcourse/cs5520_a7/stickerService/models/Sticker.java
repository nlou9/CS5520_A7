package edu.neu.madcourse.cs5520_a7.stickerService.models;

import edu.neu.madcourse.cs5520_a7.R;

public class Sticker {

    public String stickerId;
    public String imageName;

    public Sticker() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Sticker(String stickerId, String imageName) {
        this.stickerId = stickerId;
        this.imageName = imageName;
    }

}