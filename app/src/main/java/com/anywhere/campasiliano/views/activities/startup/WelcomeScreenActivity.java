package com.anywhere.campasiliano.views.activities.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.databinding.ActivityWelcomeScreenBinding;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.views.activities.auth.LoginActivity;
import com.anywhere.campasiliano.views.activities.auth.PhoneLoginActivity;
import com.anywhere.campasiliano.views.activities.auth.RegisterActivity;

public class WelcomeScreenActivity extends AppCompatActivity {

    private ActivityWelcomeScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Anywhere.activity = this;
        binding.btnLogin.setOnClickListener(WelcomeScreenActivity::onClickLogin);
        binding.btnRegister.setOnClickListener(WelcomeScreenActivity::onClickRegister);
    }

    private static void onClickLogin(View v) {
        Anywhere.start(LoginActivity.class);
    }
    private static void onClickRegister(View v) {
        Anywhere.start(RegisterActivity.class);
    }
}