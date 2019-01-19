package com.xproject.eightstudio.x_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class TaskEdit extends Fragment {
    View view;
    TextView from_t, from_d, to_t, to_d;

    DatePickerDialog.OnDateSetListener d_from = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            from_d.setText((dayOfMonth) + "-" + (monthOfYear + 1) + "-" + year);
            from_d.setTextSize(25);
        }
    };


    TimePickerDialog.OnTimeSetListener t_from = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            from_t.setText(hourOfDay + ":" + minute);
            from_t.setTextSize(25);
        }
    };

    TimePickerDialog.OnTimeSetListener t_to = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            to_t.setText(hourOfDay + ":" + minute);
            to_t.setTextSize(25);
        }
    };

    DatePickerDialog.OnDateSetListener d_to = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            to_d.setText((dayOfMonth) + "-" + (monthOfYear + 1) + "-" + year);
            to_d.setTextSize(25);
        }
    };

    Calendar dateAndTime = Calendar.getInstance();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_task_create, container, false);
            from_d = view.findViewById(R.id.from_date);
            from_t = view.findViewById(R.id.from_time);
            to_d = view.findViewById(R.id.to_date);
            to_t = view.findViewById(R.id.to_time);
            from_d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(getContext(), d_from,
                            dateAndTime.get(Calendar.YEAR),
                            dateAndTime.get(Calendar.MONTH),
                            dateAndTime.get(Calendar.DAY_OF_MONTH))
                            .show();
                }
            });
            from_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new TimePickerDialog(getContext(), t_from,
                            dateAndTime.get(Calendar.HOUR_OF_DAY),
                            dateAndTime.get(Calendar.MINUTE), true)
                            .show();
                }
            });
            to_d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(getContext(), d_to,
                            dateAndTime.get(Calendar.YEAR),
                            dateAndTime.get(Calendar.MONTH),
                            dateAndTime.get(Calendar.DAY_OF_MONTH))
                            .show();
                }
            });
            to_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new TimePickerDialog(getContext(), t_to,
                            dateAndTime.get(Calendar.HOUR_OF_DAY),
                            dateAndTime.get(Calendar.MINUTE), true)
                            .show();
                }
            });

        }
        return view;
    }
}
