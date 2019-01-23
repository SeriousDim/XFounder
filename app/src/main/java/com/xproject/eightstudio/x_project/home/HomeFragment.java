package com.xproject.eightstudio.x_project.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xproject.eightstudio.x_project.MainActivity;
import com.xproject.eightstudio.x_project.Projects;
import com.xproject.eightstudio.x_project.R;
import com.xproject.eightstudio.x_project.ViewPagerAdapter;
import com.xproject.eightstudio.x_project.dataclasses.Employee;
import com.xproject.eightstudio.x_project.task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class HomeResponse {
    String title;
    String founder;
    ArrayList<Task> tasks;
    ArrayList<Employee> workers;
}

public class HomeFragment extends Fragment {
    ViewPagerAdapter adapter;
    EmployeesFragment emplFragment;
    HomeTaskFragment taskFragment;
    TextView founderName, projectName;
    View view;
    MainActivity activity;
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Projects pro = retrofit.create(Projects.class);

    public void getList(String projectID) {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "getInfo");
        getDataParams.put("projectID", projectID);
        Call<ResponseBody> call = pro.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    HomeResponse resp = gson.fromJson(response.body().string(), HomeResponse.class);
                    emplFragment.setEmployees(resp.workers);
                    taskFragment.setTasks(resp.tasks);
                    founderName.setText("Основатель: " + resp.founder);
                    projectName.setText(resp.title);
                } catch (IOException e) {
                    Log.d("tagged", e.toString());
                }
                activity.setProgress(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getFragmentManager());
        emplFragment = new EmployeesFragment();
        taskFragment = new HomeTaskFragment();
        adapter.addFragment(emplFragment, getResources().getString(R.string.employers_work));
        adapter.addFragment(taskFragment, getResources().getString(R.string.title_tasks));
        viewPager.setAdapter(adapter);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_company_home_opened, container, false);
            ViewPager vp = view.findViewById(R.id.home_pager);
            setupViewPager(vp);
            activity = (MainActivity) getActivity();
            ((TabLayout) view.findViewById(R.id.home_tabs)).setupWithViewPager(vp);
            founderName = view.findViewById(R.id.founder_name);
            projectName = view.findViewById(R.id.project_name);
        }
        return view;
    }
}
