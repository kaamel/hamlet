package com.genesis.hamlet.ui.userdetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.genesis.hamlet.R;

/**
 * The {@link android.support.v7.widget.RecyclerView.Adapter} that renders and populates each
 * comment in the comments list.
 */
public class CommentsRecyclerAdapter extends
        RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_message, parent, false);

        CommentsRecyclerAdapter.ViewHolder viewHolder = new CommentsRecyclerAdapter.ViewHolder(
                view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentsRecyclerAdapter.ViewHolder viewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
