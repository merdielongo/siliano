package com.anywhere.campasiliano.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.databinding.ActivityHomeBinding;
import com.anywhere.campasiliano.menu.CourFragment;
import com.anywhere.campasiliano.menu.EventFragment;
import com.anywhere.campasiliano.menu.HomeFragment;
import com.anywhere.campasiliano.menu.NewsFragment;
import com.anywhere.campasiliano.models.users.User;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.views.activities.chats.ChatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private BottomAppBar mBottomAppBar;
    private BottomSheetDialog bottomSheetDialog;
    private View bottomSheetView;
    private SharedPreferences userPref;
    private FirebaseDatabase database;
    private DatabaseReference dataReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Anywhere.activity = this;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dataReference = database.getReference("users").child(firebaseUser.getUid());

        bottomSheetDialog = new BottomSheetDialog(this, R.style.bottomSheetDialogTheme);
        bottomSheetView = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet, (LinearLayout) findViewById(R.id.bottomSheetContainer));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameHomeContainer, new HomeFragment()).commit();

        binding.bottomApp.setNavigationOnClickListener(v -> {

            dataReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot document = task.getResult();
                    if(document.exists()) {
                        User user = document.getValue(User.class);
                        TextView txtMat = bottomSheetView.findViewById(R.id.txtMat);
                        TextView txtName = bottomSheetView.findViewById(R.id.txtName);
                        TextView txtLastName = bottomSheetView.findViewById(R.id.txtLastName);
                        CircleImageView imgUserProfile = bottomSheetView.findViewById(R.id.imgUserPhoto);


                        txtMat.setText(user.getMail());
                        txtName.setText(user.getFirst_name());
                        txtLastName.setText(user.getName());
                        if (user.getImageUrl().equals("default")) {
                            imgUserProfile.setImageResource(R.drawable.logo1);
                        } else {
                            Glide.with(this).load(user.getImageUrl()).into(imgUserProfile);
                        }
                    }
                }
            });

            bottomClick();
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });
    }


    private void bottomClick() {

        binding.btnMessage.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            Anywhere.start(ChatActivity.class);
        });

        bottomSheetView.findViewById(R.id.card_account).setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameHomeContainer, new HomeFragment()).commit();
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.card_event).setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameHomeContainer, new EventFragment()).commit();
            fragmentTransaction.addToBackStack(null);
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.card_lesson).setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setMessage("Aucun cour n'a ete publier");
            builder.show();
            //FragmentManager fragmentManager = getSupportFragmentManager();
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.replace(R.id.frameHomeContainer, new CourFragment()).commit();
            //fragmentTransaction.addToBackStack(null);
            //bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.card_news).setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameHomeContainer, new NewsFragment()).commit();
            fragmentTransaction.addToBackStack(null);
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.card_share).setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setMessage("Sorry cette option ne pas enore disponible");
            builder.show();
            //FragmentManager fragmentManager = getSupportFragmentManager();
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.replace(R.id.frameHomeContainer, new ShareFragment()).commit();
            //fragmentTransaction.addToBackStack(null);
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.card_setting).setOnClickListener(v -> {
            //Anywhere.start(SettingsActivity.class);
        });
    }
}