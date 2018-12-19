package com.example.awelijuh.classschedule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleItemHolder> {

    private List<Item> list;

    ScheduleRecyclerAdapter(List<Item> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ScheduleItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ScheduleItemHolder scheduleItemHolder = new ScheduleItemHolder(view);
        return scheduleItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleItemHolder holder, int position) {
        holder.bindCrime(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setStar(int index, boolean val) {
        list.get(index).setStar(val);
    }


}
