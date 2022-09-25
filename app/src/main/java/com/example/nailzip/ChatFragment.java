package com.example.nailzip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nailzip.model.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseRemoteConfig mfirebaseRemoteConfig;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        init(view);

        RecyclerView recyclerView = view.findViewById(R.id.chat_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new ChatFragmentRecyclerViewAdapter());
        
        mfirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();


        return view;
    }

    class ChatFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<Chat> chats;

        public ChatFragmentRecyclerViewAdapter(){
            chats = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("chatUsers").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    chats.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        chats.add(snapshot.getValue(Chat.class));
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){

        }

        @Override
        public int getItemCount(){
            return chats.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder{
            public ImageView imageView;
            public TextView textView;

            public CustomViewHolder(View view){
                super(view);

                imageView = view.findViewById(R.id.icon_chatUser);
                textView = view.findViewById(R.id.tv_chatUserName);

            }
        }
    }

    public void init(View view){

    }
}