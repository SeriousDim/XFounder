package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xproject.eightstudio.x_project.dataclasses.TaskClass;

public class TaskViewFragment extends Fragment {
    View view;
    TaskClass task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_full_task, container, false);
            ((TextView)view.findViewById(R.id.name)).setText(task.title);
            ((TextView)view.findViewById(R.id.creator)).setText("Создатель: "+task.creator);
        }
        return view;
    }

    public void setTask(TaskClass task) {
        this.task = task;
    }
}

