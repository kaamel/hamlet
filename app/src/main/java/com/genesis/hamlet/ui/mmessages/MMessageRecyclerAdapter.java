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
import com.genesis.hamlet.util.CommonUtils;

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
        if(viewType == 1){
            return new MMessageViewHolder(inflater.inflate(R.layout.mmessage_friend, parent, false));
        }
        else {
            return new MMessageViewHolder(inflater.inflate(R.layout.mmessage_me, parent, false));
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
            if(messageImageUrl.contains("hamlet-ea998.appspot.com")){
                Glide.with(viewHolder.getMessageImageView().getContext())
                        .load(messageImageUrl)
                        .into(viewHolder.getMessageImageView());

//                StorageReference storageReference = FirebaseStorage.getInstance()
//                        .getReferenceFromUrl(imageUrl);
//                storageReference.getDownloadUrl().addOnCompleteListener(
//                        new OnCompleteListener<Uri>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Uri> task) {
//                                if (task.isSuccessful()) {
//                                    String downloadUrl = task.getResult().toString();
//                                    Glide.with(viewHolder.getMessageImageView().getContext())
//                                            .load(downloadUrl)
//                                            .into(viewHolder.getMessageImageView());
//                                } else {
//                                    Log.w(TAG, "Getting download url was not successful.",
//                                            task.getException());
//                                }
//                            }
//                        });
            }
            else{
                Glide.with(viewHolder.getMessageImageView().getContext())
                        .load(messageImageUrl)
                        .into(viewHolder.getMessageImageView());
            }


            viewHolder.getMessageTextView().setVisibility(ImageView.INVISIBLE);
            viewHolder.getMessageImageView().setVisibility(ImageView.VISIBLE);
        }

        // add message time and messenger image
        String relativeTime = CommonUtils.getRelativeTimeAgo(mMessage.getCreateTime());
        viewHolder.getTvMessageTime().setText(relativeTime);

        //viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_account_circle));
        if (mMessage.getProfileUrl() == null) {
            viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_account_circle));
        } else {
            Glide.with(mContext)
                    .load(mMessage.getProfileUrl())
                    .into(viewHolder.messengerImageView);
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
