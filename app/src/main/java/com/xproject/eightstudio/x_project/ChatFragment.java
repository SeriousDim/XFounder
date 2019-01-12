package com.xproject.eightstudio.x_project;

import android.net.Uri;
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
import com.xproject.eightstudio.x_project.dataclasses.Message;
import com.xproject.eightstudio.x_project.dataclasses.MessageAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatFragment extends Fragment {
    EditText typeInput;
    private ListView messageView;
    View view;
    private MessageAdapter messageAdapter;
    final String divider = "�";
    String local = "1";
    ArrayList<Message> messages = new ArrayList<>();
    HashMap<String, String> IDToName = new HashMap<>();
    private final String server = "https://gleb2700.000webhostapp.com";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    int e=0;
    private Messenger mes = retrofit.create(Messenger.class);
    private Workers work = retrofit.create(Workers.class);

    public void getUpdates(String CID) {
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("CID", CID);
        postDataParams.put("command", "getMessages");
        postDataParams.put("count", messages.size()+"");
        Call<ResponseBody> call = mes.performPostCall(postDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    HashMap<String, String> resp = gson.fromJson(response.body().string(), HashMap.class);
                    if (!resp.get("success").equals("no updates")) {
                        String history[] = resp.get("history").split(divider);
                        ArrayList<String> workerIDs = new ArrayList<>(Arrays.asList(resp.get("workers").split(divider)));
                        Set<String> set = new HashSet<>(workerIDs);
                        Set<String> setN = new HashSet<>(workerIDs);
                        getAllNames(set);
                        while (true){
                            for (String id:setN) {
                                if (IDToName.containsKey(id)) setN.remove(id);
                            }
                            Toast.makeText(getContext(),setN.size()+""+IDToName.size(),Toast.LENGTH_SHORT).show();
                            break;
                        }
                        messages = new ArrayList<>();
                        for (int i = 0; i < history.length; i++) {
                            messages.add(new Message(IDToName.get(workerIDs.get(i)), history[i], (short) (workerIDs.get(i).equals(local) ? 1 : 0)));
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

    private void getAllNames(Set<String> IDs) {
        for (final String ID: IDs){
            if (!IDToName.containsKey(ID)) {
                HashMap<String, String> getDataParams = new HashMap<>();
                getDataParams.put("WID", ID);
                getDataParams.put("command", "getName");
                Call<ResponseBody> call = work.performGetCall(getDataParams);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            HashMap<String, String> resp = gson.fromJson(response.body().string(), HashMap.class);
                            IDToName.put(ID, resp.get("name"));
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
        }
    }


    private void fillView() {
        if (mListener != null) {
            messageAdapter = new MessageAdapter(getContext(), messages);
            messageView.setAdapter(messageAdapter);
            messageView.setSelection(messages.size() - 1);
        }
    }

    public void sendMessage(final String CID, final String worker) {
        final String message = typeInput.getText().toString();
        typeInput.setText("");
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("CID", CID);
        postDataParams.put("command", "addMessage");
        postDataParams.put("message", divider + message);
        postDataParams.put("worker", divider + worker);
        messages.add(new Message(worker, message, (short) 2));
        fillView();
        messages.remove(messages.size() - 2);
        Call<ResponseBody> call = mes.performPostCall(postDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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

    private OnFragmentInteractionListener mListener;

    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListener = new OnFragmentInteractionListener() {
            @Override
            public void onFragmentInteraction(Uri uri) {

            }
        };

        view = inflater.inflate(R.layout.fragment_chat, container, false);
        messageView = view.findViewById(R.id.lv);
        typeInput = view.findViewById(R.id.editText);
        ImageView send = view.findViewById(R.id.bt);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("zero0", local);
            }
        });
        fillView();
        getUpdates("zero0");
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        e=0;
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
