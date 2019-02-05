package com.xproject.eightstudio.x_project.project;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.xproject.eightstudio.x_project.R;

public class ProjectAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<LinkedTreeMap> objects;

    public ProjectAdapter(Context context, ArrayList<LinkedTreeMap> projects) {
        ctx = context;
        objects = projects;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notifyDataSetChanged();
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        view = lInflater.inflate(R.layout.company_item_layout, parent, false);
        ((TextView) view.findViewById(R.id.il_name)).setText(getProject(position).get("title").toString());
        ((TextView) view.findViewById(R.id.il_director)).setText(getProject(position).get("name").toString());
        return view;
    }

    // товар по позиции
    LinkedTreeMap getProject(int position) {
        return (LinkedTreeMap) getItem(position);
    }

}