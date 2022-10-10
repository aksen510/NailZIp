package com.example.nailzip;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.nailzip.model.Chatting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

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
    private Chat chat = new Chat();

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm");

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

        List<Chatting> chattings;
        String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        private ArrayList<String> chatUsers = new ArrayList<>();

        public ChatFragmentRecyclerViewAdapter(){
            chattings = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+myUid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chattings.clear();
                    for (DataSnapshot item : snapshot.getChildren()){
                        chattings.add(item.getValue(Chatting.class));
                    }
                    notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // InfoShopfragment에 적용
//            FirebaseDatabase.getInstance().getReference().child("chatUsers").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    chats.clear();
//                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//
//                        Chat chat = snapshot.getValue(Chat.class);
//
//                        if (chat.chatUid.equals(myUid)){
//                            continue;
//                        }
//
//                        chats.add(chat);
//                    }
//                    notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position){

//            ((CustomViewHolder)holder).tv_title.setText(chats.get(position).getUserName());
//            ((CustomViewHolder)holder).tv_lastMessage.setText();
          //  holder.tv_location.setText(arrayList.get(position).getLocation());

            CustomViewHolder customViewHolder = (CustomViewHolder) holder;
            String chatUid = null;

            for (String user : chattings.get(position).users.keySet()){
                if(!user.equals(myUid)){
                    chatUid = user;
                    chatUsers.add(chatUid);
                }
            }
            FirebaseDatabase.getInstance().getReference().child("chatUsers").child(chatUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Chat chat = snapshot.getValue(Chat.class);
                    customViewHolder.tv_title.setText(chat.userName);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // 메세지를 내림차순으로 정렬 후 마지막 메세지의 키값을 가져옴
            Map<String, Chatting.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
            commentMap.putAll(chattings.get(position).comments);
            String lastMessageKey = (String) commentMap.keySet().toArray()[0];
            customViewHolder.tv_lastMessage.setText(chattings.get(position).comments.get(lastMessageKey).message);

            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent chattingroomActivity = new Intent(getContext(), ChattingroomActivity.class);
//                    chattingroomActivity.putExtra("chatUid", chattings.get(position).getChatUid());
//                    startActivity(chattingroomActivity);
                    Intent chattingroomActivity = new Intent(getContext(), ChattingroomActivity.class);
                    chattingroomActivity.putExtra("chatUid", chatUsers.get(position));
                    startActivity(chattingroomActivity);
                }
            });

            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            long unixTime = (long) chattings.get(position).comments.get(lastMessageKey).timestamp;
            Date date = new Date(unixTime);
            customViewHolder.tv_timestamp.setText(simpleDateFormat.format(date));
        }

        @Override
        public int getItemCount(){
            return chattings.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder{
            public ImageView imageView;
            public TextView tv_title, tv_lastMessage, tv_timestamp;

            public CustomViewHolder(View view){
                super(view);

                imageView = view.findViewById(R.id.icon_chatUser);
                tv_title = view.findViewById(R.id.tv_chatUserName);
                tv_lastMessage = view.findViewById(R.id.tv_chatItem_last);
                tv_timestamp = view.findViewById(R.id.tv_timestamp);

            }
        }
    }

    public void init(View view){

    }
}