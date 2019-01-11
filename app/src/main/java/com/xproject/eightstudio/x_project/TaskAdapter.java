package com.xproject.eightstudio.x_project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xproject.eightstudio.x_project.ProjectAdapter;
import com.xproject.eightstudio.x_project.R;
import com.xproject.eightstudio.x_project.Storage;
import com.xproject.eightstudio.x_project.dataclasses.Task;

import java.util.List;

/*
 *  This class is written by
 *  (C) Dmitrii Lykov, 2019
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<Task> tasks;

    TaskAdapter(Context ctx){
        this.tasks = Storage.getInstance().companies.get(0).employees.get(0).tasks;
        this.inflater = LayoutInflater.from(ctx);
    }

    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup group, int viewType){
        View view = this.inflater.inflate(R.layout.task_item_profile, group, false);
        return new TaskAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder holder, int position){
        Task t = tasks.get(position);
        holder.name.setText(t.name);
        holder.proj.setText(t.project.name);
    }

    @Override
    public int getItemCount(){
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView name, proj;
        final ImageButton complete, delete;
        ViewHolder(View v){
            super(v);
            this.name = (TextView)v.findViewById(R.id.v_name);
            this.proj = (TextView)v.findViewById(R.id.v_proj_name);
            this.complete = (ImageButton)v.findViewById(R.id.v_complete);
            this.delete = (ImageButton)v.findViewById(R.id.v_delete);
        }
    }

}
