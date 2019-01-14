package com.xproject.eightstudio.x_project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xproject.eightstudio.x_project.dataclasses.TaskClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<TaskClass> tasks = new ArrayList<>();
    HashMap<Integer, TaskClass> idToTask = new HashMap<>();
    TaskAdapter(Context ctx) {
        this.inflater = LayoutInflater.from(ctx);
    }

    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        final View view = this.inflater.inflate(R.layout.task_item_profile, group, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) inflater.getContext()).openTask(idToTask.get(view.getId()));
            }
        });
        return new TaskAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder holder, int position) {
        TaskClass t = tasks.get(position);
        idToTask.put(holder.id,t);
        holder.title.setText(t.title);
        holder.creator.setText(t.creator);
    }

    public void setTasks(List<TaskClass> newTasks) {
        tasks = newTasks;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title, creator;
        Integer id;

        ViewHolder(View v) {
            super(v);
            this.id = v.getId();
            this.title = v.findViewById(R.id.v_name);
            this.creator = v.findViewById(R.id.v_proj_name);
        }
    }
}
