package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TaskFragment extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_task, container, false);
            RecyclerView rv = view.findViewById(R.id.tasks_list);
            rv.setAdapter(new TaskAdapter(getActivity()));
        }
        return view;
    }
}
