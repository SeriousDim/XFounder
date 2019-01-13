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

public class ChatFragment extends Fragment {
    private EditText typeInput;
    private ListView messageView;
    private View view;
    final private String divider = "�";
    String localID = "1";
    ArrayList<Message> messages = new ArrayList<>();
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Messenger mes = retrofit.create(Messenger.class);

    public void getUpdates(String CID) {
        HashMap<String, String> getDataParams = new HashMap<>();
        getDataParams.put("CID", CID);
        getDataParams.put("command", "getMessages");
        getDataParams.put("count", messages.size() + "");
        Call<ResponseBody> call = mes.performGetCall(getDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    HashMap<String, String> resp = gson.fromJson(response.body().string(), HashMap.class);
                    if (!resp.get("success").equals("no updates")) {
                        String history[] = resp.get("history").split(divider);
                        String workerIDs[] = resp.get("workerIDs").split(divider);
                        String workers[] = resp.get("workers").split(divider);
                        messages = new ArrayList<>();
                        for (int i = 0; i < history.length; i++) {
                            messages.add(new Message(workers[i], history[i], (short) (workerIDs[i].equals(localID) ? 1 : 0)));
                        }
                        fillView();
                    }
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

    public void sendMessage(final String CID, final String workerID) {
        final String message = typeInput.getText().toString();
        typeInput.setText("");
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("CID", CID);
        postDataParams.put("command", "addMessage");
        postDataParams.put("message", divider + message);
        postDataParams.put("workerID", divider + workerID);
        messages.add(new Message(workerID, message, (short) 2));
        fillView();
        Call<ResponseBody> call = mes.performPostCall(postDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                messages.remove(messages.size() - 1);
                getUpdates(CID);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreate(Bundle bun) {
        super.onCreate(bun);

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
                sendMessage("zero0", localID);
            }
        });
        getUpdates("zero0");
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
