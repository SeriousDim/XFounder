package com.xproject.eightstudio.x_project.profile;

import android.content.Context;
import android.graphics.Color;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xproject.eightstudio.x_project.main.MainActivity;
import com.xproject.eightstudio.x_project.R;
import com.xproject.eightstudio.x_project.task.Task;
import com.xproject.eightstudio.x_project.task.Tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class GanntResponse {
    ArrayList<Task> tasks;
    Long time;
}

public class GanntFragment extends Fragment {
    View view;
    Long time;
    ArrayList<Task> tasks;
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();

    private Tasks task = retrofit.create(Tasks.class);
    int width;
    int margin;
    int length;
    long allTime;
    String localID;

    public void setId(String id) {
        this.localID = id;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_gannt, container, false);
        tasks = new ArrayList<>();
        getGannt();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        margin = (int) (width * (1f / 3.5f));
        length = width - margin;

        allTime = 86400 * 31;

        return view;
    }

    public void initGannt() {

        Date d = new Date(time * 1000L);
        Date startMonth = new Date();
        startMonth.setYear(d.getYear());
        startMonth.setMonth(d.getMonth());
        startMonth.setDate(0);
        long startTime = startMonth.getTime() / 1000L;

        int i = 0;
        for (Task task : tasks) {
            i += 1;
            final long DATE1 = task.date_from;
            final long DATE2 = task.date_to;
            float spLength, liLength;
            long diff = DATE1 - startTime;
            spLength = (float) diff / allTime;
            long diff2 = DATE2 - DATE1;
            liLength = (float) diff2 / allTime;
            newLine(liLength, spLength, i, task.title);

        }
    }

    private void getGannt() {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "getGannt");
        getDataParams.put("user_id", localID);
        Call<ResponseBody> call = task.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    GanntResponse resp = gson.fromJson(response.body().string(), GanntResponse.class);
                    tasks = resp.tasks;
                    time = resp.time;
                    initGannt();
                    ((MainActivity)getActivity()).setProgress(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void newLine(float lineLength, float spaceLength, int number, String title) {
        ViewGroup layout = view.findViewById(R.id.sss);
        CardView cv = new CardView(getContext());
        RelativeLayout.LayoutParams prll = new RelativeLayout.LayoutParams((int) (length * lineLength), 60);
        prll.setMargins(margin + (int) (length * spaceLength), 20 + 80 * number, 0, 0);
        cv.setLayoutParams(prll);
        cv.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
        cv.setRadius(10);
        layout.addView(cv);


        TextView tv = new TextView(getContext());
        RelativeLayout.LayoutParams tvlp = new RelativeLayout.LayoutParams(margin - 10, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvlp.topMargin = 20 + 80 * number;
        tvlp.leftMargin = 10;
        tv.setLayoutParams(tvlp);
        if (title.length() > 14)
            title = title.substring(0, 12) + "...";
        tv.setText(title);
        tv.setTextSize(12);
        tv.setTextColor(Color.BLACK);
        layout.addView(tv);

    }
}
