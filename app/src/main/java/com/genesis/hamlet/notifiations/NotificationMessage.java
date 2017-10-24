package com.genesis.hamlet.notifiations;

/**
 * Created by kaamel on 10/23/17.
 */

public class NotificationMessage {
    private String action;

    public String getName() {
        return name;
    }

    private String name;
    private String title;
    private String message;

    public String getSenderUid() {
        return senderUid;
    }

    private String senderUid;

    public String getReceiverUid() {
        return receiverUid;
    }

    public String getChatRoom() {
        return chatRoom;
    }

    private String receiverUid;
    private String chatRoom;

    public NotificationMessage(){}

    public NotificationMessage(
            String senderUid,
            String receiverUid,
            String chatRoom,
            String name,
            String action,
            String title,
            String message) {

        this.action = action;
        this.title = title;
        this.message = message;
        this.name = name;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.chatRoom = chatRoom;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }


    public String getAction() {
        return action;
    }
}
