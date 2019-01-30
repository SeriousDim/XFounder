package com.xproject.eightstudio.x_project.profile;

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
import com.xproject.eightstudio.x_project.general.Workers;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DescriptionFragment extends Fragment {
    View view;
    EditText descriptionField;
    Button confirm;
    String description;
    private final String server = "https://gleb2700.000webhostapp.com";
    String workerID;
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Workers work = retrofit.create(Workers.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_description, container, false);
            descriptionField = view.findViewById(R.id.about);
            confirm = view.findViewById(R.id.confirm);
            descriptionField.setText(description);

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateDescription(descriptionField.getText().toString());
                }
            });
        }
        return view;
    }

    public void setWorkerID(String id){
        this.workerID = id;
    }

    private void updateDescription(String description) {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "updateDescription");
        getDataParams.put("WID", workerID);
        getDataParams.put("description", description);
        Call<ResponseBody> call = work.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    HashMap<String, String> resp = gson.fromJson(response.body().string(), HashMap.class);
                    if (!resp.get("success").equals("good"))
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error1", Toast.LENGTH_LONG).show();
            }
        });
    }

    public EditText getDescriptionField() {
        return this.descriptionField;
    }

    public Button getConfirm() {
        return this.confirm;
    }

    public void setDescription(String str) {
        descriptionField.setText(str);
    }
}
