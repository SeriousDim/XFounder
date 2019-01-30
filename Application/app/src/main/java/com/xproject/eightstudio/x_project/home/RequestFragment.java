package com.xproject.eightstudio.x_project.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xproject.eightstudio.x_project.general.Employee;
import com.xproject.eightstudio.x_project.general.Projects;
import com.xproject.eightstudio.x_project.R;
import com.xproject.eightstudio.x_project.main.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RequestResponse {
    ArrayList<Employee> workers;
    boolean can_manage;
}

public class RequestFragment extends Fragment {
    View view;
    RequestAdapter reqAdapter;
    RecyclerView rv;
    ArrayList<Employee> employees;
    MainActivity activity;
    String projectID, localID;
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
            view = inflater.inflate(R.layout.fragment_request_empl, container, false);
            rv = (view.findViewById(R.id.empl_list));
            activity = ((MainActivity) getActivity());
            projectID = activity.loadProject();
            localID = activity.loadUser();
            reqAdapter = new RequestAdapter(getContext());
            employees = new ArrayList<>();
            activity.setProgress(true);
            getRequests();

        }
        return view;
    }

    public void setEmployees() {
        reqAdapter.setEmployees(employees);
        rv.setAdapter(reqAdapter);
    }

    public void getRequests() {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "getRequests");
        getDataParams.put("userID", localID);
        getDataParams.put("projectID", projectID);
        Call<ResponseBody> call = pro.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    RequestResponse resp = gson.fromJson(response.body().string(), RequestResponse.class);
                    employees = resp.workers;
                    setEmployees();
                    activity.setProgress(false);
                } catch (IOException e) {
                    Log.d("tagged", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void deleteRequest(String id, final int pos) {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "deleteJob");
        getDataParams.put("userID", id);
        getDataParams.put("projectID", projectID);
        Call<ResponseBody> call = pro.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                employees.remove(pos);
                setEmployees();
                activity.setProgress(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void acceptRequest(String id, final int pos) {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "acceptRequest");
        getDataParams.put("userID", id);
        getDataParams.put("projectID", projectID);
        Call<ResponseBody> call = pro.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                employees.remove(pos);
                setEmployees();
                activity.setProgress(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
