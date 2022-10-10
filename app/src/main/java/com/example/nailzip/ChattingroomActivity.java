package com.example.nailzip;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nailzip.model.Chat;
import com.example.nailzip.model.Chatting;
import com.example.nailzip.model.NotificationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class ChattingroomActivity extends AppCompatActivity {

    private String TAG = "ChattingroomActivity";

    private Toolbar tb_back;
    private EditText edt_chat;
    private Button btn_send;
    private String chatwith = "닉네임";
    private static String shopName, shopLocation, chatUid;
    private static int shopPos;

    private String uid;
    private String chatRoomUid;
    public Chat chat = new Chat();

    private RecyclerView msgRecyclerView;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    int peopleCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattingroom);

        init();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();     // 채팅을 보내는 uid 즉, 로그인 된 uid
        chatUid = getIntent().getStringExtra("chatUid");          // 채팅을 받는 uid 즉, 상대방

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Chatting chatting = new Chatting();
//                chatting.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                chatting.chatUid = chatUid;

                chatting.users.put(uid, true);
                chatting.users.put(chatUid, true);

                if (chatRoomUid == null){
                    btn_send.setEnabled(false);
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatting).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            checkChatRoom();
                        }
                    });
                }
                else {
                    Chatting.Comment comment = new Chatting.Comment();
                    comment.uid = uid;
                    comment.message = edt_chat.getText().toString();
                    comment.timestamp = ServerValue.TIMESTAMP;
                    Log.d(TAG, "메세지 내용 : " + comment.message);
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sendGcm();
                            edt_chat.setText("");
                        }
                    });
                }

            }
        });
        checkChatRoom();

        // Toolbar 활성화
        setSupportActionBar(tb_back);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
        getSupportActionBar().setTitle(chatwith); // Toolbar 제목
    }

    void sendGcm(){
        Gson gson = new Gson();

        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to = chat.pushToken;
        notificationModel.notification.title = userName;
        notificationModel.notification.body = edt_chat.getText().toString();
        notificationModel.data.title = userName;
        notificationModel.data.body = edt_chat.getText().toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"), gson.toJson(notificationModel));

        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "key=AAAAbGwsNds:APA91bGR8gpu2p-AXUq2pa4VfKTuNd1aZEgzuJqeqUr0MXpgHkmki8c7cqsNDnVG0ej1gLuke7K2V2JScLCLANhqKS4TgDjpG5GPb9-YluZ6Teyvp8I-WWakUUdAeYL0h2RhOD0Xn-WX")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    void checkChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
                    Chatting chatting = item.getValue(Chatting.class);
                    if (chatting.users.containsKey(chatUid)){
                        chatRoomUid = item.getKey();
                        btn_send.setEnabled(true);
                        msgRecyclerView.setLayoutManager(new LinearLayoutManager(ChattingroomActivity.this));
                        msgRecyclerView.setAdapter(new RecyclerViewAdapter());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setShopInfo(int pos, String shopname, String location, String chatUid){
        shopPos = pos;
        shopName = shopname;
        shopLocation = location;
        chatUid = chatUid;

        Log.d(TAG, "setShopInfo : " + shopPos + " / " + shopName + " / " + shopLocation + " / " + chatUid);

    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public List<Chatting.Comment> comments;

        public RecyclerViewAdapter(){
            comments = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("chatUsers").child(chatUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chat = snapshot.getValue(Chat.class);

                    chatwith=chat.userName;
                    Log.d(TAG, "chatwith : " + chatwith);
                    getSupportActionBar().setTitle(chatwith);

                    getMessageList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        void getMessageList(){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments");
            valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    comments.clear();
                    Map<String, Object> readUsersMap = new HashMap<>();

                    for (DataSnapshot item : snapshot.getChildren()){
                        String key = item.getKey();
                        Chatting.Comment comment_origin = item.getValue(Chatting.Comment.class);
                        Chatting.Comment comment_modify = item.getValue(Chatting.Comment.class);

                        comment_modify.readUsers.put(uid, true);

                        readUsersMap.put(key, comment_modify);
                        comments.add(comment_origin);
                    }

//                    if(!(comments.get(comments.size() - 1).readUsers.containsKey(uid))) {

                        FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").updateChildren(readUsersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //메세지가 갱신됨
                                notifyDataSetChanged();

                                msgRecyclerView.scrollToPosition(comments.size() - 1);
                            }
                        });
//                    }
//                    else{
//                        //메세지가 갱신됨
//                        notifyDataSetChanged();
//
//                        msgRecyclerView.scrollToPosition(comments.size() - 1);
//                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);


            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = ((MessageViewHolder) holder);

            // 내가 보낸 메세지
            if (comments.get(position).uid.equals(uid)){
                messageViewHolder.txt_msgItem.setText(comments.get(position).message);
                messageViewHolder.txt_msgItem.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.txt_msgItem.setTextSize(15);
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
                setReadCounter(position, messageViewHolder.txt_msgItem_readCounter_left);
            }
            // 상대방이 보낸 메세지
            else {
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.ic_chatuser)
                        .apply(new RequestOptions().circleCrop())
                        .into(messageViewHolder.img_profile);
                messageViewHolder.txt_name.setText(chat.userName);
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHolder.txt_msgItem.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.txt_msgItem.setText(comments.get(position).message);
                messageViewHolder.txt_msgItem.setTextSize(15);
                messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);
                setReadCounter(position, messageViewHolder.txt_msgItem_readCounter_right);

            }

            long unixTime = (long) comments.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            messageViewHolder.timestamp.setText(time);

        }

        void setReadCounter(int position, TextView textView){
            if (peopleCount == 0) {
                FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, Boolean> users = (Map<String, Boolean>) snapshot.getValue();
                        peopleCount = users.size();

                        int count = peopleCount - comments.get(position).readUsers.size();
                        if (count > 0) {
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(String.valueOf(count));
                        } else {
                            textView.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else {
                int count = peopleCount - comments.get(position).readUsers.size();
                if (count > 0) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(String.valueOf(count));
                } else {
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder{
            public TextView txt_msgItem;
            public TextView txt_name;
            public ImageView img_profile;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;
            public TextView timestamp;
            public TextView txt_msgItem_readCounter_left;
            public TextView txt_msgItem_readCounter_right;

            public MessageViewHolder(View view){
                super(view);

                txt_msgItem = view.findViewById(R.id.txt_msgItem);
                txt_name = view.findViewById(R.id.msgItem_txt_name);
                img_profile = view.findViewById(R.id.msgItem_img_profile);
                linearLayout_destination = view.findViewById(R.id.msgItem_linearlayout_destination);
                linearLayout_main = view.findViewById(R.id.msgItem_linearlayout_main);
                timestamp = view.findViewById(R.id.txt_msgTime);
                txt_msgItem_readCounter_left = view.findViewById(R.id.txt_msgItem_readCounter_left);
                txt_msgItem_readCounter_right = view.findViewById(R.id.txt_msgItem_readCounter_right);
            }
        }


    }

    // Toolbar 뒤로가기 버튼 활성화 코드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                databaseReference.removeEventListener(valueEventListener);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void init(){
        tb_back = findViewById(R.id.tb_back);
        btn_send = findViewById(R.id.btn_send);
        edt_chat = findViewById(R.id.edt_chat);
        msgRecyclerView = findViewById(R.id.msgRecyclerView);

    }

    @Override
    public void onBackPressed(){
        databaseReference.removeEventListener(valueEventListener);
        finish();
    }
}