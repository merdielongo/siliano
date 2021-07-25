package com.anywhere.campasiliano.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.models.users.Leader;
import com.anywhere.campasiliano.models.users.User;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.utils.anywhere.AnywhereZone;
import com.anywhere.campasiliano.views.modals.DialogViewUserInfo;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LeaderAdapter extends RecyclerView.Adapter<LeaderAdapter.ViewHolder> {

    private Context mContext;
    private List<Leader> list;

    public LeaderAdapter(Context mContext, List<Leader> list) {
        this.mContext = mContext;
        this.list = list;
        Anywhere.activity = (Activity) mContext;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_leader_items, parent, false);
        return new LeaderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LeaderAdapter.ViewHolder holder, int position) {
        Leader leader = list.get(position);
        loadUser(leader.getStudent(),
                holder.imgUserPhoto,
                holder.txtLeaderName,
                holder.txtLeaderFullName
        );
        holder.txtLeaderFunction.setText(leader.getFunction());
        holder.cardLeader.setOnClickListener(v -> {
            DialogViewUserInfo info = new DialogViewUserInfo(mContext);
            loadUser(leader.getStudent(),
                    info.getImgUserPhoto(),
                    info.getTxtName(),
                    info.getTxtFirstName()
            );
            info.getTxtFunction().setText(leader.getFunction());
            info.show(new DialogViewUserInfo.OnCallBack() {
                @Override
                public void onButtonSendClick() {
                    String message = AnywhereZone.text(info.getLayout_message());
                    if(!TextUtils.isEmpty(message)) {
                        //chatService = new ChatService(context, id_receive);
                        //chatService.sendTextMsg(message, "users");
                        info.dismiss();
                    }else {
                        Anywhere.message("ecriver votre message svp");
                    }
                }

                @Override
                public void onButtonAction() {

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardLeader;
        private ImageView imgUserPhoto;
        private TextView txtLeaderName, txtLeaderFullName, txtLeaderFunction;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cardLeader = itemView.findViewById(R.id.cardLeader);
            imgUserPhoto = itemView.findViewById(R.id.imgUserPhoto);
            txtLeaderName = itemView.findViewById(R.id.txtLeaderName);
            txtLeaderFullName = itemView.findViewById(R.id.txtLeaderFullName);
            txtLeaderFunction = itemView.findViewById(R.id.txtLeaderFunction);
        }
    }

    private void loadUser(String uid, ImageView photo, TextView name, TextView firstname) {
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
                    firstname.setText(user.getFirst_name());
                }
            }
        });
    }
}
