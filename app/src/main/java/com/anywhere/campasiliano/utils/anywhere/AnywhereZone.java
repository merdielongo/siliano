package com.anywhere.campasiliano.utils.anywhere;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class AnywhereZone extends Anywhere{

    public static void onChange(TextInputLayout inputLayout) {
        Objects.requireNonNull(inputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static void empty(TextInputLayout inputLayout, String message) {
        String content = Objects.requireNonNull(inputLayout.getEditText()).getText().toString().trim();
        if(content.equals("")){
            inputLayout.setError(message);
            inputLayout.setErrorEnabled(true);
        }else {

        }
    }


    public static boolean isEmpty(TextInputLayout inputLayout) {
        String content = Objects.requireNonNull(inputLayout.getEditText()).getText().toString().trim();
        return content.isEmpty();
    }



    public static void error(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
    }

    public static String text(TextInputLayout inputLayout) {
        return Objects.requireNonNull(inputLayout.getEditText()).getText().toString().trim();
    }

    public static void clear(TextInputLayout textInputLayout) {
        textInputLayout.getEditText().getText().clear();
    }

    public static boolean compareTo(Object objects, String content) {
        return content.equals(objects);
    }

    public static void filter(Object objects, String content, String message) {
        if(objects.equals(content)) {
            MESSAGE = message;
            ERROR = true;
        }else ERROR = false;
    }

    public static void filter(Object objects, String content) {
        ERROR = objects.equals(content);
    }

    public static void textDefaultInput(TextInputLayout editText, String text) {
        Objects.requireNonNull(editText.getEditText()).setText(text);
    }

}
