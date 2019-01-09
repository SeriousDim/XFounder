package com.xproject.eightstudio.x_project.dataclasses;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xproject.eightstudio.x_project.R;

public class MessageAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Message> objects;

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        ctx = context;
        objects = messages;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        Message m = getMessage(position);
        if (m.isLocal==1) {
            view = lInflater.inflate(R.layout.message_viewr, parent, false);
        } else if (m.isLocal==0){
            view = lInflater.inflate(R.layout.message_view, parent, false);
            TextView worker = view.findViewById(R.id.worker);
            worker.setText(m.worker);
        }
        else{
            view = lInflater.inflate(R.layout.message_viewu, parent, false);

        }

        TextView message = view.findViewById(R.id.text);
        message.setText(m.message);
        return view;
    }

    // товар по позиции
    Message getMessage(int position) {
        return ((Message) getItem(position));
    }

}