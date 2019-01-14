package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.xproject.eightstudio.x_project.dataclasses.TaskClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskPager extends Fragment {
    ViewPagerAdapter viewpager;
    TaskFragment pending, inProgress, done;
    View card;

    String localID = "1";
    String projectID = "2";
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Task task = retrofit.create(Task.class);

    private void setupViewPager(ViewPager viewPager) {
        viewpager = new ViewPagerAdapter(getFragmentManager());
        pending = new TaskFragment();
        inProgress = new TaskFragment();
        done = new TaskFragment();
        viewpager.addFragment(pending, getResources().getString(R.string.pending));
        viewpager.addFragment(inProgress, getResources().getString(R.string.in_progress));
        viewpager.addFragment(done, getResources().getString(R.string.done));
        viewPager.setAdapter(viewpager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getList();
        if (card == null) {
            card = inflater.inflate(R.layout.fragment_task_pager, container, false);
            ViewPager vp = card.findViewById(R.id.v_pager);
            setupViewPager(vp);
            ((TabLayout) card.findViewById(R.id.tabs)).setupWithViewPager(vp);
        }
        return card;
    }

    public void getList() {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "getList");
        getDataParams.put("user_id", localID);
        getDataParams.put("project_id", projectID);
        Call<ResponseBody> call = task.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ArrayList<LinkedTreeMap<String, String>> resp = gson.fromJson(response.body().string(), ArrayList.class);
                    ArrayList<TaskClass> pendingL = new ArrayList<>();
                    ArrayList<TaskClass> progressL = new ArrayList<>();
                    ArrayList<TaskClass> doneL = new ArrayList<>();
                    for (int p = 0; p < resp.size(); p++) {
                        LinkedTreeMap<String, String> currrentTask = resp.get(p);
                        if (currrentTask.get("status").equals("0"))
                            pendingL.add(new TaskClass(currrentTask.get("title"),
                                                       currrentTask.get("name"),
                                                       currrentTask.get("task_id")));
                        else if (currrentTask.get("status").equals("1"))
                            progressL.add(new TaskClass(currrentTask.get("title"),
                                                        currrentTask.get("name"),
                                                        currrentTask.get("task_id")));
                        else if (currrentTask.get("status").equals("2"))
                            doneL.add(new TaskClass(currrentTask.get("title"),
                                                    currrentTask.get("name"),
                                                    currrentTask.get("task_id")));
                    }
                    pending.setTasks(pendingL);
                    inProgress.setTasks(progressL);
                    done.setTasks(doneL);
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
