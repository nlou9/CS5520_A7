package edu.neu.madcourse.cs5520_a7.stickerService.models;

import edu.neu.madcourse.cs5520_a7.R;

public class Sticker {

    public String stickerId;
    public String imageName;
    public int drawableId;

    public Sticker() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    // usage example:
    // Sticker s = new Sticker("00", "heart.png", R.drawable.heart);
    public Sticker(String stickerId, String imageName, int drawableId) {
        this.stickerId = stickerId;
        this.imageName = imageName;
        this.drawableId = drawableId;
    }

}