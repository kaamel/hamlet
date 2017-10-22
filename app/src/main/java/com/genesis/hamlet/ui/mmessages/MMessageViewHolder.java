package com.genesis.hamlet.ui.mmessages;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.genesis.hamlet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by dipenrana on 10/21/17.
 */

public class MMessageViewHolder extends RecyclerView.ViewHolder {



    @BindView(R.id.messageTextView) TextView messageTextView;
    @BindView(R.id.messageImageView) ImageView messageImageView;
    @BindView(R.id.messengerTextView) TextView messengerTextView;
    @BindView(R.id.messengerImageView) CircleImageView messengerImageView;

    public MMessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

//        messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
//        messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
//        messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
//        messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
    }

    public TextView getMessageTextView() {
        return messageTextView;
    }

    public void setMessageTextView(TextView messageTextView) {
        this.messageTextView = messageTextView;
    }

    public ImageView getMessageImageView() {
        return messageImageView;
    }

    public void setMessageImageView(ImageView messageImageView) {
        this.messageImageView = messageImageView;
    }

    public TextView getMessengerTextView() {
        return messengerTextView;
    }

    public void setMessengerTextView(TextView messengerTextView) {
        this.messengerTextView = messengerTextView;
    }

    public CircleImageView getMessengerImageView() {
        return messengerImageView;
    }

    public void setMessengerImageView(CircleImageView messengerImageView) {
        this.messengerImageView = messengerImageView;
    }
}
