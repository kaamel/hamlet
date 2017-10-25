package com.genesis.hamlet.ui.mmessages;

import android.content.Context;

import com.genesis.hamlet.data.models.mmessage.ChatMessage;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.mvp.IBasePresenter;
import com.genesis.hamlet.util.mvp.IBaseView;

import java.util.List;

/**
 * Created by dipenrana on 10/24/17.
 */

public class MMessagesContract {

    interface View extends IBaseView {
        void onMessageReceived(List<ChatMessage> messages,User user, String ChatRoom);
    }

    interface Presenter extends IBasePresenter<View> {
        boolean isConnected();
        void connect(Context context);
        void sendMessage(ChatMessage message);

    }
}
