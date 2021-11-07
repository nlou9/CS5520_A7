package edu.neu.madcourse.cs5520_a7.stickerService.models;

public class Sticker {

    public String stickerId;
    public Byte image;

    public Sticker() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Sticker(String stickerId, Byte image) {
        this.stickerId = stickerId;
        this.image = image;
    }

}