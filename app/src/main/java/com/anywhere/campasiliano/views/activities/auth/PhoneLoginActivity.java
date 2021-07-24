package com.anywhere.campasiliano.views.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.anywhere.campasiliano.databinding.ActivityPhoneLoginBinding;
import com.anywhere.campasiliano.models.users.User;
import com.anywhere.campasiliano.utils.anywhere.Anywhere;
import com.anywhere.campasiliano.utils.anywhere.AnywhereZone;
import com.anywhere.campasiliano.views.modals.auth.DialogAuthConfirm;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    private ActivityPhoneLoginBinding binding;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private ProgressDialog dialog;
    private FirebaseUser firebaseUser;
    //private FirebaseDatabase database;
    //private DatabaseReference dataReference;
    private SharedPreferences userPref;
    private static final String TAG = "PhoneLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Anywhere.activity = this;
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("fr");

        //database = FirebaseDatabase.getInstance();
        //dataReference = database.getReference("users");
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            Anywhere.start(UserInfoActivity.class);
            finish();
        }

        dialog = new ProgressDialog(this);
        binding.btnVerify.setOnClickListener(v -> {
            String t1 = binding.ccp.getSelectedCountryCodeWithPlus();
            String t2 = AnywhereZone.text(binding.edPhoneNumber);
            String phone = t1+t2;
            startPhoneNumberVerification(phone);
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                dialog.dismiss();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                dialog.dismiss();
                DialogAuthConfirm authConfirm = new DialogAuthConfirm(Anywhere.activity);
                authConfirm.show(() -> {
                    String code = AnywhereZone.text(authConfirm.getEd_code());
                    if(!TextUtils.isEmpty(code)) {
                        verifyPhoneNumberWithCode(mVerificationId, code);
                    }
                });
                //Anywhere.start(CodeValidationActivity.class);
                //binding.btnNext.setText("Confirm");
            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        dialog.setMessage("Verification : " + phoneNumber);
        dialog.show();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        dialog.dismiss();
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUserMetadata metadata = mAuth.getCurrentUser().getMetadata();
                        SharedPreferences.Editor editor = userPref.edit();

                        if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                            // The user is new, show them a fancy intro screen!
                            firebaseUser = task.getResult().getUser();
                            if(firebaseUser != null) {
                                String userID = firebaseUser.getUid();
                                User user = new User();
                                user.setUser_id(userID);
                                user.setPhone(firebaseUser.getPhoneNumber());
                                //dataReference.child(userID).setValue(user);
                                Anywhere.start(UserInfoActivity.class);
                                finish();
                            } else {
                                Anywhere.message("Une erreur c'est produit");
                            }
                        } else {
                            // This is an existing user, show them a welcome back screen.
                            // editor.putBoolean("isLoggedIn", true);
                            // editor.apply();
                            Anywhere.start(UserInfoActivity.class);
                            finish();
                        }

                        // Update UI
                    } else {
                        dialog.dismiss();
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                    }
                });
    }
}