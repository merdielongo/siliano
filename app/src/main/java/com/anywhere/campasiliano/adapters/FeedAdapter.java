package com.anywhere.campasiliano.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.common.ItemClickListener;
import com.anywhere.campasiliano.models.posts.RssObject;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;


public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder>{

    private RssObject rssObject;
    private Context mContext;
    private LayoutInflater inflater;

    public FeedAdapter(RssObject rssObject, Context mContext) {
        this.rssObject = rssObject;
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        Anywhere.activity = (Activity) mContext;
    }

    @NonNull
    @NotNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_post_items, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FeedViewHolder holder, int position) {
        Glide.with(mContext).load(rssObject.getItems().get(position).getThumbnail()).into(holder.imgPost);
        holder.txtCategory.setText(rssObject.getItems().get(position).getCategories().toString());
        holder.txtTitle.setText(rssObject.getItems().get(position).getTitle());
        holder.txtPostContent.setText(rssObject.getItems().get(position).getDescription());
        holder.txtDate.setText(rssObject.getItems().get(position).getPubDate());
        holder.setItemClickListener((view, position1, isLongClick) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssObject.getItems().get(position).getLink()));
            mContext.startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return rssObject.getItems().size();
    }
}


class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView txtPostContent, txtDate, txtTitle, txtLikes, txtComments;
    public ExtendedFloatingActionButton txtCategory;
    public CircleImageView imgProfile;
    public ImageView imgPost;
    public ImageButton btnPostOption, btnLike, btnComment;
    public MaterialCardView cardPost;
    private ItemClickListener itemClickListener;

    public FeedViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        cardPost = itemView.findViewById(R.id.cardPostOne);
        txtPostContent = itemView.findViewById(R.id.txtPostContent);
        txtDate = itemView.findViewById(R.id.txtPostDate);
        txtCategory = itemView.findViewById(R.id.txtPostCategory);
        txtTitle = itemView.findViewById(R.id.txtPostTitle);
        imgPost = itemView.findViewById(R.id.imgPostPhoto);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    @Override
    public boolean onLongClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), true);
        return true;
    }
}
