package com.xproject.eightstudio.x_project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xproject.eightstudio.x_project.dataclasses.Task;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeTaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeTaskFragment extends Fragment {
    View view;
    TaskAdapter taskAdapter;
    ArrayList<Task> tasks;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeTaskFragment newInstance(String param1, String param2) {
        HomeTaskFragment fragment = new HomeTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_task, container, false);
            RecyclerView rv = (view.findViewById(R.id.home_tasks));
            taskAdapter = new TaskAdapter(getActivity());
            tasks = new ArrayList<>();
            for (int i=0; i<=25; i++) {
                Task t = new Task();
                t.title = "Task "+i;
                t.name = "Man "+i;
                tasks.add(t);
            }
            taskAdapter.setTasks(tasks);
            rv.setAdapter(taskAdapter);
            Log.d("debug", tasks.size()+"");
            /*view.findViewById(R.id.add_task).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) inflater.getContext()).addTask();
                }
            });*/
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
