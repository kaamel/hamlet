package com.genesis.hamlet.ui.users;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.genesis.hamlet.R;
import com.genesis.hamlet.data.models.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link android.support.v7.widget.RecyclerView.Adapter} that renders and populates each photo
 * in the photos list.
 */
public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {

    private List<User> users = new ArrayList<>();
    private Fragment fragment;

    public UsersRecyclerAdapter(Fragment fragment, List<User> users) {
        this.users = users;
        this.fragment = fragment;
    }

    public void add(User user) {
        int prevSize = getItemCount();
        this.users.add(user);
        notifyItemInserted(prevSize);
    }

    public void remove(User user) {
        int size = users.size();
        int position = -1;
        for (int i=0; i< size; i++) {
            if (users.get(i).getUid().equals(user.getUid())) {
                position = i;
                users.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDisplayName;
        TextView tvDetailMessage;
        public ViewHolder(View itemView) {
            super(itemView);
            tvDisplayName = (TextView) itemView.findViewById(R.id.tvDisplayName);
            tvDetailMessage = (TextView) itemView.findViewById(R.id.tvDetailMessage);
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

        viewHolder.tvDisplayName.setText(user.getUid());
        viewHolder.tvDetailMessage.setText(user.getDisplayName());

        /*Glide.with(fragment).load(user.getPhotoUrl()).placeholder(R.drawable.drawable_placeholder).error(
                R.drawable.drawable_placeholder).into(viewHolder.ivUserUrl);*/
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void clear() {
        int size = getItemCount();
        users.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addAll(List<User> users) {
        int prevSize = getItemCount();
        this.users.addAll(users);
        notifyItemRangeInserted(prevSize, users.size());
    }

}
