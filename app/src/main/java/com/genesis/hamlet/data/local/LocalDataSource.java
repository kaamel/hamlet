package com.genesis.hamlet.data.local;


import android.content.Context;

import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

import java.util.List;

/**
 * The class for fetching from and storing data into a local SQLite DB on a background thread and
 * returning data via callbacks on the main UI thread
 */
public class LocalDataSource extends DataSource implements DataSource.OnUsersCallback {
    private static LocalDataSource localDataSource;

    private LocalDataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        super(mainUiThread, threadExecutor);
    }

    @Override
    public void disconnect(Context context) {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void sendNotification(User user, String chatRoom, String action, String title, String message) {

    }

    @Override
    public void sendNotification(String senduerUid, String receiverUid, String chatRoom, String action, String title, String message) {

    }

    @Override
    public User getLoggedInUser() {
        return LocalDatabase.getLoggedInUser();
    }

    public static synchronized LocalDataSource getInstance(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        if (localDataSource == null) {
            localDataSource = new LocalDataSource(mainUiThread, threadExecutor);
        }
        return localDataSource;
    }


    private OnUsersCallback callback;
    @Override
    public void goOnline(Context context, OnUsersCallback onUsersCallback, long maxJoinTime) {
        callback = onUsersCallback;
        onUsersCallback.onSuccess(LocalDatabase.getUsers());
    }

    @Override
    public void refereshUsers(Context context) {

    }

    @Override
    public void updateUser() {

    }

    @Override
    public void connectChatroom(Context context, OnMMessagesCallback onMMessageCallback, String Chatroom, int page) {

    }

    @Override
    public void sendMMessage(MMessage message, User user, String chatRoom) {

    }

    //@Override
    public void storeUsers(List<User> users) {
        //// TODO: 10/14/17
    }

    @Override
    public void onSuccess(List<User> users) {
        LocalDatabase.storeUsers(users);
        callback.onSuccess(users);
    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onNetworkFailure() {

    }

    @Override
    public void onNewUserJoined(User user) {
        LocalDatabase.addUser(user);
        callback.onNewUserJoined(user);
    }

    @Override
    public void onUserLeft(User user) {
        LocalDatabase.removeUser(user);
        callback.onUserLeft(user);
    }

    @Override
    public void onUserUpdate(User user) {
        //
    }

    /**
    // * When data is already available locally, we don't go back to Firebase. The threading and asysnc calls
    // would be managed here while the actual data would be in {@linke LocalDatabase}
    / */


}
