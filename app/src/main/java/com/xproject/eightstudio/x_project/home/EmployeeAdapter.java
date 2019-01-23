package com.xproject.eightstudio.x_project.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xproject.eightstudio.x_project.MainActivity;
import com.xproject.eightstudio.x_project.R;
import com.xproject.eightstudio.x_project.dataclasses.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<Employee> employees = new ArrayList<>();
    Context ctx;

    EmployeeAdapter(Context ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }

    public EmployeeAdapter.ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        final View view = this.inflater.inflate(R.layout.employee_item_layout, group, false);
        return new EmployeeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeAdapter.ViewHolder holder, int position) {
        Employee e = employees.get(position);
        holder.e = e;
        holder.emplName.setText(e.name);
        holder.job.setText(e.job);
    }

    public void setEmployees(List<Employee> newEmployees) {
        employees = newEmployees;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView emplName, job;
        Employee e;

        ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) inflater.getContext()).openProfile(e.id);
                }
            });
            this.emplName = v.findViewById(R.id.il_name);
            this.job = v.findViewById(R.id.il_job);
        }
    }

}
