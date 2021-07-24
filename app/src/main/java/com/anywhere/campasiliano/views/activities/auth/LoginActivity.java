package com.anywhere.campasiliano.views.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.databinding.ActivityLoginBinding;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.utils.anywhere.AnywhereZone;
import com.anywhere.campasiliano.views.activities.HomeActivity;
import com.anywhere.campasiliano.views.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Anywhere.activity = this;
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("please wait ...");
        dialog.setCancelable(false);
        initialize();
    }

    private void initialize(){

        binding.forgotPassword.setOnClickListener(v -> {
            Anywhere.start(ResetPasswordActivity.class);
        });

        binding.btnLogin.setOnClickListener(v -> {
            // validate fields first
            if(validate()) {
                // do something
                String email = AnywhereZone.text(binding.inputLayoutUsername);
                String password = AnywhereZone.text(binding.inputLayoutPassword);
                login(email, password);
            }
        });
    }

    private void login(String email, String password){
        errorClear();
        binding.btnLogin.setOnClickListener(v -> {
            dialog.show();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            dialog.dismiss();
                            SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = userPref.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else {
                            dialog.dismiss();
                            Anywhere.message("Impossible de se connecter");
                        }
                    }).addOnFailureListener(Throwable::printStackTrace);
        });
    }

    private void errorClear() {
        binding.inputLayoutUsername.addOnEditTextAttachedListener(AnywhereZone::onChange);
        binding.inputLayoutPassword.addOnEditTextAttachedListener(AnywhereZone::onChange);
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
        return true;
    }
}