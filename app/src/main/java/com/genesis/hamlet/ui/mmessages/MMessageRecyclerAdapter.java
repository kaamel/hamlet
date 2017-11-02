package com.genesis.hamlet.ui.mmessages;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.genesis.hamlet.R;
import com.genesis.hamlet.data.models.interests.MyInterests;
import com.genesis.hamlet.data.models.mmessage.MMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dipenrana on 10/24/17.
 */

public class MMessageRecyclerAdapter extends RecyclerView.Adapter<MMessageViewHolder>{

    private List<MMessage> mMessages = new ArrayList<>();
    private Context mContext;
    //private User user;

    //(Context context, ArrayList<ChatMessage> messages, User user) {
    public MMessageRecyclerAdapter(Context context){
        mContext = context;
    }

    @Override
    public MMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == 0){
            return new MMessageViewHolder(inflater.inflate(R.layout.mmessage_friend, parent, false));
        }
        else {
            return new MMessageViewHolder(inflater.inflate(R.layout.mmessage_user, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(MMessageViewHolder viewHolder, int position) {

        MMessage mMessage = mMessages.get(position);
        // if message is text then add text else if message is image use glide to load image
        if (mMessage.getText() != null) {
            viewHolder.getMessageTextView().setText(mMessage.getText());
            viewHolder.getMessageTextView().setVisibility(TextView.VISIBLE);
            viewHolder.getMessageImageView().setVisibility(ImageView.GONE);
        }
        else {
            String messageImageUrl = mMessage.getImageUrl();
            Glide.with(viewHolder.getMessageImageView().getContext())
                    .load(messageImageUrl)
                    .into(viewHolder.getMessageImageView());

            viewHolder.getMessageTextView().setVisibility(ImageView.INVISIBLE);
            viewHolder.getMessageImageView().setVisibility(ImageView.VISIBLE);
        }

        // add messenger name and image
        viewHolder.getMessengerTextView().setText(mMessage.getDisplayName());
        //viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_account_circle));
        if (mMessage.getProfileUrl() == null) {
            viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.ic_launcher_round));
        } else {
            Glide.with(mContext)
                    .load(mMessage.getProfileUrl())
                    .into(viewHolder.messengerImageView);
        }

        if (mMessage.getImageUrl() == null) {
            viewHolder.messageImageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.ic_launcher_round));
        } else {
            Glide.with(mContext)
                    .load(mMessage.getImageUrl())
                    .into(viewHolder.messageImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        MMessage mMsg = mMessages.get(position);

        if(mMsg.getSenderUid().equals(MyInterests.getInstance().getMyUid())){
            return 0;
        }
        else
            return 1;

    }

    public void addMmessage(MMessage mMessage){
        mMessages.add(mMessage);
        this.notifyItemInserted(mMessages.size()-1);
    }


}