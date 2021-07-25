package com.anywhere.campasiliano.views.modals;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.anywhere.campasiliano.R;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

public class DialogViewGroupInfo {

    private Context context;
    private Dialog dialog;
    private TextView txtNames, txtEmail, txtPhone, txtDesc, txtName;
    private RoundedImageView imgUserPhoto;
    private MaterialButton btnAction;

    public DialogViewGroupInfo(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
        initialize();
    }

    public void initialize() {
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR); //before
        dialog.setContentView(R.layout.layout_dialog_view_group_info);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        txtName = dialog.findViewById(R.id.txt_name);
        txtNames = dialog.findViewById(R.id.txt_names);
        txtEmail = dialog.findViewById(R.id.txt_email);
        txtPhone = dialog.findViewById(R.id.txt_phone);
        txtDesc = dialog.findViewById(R.id.txt_desc);
        btnAction = dialog.findViewById(R.id.btn_action);
        imgUserPhoto = dialog.findViewById(R.id.imgUserPhoto);
    }

    public TextView getTxtNames() {
        return txtNames;
    }

    public TextView getTxtName() {
        return txtName;
    }

    public TextView getTxtEmail() {
        return txtEmail;
    }

    public TextView getTxtPhone() {
        return txtPhone;
    }

    public TextView getTxtDesc() {
        return txtDesc;
    }

    public RoundedImageView getImgUserPhoto() {
        return imgUserPhoto;
    }

    public void show(OnCallBack onCallBack) {
        dialog.show();
        //imgUserPhoto.setImageBitmap(bitmap);
        btnAction.setOnClickListener(v -> {
            onCallBack.onButtonAction();
            dialog.dismiss();
        });
    }

    public interface OnCallBack {
        void onButtonAction();
    }

}
