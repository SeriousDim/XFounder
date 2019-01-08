package com.xproject.eightstudio.x_project.dataclasses;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xproject.eightstudio.x_project.R;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

    public class ViewHolder{
        TextView name, completed, percent;
        ProgressBar progress;
        ViewHolder(View v){
            this.name = (TextView)v.findViewById(R.id.name);
        }
    }

}
