package com.xproject.eightstudio.x_project;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;

public interface Tasks {
    @FormUrlEncoded
    @POST("/task.php")
    Call<ResponseBody> performPostCall(@FieldMap HashMap<String, String> postDataParams);

    @GET("/task.php")
    Call<ResponseBody> performGetCall(@QueryMap HashMap<String, String> getDataParams);
}