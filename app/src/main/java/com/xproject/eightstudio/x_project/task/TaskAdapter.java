package com.xproject.eightstudio.x_project.task;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xproject.eightstudio.x_project.MainActivity;
import com.xproject.eightstudio.x_project.R;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Task> tasks = new ArrayList<>();

    public TaskAdapter(Context ctx) {
        this.inflater = LayoutInflater.from(ctx);
    }

    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        final View view = this.inflater.inflate(R.layout.task_item_profile, group, false);
        return new TaskAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder holder, int position) {
        Task t = tasks.get(position);
        holder.t = t;
        holder.title.setText(t.title);
        holder.creator.setText(t.name);
    }

    public void setTasks(List<Task> newTasks) {
        tasks = newTasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title, creator;
        Task t;

        ViewHolder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) inflater.getContext()).openTask(t);
                }
            });
            this.title = v.findViewById(R.id.il_name);
            this.creator = v.findViewById(R.id.il_job);
        }
    }
}
