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
import android.widget.EditText;
import android.widget.ImageButton;

import com.genesis.hamlet.R;
import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.EndlessRecyclerViewScrollListener;
import com.genesis.hamlet.util.mvp.BaseView;

import java.util.List;

/**
 * Created by dipenrana on 10/24/17.
 */

public class MMessagesFragment extends BaseView implements MMessagesContract.View,View.OnClickListener {

    private EndlessRecyclerViewScrollListener endlessScrollListener;
    private MMessagesContract.Presenter presenter;
    private BaseFragmentInteractionListener fragmentInteractionListener;
    private boolean shouldRefreshUsers;

    private DataRepository dataRepository;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvMMessages;
    private MMessageRecyclerAdapter mMessageRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    ImageButton btnSend;
    EditText mMessageEditText;

    //// TODO: 10/26/17 I have added these two item that are need to be loaded in when the fragment is created - look at the UserDetailFragment to see how they are loaded as an example
    String chatRoom;
    User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mmessages, container, false);
        rvMMessages = (RecyclerView) view.findViewById(R.id.messageRecyclerView );
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.messagesSwipeRefreshLayout);
        btnSend = (ImageButton) view.findViewById(R.id.sendButton);
        mMessageEditText = (EditText) view.findViewById(R.id.messageEditText);

        //// TODO: 10/26/17 after picking up the chatRoom and user here, setup a callback

        presenter.connectChatroom(fragmentInteractionListener, new DataSource.OnMMessagesCallback() {
            @Override
            public void onSuccess(List<MMessage> mMessages, String chatRoom, String senderId) {
                //// TODO: 10/26/17 deal with incoming messages
            }

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onNetworkFailure() {

            }
        }, chatRoom, 0);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMessageRecyclerAdapter = new MMessageRecyclerAdapter();
        rvMMessages.setAdapter(mMessageRecyclerAdapter);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setStackFromEnd(true);


        btnSend.setOnClickListener(this);

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
                //refreshUsers();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //send text message
            case R.id.sendButton:
                sendMMessage(mMessageEditText.getText().toString());
                break;
            //send images
            case R.id.addMessageImageView:
                break;
        }

    }

    private void sendMMessage(String s) {
        //// TODO: 10/26/17 create a new MMessage object The line bellow is just a dummy so the rest of the code can work
        MMessage msg = new MMessage();

        presenter.sendMMessage(fragmentInteractionListener, msg, user, chatRoom);
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
}
