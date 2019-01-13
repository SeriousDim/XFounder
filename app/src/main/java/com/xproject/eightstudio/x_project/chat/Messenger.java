package com.xproject.eightstudio.x_project.chat;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


public interface Messenger {
    @FormUrlEncoded
    @POST("/messenger.php")
    Call<ResponseBody> performPostCall(@FieldMap HashMap<String, String> postDataParams);


    @GET("/messenger.php")
    Call<ResponseBody> performGetCall(@QueryMap HashMap<String, String> getDataParams);
}