package com.genesis.hamlet.ui.mmessages;

import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.DataSource.OnMMessagesCallback;
import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.mvp.BasePresenter;

/**
 * Created by dipenrana on 10/24/17.
 */

public class MMessagesPresenter extends BasePresenter<MMessagesContract.View> implements  MMessagesContract.Presenter{

    DataRepository dataRepository;
    MMessagesContract.View view;
    MMessagesPresenter(MMessagesContract.View view, DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        this.view = view;
    }

    public void connectChatroom(OnMMessagesCallback onMMessageCallback, String chatroom, int page) {
        dataRepository.connectChatroom(onMMessageCallback, chatroom, page);
    }
    public void sendMMessage(MMessage message, User user, String chatRoom) {
        dataRepository.sendMMessage(message, user, chatRoom);
    }

    @Override
    public void storeFileRemote(String localPath, DataSource.OnFileStored onFileStored) {
        dataRepository.storeFileRemote(localPath, onFileStored);
    }
}
