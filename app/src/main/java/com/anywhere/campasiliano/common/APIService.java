package com.anywhere.campasiliano.common;

import com.anywhere.campasiliano.notifications.MyResponse;
import com.anywhere.campasiliano.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAABStlqVw:APA91bFmNBKyiFD8rlVvQP-AFnaCUf0FPSlxFSeXYFoglFT8A2JwB7g5yW1YhrYVzrnBIwHOQO-YcsCxcpXhMa7VZLV2E16SqVG3bcXGT7p8CTDJSedYglfmPMDSMiN1XHqvucXc1EVI"
        }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
