package com.genesis.hamlet.di;


import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.remote.FirebaseCloudDataSource;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

public class Injection {

    public static DataRepository provideDataRepository() {
        MainUiThread mainUiThread = MainUiThread.getInstance();
        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        return DataRepository.getInstance(FirebaseCloudDataSource.getInstance(mainUiThread, threadExecutor));
    }
}
