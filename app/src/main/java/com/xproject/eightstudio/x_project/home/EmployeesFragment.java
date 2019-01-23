package com.xproject.eightstudio.x_project.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xproject.eightstudio.x_project.R;
import com.xproject.eightstudio.x_project.dataclasses.Employee;
import java.util.ArrayList;

public class EmployeesFragment extends Fragment {
    View view;
    EmployeeAdapter emplAdapter;
    RecyclerView rv;
    ArrayList<Employee> employees;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_employees, container, false);
            rv = (view.findViewById(R.id.empl_list));
            emplAdapter = new EmployeeAdapter(getContext());
            employees = new ArrayList<>();
        }
        return view;
    }
    public void setEmployees(ArrayList<Employee> employees){
        this.employees = employees;
        emplAdapter.setEmployees(employees);
        rv.setAdapter(emplAdapter);
    }

}
