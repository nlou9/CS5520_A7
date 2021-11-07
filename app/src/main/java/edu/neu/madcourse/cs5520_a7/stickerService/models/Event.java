package edu.neu.madcourse.cs5520_a7.stickerService.models;

public class Event {

    public String eventId;
    public String stickerId;
    public String sender;
    public String receiver;
    public String timestamp;
    public Boolean status;


    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public Event(String eventId, String stickerId, String sender, String receiver, String timestamp, Boolean status) {
        this.eventId = eventId;
        this.stickerId = stickerId;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.status = status;
    }
}

