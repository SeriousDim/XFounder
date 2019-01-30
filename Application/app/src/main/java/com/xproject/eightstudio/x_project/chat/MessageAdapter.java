package com.xproject.eightstudio.x_project.chat;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xproject.eightstudio.x_project.R;

public class MessageAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Message> objects;
    String localID;

    public MessageAdapter(Context context, ArrayList<Message> messages, String localID) {
        ctx = context;
        this.localID = localID;
        objects = messages;
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
        Message m = getMessage(position);
        if (m.isLoading) {
            view = lInflater.inflate(R.layout.message_viewu, parent, false);

        } else {
            if (m.sender_id.equals(localID)) {
                view = lInflater.inflate(R.layout.message_viewr, parent, false);
            } else {
                view = lInflater.inflate(R.layout.message_view, parent, false);
                TextView worker = view.findViewById(R.id.worker);
                worker.setText(m.name);
            }
        }
        TextView message = view.findViewById(R.id.text);
        message.setText(m.data);
        return view;
    }

    // товар по позиции
    Message getMessage(int position) {
        return ((Message) getItem(position));
    }

}