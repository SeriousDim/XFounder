package com.xproject.eightstudio.x_project.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xproject.eightstudio.x_project.main.MainActivity;
import com.xproject.eightstudio.x_project.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class MessageResponse {
    String time;
    ArrayList<Message> messages;
}

public class ChatFragment extends Fragment {
    private EditText typeInput;
    private ListView messageView;
    private View view;
    MainActivity activity;
    String localID, projectID;
    private String lastTime = "0";
    ArrayList<Message> messages = new ArrayList<>();
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Messenger mes = retrofit.create(Messenger.class);

    class MyClass implements Runnable {
        public void run() {
            try {
                Thread.sleep(2000);
                getUpdates();
            } catch (Exception e) {
                Log.d("tagged", e.toString());
            }
        }
    }

    public void getUpdates() {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("command", "getMessages");
        getDataParams.put("projectID", projectID);
        getDataParams.put("time", lastTime);
        Call<ResponseBody> call = mes.performGetCall(getDataParams);
        Response<ResponseBody> response = null;
        try {
            response = call.execute();
            final MessageResponse resp = gson.fromJson(response.body().string(), MessageResponse.class);
            if (resp.messages.size() != 0) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messages.addAll(resp.messages);
                        fillView();
                    }
                });
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.setProgress(false);
                }
            });
            lastTime = resp.time;
            Thread t1 = new Thread(new MyClass());
            t1.start();
        } catch (IOException e) {
            Log.d("tagged", e.toString());
        }
    }

    private void fillView() {
        if (getContext() != null) {
            MessageAdapter messageAdapter = new MessageAdapter(getContext(), messages, localID);
            messageView.setAdapter(messageAdapter);
            messageView.setSelection(messages.size() - 1);
        }
    }

    public void sendMessage() {
        final String message = typeInput.getText().toString();
        if (message.equals("")) {
            Toast.makeText(getContext(), "Empty message", Toast.LENGTH_SHORT).show();
            return;
        }
        typeInput.setText("");
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("command", "addMessage");
        postDataParams.put("message", message);
        postDataParams.put("workerID", localID);
        postDataParams.put("projectID", projectID);
        Message m = new Message();
        m.setData(message);
        m.setIsLoading(true);
        messages.add(m);
        fillView();
        Call<ResponseBody> call = mes.performPostCall(postDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                messages.remove(messages.size() - 1);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        messageView = view.findViewById(R.id.lv);
        typeInput = view.findViewById(R.id.editText);
        ImageView send = view.findViewById(R.id.bt);
        activity = (MainActivity) getActivity();
        localID = activity.loadUser();
        activity.setProgress(true);
        projectID = ((MainActivity) getActivity()).loadProject();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        Thread t1 = new Thread(new MyClass());
        t1.start();
        fillView();
        return view;
    }

}
