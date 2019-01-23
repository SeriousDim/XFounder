package com.xproject.eightstudio.x_project;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xproject.eightstudio.x_project.dataclasses.Project;

import java.util.ArrayList;

public class NewProjectListAdapter extends ArrayAdapter<Project> {

    final Activity ctx;
    final ArrayList<Project> data;
    int currentProject;

    NewProjectListAdapter(Activity ctx, ArrayList<Project> data) {
        super(ctx, R.layout.company_item_layout, data);
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public View getView(int pos, View convert, ViewGroup parent) {
        ViewHolder holder;
        View row = convert;
        if (row == null) {
            LayoutInflater inflater = ctx.getLayoutInflater();
            row = inflater.inflate(R.layout.company_item_layout, null, true);
            holder = new ViewHolder();
            holder.title = row.findViewById(R.id.il_name);
            holder.founder = row.findViewById(R.id.il_director);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Project p = this.data.get(pos);
        holder.title.setText(p.title);
        holder.founder.setText(p.name);
        if (pos==currentProject) {
            ((CardView) row).setCardBackgroundColor(ctx.getResources().getColor(R.color.colorPrimaryDark));
            holder.title.setTextColor(ctx.getResources().getColor(android.R.color.white));
            holder.founder.setTextColor(ctx.getResources().getColor(android.R.color.white));
        } else {
            ((CardView) row).setCardBackgroundColor(ctx.getResources().getColor(android.R.color.white));
            holder.title.setTextColor(ctx.getResources().getColor(R.color.standart));
            holder.founder.setTextColor(ctx.getResources().getColor(R.color.standart));
        }

        return row;
    }

    static class ViewHolder {
        TextView title, founder;
    }

}
