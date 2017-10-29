package com.genesis.hamlet.ui.mmessages;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
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
import com.genesis.hamlet.util.Properties;
import com.genesis.hamlet.util.mvp.BaseView;

import org.parceler.Parcels;

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
    //private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvMMessages;
    private MMessageRecyclerAdapter mMessageRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    ImageButton btnSend;
    EditText mMessageEditText;

    String chatRoom;
    User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        dataRepository = fragmentInteractionListener.getDataRepository();
        presenter = new MMessagesPresenter(this, dataRepository);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mmessages, container, false);
        rvMMessages = (RecyclerView) view.findViewById(R.id.messageRecyclerView );

        btnSend = (ImageButton) view.findViewById(R.id.sendButton);
        mMessageEditText = (EditText) view.findViewById(R.id.messageEditText);

        Parcelable parcelable = getArguments().getParcelable(Properties.BUNDLE_KEY_USER);
        user = Parcels.unwrap(parcelable);
        chatRoom = getArguments().getString("chatRoom");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMessageRecyclerAdapter = new MMessageRecyclerAdapter(getContext());
        rvMMessages.setAdapter(mMessageRecyclerAdapter);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setStackFromEnd(true);
        rvMMessages.setLayoutManager(mLinearLayoutManager);


        btnSend.setOnClickListener(this);

        endlessScrollListener = new EndlessRecyclerViewScrollListener(mLinearLayoutManager,
                0) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //presenter.loadMoreUsers(getContext(), page);
            }
        };

        // rvMMessages.addOnScrollListener(endlessScrollListener);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                //refreshUsers();
//            }
//        });
//
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);

        presenter.connectChatroom(new DataSource.OnMMessagesCallback() {
            @Override
            public void onSuccess(List<MMessage> mMessages, String chatRoom, String senderId) {
                for (MMessage msg: mMessages) {
                    mMessageRecyclerAdapter.addMmessage(msg);
                }
            }

            @Override
            public void onSuccess(MMessage mMessage, String chatRoom, String senderId) {
                mMessageRecyclerAdapter.addMmessage(mMessage);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onNetworkFailure() {

            }
        }, chatRoom, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //send text message
            case R.id.sendButton:
                MMessage mMsg = new MMessage();
                mMsg.setText(mMessageEditText.getText().toString());
                sendMMessage(mMsg);
                mMessageEditText.setText("");
                break;
            //send images
            case R.id.addMessageImageView:
                break;
        }

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

    public void sendMMessage(MMessage mMsg){
        presenter.sendMMessage(mMsg,user,chatRoom);
    }


}
