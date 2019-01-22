package com.xproject.eightstudio.x_project.profile;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xproject.eightstudio.x_project.ViewPagerAdapter;
import com.xproject.eightstudio.x_project.Workers;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.xproject.eightstudio.x_project.R;

public class ProfileFragment extends Fragment {

    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    ViewPagerAdapter adapter;
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Workers work = retrofit.create(Workers.class);
    String workerID;
    TextView nameField, jobField;
    View view;
    DescriptionFragment desc;
    GanntFragment chart;
    EditText descriptionField;
    Button confirm;

    private void setupViewPager(ViewPager viewPager) {
        desc = new DescriptionFragment();
        desc.setWorkerID(workerID);
        chart = new GanntFragment();
        adapter.addFragment(desc, getResources().getString(R.string.decsription));
        adapter.addFragment(chart, getResources().getString(R.string.chart));
        viewPager.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_profile_list, container, false);

            adapter = new ViewPagerAdapter(getFragmentManager());
            ViewPager pager = view.findViewById(R.id.profile_pager);
            setupViewPager(pager);
            ((TabLayout)view.findViewById(R.id.prof_tabs)).setupWithViewPager(pager);

            nameField = view.findViewById(R.id.empl_name);
            jobField = view.findViewById(R.id.empl_job);
            descriptionField = desc.getDescriptionField();
            confirm = desc.getConfirm();

            getInfo();
        }

        return view;
    }
    public void setID(String id){
        this.workerID = id;
    }
    private void getInfo() {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("WID", workerID);
        getDataParams.put("command", "getAll");
        Call<ResponseBody> call = work.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    HashMap<String, String> resp = gson.fromJson(response.body().string(), HashMap.class);
                    String name = resp.get("name");
                    String description = resp.get("description");
                    String job = resp.get("job");
                    desc.setDescription(description);
                    nameField.setText(name);
                    jobField.setText(job);
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
