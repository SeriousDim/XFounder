package com.xproject.eightstudio.x_project.task;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xproject.eightstudio.x_project.main.MainActivity;
import com.xproject.eightstudio.x_project.R;
import com.xproject.eightstudio.x_project.general.ViewPagerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class TaskResponse {
    ArrayList<Task> tasks;
}

public class TaskPager extends Fragment {
    ViewPagerAdapter viewpager;
    TaskFragment pending, inProgress, done;
    MainActivity activity;
    View view;
    String localID;
    String projectID;
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Tasks tasks = retrofit.create(Tasks.class);

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_task_pager, container, false);
            ViewPager vp = view.findViewById(R.id.v_pager);
            activity = ((MainActivity) getActivity());
            getList();
            setupViewPager(vp);
            ((TabLayout) view.findViewById(R.id.my_tabs)).setupWithViewPager(vp);

            view.findViewById(R.id.add_task).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) inflater.getContext()).addTask();
                }
            });
        }
        return view;
    }


    public void getList() {
        activity.setProgress(true);
        projectID = activity.loadProject();
        localID = activity.loadUser();
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "getList");
        getDataParams.put("user_id", localID);
        getDataParams.put("project_id", projectID);
        Call<ResponseBody> call = tasks.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    TaskResponse resp = gson.fromJson(response.body().string(), TaskResponse.class);
                    ArrayList<Task> pendingL = new ArrayList<>();
                    ArrayList<Task> progressL = new ArrayList<>();
                    ArrayList<Task> doneL = new ArrayList<>();
                    for (int p = 0; p < resp.tasks.size(); p++) {
                        Task currrentTask = resp.tasks.get(p);
                        switch (currrentTask.status){
                            case "0":
                                pendingL.add(currrentTask);
                                break;
                            case "1":
                                progressL.add(currrentTask);
                                break;
                            case "2":
                                doneL.add(currrentTask);
                                break;
                        }
                    }
                    pending.setTasks(pendingL);
                    inProgress.setTasks(progressL);
                    done.setTasks(doneL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                activity.setProgress(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                activity.setProgress(false);
            }
        });
    }
}
