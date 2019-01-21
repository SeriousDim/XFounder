package com.xproject.eightstudio.x_project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xproject.eightstudio.x_project.task.Task;
import com.xproject.eightstudio.x_project.task.TaskAdapter;

import java.util.ArrayList;

public class HomeTaskFragment extends Fragment {
    View view;
    TaskAdapter taskAdapter;
    ArrayList<Task> tasks;
    RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_task, container, false);
            rv = (view.findViewById(R.id.home_tasks));
            taskAdapter = new TaskAdapter(getContext());
            tasks = new ArrayList<>();
        }
        return view;
    }
    public void setTasks(ArrayList<Task> tasks){
        this.tasks = tasks;
        taskAdapter.setTasks(tasks);
        rv.setAdapter(taskAdapter);
    }
}
