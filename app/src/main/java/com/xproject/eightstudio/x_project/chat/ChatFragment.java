package com.xproject.eightstudio.x_project.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
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
    String localID = "1";
    private String lastTime = "0";
    ArrayList<Message> messages = new ArrayList<>();
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Messenger mes = retrofit.create(Messenger.class);

    public void getUpdates(String projectID) {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("projectID", projectID);
        getDataParams.put("command", "getMessages");
        getDataParams.put("time", lastTime);
        Call<ResponseBody> call = mes.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    MessageResponse resp = gson.fromJson(response.body().string(), MessageResponse.class);
                    messages.addAll(resp.messages);
                    lastTime = resp.time;
                    fillView();
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

    private void fillView() {
        if (getContext() != null) {
            MessageAdapter messageAdapter = new MessageAdapter(getContext(), messages);
            messageView.setAdapter(messageAdapter);
            messageView.setSelection(messages.size() - 1);
        }
    }

    public void sendMessage(final String projectID, final String workerID) {
        final String message = typeInput.getText().toString();
        typeInput.setText("");
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("command", "addMessage");
        postDataParams.put("message", message);
        postDataParams.put("workerID", workerID);
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
                getUpdates(projectID);
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
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("1", localID);
            }
        });
        fillView();
        getUpdates("1");
        return view;
    }

}
