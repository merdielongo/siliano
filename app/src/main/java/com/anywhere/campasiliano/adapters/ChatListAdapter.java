package com.anywhere.campasiliano.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.models.chats.ChatList;
import com.anywhere.campasiliano.views.activities.chats.ChatsActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {

    private List<ChatList> list;
    private Context context;

    public ChatListAdapter(List<ChatList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.Holder holder, int position) {
        ChatList chatList = list.get(position);
        holder.tvName.setText(chatList.getUsername());
        holder.tvDesc.setText(chatList.getDescription());
        holder.tvDate.setText(chatList.getDate());
        if(chatList.getUrlProfile() == null || chatList.getUrlProfile().equals("")) {
            holder.imgUserProfile.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }else {
            Glide.with(context).load(chatList.getUrlProfile()).into(holder.imgUserProfile);
        }
        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, ChatsActivity.class)
                    .putExtra("userId", chatList.getUserID())
                    .putExtra("userName", chatList.getUsername())
                    .putExtra("imageProfile", chatList.getUrlProfile()));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView tvName, tvDesc, tvDate;
        private CircleImageView imgUserProfile;

        public Holder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvDate = itemView.findViewById(R.id.tvDate);
            imgUserProfile = itemView.findViewById(R.id.imgUserProfile);
        }
    }

}
