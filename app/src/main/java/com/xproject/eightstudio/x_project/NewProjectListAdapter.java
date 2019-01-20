package com.xproject.eightstudio.x_project;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xproject.eightstudio.x_project.dataclasses.Company;

public class NewProjectListAdapter extends ArrayAdapter<Company> {

    final Activity ctx;
    final Company[] data;
    int currentComapny;

    NewProjectListAdapter(Activity ctx, Company[] data){
        super(ctx, R.layout.company_item_layout, data);
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public View getView(int pos, View convert, ViewGroup parent){
        ViewHolder holder;
        View row = convert;
        if (row == null) {
            LayoutInflater inflater = ctx.getLayoutInflater();
            row = inflater.inflate(R.layout.company_item_layout, null, true);
            holder = new ViewHolder();
            holder.name = row.findViewById(R.id.il_name);
            holder.director = row.findViewById(R.id.il_director);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.name.setText(this.data[pos].name);
        holder.director.setText(this.data[pos].director.name);
        if (currentComapny==pos){
            ((CardView)row).setCardBackgroundColor(ctx.getResources().getColor(R.color.colorPrimaryDark));
            holder.name.setTextColor(ctx.getResources().getColor(android.R.color.white));
            holder.director.setTextColor(ctx.getResources().getColor(android.R.color.white));
        }
        else if (holder.director.getCurrentTextColor()==ctx.getResources().getColor(android.R.color.white) &&
                currentComapny != pos){
            ((CardView)row).setCardBackgroundColor(ctx.getResources().getColor(android.R.color.white));
            holder.name.setTextColor(ctx.getResources().getColor(R.color.standart));
            holder.director.setTextColor(ctx.getResources().getColor(R.color.standart));
        }

        return row;
    }

    static class ViewHolder{
        TextView name, director;
        ImageView img;
    }

}
