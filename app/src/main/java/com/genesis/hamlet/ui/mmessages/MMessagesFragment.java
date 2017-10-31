package com.genesis.hamlet.ui.mmessages;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.genesis.hamlet.R;
import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.ui.users.UsersFragment;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.EndlessRecyclerViewScrollListener;
import com.genesis.hamlet.util.Properties;
import com.genesis.hamlet.util.mvp.BaseView;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dipenrana on 10/24/17.
 */

public class MMessagesFragment extends BaseView implements MMessagesContract.View,View.OnClickListener {

    private static final String TAG = "MMessagesFragment ";
    private EndlessRecyclerViewScrollListener endlessScrollListener;
    private MMessagesContract.Presenter presenter;
    private BaseFragmentInteractionListener fragmentInteractionListener;
    private boolean shouldRefreshUsers;
    private static final int REQUEST_IMAGE = 2;
    private DataRepository dataRepository;
    private RecyclerView rvMMessages;
    private MMessageRecyclerAdapter mMessageRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    ImageButton btnMessageSend;
    ImageButton btnImageSend;
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

        btnMessageSend = (ImageButton) view.findViewById(R.id.sendButton);
        btnMessageSend.setVisibility(View.GONE);
        btnImageSend = (ImageButton) view.findViewById(R.id.ibSendImage);
        btnImageSend.setVisibility(View.VISIBLE);
        mMessageEditText = (EditText) view.findViewById(R.id.etMMessage);

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


        btnMessageSend.setOnClickListener(this);
        btnImageSend.setOnClickListener(this);


        endlessScrollListener = new EndlessRecyclerViewScrollListener(mLinearLayoutManager,
                0) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //presenter.loadMoreUsers(getContext(), page);
            }
        };

        presenter.connectChatroom(new DataSource.OnMMessagesCallback() {
            @Override
            public void onSuccess(List<MMessage> mMessages, String chatRoom, String senderId) {
                for (MMessage msg: mMessages) {
                    Log.d("Received Message:  ", msg.getCreateTime());
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


        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkNewItemForEmptyValues();
            }
        });


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
            case R.id.ibSendImage:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Please Select an Image"), REQUEST_IMAGE);
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(user.getDisplayName());
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
        inflater.inflate(R.menu.menu_mmessages,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Close")) {
            closeChatRoom();
            return true;
        }
        if (item.getTitle().equals("Report Spam")) {
            reportSpam();
            return true;
        }
        if (item.getTitle().equals("Block User")) {
            reportSpam();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri imageUri = data.getData();

            try{
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap( getActivity().getApplicationContext().getContentResolver(), imageUri);
                //mMessageEditText.setCompoundDrawables(null,null,getResources().getDrawable(bitmap), null);
                presenter.sendImages(imageUri, user, chatRoom);
            }
            catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private void closeChatRoom() {
        //dataRepository.closeChatroom(chatRoom);
        fragmentInteractionListener.showFragment(UsersFragment.class, null, new Bundle(), false);
    }

    public void sendMMessage(MMessage mMsg){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(currentTime);
        mMsg.setCreateTime(formattedDate);
        Log.d("Send Message: ", formattedDate);
        presenter.sendMMessage(mMsg,user,chatRoom);
    }

    public void reportSpam(){
        String title = "Are you sure you want to delete the selected item.";

        AlertDialog builder = new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_report_problem)
                .setTitle(title)
                .setPositiveButton("Report",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                closeChatRoom();
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        }
                ).create();
    }

    void checkNewItemForEmptyValues(){

        String newItem = mMessageEditText.getText().toString();

        if(newItem.equals("")){
            btnImageSend.setVisibility(View.VISIBLE);
            btnMessageSend.setVisibility(View.GONE);
        } else {
            btnImageSend.setVisibility(View.GONE);
            btnMessageSend.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mMessageEditText.getLayoutParams();
            params.addRule(RelativeLayout.LEFT_OF, R.id.sendButton);
            mMessageEditText.setLayoutParams(params); //causes layout updat

        }
    }

}
