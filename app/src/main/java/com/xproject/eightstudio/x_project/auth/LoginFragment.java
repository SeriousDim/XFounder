package com.xproject.eightstudio.x_project.auth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xproject.eightstudio.x_project.R;
import com.xproject.eightstudio.x_project.main.MainActivity;

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
    MainActivity activity;
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
            activity = (MainActivity) getActivity();
            reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.setFragmentClass(new RegistationFragment());
                    activity.currentFragment = 14;
                }
            });
            Button log = view.findViewById(R.id.btn_login);
            final EditText login = view.findViewById(R.id.edit_login);
            final EditText password = view.findViewById(R.id.edit_pass);
            log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String lg = login.getText().toString(), ps = password.getText().toString();
                    if (checking(lg, ps)) {
                        activity.setProgress(true);
                        login(lg, ps);
                    } else {
                        Toast.makeText(getContext(), "Неправильный логин или пароль", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        return view;
    }

    private boolean checking(String login, String password) {
        if (login.contains(" ") || password.contains(" ")){
            return false;
        }
        return true;
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
                    HashMap<String, String> resp = gson.fromJson(response.body().string(), HashMap.class);
                    if (resp.get("success").equals("good")) {
                        MainActivity m = (MainActivity) getActivity();
                        m.saveUser(resp.get("user_id"));
                        m.loginSuccess();
                    } else {
                        activity.setProgress(false);
                        Toast.makeText(getContext(), "Неправильный логин или пароль", Toast.LENGTH_LONG).show();
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
