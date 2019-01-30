package com.xproject.eightstudio.x_project.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xproject.eightstudio.x_project.main.MainActivity;
import com.xproject.eightstudio.x_project.general.Projects;
import com.xproject.eightstudio.x_project.R;
import com.xproject.eightstudio.x_project.general.Employee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class SpinnerResponse {
    ArrayList<Employee> workers;
}


public class TaskCreateFragment extends Fragment {
    MainActivity activity;
    Task task = new Task();
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Tasks tasks = retrofit.create(Tasks.class);
    private Projects pro = retrofit.create(Projects.class);
    String localID;
    String projectID;
    View view;
    TextView from_t, from_d, to_t, to_d;
    EditText task_name, task_desc;
    Spinner sp;
    boolean fT = false, fD = false, tT = false, tD = false;

    Calendar dt_from = Calendar.getInstance();
    Calendar dt_to = Calendar.getInstance();

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return this.task;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_task_create, container, false);
            activity = (MainActivity) getActivity();
            initDateAndTime(view);
            localID = activity.loadUser();
            projectID = activity.loadProject();
            task_desc = view.findViewById(R.id.task_desc);
            task_name = view.findViewById(R.id.task_name);
            sp = view.findViewById(R.id.spinner);
            activity.setProgress(true);
            getWorkers();
            if (task.title != null) {
                fT = true;
                fD = true;
                tT = true;
                tD = true;
                initView();
                view.findViewById(R.id.create_task).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checking()) {
                            activity.setProgress(true);
                            editTask();
                        }
                    }
                });
            } else {
                view.findViewById(R.id.create_task).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checking()) {
                            activity.setProgress(true);
                            createTask();
                        }
                    }
                });
            }

        }
        return view;
    }

    private void editTask() {
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("command", "updateTask");
        postDataParams.put("date_from", (dt_from.getTimeInMillis() / 1000L) + "");
        postDataParams.put("date_to", (dt_to.getTimeInMillis() / 1000L) + "");
        postDataParams.put("title", task_name.getText().toString());
        postDataParams.put("description", task_desc.getText().toString());
        postDataParams.put("performer_id", localID);
        postDataParams.put("project_id", projectID);
        postDataParams.put("task_id", task.task_id);

        Call<ResponseBody> call = tasks.performPostCall(postDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                activity.openTask(task);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), call.request().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView() {
        task_name.setText(task.title);
        task_desc.setText(task.description);
        dt_from.setTimeInMillis(task.date_from * 1000L);
        dt_to.setTimeInMillis(task.date_to * 1000L);
        to_d.setText(dt_to.get(Calendar.DAY_OF_MONTH) + "-" + (dt_to.get(Calendar.MONTH) + 1) + "-" + dt_to.get(Calendar.YEAR));
        to_d.setTextSize(22);
        from_d.setText(dt_from.get(Calendar.DAY_OF_MONTH) + "-" + (dt_from.get(Calendar.MONTH) + 1) + "-" + dt_from.get(Calendar.YEAR));
        from_d.setTextSize(22);
        to_t.setText(dt_to.get(Calendar.HOUR_OF_DAY) + ":" + dt_to.get(Calendar.MINUTE));
        to_t.setTextSize(25);
        from_t.setText(dt_from.get(Calendar.HOUR_OF_DAY) + ":" + dt_from.get(Calendar.MINUTE));
        from_t.setTextSize(25);

    }

    private void createTask() {
        HashMap<String, String> postDataParams = new HashMap<>();
        task.date_from = (dt_from.getTimeInMillis() / 1000L);
        task.date_to = (dt_to.getTimeInMillis() / 1000L);
        task.title = task_name.getText().toString();
        task.description = task_desc.getText().toString();
        task.performer_id = ((Employee) sp.getSelectedItem()).id;
        task.author_id = localID;
        postDataParams.put("command", "createTask");
        postDataParams.put("creator_id", localID);
        postDataParams.put("project_id", projectID);
        postDataParams.put("date_from", task.date_from + "");
        postDataParams.put("date_to", task.date_to + "");
        postDataParams.put("title", task.title);
        postDataParams.put("description", task.description);
        postDataParams.put("performer_id", task.performer_id);


        Call<ResponseBody> call = tasks.performPostCall(postDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                activity.openTask(task);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    private boolean checking() {
        if (task_name.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Пустое название задачи", Toast.LENGTH_SHORT).show();
            return false;
        } else if (task_desc.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Пустое описание", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dt_from.compareTo(dt_to) > -1) {
            Toast.makeText(getContext(), "Срок окончания должен быть позже срока начала", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!fD || !fT || !tD || !tT) {
            Toast.makeText(getContext(), "Не выбрано время", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initDateAndTime(View view) {
        from_d = view.findViewById(R.id.from_date);
        from_t = view.findViewById(R.id.from_time);
        to_d = view.findViewById(R.id.to_date);
        to_t = view.findViewById(R.id.to_time);

        final DatePickerDialog.OnDateSetListener d_from = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dt_from.set(Calendar.YEAR, year);
                dt_from.set(Calendar.MONTH, monthOfYear);
                dt_from.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                from_d.setText((dayOfMonth) + "-" + (monthOfYear + 1) + "-" + year);
                from_d.setTextSize(25);
                fD = true;
            }
        };


        final TimePickerDialog.OnTimeSetListener t_from = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dt_from.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dt_from.set(Calendar.MINUTE, minute);
                from_t.setText(hourOfDay + ":" + minute);
                from_t.setTextSize(25);
                fT = true;
            }
        };

        final TimePickerDialog.OnTimeSetListener t_to = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dt_to.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dt_to.set(Calendar.MINUTE, minute);
                to_t.setText(hourOfDay + ":" + minute);
                to_t.setTextSize(22);
                tT = true;
            }
        };

        final DatePickerDialog.OnDateSetListener d_to = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dt_to.set(Calendar.YEAR, year);
                dt_to.set(Calendar.MONTH, monthOfYear);
                dt_to.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                to_d.setText((dayOfMonth) + "-" + (monthOfYear + 1) + "-" + year);
                to_d.setTextSize(22);
                tD = true;
            }
        };

        from_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), d_from,
                        dt_from.get(Calendar.YEAR),
                        dt_from.get(Calendar.MONTH),
                        dt_from.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
        from_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(getContext(), t_from,
                        dt_from.get(Calendar.HOUR_OF_DAY),
                        dt_from.get(Calendar.MINUTE), true)
                        .show();
            }
        });
        to_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), d_to,
                        dt_to.get(Calendar.YEAR),
                        dt_to.get(Calendar.MONTH),
                        dt_to.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
        to_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(getContext(), t_to,
                        dt_to.get(Calendar.HOUR_OF_DAY),
                        dt_to.get(Calendar.MINUTE), true)
                        .show();
            }
        });
    }

    public void getWorkers() {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "getWorkers");
        getDataParams.put("userID", localID);
        getDataParams.put("projectID", projectID);
        Call<ResponseBody> call = pro.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    SpinnerResponse resp = gson.fromJson(response.body().string(), SpinnerResponse.class);
                    SpinAdapter spa = new SpinAdapter(getActivity(), resp.workers);
                    sp.setAdapter(spa);
                    activity.setProgress(false);
                } catch (IOException e) {
                    Log.d("tagged", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
