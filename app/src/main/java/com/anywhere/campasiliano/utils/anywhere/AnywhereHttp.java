package com.anywhere.campasiliano.utils.anywhere;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class AnywhereHttp {

    private Context context;

    public final int DELETE = 3;
    public final int GET = 0;
    public final int OPTION = 5;
    public final int PATCH = 7;
    public final int POST = 1;
    public final int PUT = 2;

    public AnywhereHttp(Context context) {
        this.context = context;
    }

    public void request(String token, int method, String route, AnResponse anResponse) {
        StringRequest request = new StringRequest(method, route, response -> {
            try {
                anResponse.response(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer "+ token);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public void request(int method, String route, AnResponse anResponse) {
        StringRequest request = new StringRequest(method, route, response -> {
            try {
                anResponse.response(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public void request(String token, int method, String route, HashMap<String, String> params, AnResponse anResponse) {
        StringRequest request = new StringRequest(method, route, response -> {
            try {
                anResponse.response(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace){

            // token authorization

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer "+ token);
                return map;
            }

            // params


            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public void request(int method, String route, HashMap<String, String> params, AnResponse anResponse) {
        StringRequest request = new StringRequest(method, route, response -> {
            try {
                anResponse.response(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace){

            // params

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

}
