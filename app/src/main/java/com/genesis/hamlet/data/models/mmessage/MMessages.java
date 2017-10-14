
package com.genesis.hamlet.data.models.mmessage;

import java.util.List;

public class MMessages {

    private List<MMessage> MMessages = null;
    private long mMessagesId;

    public long getMMessagesId() {
        return mMessagesId;
    }

    public void setmMessagesId(long mMessageId) {
        this.mMessagesId = mMessageId;
    }

    public List<MMessage> getMMessages() {
        return MMessages;
    }

    public void setMMessages(List<MMessage> MMessages) {
        this.MMessages = MMessages;
    }

}
