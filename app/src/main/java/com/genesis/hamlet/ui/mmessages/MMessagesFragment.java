package com.genesis.hamlet.ui.mmessages;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.genesis.hamlet.R;
import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.models.mmessage.ChatMessage;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.EndlessRecyclerViewScrollListener;
import com.genesis.hamlet.util.mvp.BaseView;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

import java.util.List;

/**
 * Created by dipenrana on 10/24/17.
 */

public class MMessagesFragment extends BaseView implements MMessagesContract.View  {

    private EndlessRecyclerViewScrollListener endlessScrollListener;
    private MMessagesContract.Presenter presenter;
    private BaseFragmentInteractionListener fragmentInteractionListener;
    private boolean shouldRefreshUsers;

    private DataRepository dataRepository;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvMMessages;
    private MMessageRecyclerAdapter mMessageRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupRepository();
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mmessages, container, false);
        rvMMessages = (RecyclerView) view.findViewById(R.id.messageRecyclerView );
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.messagesSwipeRefreshLayout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMessageRecyclerAdapter = new MMessageRecyclerAdapter();
        rvMMessages.setAdapter(mMessageRecyclerAdapter);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setStackFromEnd(true);

        endlessScrollListener = new EndlessRecyclerViewScrollListener(mLinearLayoutManager,
                0) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //presenter.loadMoreUsers(getContext(), page);
            }
        };

        rvMMessages.addOnScrollListener(endlessScrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshUsers();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInteractionListener = (BaseFragmentInteractionListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        //presenter.connect(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void refreshUsers() {

    }

    private void setupRepository() {
        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();
        dataRepository = fragmentInteractionListener.getDataRepository();
        presenter = new MMessagesPresenter(this, dataRepository, threadExecutor, mainUiThread);
    }

    @Override
    public void showMMessages(List<ChatMessage> messages) {

        mMessageRecyclerAdapter.loadMessages(messages);

    }


    @Override
    public void addNewMMessage(ChatMessage message) {
        mMessageRecyclerAdapter.addNewMessages(message);
    }

    @Override
    public void remove(ChatMessage message) {

    }
}
