package com.xproject.eightstudio.x_project.project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.xproject.eightstudio.x_project.main.MainActivity;
import com.xproject.eightstudio.x_project.general.Projects;
import com.xproject.eightstudio.x_project.R;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProjectFragment extends Fragment {
    View view;
    MainActivity activity;
    String localID;
    LinkedTreeMap<String, String> project;
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Projects pro = retrofit.create(Projects.class);

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_company_home_closed, container, false);
            activity = (MainActivity) getActivity();
            view.findViewById(R.id.join).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.GONE);
                    addRequest();
                }
            });
            localID = activity.loadUser();
            activity.setProgress(true);
            getDescription();
        }
        return view;
    }

    private void addRequest() {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "addRequest");
        getDataParams.put("userID", localID);
        getDataParams.put("projectID", project.get("p_id"));
        Call<ResponseBody> call = pro.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                activity.setProgress(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setProject(LinkedTreeMap project) {
        this.project = project;
    }

    public void getDescription() {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "getForeignProject");
        getDataParams.put("projectID", project.get("p_id"));
        Call<ResponseBody> call = pro.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    HashMap<String, String> resp = gson.fromJson(response.body().string(), HashMap.class);
                    ((TextView) view.findViewById(R.id.about)).setText(resp.get("description"));
                    ((TextView) view.findViewById(R.id.project_name)).setText(project.get("title"));
                    ((TextView) view.findViewById(R.id.founder_name)).setText("Основатель: " + project.get("name"));
                    activity.setProgress(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });

    }

}
