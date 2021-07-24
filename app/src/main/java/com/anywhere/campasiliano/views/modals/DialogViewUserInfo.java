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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogViewUserInfo {

    private Context mContext;
    private Dialog dialog;
    private TextView txtName, txtLastName, txtFirstName, txtSex, txtPhone, txtEmail, txtFunction, txtDate;
    private MaterialButton btnAction;
    private FloatingActionButton btnSend;
    private CircleImageView imgUserPhoto;
    private TextInputLayout layout_message;

    public DialogViewUserInfo(Context mContext) {
        this.mContext = mContext;
        this.dialog = new Dialog(mContext);
        initialize();
    }

    private void initialize() {
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR); //before
        dialog.setContentView(R.layout.layout_dialog_view_coordination_info);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.setCancelable(false);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        txtName = dialog.findViewById(R.id.txt_name);
        txtLastName = dialog.findViewById(R.id.txt_last_name);
        txtFirstName = dialog.findViewById(R.id.txt_first_name);
        txtSex = dialog.findViewById(R.id.txt_sex);
        txtPhone = dialog.findViewById(R.id.txt_phone);
        txtEmail = dialog.findViewById(R.id.txt_email);
        txtFunction = dialog.findViewById(R.id.txt_function);
        txtDate = dialog.findViewById(R.id.txt_date);
        btnAction = dialog.findViewById(R.id.btn_action);
        btnSend = dialog.findViewById(R.id.btn_send);
        layout_message = dialog.findViewById(R.id.ed_message);
        imgUserPhoto = dialog.findViewById(R.id.imgUserPhoto);
    }

    public void show(OnCallBack onCallBack) {
        dialog.show();
        btnAction.setOnClickListener(v -> onCallBack.onButtonAction());
        btnSend.setOnClickListener(v -> onCallBack.onButtonSendClick());
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public interface OnCallBack {
        void onButtonSendClick();
        void onButtonAction();
    }

    public TextView getTxtName() {
        return txtName;
    }

    public TextView getTxtLastName() {
        return txtLastName;
    }

    public TextView getTxtFirstName() {
        return txtFirstName;
    }

    public TextView getTxtSex() {
        return txtSex;
    }

    public TextView getTxtPhone() {
        return txtPhone;
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

    public FloatingActionButton getBtnSend() {
        return btnSend;
    }

    public CircleImageView getImgUserPhoto() {
        return imgUserPhoto;
    }

    public TextInputLayout getLayout_message() {
        return layout_message;
    }
}
