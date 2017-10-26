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
import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dipenrana on 10/24/17.
 */

public class MMessageRecyclerAdapter extends RecyclerView.Adapter<MMessageViewHolder>{

    private static List<MMessage> mmessages = new ArrayList<>();
    private static User currentUser = new User();
    private Context mContext;

//(Context context, ArrayList<ChatMessage> messages, User user) {
    public MMessageRecyclerAdapter(){

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

        MMessage friendlyMessage = mmessages.get(position);

        // if message is text then add text else if message is image use glide to load image
        if (friendlyMessage.getText() != null) {
            viewHolder.getMessageTextView().setText(friendlyMessage.getText());
            viewHolder.getMessageTextView().setVisibility(TextView.VISIBLE);
            viewHolder.getMessageImageView().setVisibility(ImageView.GONE);
        }
        else {
            String messageImageUrl = friendlyMessage.getImageUrl();
            Glide.with(viewHolder.getMessageImageView().getContext())
                    .load(messageImageUrl)
                    .into(viewHolder.getMessageImageView());

            viewHolder.getMessageTextView().setVisibility(ImageView.INVISIBLE);
            viewHolder.getMessageImageView().setVisibility(ImageView.VISIBLE);
        }

        // add messenger name and image
        viewHolder.getMessengerTextView().setText(friendlyMessage.getDisplayName());
        //viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_account_circle));
        if (friendlyMessage.getUserImage() == null) {
            viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_account_circle));
        } else {
            Glide.with(mContext)
                    .load(friendlyMessage.getUserImage())
                    .into(viewHolder.messengerImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mmessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        MMessage msg = mmessages.get(position);

        if(msg.getSenderUid().equals(currentUser.getDisplayName())){
            return 0;
        }
        else
            return 1;

    }

    //load past messages
    public void showMMessages(List<MMessage> messages){
        //this.mmessages.clear();
        this.mmessages.addAll(messages);
        this.notifyDataSetChanged();

    }

}
