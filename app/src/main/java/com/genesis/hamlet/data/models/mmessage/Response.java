
package com.genesis.hamlet.data.models.mmessage;

import java.util.List;

public class Response {

    private List<MMessage> mMessages;
    private MMessage mMessage;
    private String status;

    public Response(String status, MMessage mMessage, List<MMessage> mMessages) {
        this.status = status;
        this.mMessage = mMessage;
        this.mMessages = mMessages;
    }

    public MMessage getMMessage() {
        return mMessage;
    }
    public List<MMessage> getMMessages() {
        return mMessages;
    }

    public String getStat() {
        return status;
    }

}
