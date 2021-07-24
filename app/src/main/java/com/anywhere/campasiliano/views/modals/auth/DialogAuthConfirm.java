package com.anywhere.campasiliano.views.modals.auth;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.anywhere.campasiliano.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class DialogAuthConfirm {

    private Context context;
    private Dialog dialog;
    private ProgressBar progressBar;
    private TextInputLayout ed_code;
    private MaterialButton btnConfirm;

    public DialogAuthConfirm(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
        initialize();
    }

    public void initialize() {
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR); //before
        dialog.setContentView(R.layout.layout_auth_confirm_dialog);

        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);

        ed_code = dialog.findViewById(R.id.ed_code);
        btnConfirm = dialog.findViewById(R.id.btn_confirm);
    }

    public TextInputLayout getEd_code() {
        return ed_code;
    }

    public void show(OnCallBack onCallBack) {
        dialog.show();
        btnConfirm.setOnClickListener(v -> {
            onCallBack.onButtonSendClick();
            dialog.dismiss();
        });
    }

    public interface OnCallBack {
        void onButtonSendClick();
    }
}
