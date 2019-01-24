package com.xproject.eightstudio.x_project;

import android.app.DownloadManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xproject.eightstudio.x_project.dataclasses.Employee;

import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Employee> employees = new ArrayList<>();
    Context ctx;
    MainActivity activity;

    RequestAdapter(Context ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.activity = ((MainActivity) inflater.getContext());
    }

    public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        final View view = this.inflater.inflate(R.layout.employee_request, group, false);
        return new RequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestAdapter.ViewHolder holder, int position) {
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
            v.findViewById(R.id.avatar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.openProfile(e.id);
                }
            });
            v.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.acceptRequest(e.id);
                }
            });
            v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.deleteRequest(e.id);
                }
            });
            this.emplName = v.findViewById(R.id.il_name);
            this.job = v.findViewById(R.id.il_job);
        }
    }

}
