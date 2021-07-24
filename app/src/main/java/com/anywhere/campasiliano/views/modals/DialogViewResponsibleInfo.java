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

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogViewResponsibleInfo {

    private Context context;
    private Dialog dialog;
    private TextView txtName, txtEmail, txtFunction, txtDate;
    private MaterialButton btnAction;
    private CircleImageView imgUserPhoto;

    public DialogViewResponsibleInfo(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
        initialize();
    }

    public void initialize() {
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR); //before
        dialog.setContentView(R.layout.layout_dialog_view_responsible_info);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        txtName = dialog.findViewById(R.id.txt_name);
        txtEmail = dialog.findViewById(R.id.txt_email);
        txtFunction = dialog.findViewById(R.id.txt_function);
        txtDate = dialog.findViewById(R.id.txt_date);
        btnAction = dialog.findViewById(R.id.btnAction);
        imgUserPhoto = dialog.findViewById(R.id.imgUserPhoto);
    }

    public void show(OnCallBack onCallBack) {
        dialog.show();
        //imgUserPhoto.setImageBitmap(bitmap);
        btnAction.setOnClickListener(v -> {
            onCallBack.onButtonSendClick();
            dialog.dismiss();
        });
    }

    public interface OnCallBack {
        void onButtonSendClick();
    }

    public TextView getTxtName() {
        return txtName;
    }

    public TextView getTxtEmail() {
        return txtEmail;
    }

    public TextView getTxtFunction() {
        return txtFunction;
    }

    public TextView getTxtDate() {
        return txtDate;
    }

    public MaterialButton getBtnAction() {
        return btnAction;
    }

    public CircleImageView getImgUserPhoto() {
        return imgUserPhoto;
    }
}
