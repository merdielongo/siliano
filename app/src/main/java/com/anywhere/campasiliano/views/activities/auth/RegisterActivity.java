package com.anywhere.campasiliano.views.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.databinding.ActivityRegisterBinding;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.utils.anywhere.AnywhereZone;
import com.anywhere.campasiliano.views.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference dataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Anywhere.activity = this;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dataReference = database.getReference("users");
        initialize();
    }

    private void initialize() {
        errorClear();
        binding.btnRegister.setOnClickListener(v -> {
            // validate fields first
            if(validate()) {
                // do something
                String password = AnywhereZone.text(binding.inputLayoutPassword);
                String confirm = AnywhereZone.text(binding.inputLayoutRepeatPassword);
                String email = AnywhereZone.text(binding.inputLayoutUsername);

                if(AnywhereZone.compareTo(password, confirm)) {
                    register(email, password);
                }else {
                    AnywhereZone.error(binding.inputLayoutRepeatPassword, "Les deux mot de passe ne sont pas identique");
                }
            }
        });
    }

    private void errorClear() {
        binding.inputLayoutUsername.addOnEditTextAttachedListener(AnywhereZone::onChange);
        binding.inputLayoutPassword.addOnEditTextAttachedListener(AnywhereZone::onChange);
        binding.inputLayoutRepeatPassword.addOnEditTextAttachedListener(AnywhereZone::onChange);
    }


    private boolean validate() {
        if(AnywhereZone.isEmpty(binding.inputLayoutUsername)) {
            AnywhereZone.empty(binding.inputLayoutUsername, "Entre votre e-mail svp !");
            return false;
        }

        if(AnywhereZone.isEmpty(binding.inputLayoutPassword)) {
            AnywhereZone.empty(binding.inputLayoutPassword, "Entre votre un mot de passe !");
            return false;
        }

        if(AnywhereZone.isEmpty(binding.inputLayoutRepeatPassword)) {
            AnywhereZone.empty(binding.inputLayoutRepeatPassword, "Entre votre un mot de passe !");
            return false;
        }

        return true;
    }

    private void register(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        String uid = firebaseUser.getUid();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("user_id", uid);
                        hashMap.put("mail", email);
                        hashMap.put("imageUrl", "default");
                        hashMap.put("status", "offline");
                        dataReference.child(uid).setValue(hashMap).addOnCompleteListener(this::onComplete);

                    }else {
                        Anywhere.message("Adresse email ou mot de passe incorrect");
                    }
                });
    }

    private void onComplete(Task<Void> task) {
        if (task.isSuccessful()) {
            SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = userPref.edit();
            editor.putBoolean("isRegisterIn", true);
            editor.apply();
            Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}