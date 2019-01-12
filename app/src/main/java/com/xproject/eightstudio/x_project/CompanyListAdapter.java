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
        holder.director.setText(pr.director.name);
    }

    @Override
    public int getItemCount(){
        return companies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView name, director;
        ViewHolder(View v){
            super(v);
            name = v.findViewById(R.id.v_name);
            director = v.findViewById(R.id.il_director);
        }
    }

}
