package com.anywhere.campasiliano.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.models.events.Event;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {

    private Context context;
    private List<Event> list;

    public EventAdapter(Context context, List<Event> list) {
        this.context = context;
        this.list = list;
        Anywhere.activity = (Activity) context;
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_items, parent, false);
        return new EventAdapter.EventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        Event event = list.get(position);
        if(event.getCover().equals("default")) {
            holder.imgEventCover.setImageResource(R.drawable.logo1);
        }else {
            Glide.with(context).load(event.getCover()).into(holder.imgEventCover);
        }
        holder.txtEventName.setText(event.getName());
        holder.txtEventDesc.setText(event.getDesc());
        holder.txtEventDate.setText(event.getDate_event());
        holder.cardEvent.setOnClickListener(v -> {
            Anywhere.message("Success");
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EventHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardEvent;
        private ImageView imgEventCover;
        private TextView txtEventName, txtEventDesc, txtEventDate;
        private MaterialButton btnCheck;

        public EventHolder(@NonNull View itemView) {
            super(itemView);

            cardEvent = itemView.findViewById(R.id.cardEvent);
            imgEventCover = itemView.findViewById(R.id.imgEventCover);
            txtEventName = itemView.findViewById(R.id.txtEventName);
            txtEventDesc = itemView.findViewById(R.id.txtEventDesc);
            txtEventDate = itemView.findViewById(R.id.txtEventDate);
            btnCheck = itemView.findViewById(R.id.btnCheck);

        }
    }

}
