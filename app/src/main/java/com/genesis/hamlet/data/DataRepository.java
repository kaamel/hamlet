package com.genesis.hamlet.data;

import android.content.Context;

import com.genesis.hamlet.data.local.LocalDataSource;
import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.data.remote.RemoteDataSource;
import com.genesis.hamlet.util.mvp.BasePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The primary class for the presenters that extend
 * {@link BasePresenter} to interact with
 * for fetching and storing data.
 * It is the middleman in front of all data sources such as
 * {@link RemoteDataSource}
 * and {@link LocalDataSource} and delegates the work to
 * them depending on conditions such as network availability, etc.
 */
public class DataRepository {

    private static DataSource remoteDataSource;

    private static DataRepository dataRepository;

    private DataRepository(DataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static synchronized DataRepository getInstance(DataSource remoteDataSource) {
        if (dataRepository == null) {
            dataRepository = new DataRepository(remoteDataSource);
        }
        return dataRepository;
    }

    public void refereshUsers() {
        dataRepository.refereshUsers();
    }

    public void destroyInstance(Context context) {
        disconnect(context);
    }

    public void connectRemote(Context context, final DataSource.OnUsersCallback onUsersCallback, int page) {
        startUsersStream(context, onUsersCallback);
    }

    public void sendNotification(User user, String action, String chatRoom, String title, String message) {
        FirebaseUser me = FirebaseAuth.getInstance().getCurrentUser();
        if (me != null)
            remoteDataSource.sendNotification(user, chatRoom, action, title, message);
    }

    public void sendNotification(String senduerUid, String receiverUid, String chatRoom, String action, String title, String message) {
        remoteDataSource.sendNotification(senduerUid, receiverUid, chatRoom, action, title, message);
    }

    public void updateUserInterests() {
        remoteDataSource.updateUser();
    }

    private void startUsersStream(Context context, DataSource.OnUsersCallback onUsersCallback) {
        remoteDataSource.goOnline(context, onUsersCallback, 0);
    }

    public void connectChatroom(DataSource.OnMMessagesCallback onMMessageCallback, String chatroom, int page) {
        remoteDataSource.connectChatroom(onMMessageCallback, chatroom, page);
    }

    public void sendMMessage(MMessage message, User user, String chatRoom) {
        remoteDataSource.sendMMessage(message, user, chatRoom);
    }

    public void disconnect(Context context) {
        remoteDataSource.disconnect(context);
        dataRepository = null;
        //remoteDataSource = null;
    }

    public boolean isConnected() {
        return remoteDataSource.isConnected();
    }
}
