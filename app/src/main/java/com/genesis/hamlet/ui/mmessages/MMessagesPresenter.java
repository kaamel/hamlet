package com.genesis.hamlet.ui.mmessages;

import com.genesis.hamlet.data.DataSource.OnMMessagesCallback;
import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.mvp.BasePresenter;

/**
 * Created by dipenrana on 10/24/17.
 */

public class MMessagesPresenter extends BasePresenter<MMessagesContract.View> implements  MMessagesContract.Presenter{

    //// TODO: 10/26/17 complete these two calls. To get a pointer to the dataReposity by using fragmentInteractionListener
    public void connectChatroom(BaseFragmentInteractionListener fragmentInteractionListener, OnMMessagesCallback onMMessageCallback, String Chatroom, int page) {
        //// TODO: 10/26/17 connect to chatroom via dataRepository (and nothing else)
    }
    public void sendMMessage(BaseFragmentInteractionListener fragmentInteractionListener, MMessage message, User user, String chatRoom) {
        //// TODO: 10/26/17 use dataRepository to send messages here.
    }
}
