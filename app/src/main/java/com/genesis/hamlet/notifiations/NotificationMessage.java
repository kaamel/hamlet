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

    public String getUid() {
        return uid;
    }

    private String uid;

    public NotificationMessage(){}

    public NotificationMessage(String uid, String name, String action, String title, String message) {
        this.action = action;
        this.title = title;
        this.message = message;
        this.name = name;
        this.uid = uid;
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
