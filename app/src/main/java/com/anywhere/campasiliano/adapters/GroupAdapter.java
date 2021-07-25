package com.anywhere.campasiliano.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.models.chats.Group;
import com.anywhere.campasiliano.models.users.User;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.views.modals.DialogViewGroupInfo;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>{

    private Context mContext;
    private List<Group> list;

    public GroupAdapter(Context mContext, List<Group> list) {
        this.mContext = mContext;
        this.list = list;
    }

    private static void onButtonAction() {
        Anywhere.message("votre demande a ete envoyer");
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_group_items, parent, false);
        return new GroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GroupAdapter.ViewHolder holder, int position) {
        Group group = list.get(position);
        loadUser(group.getUser(),
                holder.imgUserPhoto,
                holder.groupLeaderName,
                holder.groupLeaderEmail
        );
        holder.groupName.setText(group.getName());
        holder.cardGroup.setOnClickListener(v -> {
            DialogViewGroupInfo info = new DialogViewGroupInfo(mContext);
            loadUser(group.getUser(),
                    info.getImgUserPhoto(),
                    info.getTxtName(),
                    info.getTxtEmail()
            );
            info.getTxtName().setText(group.getName());
            info.getTxtDesc().setText(group.getDesc());
            info.show(GroupAdapter::onButtonAction);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardGroup;
        private ImageView imgGroup;
        private TextView groupLeaderName, groupLeaderEmail, groupName;
        private CircleImageView imgUserPhoto;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cardGroup = itemView.findViewById(R.id.cardGroup);
            imgGroup = itemView.findViewById(R.id.imgGroup);
            groupLeaderName = itemView.findViewById(R.id.groupLeaderName);
            groupLeaderEmail = itemView.findViewById(R.id.groupLeaderEmail);
            imgUserPhoto = itemView.findViewById(R.id.imgUserPhoto);
            groupName = itemView.findViewById(R.id.groupName);
        }
    }

    private void loadUser(String uid, ImageView photo, TextView name, TextView email) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(uid);
        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;
                    if(user.getImageUrl().equals("default")) {
                        photo.setImageResource(R.drawable.logo1);
                    }else {
                        Glide.with(mContext).load(user.getImageUrl()).into(photo);
                    }
                    name.setText(user.getName());
                    email.setText(user.getMail());
                }
            }
        });
    }
}
