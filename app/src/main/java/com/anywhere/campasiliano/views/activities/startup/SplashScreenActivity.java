package com.anywhere.campasiliano.views.activities.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.views.activities.HomeActivity;
import com.anywhere.campasiliano.views.activities.MainActivity;
import com.anywhere.campasiliano.views.activities.auth.StudentInfoActivity;
import com.anywhere.campasiliano.views.activities.auth.UserInfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        Handler handler = new Handler();
        handler.postDelayed(this::run, 3000);
//        if(firebaseUser != null) {
//            new Handler().postDelayed(() -> {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                finish();
//            }, 3000);
//        }else {
//            new Handler().postDelayed(() -> {
//                startActivity(new Intent(getApplicationContext(), WelcomeScreenActivity.class));
//                finish();
//            }, 3000);
//        }
    }

    private void run() {
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean isLoggedIn = userPref.getBoolean("isLoggedIn", false);
        boolean isStudentIn = userPref.getBoolean("isStudentIn", false);
        boolean isRegisterIn = userPref.getBoolean("isRegisterIn", false);
        // isFirstTime();
        if (isLoggedIn) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        } else if (isRegisterIn) {
            startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
            finish();
        } else if (isStudentIn) {
            startActivity(new Intent(getApplicationContext(), StudentInfoActivity.class));
            finish();
        } else {
            startActivity(new Intent(getApplicationContext(), WelcomeScreenActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if(firebaseUser != null) {
//            Intent intent = new Intent(this, HomeActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }
}