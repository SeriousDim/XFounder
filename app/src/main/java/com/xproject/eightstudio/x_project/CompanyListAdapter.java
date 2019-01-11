package com.xproject.eightstudio.x_project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xproject.eightstudio.x_project.dataclasses.Company;

import java.util.List;

/*
 *  This class is written by
 *  (C) Dmitrii Lykov, 2019
 */

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<Company> companies;

    CompanyListAdapter(Context ctx){
        this.companies = Storage.getInstance().companies;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public CompanyListAdapter.ViewHolder onCreateViewHolder(ViewGroup group, int viewType){
        View view = this.inflater.inflate(R.layout.company_item_layout, group, false);
        return new CompanyListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompanyListAdapter.ViewHolder holder, int position){
        Company pr = this.companies.get(position);
        holder.name.setText(pr.name);
        holder.director.setText(pr.director.name+" "+pr.director.surname);
    }

    @Override
    public int getItemCount(){
        return companies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView name, director;
        final CheckBox box;
        //final ImageView img;
        ViewHolder(View v){
            super(v);
            name = (TextView)v.findViewById(R.id.v_name);
            director = (TextView)v.findViewById(R.id.il_director);
            box = (CheckBox)v.findViewById(R.id.il_selected);
            //img = (ImageView)v.findViewById(R.id.il_logo);
        }
    }

}
