package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xproject.eightstudio.x_project.dataclasses.Employee;

import java.util.ArrayList;

public class RequestFragment extends Fragment {
    View view;
    RequestAdapter reqAdapter;
    RecyclerView rv;
    ArrayList<Employee> employees;
    String projectID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_request_empl, container, false);
            rv = (view.findViewById(R.id.empl_list));
            projectID = ((MainActivity)getActivity()).loadProject();
            reqAdapter = new RequestAdapter(getContext());
            employees = new ArrayList<>();
            Employee e = new Employee();
            e.name = "Gleb 2 5 2";
            e.job = "PR-management";
            e.id = "0";
            employees.add(e);
            setEmployees(employees);
        }
        return view;
    }

    public void setEmployees(ArrayList<Employee> employees){
        this.employees = employees;
        reqAdapter.setEmployees(employees);
        rv.setAdapter(reqAdapter);
    }
}
