package com.xproject.eightstudio.x_project;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class GanntFragment extends Fragment {
    View view;
    ArrayList<String> tasks;
    float spLength, liLength;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_gannt, container, false);
        tasks = new ArrayList<>();

        final long allTime = 86400 * 31;

        final long DATE1 = 1547455181L;
        final long DATE2 = 1547811280L;
        Date d = new Date(DATE1 * 1000L);

        Date startMonth = new Date();
        startMonth.setYear(d.getYear());
        startMonth.setMonth(d.getMonth());
        startMonth.setDate(0);
        long startTime = startMonth.getTime()/1000L;
        long diff = DATE1 - startTime;
        spLength = (float)diff/allTime;
        long diff2 = DATE2 - DATE1;
        liLength = (float)diff2/allTime;

        for (int i=0; i<27; i+=2) {
            newLine(liLength, spLength, i+1);
            newLine(0.5f, 0.2f, i+2);
        }
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void addTask(String name, int number, ViewGroup layout){
        tasks.add(name);
        // add the cardview with task name
        TextView tv = new TextView(getContext());
        RelativeLayout.LayoutParams tvlp = new RelativeLayout.LayoutParams(140, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvlp.topMargin = 75 * number;
        tv.setLayoutParams(tvlp);
        tv.setText(name.substring(0, 12)+"...");
        tv.setTextSize(14);
        layout.addView(tv);
    }

    public void newLine(float lineLength, float spaceLength, int number) {
        ViewGroup layout = view.findViewById(R.id.sss);
        CardView cv = new CardView(getContext());

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        int margin = (int)(width*(1f/3.5f));
        int length= width-margin;
        //Toast.makeText(getContext(),margin+" "+length+"\n"+width+' '+height,Toast.LENGTH_LONG).show();

        RelativeLayout.LayoutParams prll = new RelativeLayout.LayoutParams((int)(length*lineLength),60);
        prll.setMargins(margin + (int) (length * spaceLength), 75 * number, 0, 0);
        cv.setLayoutParams(prll);
        cv.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
        cv.setRadius(10);
        layout.addView(cv);

        addTask("Очень огромное название", number, layout);
    }
}
