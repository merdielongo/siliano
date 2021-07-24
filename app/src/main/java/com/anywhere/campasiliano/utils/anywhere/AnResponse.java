package com.anywhere.campasiliano.utils.anywhere;

import com.android.volley.Response;

import org.json.JSONException;

public interface AnResponse extends Response.ErrorListener {

    void response(String response) throws JSONException;

}
