package com.anywhere.campasiliano.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.databinding.ChatItemLeftBinding;
import com.anywhere.campasiliano.databinding.ChatItemRightBinding;
import com.anywhere.campasiliano.databinding.UserItemBinding;
import com.anywhere.campasiliano.models.chats.Chat;
import com.anywhere.campasiliano.models.users.User;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.views.activities.chats.MessageActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>  {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChats;
    private String imageUrl;
    private FirebaseUser firebaseUser;
    private ChatItemRightBinding rightBinding;
    private ChatItemLeftBinding leftBinding;

    public MessageAdapter(Context mContext, List<Chat> mChats, String imageUrl) {
        this.mContext = mContext;
        this.mChats = mChats;
        this.imageUrl = imageUrl;
        Anywhere.activity = (Activity) mContext;
    }

    @NonNull
    @NotNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        rightBinding = ChatItemRightBinding.inflate(LayoutInflater.from(mContext), parent, false);
        leftBinding = ChatItemLeftBinding.inflate(LayoutInflater.from(mContext), parent, false);
        if(viewType == MSG_TYPE_RIGHT) {
            return new MessageAdapter.ViewHolder(rightBinding.getRoot());
        }else {
            return new MessageAdapter.ViewHolder(leftBinding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChats.get(position);
        holder.show_message.setText(chat.getMessage());

        if(imageUrl.equals("default")) {
            holder.profile_image.setImageResource(R.drawable.logo1);
        }else {
            Glide.with(mContext).load(imageUrl).into(holder.profile_image);
        }

        if (position == mChats.size() - 1) {
            if (chat.isIsseen()) {
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
        public TextView txt_seen;
        public ImageView profile_image;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            //show_message = itemView.findViewById(R.id.show_message);
            //txt_seen = itemView.findViewById(R.id.txt_seen);
            profile_image = itemView.findViewById(R.id.profile_image);

        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChats.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
