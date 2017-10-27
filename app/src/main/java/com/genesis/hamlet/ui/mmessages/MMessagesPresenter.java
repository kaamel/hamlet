package com.genesis.hamlet.ui.mmessages;

import android.content.Context;

import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.DataSource.OnMMessagesCallback;
import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.mvp.BasePresenter;

import java.util.List;

/**
 * Created by dipenrana on 10/24/17.
 */

public class MMessagesPresenter extends BasePresenter<MMessagesContract.View> implements  MMessagesContract.Presenter{

    private DataRepository dataRepository;
    private Context mContext;
    public MMessagesPresenter(Context context){
        mContext = context;
    }

    //// TODO: 10/26/17 complete these two calls. To get a pointer to the dataReposity by using fragmentInteractionListener
    public void connectChatroom(BaseFragmentInteractionListener fragmentInteractionListener, final OnMMessagesCallback onMMessageCallback, String Chatroom, int page) {
        //// TODO: 10/26/17 connect to chatroom via dataRepository (and nothing else)
        dataRepository = fragmentInteractionListener.getDataRepository();
        final User user = dataRepository.getLoggedInUser();
        dataRepository.connectChatroom(mContext,new DataSource.OnMMessagesCallback(){

            @Override
            public void onSuccess(List<MMessage> mMessages, String chatRoom) {
                 onMMessageCallback.onSuccess(mMessages,chatRoom);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onNetworkFailure() {

            }
        },Chatroom,0);
    }
    public void sendMMessage(BaseFragmentInteractionListener fragmentInteractionListener, MMessage message, User user, String chatRoom) {
        //// TODO: 10/26/17 use dataRepository to send messages here.
        dataRepository.sendMMessage(message,user,chatRoom);
    }
}
