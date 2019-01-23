package com.xproject.eightstudio.x_project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateProjectFragment extends Fragment {
    View view;
    EditText title, desc;
    String localID;
    MainActivity activity;
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Projects pro = retrofit.create(Projects.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_create_project, container, false);
            title = view.findViewById(R.id.name);
            desc = view.findViewById(R.id.desc);
            activity = (MainActivity)getActivity();
            localID = activity.loadUser();
            view.findViewById(R.id.create_proj).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addProject();
                }
            });
        }
        return view;
    }

    private void addProject() {
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("command", "addProject");
        postDataParams.put("userID", localID);
        Log.d("tagged",localID);
        postDataParams.put("title", title.getText().toString());
        postDataParams.put("description", desc.getText().toString());

        Call<ResponseBody> call = pro.performPostCall(postDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                activity.getProjectsForNav();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
