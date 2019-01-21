package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchProjectFragment extends Fragment {
    View view;
    ArrayList<LinkedTreeMap<String, String>> projects = new ArrayList<>();
    private ListView projectsView;
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Projects pro = retrofit.create(Projects.class);


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_find_project, container, false);
            getProjects();
            ((EditText) view.findViewById(R.id.search)).addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    updateList(((EditText) view.findViewById(R.id.search)).getText().toString());
                }
            });
        }
        return view;
    }

    private void updateList(String text) {
        ArrayList<LinkedTreeMap> ar = new ArrayList<>();
        for (LinkedTreeMap hm : projects) {
            if (hm.get("title").toString().startsWith(text)) {
                ar.add(hm);
            }
        }
        ProjectAdapter adapter = new ProjectAdapter(getContext(), ar);
        ((ListView) view.findViewById(R.id.projects)).setAdapter(adapter);

    }

    public void getProjects() {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "getAllProjects");
        Call<ResponseBody> call = pro.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    projects = gson.fromJson(response.body().string(), ArrayList.class);
                    updateList("");
                } catch (IOException e) {
                    try {
                        Log.d("tagged",response.body().string());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
