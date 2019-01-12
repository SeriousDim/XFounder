package com.xproject.eightstudio.x_project.chat;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface Messenger {
    @FormUrlEncoded
    @POST("/messenger.php")
    Call<ResponseBody> performPostCall(@FieldMap HashMap<String, String> postDataParams);
}