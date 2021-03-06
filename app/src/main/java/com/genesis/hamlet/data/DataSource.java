package com.genesis.hamlet.data;

import android.content.Context;
import android.location.Location;

import com.genesis.hamlet.data.local.LocalDataSource;
import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.data.remote.RemoteDataSource;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

import java.util.List;

/**
 * The interface that exposes fetching and storing Firebase storage data through helper methods. This is to be
 * implemented by all data sources such as
 * {@link RemoteDataSource} and
 * {@link LocalDataSource}
 */
public abstract class DataSource {

    public static final String USER_ARRIVAL_DEPARTURE_ACTION = "com.hamlet.broadcast.USER_IN_OUT";

    protected MainUiThread mainUiThread;
    protected ThreadExecutor threadExecutor;

    Location currentLocation;

    public DataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        this.mainUiThread = mainUiThread;
        this.threadExecutor = threadExecutor;


    }

    public abstract void disconnect(Context context);

    public abstract boolean isConnected();

    public interface OnUserCallback {
        void onSuccess(User user);

        void onFailure(Throwable throwable);

        void onNetworkFailure();

    }

    public interface OnUsersCallback {
        void onSuccess(List<User> users);
        void onFailure(Throwable throwable);
        void onNetworkFailure();
        void onNewUserJoined(User user);
        void onUserLeft(User user);

        void onUserUpdate(User user);
    }

    public interface OnMMessagesCallback {

        void onSuccess(List<MMessage> mMessages, String chatRoom, String senderId);
        void onSuccess(MMessage mMessage, String chatRoom, String senderId);

        void onFailure(Throwable throwable);

        void onNetworkFailure();

    }

    public abstract String sendNotification(User user, String chatRoom, String action, String title, String message);
    public abstract String sendNotification(String senduerUid, String receiverUid, String chatRoom, String action, String title, String message);
    public abstract User getLoggedInUser();
    public abstract void findUserById(String uId, OnUserCallback userCallback);
    public abstract void goOnline(Context context, OnUsersCallback onUsersCallback, long maxJoinTime);
    public abstract void refereshUsers(Context context);
    public abstract void updateUser();

    public abstract void connectChatroom(OnMMessagesCallback onMMessageCallback, String chatroom, int page);
    public abstract void closeChatroom(String chatroom);
    public abstract void sendMMessage(MMessage message, User user, String chatRoom);

}
