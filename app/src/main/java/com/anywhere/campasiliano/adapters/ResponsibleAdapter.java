package com.anywhere.campasiliano.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.models.etablishment.Responsibles;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.views.modals.DialogViewResponsibleInfo;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResponsibleAdapter extends RecyclerView.Adapter<ResponsibleAdapter.ViewHolder>{

    private Context mContext;
    private List<Responsibles> list;

    public ResponsibleAdapter(Context mContext, List<Responsibles> list) {
        this.mContext = mContext;
        this.list = list;
        Anywhere.activity = (Activity) mContext;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_responsible_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ResponsibleAdapter.ViewHolder holder, int position) {
        Responsibles responsibles = list.get(position);

        holder.txtResponsibleName.setText(responsibles.getName());
        holder.txtResponsibleFunction.setText(responsibles.getFunction());
        holder.cardResponsible.setOnClickListener(v -> {
            DialogViewResponsibleInfo info = new DialogViewResponsibleInfo(mContext);
            if(responsibles.getPhoto().equals("default")) {
                holder.imgResponsiblePhoto.setImageResource(R.drawable.logo1);
            }else {
                Glide.with(mContext).load(responsibles.getPhoto()).into(info.getImgUserPhoto());
            }
            info.getTxtName().setText(responsibles.getName());
            info.getTxtEmail().setText(responsibles.getMail());
            info.getTxtFunction().setText(responsibles.getFunction());
            info.getTxtDate().setText("online");
            info.show(() -> {
                Anywhere.messageSnack(holder.itemView, "Votre demande a ete envoyer");
            });
        });

        if(responsibles.getPhoto().equals("default")) {
            holder.imgResponsiblePhoto.setImageResource(R.drawable.logo1);
        }else {
            Glide.with(mContext).load(responsibles.getPhoto()).into(holder.imgResponsiblePhoto);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardResponsible;
        private CircleImageView imgResponsiblePhoto;
        private TextView txtResponsibleName;
        private TextView txtResponsibleFunction;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            cardResponsible = itemView.findViewById(R.id.card_responsible);
            imgResponsiblePhoto = itemView.findViewById(R.id.img_responsible_photo);
            txtResponsibleName = itemView.findViewById(R.id.txt_responsible_name);
            txtResponsibleFunction = itemView.findViewById(R.id.txt_responsible_function);
        }
    }
}
