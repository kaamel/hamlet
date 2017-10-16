package com.genesis.hamlet.data.remote;


import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

/**
 * The class for fetching data from Firebase
 */
public class RemoteDataSource extends FirebaseCloudDataSource {
    public RemoteDataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        super(mainUiThread, threadExecutor);
    }
}
