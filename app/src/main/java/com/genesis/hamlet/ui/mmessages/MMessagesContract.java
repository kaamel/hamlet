package com.genesis.hamlet.ui.mmessages;

import android.net.Uri;

import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.mvp.IBasePresenter;
import com.genesis.hamlet.util.mvp.IBaseView;

/**
 * Created by dipenrana on 10/24/17.
 */

public class MMessagesContract {

    interface View extends IBaseView {
    }

    interface Presenter extends IBasePresenter<View> {
        void connectChatroom(DataSource.OnMMessagesCallback onMMessageCallback, String chatroom, int page);
        void sendMMessage(MMessage message, User user, String chatRoom);
        void sendImages(Uri mmessage, User user, String chatRoom);

    }
}
