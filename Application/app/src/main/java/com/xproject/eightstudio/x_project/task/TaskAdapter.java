package com.xproject.eightstudio.x_project.task;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xproject.eightstudio.x_project.main.MainActivity;
import com.xproject.eightstudio.x_project.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Task> tasks = new ArrayList<>();
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

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
        Date date_to = new Date(1000L * t.date_to);
        holder.date_to.setText("До "+ df.format(date_to));
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
        final TextView title, creator, date_to;
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
            this.date_to = v.findViewById(R.id.date);
        }
    }
}
