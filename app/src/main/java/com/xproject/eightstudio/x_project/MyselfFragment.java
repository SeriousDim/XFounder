package com.xproject.eightstudio.x_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MyselfFragment extends Fragment {
    private View mView;



    public MyselfFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setAbout(int index){
        //TODO: add getting description and name via retrofit
        EditText et = (EditText)mView.findViewById(R.id.about);
        Storage.getInstance().companies.get(0).employees.get(index).about = et.getText().toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null)
            mView = inflater.inflate(R.layout.fragment_myself, container, false);
        final EditText et = mView.findViewById(R.id.about);
        et.setText(Storage.getInstance().companies.get(0).employees.get(0).about);
        (mView.findViewById(R.id.confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDescription(et.getText().toString());
            }
        });

        return mView;
    }

    private void updateDescription(String newDescription) {
        //TODO: retrofit update description via workers.php
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
