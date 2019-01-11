package com.xproject.eightstudio.x_project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xproject.eightstudio.x_project.dataclasses.Project;

import java.util.List;

/*
 *  This class is written by
 *  (C) Dmitrii Lykov, 2019
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<Project> projects;

    ProjectAdapter(Context ctx){
        this.projects = Storage.getInstance().companies.get(0).projects;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder(ViewGroup group, int viewType){
        View view = this.inflater.inflate(R.layout.project_item, group, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectAdapter.ViewHolder holder, int position){
        Project pr = this.projects.get(position);
        int completedTasks = pr.getCompleteTasksAmount();
        int totalTasks = pr.getTasksAmount();
        holder.name.setText(pr.name);
        holder.empl.setText(pr.getEmployeesAmount()+"");
        holder.priority.setText(pr.priority+"");
        holder.tasks.setText(completedTasks+"/"+totalTasks);
        holder.progress.setMax(totalTasks);
        holder.progress.setProgress(completedTasks);
        if (pr.favorite)
            holder.important.setImageResource(R.drawable.ic_star_black_24dp);
        else
            holder.important.setImageResource(R.drawable.ic_star_border_black_24dp);
    }

    @Override
    public int getItemCount(){
        return projects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView name, empl, priority, tasks;
        final ProgressBar progress;
        final ImageButton important;
        ViewHolder(View v){
            super(v);
            name = (TextView)v.findViewById(R.id.v_name);
            empl = (TextView)v.findViewById(R.id.il_empl);
            priority = (TextView)v.findViewById(R.id.il_priority);
            tasks = (TextView)v.findViewById(R.id.il_tasks);
            progress = (ProgressBar) v.findViewById(R.id.il_t_progress);
            important = (ImageButton) v.findViewById(R.id.il_important);
        }
    }

}
