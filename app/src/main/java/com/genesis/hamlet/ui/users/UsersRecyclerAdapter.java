package com.genesis.hamlet.ui.users;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.genesis.hamlet.data.models.user.User;

import java.util.List;

/**
 * The {@link android.support.v7.widget.RecyclerView.Adapter} that renders and populates each photo
 * in the photos list.
 */
public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {

    public UsersRecyclerAdapter(Fragment fragment, List<User> users) {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }

    @Override
    public UsersRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        return null;
    }

    @Override
    public void onBindViewHolder(UsersRecyclerAdapter.ViewHolder viewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
