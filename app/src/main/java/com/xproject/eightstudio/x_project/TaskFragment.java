package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xproject.eightstudio.x_project.dataclasses.Task;

import java.util.ArrayList;

public class TaskFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View view;
    RecyclerView taskView;
    TaskAdapter tAdapter;
    ArrayList<Task> tasks = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_task, container, false);
            taskView = view.findViewById(R.id.tasks_list);
            mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

            tAdapter = new TaskAdapter(getActivity());
            tAdapter.setTasks(tasks);
            taskView.setAdapter(tAdapter);
        }
        return view;
    }

    public void setTasks(ArrayList<Task> newtasks) {
        if (getContext() != null) {
            tAdapter.setTasks(newtasks);
            taskView.setAdapter(tAdapter);
        }
        tasks = newtasks;
    }

    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 4000);
    }
}
