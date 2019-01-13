package com.xproject.eightstudio.x_project.profile;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
public class ProfileListFragment extends Fragment {

    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Workers work = retrofit.create(Workers.class);
    String workerID="2";
    ViewPagerAdapter view;
    TextView nameField;
    View card;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupViewPager(ViewPager viewPager) {
        view = new ViewPagerAdapter(getFragmentManager());
        view.addFragment(new MyselfFragment(), getResources().getString(R.string.about_me));
        view.addFragment(new TaskFragment(), getResources().getString(R.string.tasks));
        viewPager.setAdapter(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (card == null) {
            card = inflater.inflate(R.layout.fragment_profile_list, container, false);
            ViewPager vp = card.findViewById(R.id.v_pager);
            setupViewPager(vp);
            getInfo(workerID);
            nameField = card.findViewById(R.id.empl_name);
            ((TabLayout) card.findViewById(R.id.tabs)).setupWithViewPager(vp);
        }

        return card;
    }
    private void getInfo(String WID){
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("WID", WID);
        getDataParams.put("command", "getAll");
        Call<ResponseBody> call = work.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    HashMap<String, String> resp = gson.fromJson(response.body().string(), HashMap.class);
                    String name = resp.get("name");
                    String description = resp.get("description");
                    MyselfFragment frag = (MyselfFragment) view.getItem(0);
                    frag.setDescription(description);
                    nameField.setText(name);
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
    @Override
    public void onDetach() {
        super.onDetach();
    }
}
