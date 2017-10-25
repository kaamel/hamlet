package com.genesis.hamlet.data.models.mmessage;

/**
 * Created by dipenrana on 10/21/17.
 */

public class ChatMessage {

    private String id;
    private String text;
    private String name;
    private String messengerImageUrl;
    private String messageImageUrl;

    public ChatMessage() {
    }

    public ChatMessage(String text, String name, String photoUrl, String imageUrl) {
        this.text = text;
        this.name = name;
        this.messengerImageUrl = photoUrl;
        this.messageImageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessengerImageUrl() {
        return messengerImageUrl;
    }

    public void setMessengerImageUrl(String messengerImageUrl) {
        this.messengerImageUrl = messengerImageUrl;
    }

    public String getMessageImageUrl() {
        return messageImageUrl;
    }

    public void setMessageImageUrl(String messageImageUrl) {
        this.messageImageUrl = messageImageUrl;
    }

}

