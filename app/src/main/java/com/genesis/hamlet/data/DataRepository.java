package com.genesis.hamlet.data;

import android.content.Context;

import com.genesis.hamlet.data.local.LocalDataSource;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.data.remote.RemoteDataSource;
import com.genesis.hamlet.util.mvp.BasePresenter;

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
    boolean connected = false;

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

    public void destroyInstance(Context context) {
        disconnect(context);
    }

    public void connectRemote(Context context, final DataSource.OnUsersCallback onUsersCallback, int page) {

        if (!connected || page ==0) {
            connected = true;
            startUsersStream(context, onUsersCallback);
        }
        else {
            if (page == 0) {
                //// TODO: 10/20/17 refresh data
            }
            //onUsersCallback.onSuccess(new ArrayList<User>());
        }
    }

    public void connectRemote(Context context, final DataSource.OnMMessagesCallback onMMessagesCallback, int page) {

        if (!connected || page ==0) {
            connected = true;
            startUsersStream(context, onMMessagesCallback);
        }
        else {
            if (page == 0) {
                //// TODO: 10/20/17 refresh data
            }
            //onUsersCallback.onSuccess(new ArrayList<User>());
        }
    }

    public void sendNotification(User user, String action, String title, String message) {
        remoteDataSource.sendNotification(user, action, title, message);
    }

    public void sendNotification(String uId, String action, String title, String message) {
        remoteDataSource.sendNotification(uId, action, title, message);
    }

    public void updateUserInterests() {
        remoteDataSource.updateUser();
    }

    private void startUsersStream(Context context, DataSource.OnUsersCallback onUsersCallback) {
        remoteDataSource.goOnline(context, onUsersCallback, 0);
    }

    private void startUsersStream(Context context, DataSource.OnMMessagesCallback onMMessagesCallback) {

    }

    public void disconnect(Context context) {
        remoteDataSource.disconnect(context);
        dataRepository = null;
        //remoteDataSource = null;
        connected = false;
    }

    public boolean isConnected() {
        return connected;
    }
}
