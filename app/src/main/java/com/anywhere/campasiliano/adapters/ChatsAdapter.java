package com.anywhere.campasiliano.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.models.chats.Chats;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder>{

    private List<Chats> list;
    private Context context;
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private FirebaseUser firebaseUser;

    public ChatsAdapter(List<Chats> list, Context context) {
        this.list = list;
        this.context = context;
        Anywhere.activity = (Activity) context;
    }

    public void setList(List<Chats> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==MSG_TYPE_LEFT) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatsAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageUser;
        private TextView tv_message;
        private LinearLayout layout_text, layout_image;
        private ImageView imageMessage;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageUser = itemView.findViewById(R.id.image_profile);
            tv_message = itemView.findViewById(R.id.tv_message);
            layout_image = itemView.findViewById(R.id.layout_image);
            layout_text = itemView.findViewById(R.id.layout_text);
            imageMessage = itemView.findViewById(R.id.img_chat);
        }

        void bind(Chats chats) {
            if(chats != null) {
                switch (chats.getType()) {
                    case "TEXT":
                        layout_text.setVisibility(View.VISIBLE);
                        layout_image.setVisibility(View.GONE);
                        tv_message.setText(chats.getTextMessage());
                        break;
                    case "IMAGE":
                        layout_image.setVisibility(View.VISIBLE);
                        layout_text.setVisibility(View.GONE);
                        Glide.with(context).load(chats.getUrl()).into(imageMessage);
                        break;
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(list.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
