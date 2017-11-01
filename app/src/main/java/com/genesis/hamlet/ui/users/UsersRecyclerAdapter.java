package com.genesis.hamlet.ui.users;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.genesis.hamlet.R;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.RoundedCornersTransformation;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link android.support.v7.widget.RecyclerView.Adapter} that renders and populates each photo
 * in the photos list.
 */
public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {

    private static final List<User> users = new ArrayList<>();
    private Context context;

    public UsersRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void add(User user) {
        update(user);
/*
        int prevSize = getItemCount();
        this.users.add(user);
        notifyItemInserted(prevSize);
*/
    }

    public void remove(User user) {
        int size = users.size();
        for (int i=0; i< size; i++) {
            if (users.get(i).getUid().equals(user.getUid())) {
                users.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void update(User user) {
        for (int i= 0; i< users.size(); i++) {
            if (users.get(i).getUid().equals(user.getUid())){
                users.set(i, user);
                notifyItemChanged(i);
                return;
            }
        }
        users.add(user);
        notifyItemInserted(users.size()-1);
    }

    public User getItem(int position) {
        return users.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDisplayName;
        TextView tvIntroTitle;
        ImageView ivProfile;
        public ViewHolder(View itemView) {
            super(itemView);
            tvDisplayName = (TextView) itemView.findViewById(R.id.tvDisplayName);
            tvIntroTitle = (TextView) itemView.findViewById(R.id.tvIntroTitle);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_user, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UsersRecyclerAdapter.ViewHolder viewHolder, int position) {
        User user = users.get(position);

        viewHolder.tvDisplayName.setText(user.getDisplayName());
        viewHolder.tvIntroTitle.setText(user.getIntroTitle());

        if (context != null && !((Activity) context).isFinishing())
            Glide.with(context).load(user.getPhotoUrl()).placeholder(R.mipmap.ic_launcher_round)
                    .bitmapTransform(new RoundedCornersTransformation( context, 72, 2))
                    .error(R.mipmap.ic_launcher_round).into(viewHolder.ivProfile);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void clear() {
        int size = getItemCount();
        users.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }

}
