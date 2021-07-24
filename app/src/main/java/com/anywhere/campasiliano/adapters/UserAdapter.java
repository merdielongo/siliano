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
import com.anywhere.campasiliano.databinding.FragmentUsersBinding;
import com.anywhere.campasiliano.databinding.UserItemBinding;
import com.anywhere.campasiliano.models.chats.Chat;
import com.anywhere.campasiliano.models.users.User;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.views.activities.chats.MessageActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private UserItemBinding binding;
    private boolean isChat;
    private String theLastMessage;

    public UserAdapter(Context mContext, List<User> mUsers, boolean isChat) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        Anywhere.activity = (Activity) mContext;
        this.isChat = isChat;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        binding = UserItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.username.setText(user.getFirst_name()+" "+user.getName());
        if(user.getImageUrl().equals("default")) {
            holder.profile_image.setImageResource(R.drawable.logo1);
        }else {
            Glide.with(mContext).load(user.getImageUrl()).into(holder.profile_image);
        }

        if (isChat) {
            lastMessage(user.getUser_id(), holder.last_msg);
        } else {
            holder.last_msg.setVisibility(View.GONE);
        }

        if (isChat) {
            if (user.getStatus().equals("online")) {
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, MessageActivity.class);
            intent.putExtra("uid", user.getUser_id());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profile_image;
        public TextView last_msg;
        public ImageView img_on;
        public ImageView img_off;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            last_msg = itemView.findViewById(R.id.last_msg);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);

        }
    }

    private void lastMessage(String uid, TextView last_msg) {
        theLastMessage = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(uid) || chat.getReceiver().equals(uid) && chat.getSender().equals(firebaseUser.getUid())) {
                        theLastMessage = chat.getMessage();
                    }
                }

                switch (theLastMessage) {
                    case "default":
                        last_msg.setText("No message");
                        break;
                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
