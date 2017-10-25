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
import com.genesis.hamlet.data.models.mmessage.ChatMessage;
import com.genesis.hamlet.data.models.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dipenrana on 10/24/17.
 */

public class MMessageRecyclerAdapter extends RecyclerView.Adapter<MMessageViewHolder>{

    private static List<ChatMessage> messages = new ArrayList<>();
    private static User currentUser = new User();
    private Context mContext;

//(Context context, ArrayList<ChatMessage> messages, User user) {
    public MMessageRecyclerAdapter(){
//        mContext = context;
//        this.messages = messages;
//        currentUser = user;

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

        ChatMessage friendlyMessage = messages.get(position);

        // if message is text then add text else if message is image use glide to load image
        if (friendlyMessage.getText() != null) {
            viewHolder.getMessageTextView().setText(friendlyMessage.getText());
            viewHolder.getMessageTextView().setVisibility(TextView.VISIBLE);
            viewHolder.getMessageImageView().setVisibility(ImageView.GONE);
        }
        else {
            String messageImageUrl = friendlyMessage.getMessageImageUrl();
            Glide.with(viewHolder.getMessageImageView().getContext())
                    .load(messageImageUrl)
                    .into(viewHolder.getMessageImageView());

            viewHolder.getMessageTextView().setVisibility(ImageView.INVISIBLE);
            viewHolder.getMessageImageView().setVisibility(ImageView.VISIBLE);
        }

        // add messenger name and image
        viewHolder.getMessengerTextView().setText(friendlyMessage.getName());
        if (friendlyMessage.getMessengerImageUrl() == null) {
            viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_account_circle));
        } else {
            Glide.with(mContext)
                    .load(friendlyMessage.getMessengerImageUrl())
                    .into(viewHolder.messengerImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage msg = messages.get(position);

        if(msg.getName().equals(currentUser.getDisplayName())){
            return 0;
        }
        else
            return 1;

    }

    //load past messages
    public void loadMessages(List<ChatMessage> messages){
        this.messages.clear();
        this.messages = messages;
        notifyDataSetChanged();

    }

    //add new messages
    public void addNewMessages(ChatMessage newMessage){
        messages.add(newMessage);
        notifyItemChanged(messages.size()-1);

    }
}
