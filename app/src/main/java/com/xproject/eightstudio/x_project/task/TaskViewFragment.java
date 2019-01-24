package com.xproject.eightstudio.x_project.task;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xproject.eightstudio.x_project.MainActivity;
import com.xproject.eightstudio.x_project.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import io.gloxey.cfv.CFButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskViewFragment extends Fragment {
    View view;
    MainActivity activity;
    ArrayAdapter<?> spinAdapter;
    Spinner spin;
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Task task;
    private Tasks tasks = retrofit.create(Tasks.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_full_task, container, false);
            activity = (MainActivity) getActivity();
            getTaskInfo();
            spinAdapter = new ArrayAdapter<String>(getContext(), R.layout.status_item, getContext().getResources().getStringArray(R.array.statuses));
                    //ArrayAdapter.createFromResource(getContext(), R.array.statuses, R.layout.status_item);
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin = view.findViewById(R.id.status);
            spin.setAdapter(spinAdapter);
            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getContext(), "New item selected "+i, Toast.LENGTH_SHORT).show();
                    // save new status here
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        return view;
    }

    public Task getTask() {
        return task;
    }

    public void getTaskInfo() {
        activity.setProgress(true);
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
                    task.title = resp.get("title");
                    ((TextView) view.findViewById(R.id.name)).setText(task.title);
                    ((TextView) view.findViewById(R.id.creator)).setText("Создатель: " + task.name);
                    task.description = resp.get("description");
                    ((TextView) view.findViewById(R.id.description)).setText(task.description);
                    task.date_from = Long.parseLong(resp.get("date_from"));
                    Date date_from = new Date(1000L * task.date_from);
                    task.date_to = Long.parseLong(resp.get("date_to"));
                    Date date_to = new Date(1000L * task.date_to);
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    final String dateFrom = df.format(date_from);
                    final String dateTo = df.format(date_to);
                    ((TextView) view.findViewById(R.id.date_from)).setText("Начало: " + dateFrom);
                    ((TextView) view.findViewById(R.id.date_to)).setText("Конец: " + dateTo);
                    //((TextView) view.findViewById(R.id.status)).setText(new int[]{R.string.pending, R.string.in_progress, R.string.done}[Integer.parseInt(resp.get("status"))]);
                    spin.setSelection(Integer.parseInt(resp.get("status")));
                    activity.setProgress(false);

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