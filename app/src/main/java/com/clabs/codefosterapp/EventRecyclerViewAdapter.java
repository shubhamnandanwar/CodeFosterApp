package com.clabs.codefosterapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shubh on 23-11-2016.
 */
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.EventHolder> {


    public EventRecyclerViewAdapter(){

    }
    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class  EventHolder extends RecyclerView.ViewHolder{

        public EventHolder(View itemView) {
            super(itemView);
        }
    }
}
