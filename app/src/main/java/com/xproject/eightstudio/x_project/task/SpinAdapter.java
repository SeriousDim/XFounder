package com.xproject.eightstudio.x_project.task;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xproject.eightstudio.x_project.R;
import com.xproject.eightstudio.x_project.dataclasses.Employee;

import java.util.ArrayList;

public class SpinAdapter extends ArrayAdapter<Employee> {

    final Activity ctx;
    ArrayList<Employee> employees;

    SpinAdapter(Activity ctx, ArrayList<Employee> employees) {
        super(ctx, android.R.layout.simple_spinner_dropdown_item, employees);
        this.ctx = ctx;
        this.employees = employees;
    }
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = ctx.getLayoutInflater();
        View row = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent,false);
        holder = new ViewHolder();
        holder.name = row.findViewById(android.R.id.text1);
        holder.name.setText(getItem(position).name);
        return row;
    }
    @Override
    public View getView(int pos, View convert, ViewGroup parent) {
        ViewHolder holder;
        View row = convert;
        if (row == null) {
            LayoutInflater inflater = ctx.getLayoutInflater();
            row = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null, true);
            holder = new ViewHolder();
            holder.name = row.findViewById(android.R.id.text1);
            holder.name.setText(getItem(pos).name);
        }

        return row;
    }

    static class ViewHolder {
        TextView name;
    }

}
