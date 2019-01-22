package com.xproject.eightstudio.x_project.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xproject.eightstudio.x_project.MainActivity;
import com.xproject.eightstudio.x_project.Projects;
import com.xproject.eightstudio.x_project.R;
import com.xproject.eightstudio.x_project.dataclasses.Employee;

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
    Task task;
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Tasks tasks = retrofit.create(Tasks.class);
    private Projects pro = retrofit.create(Projects.class);
    String localID;
    String projectID = "1";
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_task_create, container, false);
            initDateAndTime(view);
            localID = ((MainActivity) getActivity()).loadUser();
            //projectID =  ((MainActivity)getActivity()).loadProject();
            task_desc = view.findViewById(R.id.task_desc);
            task_name = view.findViewById(R.id.task_name);
            sp = view.findViewById(R.id.spinner);
            getWorkers();
            view.findViewById(R.id.create_task).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checking()) {
                        createTask();
                    }
                }
            });

        }
        return view;
    }

    private void createTask() {
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("command", "createTask");
        postDataParams.put("creator_id", localID);
        postDataParams.put("project_id", projectID);
        postDataParams.put("date_from", (Long) (dt_from.getTimeInMillis() / 1000L) + "");
        postDataParams.put("date_to", (Long) (dt_to.getTimeInMillis() / 1000L) + "");
        postDataParams.put("title", task_name.getText().toString());
        postDataParams.put("description", task_desc.getText().toString());
        postDataParams.put("performer_id", ((Employee) sp.getSelectedItem()).id);

        Call<ResponseBody> call = tasks.performPostCall(postDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                MainActivity m = (MainActivity) getActivity();
                m.setFragment(R.id.navigation_task);
                m.updateTasks();
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
