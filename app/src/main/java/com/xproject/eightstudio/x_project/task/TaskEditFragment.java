package com.xproject.eightstudio.x_project.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xproject.eightstudio.x_project.MainActivity;
import com.xproject.eightstudio.x_project.R;

import java.util.Calendar;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskEditFragment extends Fragment {
    private final String server = "https://gleb2700.000webhostapp.com";
    Task task;
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Tasks tasks = retrofit.create(Tasks.class);
    String localID = "1";
    String projectID = "2";
    View view;
    TextView from_t, from_d, to_t, to_d;
    EditText task_name, task_desc;

    Calendar dt_from = Calendar.getInstance();
    Calendar dt_to = Calendar.getInstance();

    public void setTask(Task task) {
        this.task = task;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_task_edit, container, false);
            from_d = view.findViewById(R.id.from_date);
            from_t = view.findViewById(R.id.from_time);
            to_d = view.findViewById(R.id.to_date);
            to_t = view.findViewById(R.id.to_time);
            task_desc = view.findViewById(R.id.task_desc);
            task_name = view.findViewById(R.id.task_name);
            initView();
            initDateAndTime(view);
            view.findViewById(R.id.save_task).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checking()) {
                        editTask();
                    }
                }
            });

        }
        return view;
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
                MainActivity m = (MainActivity) getActivity();
                m.openTask(task);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), call.request().toString(), Toast.LENGTH_LONG).show();
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
        }
        return true;
    }

    private void initDateAndTime(View view) {
        final DatePickerDialog.OnDateSetListener d_from = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dt_from.set(Calendar.YEAR, year);
                dt_from.set(Calendar.MONTH, monthOfYear);
                dt_from.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                from_d.setText((dayOfMonth) + "-" + (monthOfYear + 1) + "-" + year);
                from_d.setTextSize(25);
            }
        };


        final TimePickerDialog.OnTimeSetListener t_from = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dt_from.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dt_from.set(Calendar.MINUTE, minute);
                from_t.setText(hourOfDay + ":" + minute);
                from_t.setTextSize(25);
            }
        };

        final TimePickerDialog.OnTimeSetListener t_to = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dt_to.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dt_to.set(Calendar.MINUTE, minute);
                to_t.setText(hourOfDay + ":" + minute);
                to_t.setTextSize(22);
            }
        };

        final DatePickerDialog.OnDateSetListener d_to = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dt_to.set(Calendar.YEAR, year);
                dt_to.set(Calendar.MONTH, monthOfYear);
                dt_to.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                to_d.setText((dayOfMonth) + "-" + (monthOfYear + 1) + "-" + year);
                to_d.setTextSize(22);
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
}
