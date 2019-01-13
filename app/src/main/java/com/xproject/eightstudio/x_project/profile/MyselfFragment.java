package com.xproject.eightstudio.x_project.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class MyselfFragment extends Fragment {
    private View mView;
    String workerID = "2";
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    EditText descriptionField;
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Workers work = retrofit.create(Workers.class);

    public void setDescription(String description) {
        descriptionField.setText(description);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null)
            mView = inflater.inflate(R.layout.fragment_myself, container, false);
        descriptionField = mView.findViewById(R.id.about);
        (mView.findViewById(R.id.confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDescription(descriptionField.getText().toString());
            }
        });
        return mView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
