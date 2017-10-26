package com.genesis.hamlet.ui.mmessages;

import android.content.Context;

import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.mvp.BasePresenter;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by dipenrana on 10/24/17.
 */

public class MMessagesPresenter extends BasePresenter<MMessagesContract.View> implements  MMessagesContract.Presenter{

    private DataRepository dataRepository;
    private ThreadExecutor threadExecutor;
    private MainUiThread mainUiThread;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;


    public MMessagesPresenter(MMessagesContract.View view, DataRepository dataRepository, ThreadExecutor threadExecutor, MainUiThread mainUiThread)
    {
        this.view = view;
        this.dataRepository = dataRepository;
        this.threadExecutor = threadExecutor;
        this.mainUiThread = mainUiThread;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    public boolean isConnected() {
        return dataRepository.isConnected();
    }

    @Override
    public void connect(Context context,String chatRoom)
    {
//        if (dataRepository.isConnected())
//            return;
        //view.setProgressBar(true);

        dataRepository.connectChatroom(context,new DataSource.OnMMessagesCallback(){

            @Override
            public void onSuccess(List<MMessage> mMessages, String senderId) {
                view.onMessageReceived(mMessages,senderId);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onNetworkFailure() {

            }
        },chatRoom,0);

    }

    //send message from fragment to data repos
    @Override
    public void sendMessage(String message,User user,String chatRoom) {
        MMessage friendlyMessage = new MMessage();
        friendlyMessage.setDisplayName(mFirebaseUser.getDisplayName());
        friendlyMessage.setUserImage(mFirebaseUser.getPhotoUrl().toString());
        friendlyMessage.setText(message);
        dataRepository.sendMMessage(friendlyMessage,user,chatRoom);

    }

}
