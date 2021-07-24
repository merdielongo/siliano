package com.anywhere.campasiliano.views.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.databinding.ActivityCodeValidationBinding;
import com.anywhere.campasiliano.models.users.User;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class CodeValidationActivity extends AppCompatActivity {

    private ActivityCodeValidationBinding binding;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    private FirebaseUser firebaseUser;
    //private FirebaseDatabase database;
    //private DatabaseReference dataReference;
    private static final String TAG = "CodeValidationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCodeValidationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Anywhere.activity = this;
        binding.btnVerify.setOnClickListener(v -> {

        });
    }

}