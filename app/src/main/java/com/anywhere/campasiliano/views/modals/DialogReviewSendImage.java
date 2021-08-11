package com.anywhere.campasiliano.views.modals;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.anywhere.campasiliano.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DialogReviewSendImage {

    private Context context;
    private Dialog dialog;
    private ProgressBar progressBar;
    private Bitmap bimap;
    ImageView image;
    FloatingActionButton btnSend;

    public DialogReviewSendImage(Context context, Bitmap bimap) {
        this.context = context;
        this.bimap = bimap;
        this.dialog = new Dialog(context);
        initialize();
    }

    public void initialize() {
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR); //before
        dialog.setContentView(R.layout.activity_review_send_image_chat);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);

        image = dialog.findViewById(R.id.imageView);
        btnSend = dialog.findViewById(R.id.btn_send);
    }

    public void show(OnCallBack onCallBack) {
        dialog.show();
        image.setImageBitmap(bimap);
        btnSend.setOnClickListener(v -> {
            onCallBack.onButtonSendClick();
            dialog.dismiss();
        });
    }

    public interface OnCallBack {
        void onButtonSendClick();
    }

}
