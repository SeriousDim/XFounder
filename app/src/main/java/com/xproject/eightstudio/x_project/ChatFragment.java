package com.xproject.eightstudio.x_project;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatFragment extends Fragment {
    EditText ed;
    private ListView lv;
    private MessageAdapter messageAdapter;
    final String divider = "�";
    View view;
    ArrayList<Message> arrm = new ArrayList<>();
    private final String server = "https://gleb2700.000webhostapp.com";
    String local = "Nobody";
    final Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private Messenger mes = retrofit.create(Messenger.class);
    public void getUpdates(String CID){
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("CID", CID);
        postDataParams.put("command", "getMessages");
        Call<ResponseBody> call = mes.performPostCall(postDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    HashMap<String,String> resp = gson.fromJson(response.body().string(), HashMap.class);
                    String arr[] = resp.get("history").split(divider);
                    String arr2[] = resp.get("workers").split(divider);
                    arrm=new ArrayList<>();
                    for (int i=0;i<arr.length;i++){
                        arrm.add(new Message(arr2[i],arr[i],(short)(arr2[i].equals(local)?1:0)));
                    }
                    messageAdapter = new MessageAdapter(getContext(),arrm);
                    lv.setAdapter(messageAdapter);
                    lv.setSelection(arrm.size()-1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void sendMessage(final String CID, final String message, final String worker){
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("CID", CID);
        postDataParams.put("command", "addMessage");
        postDataParams.put("message", divider+message);
        postDataParams.put("worker", divider+worker);
        arrm.add(new Message(worker,message,(short)2));
        messageAdapter = new MessageAdapter(getContext(),arrm);
        lv.setAdapter(messageAdapter);
        lv.setSelection(arrm.size()-1);
        Call<ResponseBody> call = mes.performPostCall(postDataParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getUpdates(CID);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onCreate(Bundle bun) {
        super.onCreate(bun);

    }

    private OnFragmentInteractionListener mListener;

    public ChatFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_chat, container, false);
        lv = view.findViewById(R.id.lv);
        ed = view.findViewById(R.id.editText);
        ImageView send = view.findViewById(R.id.bt);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("zero0", ed.getText().toString(), local);
                ed.setText("");
            }
        });
        getUpdates("zero0"); //TODO:NullPtrExc; add checking
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
