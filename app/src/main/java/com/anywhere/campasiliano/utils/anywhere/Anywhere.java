package com.anywhere.campasiliano.utils.anywhere;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Anywhere {

    public static Boolean validate = false;
    public static Boolean ERROR = true;
    public static String MESSAGE;
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;

    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static void message(@NonNull String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }


    public static void start(@NonNull Class<?> aClass) {
        activity.startActivity(new Intent(activity, aClass));
    }



    public static void messageSnack(@NonNull View view, @NonNull String message, @NonNull int duration) {
        Snackbar.make(view, message, duration).show();
    }

    public static void messageSnack(@NonNull View view, @NonNull String message) {
        Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    public static Date DateFromString(String dateToSaved){

        try {
            Date date = format.parse(dateToSaved);
            return date ;
        } catch (ParseException e){
            return null ;
        }

    }

    public static String bitmapToString(Bitmap bitmap) {
        if(bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array, Base64.DEFAULT);
        }
        return "";
    }

    public static void startFragment(FragmentManager fragmentManager, int container, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, fragment).commit();
        fragmentTransaction.addToBackStack(null);
    }

    public static void startFragment(FragmentManager fragmentManager, int container, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, fragment);
        fragmentTransaction.addToBackStack(null);
    }


}
