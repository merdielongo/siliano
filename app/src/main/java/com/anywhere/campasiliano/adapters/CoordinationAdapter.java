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
import com.anywhere.campasiliano.models.etablishment.Coordination;
import com.anywhere.campasiliano.models.users.User;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.utils.anywhere.AnywhereZone;
import com.anywhere.campasiliano.views.modals.DialogViewUserInfo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CoordinationAdapter extends RecyclerView.Adapter<CoordinationAdapter.ViewHolder>{

    private Context mContext;
    private List<Coordination> list;

    public CoordinationAdapter(Context mContext, List<Coordination> list) {
        this.mContext = mContext;
        this.list = list;
        Anywhere.activity = (Activity) mContext;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_coordination_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CoordinationAdapter.ViewHolder holder, int position) {
        Coordination coordination = list.get(position);
        loadUser(
                coordination.getUser(),
                holder.imgCoordinationPhoto,
                holder.txtCoordinationName,
                holder.txtCoordinationFirstName
        );
        holder.txtCoordinationFunction.setText(coordination.getFunction());
        holder.cardCoordination.setOnClickListener(v -> {
            DialogViewUserInfo info = new DialogViewUserInfo(mContext);
            loadUser(coordination.getUser(),
                    info.getImgUserPhoto(),
                    info.getTxtName(),
                    info.getTxtFirstName()
            );
            info.getTxtFunction().setText(coordination.getFunction());
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

        private MaterialCardView cardCoordination;
        private CircleImageView imgCoordinationPhoto;
        private TextView txtCoordinationFunction, txtCoordinationFirstName, txtCoordinationName;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cardCoordination = itemView.findViewById(R.id.cardCoordination);
            imgCoordinationPhoto = itemView.findViewById(R.id.ImgCoordinationPhoto);
            txtCoordinationFunction = itemView.findViewById(R.id.txtCoordinationFunction);
            txtCoordinationFirstName = itemView.findViewById(R.id.txtCoordinationFirstName);
            txtCoordinationName = itemView.findViewById(R.id.txtCoordinationName);
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
