package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginFragment extends Fragment {
    View view;
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Profile profile = retrofit.create(Profile.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_login_page, container, false);
            Button reg = view.findViewById(R.id.btn_reg);
            reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity m  = (MainActivity) getActivity();
                    m.setFragmentClass(new RegistationFragment());
                    m.lastFragment = 6;
                    m.currentFragment = 6;
                }
            });
            Button log = view.findViewById(R.id.btn_login);
            final EditText login = view.findViewById(R.id.edit_login);
            final EditText password = view.findViewById(R.id.edit_pass);
            log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login(login.getText().toString(), password.getText().toString());
                }
            });
        }
        return view;
    }

    public void login(String login, String password) {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "login");
        getDataParams.put("login", login);
        getDataParams.put("password", password);
        Call<ResponseBody> call = profile.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    HashMap<String,String> resp = gson.fromJson(response.body().string(), HashMap.class);
                    if (resp.get("success").equals("good")){
                        MainActivity m = (MainActivity) getActivity();
                        m.saveUser(resp.get("user_id"));
                        m.loginSuccess();
                    }
                    else{
                        Toast.makeText(getContext(), "Wrong pass or login", Toast.LENGTH_LONG).show();
                    }
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
