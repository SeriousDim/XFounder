package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xproject.eightstudio.x_project.dataclasses.TaskClass;

import java.util.ArrayList;

public class TaskFragment extends Fragment {
    View view;
    RecyclerView taskView;
    TaskAdapter tAdapter;
    ArrayList<TaskClass> tasks = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_task, container, false);
            taskView = view.findViewById(R.id.tasks_list);
            tAdapter = new TaskAdapter(getActivity());
            tAdapter.setTasks(tasks);
            taskView.setAdapter(tAdapter);
        }
        return view;
    }

    public void setTasks(ArrayList<TaskClass> newtasks){
        if (getContext() != null) {
            tAdapter.setTasks(newtasks);
            taskView.setAdapter(tAdapter);
        }
        tasks=newtasks;
    }
}
