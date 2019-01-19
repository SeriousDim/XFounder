package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xproject.eightstudio.x_project.dataclasses.Task;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskViewFragment extends Fragment {
    View view;
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    Task task;
    private Tasks tasks = retrofit.create(Tasks.class);

    private Button btn_edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_full_task, container, false);
            ((TextView) view.findViewById(R.id.name)).setText(task.title);
            ((TextView) view.findViewById(R.id.creator)).setText("Создатель: " + task.name);
            btn_edit = view.findViewById(R.id.btn_edit);
            getTask();
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Hello!",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        return view;
    }

    public void getTask() {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "getTask");
        getDataParams.put("taskID", task.task_id);
        Call<ResponseBody> call = tasks.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    HashMap<String, String> resp = gson.fromJson(response.body().string(), HashMap.class);
                    ((TextView) view.findViewById(R.id.performer)).setText("Исполнитель: " + resp.get("name"));
                    ((TextView) view.findViewById(R.id.description)).setText(resp.get("description"));
                    Date date_from = new Date(1000L * Long.parseLong(resp.get("date_from")));
                    Date date_to = new Date(1000L * Long.parseLong(resp.get("date_to")));
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    final String dateFrom = df.format(date_from);
                    final String dateTo = df.format(date_to);
                    ((TextView) view.findViewById(R.id.date_from)).setText("Начало: " + dateFrom);
                    ((TextView) view.findViewById(R.id.date_to)).setText("Конец: " + dateTo);
                    ((TextView) view.findViewById(R.id.status)).setText(new int[]{R.string.pending, R.string.in_progress, R.string.done}[Integer.parseInt(resp.get("status"))]);

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


    public void setTask(Task task) {
        this.task = task;
    }
}