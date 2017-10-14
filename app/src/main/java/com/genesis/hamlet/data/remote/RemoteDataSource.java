package com.genesis.hamlet.data.remote;


import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

/**
 * The class for fetching data from Firebase
 */
public class RemoteDataSource extends DataSource {
    
    public RemoteDataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        super(mainUiThread, threadExecutor);
    }

    @Override
    public User getLoggedInUser() {
        //// TODO: 10/13/17
        return null;
    }


    @Override
    public void getUsers(GetUsersCallback getUsersCallback, long maxJoinTime) {
        //// TODO: 10/13/17  
    }

    @Override
    public void getMMessages(GetMMessagesCallback getMMessagesCallback, long maxId) {
        //// TODO: 10/13/17  
    }
}
