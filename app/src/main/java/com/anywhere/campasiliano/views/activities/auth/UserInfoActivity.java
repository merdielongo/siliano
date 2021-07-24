package com.anywhere.campasiliano.views.activities.auth;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.databinding.ActivityUserInfoBinding;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.utils.anywhere.AnywhereZone;
import com.anywhere.campasiliano.views.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class UserInfoActivity extends AppCompatActivity {


    private ActivityUserInfoBinding binding;
    private SharedPreferences userPref;
    private ProgressDialog dialog;
    private CharSequence[] mSexItems;
    private String sex;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference dataReference;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Anywhere.activity = this;
        dialog = new ProgressDialog(this);
        mSexItems = new CharSequence[]{"homme", "femme"};

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dataReference = database.getReference("users");

        dialog.setCancelable(false);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initialize() {
        errorClear();
        binding.btnSex.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle(getResources().getString(R.string.select_sex));
            builder.setBackground(getDrawable(R.drawable.alert_dialog_bg));
            builder.setSingleChoiceItems(mSexItems, 0, (dialog, which) -> {
                // Do Something here
                binding.btnSex.setText(mSexItems[which]);
                sex = (String) mSexItems[which];
                dialog.dismiss();
            });
            builder.show();
        });

        binding.btnSave.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (validate()) {
                    saveUserInfo();
                }
            }
        });
    }
    private void saveUserInfo() {
        String name = AnywhereZone.text(binding.inputLayoutName);
        String last_name = AnywhereZone.text(binding.inputLayoutLastName);
        String first_name = AnywhereZone.text(binding.inputLayoutFirstName);
        String sex = (String) this.sex;

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("last_name", last_name);
        map.put("first_name", first_name);
        map.put("sex", sex);

        dialog.setCancelable(false);
        dialog.setTitle("Enregistrement");
        dialog.setMessage("veuillez patienter !");
        dialog.show();

        dataReference.child(firebaseUser.getUid())
                .updateChildren(map)
                .addOnSuccessListener(this::onSuccess)
                .addOnFailureListener(Throwable::printStackTrace);

    }

    private void onSuccess(Void aVoid) {
        SharedPreferences.Editor  editor = userPref.edit();
        editor.putBoolean("isRegisterIn", false);
        editor.putBoolean("isStudentIn", true);
        editor.apply();
        Anywhere.start(StudentInfoActivity.class);
        finish();
    }

    private void errorClear() {
        binding.inputLayoutName.addOnEditTextAttachedListener(AnywhereZone::onChange);
        binding.inputLayoutLastName.addOnEditTextAttachedListener(AnywhereZone::onChange);
        binding.inputLayoutFirstName.addOnEditTextAttachedListener(AnywhereZone::onChange);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean validate() {
        if(AnywhereZone.isEmpty(binding.inputLayoutName)) {
            AnywhereZone.empty(binding.inputLayoutName, "Entre votre nom svp !");
            return false;
        }

        if(AnywhereZone.isEmpty(binding.inputLayoutLastName)) {
            AnywhereZone.empty(binding.inputLayoutLastName, "Entre votre postnom svp !");
            return false;
        }

        if(AnywhereZone.isEmpty(binding.inputLayoutFirstName)) {
            AnywhereZone.empty(binding.inputLayoutFirstName, "Entre votre prenom svp !");
            return false;
        }

        if(AnywhereZone.compareTo(binding.btnSex.getText().toString(), "Sexe")) {
            binding.btnSex.setBackgroundColor(getColor(R.color.design_default_color_primary_dark));
            Anywhere.message("Veuillez selectionner votre sexe");
            return false;
        }
        return true;
    }
}