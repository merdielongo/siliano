package com.anywhere.campasiliano.views.activities.auth;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.databinding.ActivityStudentInfoBinding;
import com.anywhere.campasiliano.models.etablishment.Establishment;
import com.anywhere.campasiliano.models.etablishment.Orientation;
import com.anywhere.campasiliano.models.etablishment.Promotion;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.views.activities.MainActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentInfoActivity extends AppCompatActivity {

    private ActivityStudentInfoBinding binding;
    private SharedPreferences userPref;
    private ProgressDialog dialog;
    private CharSequence[] mEstablishmentItems, mPromotionItems, mOrientationItems, mVacationItems;
    private ArrayList<String> mEstablishments, mPromotions, mOrientations, mVacation, key_establishment, key_orientation, key_promotion;
    private String token, establishment, promotion, orientation, vacation, mVacations;
    private String eta_key, promo_key, orien_key;
    private boolean success = false;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference dataReference;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Anywhere.activity = this;
        dialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        //db = FirebaseFirestore.getInstance();
        firebaseUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dialog.setCancelable(false);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        mEstablishments = new ArrayList<>();
        mPromotions = new ArrayList<>();
        key_establishment = new ArrayList<>();
        key_orientation = new ArrayList<>();
        key_promotion = new ArrayList<>();

        getEstablishment();
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initialize() {
        binding.btnEstablishment.setOnClickListener(v -> {
            mEstablishmentItems = mEstablishments.toArray(new String[0]);
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle(getResources().getString(R.string.establishment));
            builder.setBackground(getDrawable(R.drawable.alert_dialog_bg));
            builder.setSingleChoiceItems(mEstablishmentItems, 0, (dialog1, which) -> {
                binding.btnEstablishment.setText(mEstablishmentItems[which]);
                establishment = (String) mEstablishmentItems[which];
                eta_key = key_establishment.get(which);
                getOrientation(eta_key);
                dialog1.dismiss();
            });
            builder.show();
            dialog.dismiss();
        });

        binding.btnOrientation.setOnClickListener(v -> {
            mOrientationItems = mOrientations.toArray(new String[0]);
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle(getResources().getString(R.string.orientation));
            builder.setBackground(getDrawable(R.drawable.alert_dialog_bg));
            builder.setSingleChoiceItems(mOrientationItems, 0, (dialog1, which) -> {
                binding.btnOrientation.setText(mOrientationItems[which]);
                orientation = (String) mOrientationItems[which];
                orien_key = key_orientation.get(which);
                getPromotion(eta_key, orien_key);
                dialog1.dismiss();
            });
            builder.show();
        });

        binding.btnPromotion.setOnClickListener(v -> {
            mPromotionItems = mPromotions.toArray(new String[0]);
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle(getResources().getString(R.string.promotion));
            builder.setBackground(getDrawable(R.drawable.alert_dialog_bg));
            builder.setSingleChoiceItems(mPromotionItems, 0, (dialog1, which) -> {
                binding.btnPromotion.setText(mPromotionItems[which]);
                promotion = (String) mPromotionItems[which];
                promo_key = key_promotion.get(which);
                //getVacation(eta_key, orien_key, promo_key);
                success = true;
                dialog1.dismiss();
            });
            builder.show();
            dialog.dismiss();
        });

        binding.btnVacation.setOnClickListener(v -> {
            mVacationItems = mVacation.toArray(new String[0]);
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle(getResources().getString(R.string.vacation));
            builder.setBackground(getDrawable(R.drawable.alert_dialog_bg));
            builder.setSingleChoiceItems(mVacationItems, 0, (dialog1, which) -> {
                binding.btnVacation.setText(mVacationItems[which]);
                vacation = (String) mVacationItems[which];
                success = true;
                dialog1.dismiss();
            });
            builder.show();
        });

        binding.btnSave.setOnClickListener(this::onClick);
    }

    private void onClick(View v) {
        if (success) {
            student();
        } else {
            Anywhere.message("veuillez selectioner les information concernat votre etablisment");
        }
    }

    private void getEstablishment() {
        dialog.setMessage("please wait ...");
        dialog.show();
        dataReference = database.getReference("establishments");
        dataReference.get().addOnCompleteListener(this::onComplete);
    }

    public void getOrientation(String establishment) {
        dialog.setMessage("please wait ...");
        dialog.show();
        mOrientations = new ArrayList<>();
        dataReference = database.getReference("establishments").child(establishment).child("orientation");
        dataReference.get().addOnCompleteListener(this::onComplete2);
    }

    public void getPromotion(String establishment, String orientation) {
        dialog.setMessage("please wait ...");
        dialog.show();
        mPromotions = new ArrayList<>();
        dataReference = database.getReference("establishments")
                .child(establishment)
                .child("orientation")
                .child(orientation)
                .child("promotions");
        dataReference.get().addOnCompleteListener(this::onComplete3);
    }

    private void getVacation(String establishment, String orientation, String promotion) {
        mVacation = new ArrayList<>();
        
    }

    private void student() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user", firebaseUser.getUid());
        map.put("establishment", eta_key);
        map.put("orientation", orien_key);
        map.put("promotion", promo_key);

        dialog.setCancelable(false);
        dialog.setTitle("Loading ...");
        dialog.setMessage("Please waite ... !");
        dialog.show();

        dataReference = database.getReference("students");
        dataReference.child(firebaseUser.getUid())
                .setValue(map)
                .addOnSuccessListener(this::onSuccess)
                .addOnFailureListener(Throwable::printStackTrace);
    }

    private void onSuccess(Void aVoid) {
        SharedPreferences.Editor  editor = userPref.edit();
        editor.putBoolean("isRegisterIn", false);
        editor.putBoolean("isStudentIn", false);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
        dialog.dismiss();
        Anywhere.start(MainActivity.class);
        finish();
    }

    private void onComplete(Task<DataSnapshot> task) {
        dialog.dismiss();
        if (task.isSuccessful()) {
            dialog.dismiss();
            DataSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    Establishment establishment = document.getValue(Establishment.class);
                    mEstablishments.add(establishment.getName());
                    key_establishment.add(document.getKey());
                }
            } else {
                dialog.dismiss();
            }
            dialog.dismiss();
        }
    }

    private void onComplete2(Task<DataSnapshot> task) {
        dialog.dismiss();
        if (task.isSuccessful()) {
            DataSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    Orientation orientation = document.getValue(Orientation.class);
                    mOrientations.add(orientation.getName());
                    key_orientation.add(document.getKey());
                }
            } else {
                dialog.dismiss();
            }
        }
    }

    private void onComplete3(Task<DataSnapshot> task) {
        if (task.isSuccessful()) {
            dialog.dismiss();
            DataSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    Anywhere.message(document.toString());
                    Promotion promotion = document.getValue(Promotion.class);
                    mPromotions.add(promotion.getName());
                    key_promotion.add(document.getKey());
                }
            } else {
                dialog.dismiss();
            }
        } else {
            dialog.dismiss();
        }
    }
}