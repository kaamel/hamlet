
package com.genesis.hamlet.data.models.mmessage;

import com.genesis.hamlet.data.models.user.User;

import java.util.ArrayList;
import java.util.List;

public class MMessages {

    private List<MMessage> mMessages = null;
    private String mMessagesId;
    private User participant;
    private long createdTime;

    public MMessages(String id, User participant) {
        createdTime = System.currentTimeMillis();
        mMessagesId = id;
        this.participant = participant;
        mMessages = new ArrayList<>();
    }

    public String getMMessagesId() {
        return mMessagesId;
    }
    public List<MMessage> getMMessages() {
        return mMessages;
    }

    public void setMMessages(List<MMessage> mMessages) {
        this.mMessages = mMessages;
    }
    public MMessage getMMessage(int position) {
        return mMessages.get(position);
    }

    public void addMMessage(MMessage mMessage) {
        mMessages.add(mMessage);
    }

    public void deleteMMessage(int position) {
        mMessages.remove(position);
    }

    public void replaceMMessage(MMessage oldMessage, MMessage newMMessage) {
        synchronized (mMessages) {
            int position = mMessages.indexOf(oldMessage);
            if (position > -1)
                mMessages.set(position, newMMessage);
        }
    }

    public void replaceMMessage(String mMessagesId, MMessage newMMessage) {
        synchronized (mMessages) {
            MMessage mMessage = findMMessageById(mMessagesId);
            if (mMessage != null)
                replaceMMessage(mMessage, newMMessage);
        }
    }

    public MMessage findMMessageById(String id) {
        for (MMessage mMessage: mMessages) {
            if (mMessage.getId().equals(id))
                return mMessage;
        }
        return null;
    }

    public User getParticipant() {
        return participant;
    }
}
