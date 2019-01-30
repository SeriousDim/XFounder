package com.xproject.eightstudio.x_project.general;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface Workers {
    @GET("/workers.php")
    Call<ResponseBody> performGetCall(@QueryMap HashMap<String, String> getDataParams);
}