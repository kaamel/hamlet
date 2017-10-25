package com.genesis.hamlet.ui.mmessages;

import android.content.Context;

import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.mmessage.ChatMessage;
import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.util.mvp.BasePresenter;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

import java.util.List;

/**
 * Created by dipenrana on 10/24/17.
 */

public class MMessagesPresenter extends BasePresenter<MMessagesContract.View> implements  MMessagesContract.Presenter{

    private DataRepository dataRepository;
    private ThreadExecutor threadExecutor;
    private MainUiThread mainUiThread;


    public MMessagesPresenter(MMessagesContract.View view, DataRepository dataRepository,
                              ThreadExecutor threadExecutor, MainUiThread mainUiThread) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.threadExecutor = threadExecutor;
        this.mainUiThread = mainUiThread;
    }

    @Override
    public boolean isConnected() {
        return dataRepository.isConnected();
    }

    @Override
    public void connect(Context context) {
        if (dataRepository.isConnected())
            return;
        view.setProgressBar(true);

        dataRepository.connectRemote(context, new DataSource.OnMMessagesCallback() {

            @Override
            public void onSuccess(List<MMessage> mMessages) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onNetworkFailure() {

            }
        },0);

    }

    @Override
    public void sendMessage(ChatMessage message) {

    }

    public interface View {

    }
}
