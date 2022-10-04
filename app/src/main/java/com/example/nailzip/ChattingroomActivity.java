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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private RecyclerView msgRecyclerView;

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
                    Log.d(TAG, "메세지 내용 : " + comment.message);
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
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
        getSupportActionBar().setTitle(chatwith); // Toolbar 제목 제거
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
        public Chat chat = new Chat();

        public RecyclerViewAdapter(){
            comments = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("chatUsers").child(chatUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chat = snapshot.getValue(Chat.class);

                    getMessageList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        void getMessageList(){
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    comments.clear();

                    for (DataSnapshot item : snapshot.getChildren()){
                        comments.add(item.getValue(Chatting.Comment.class));
                    }
                    //메세지가 갱신됨
                    notifyDataSetChanged();

                    msgRecyclerView.scrollToPosition(comments.size() - 1);
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

            public MessageViewHolder(View view){
                super(view);

                txt_msgItem = view.findViewById(R.id.txt_msgItem);
                txt_name = view.findViewById(R.id.msgItem_txt_name);
                img_profile = view.findViewById(R.id.msgItem_img_profile);
                linearLayout_destination = view.findViewById(R.id.msgItem_linearlayout_destination);
                linearLayout_main = view.findViewById(R.id.msgItem_linearlayout_main);
            }
        }


    }

    // Toolbar 뒤로가기 버튼 활성화 코드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
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
}