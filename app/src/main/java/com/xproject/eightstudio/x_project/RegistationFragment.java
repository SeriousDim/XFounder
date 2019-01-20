package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xproject.eightstudio.x_project.chat.Message;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistationFragment extends Fragment {
    View view;
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Profile profile = retrofit.create(Profile.class);
    EditText editName, editJob, editDesc, editSurname, editLogin, editPass;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reg_page, container, false);
        editName = view.findViewById(R.id.reg_name);
        editJob = view.findViewById(R.id.reg_job);
        editDesc = view.findViewById(R.id.reg_desc);
        editSurname = view.findViewById(R.id.reg_lastname);
        editLogin = view.findViewById(R.id.reg_login);
        editPass = view.findViewById(R.id.reg_pass);
        view.findViewById(R.id.btn_reg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        return view;
    }

    public void signUp() {
        if (checking()) {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("command", "signUp");
            postDataParams.put("name", editName.getText().toString() + " " + editSurname.getText().toString());
            postDataParams.put("description", editDesc.getText().toString());
            postDataParams.put("job", editJob.getText().toString());
            postDataParams.put("login", editLogin.getText().toString());
            postDataParams.put("password", editPass.getText().toString());
            Call<ResponseBody> call = profile.performPostCall(postDataParams);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        HashMap<String, String> resp = gson.fromJson(response.body().string(), HashMap.class);
                        if (resp.get("success").equals("good")) {
                            MainActivity m = (MainActivity) getActivity();
                            m.saveUser(resp.get("user_id"));
                            m.loginSuccess();
                        } else if (resp.get("success").equals("not unique")) {
                            Toast.makeText(getContext(), "Login is not unique", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean checking() {
        if (editName.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Пустое имя", Toast.LENGTH_SHORT).show();
            return false;
        } else if (editSurname.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Пустая фамилия", Toast.LENGTH_SHORT).show();
            return false;
        } else if (editDesc.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Пустое описание", Toast.LENGTH_SHORT).show();
            return false;
        } else if (editJob.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Пустая профессия", Toast.LENGTH_SHORT).show();
            return false;
        } else if (editLogin.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Пустой логин", Toast.LENGTH_SHORT).show();
            return false;
        } else if (editPass.getText().toString().length()<8) {
            Toast.makeText(getContext(), "Слишком короткий пароль", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
