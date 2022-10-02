package com.example.nailzip;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nailzip.model.Chatting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ChattingroomActivity extends AppCompatActivity {

    private String TAG = "ChattingroomActivity";

    private Toolbar tb_back;
    private EditText edt_chat;
    private Button btn_send;
    private String chatwith = "닉네임";
    private static String shopName, shopLocation, chatUid;
    private static int shopPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattingroom);

        init();
        chatUid = getIntent().getStringExtra("chatUid");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Chatting chatting = new Chatting();
                chatting.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                chatting.chatUid = chatUid;

                FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatting);
            }
        });

        // Toolbar 활성화
        setSupportActionBar(tb_back);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
        getSupportActionBar().setTitle(chatwith); // Toolbar 제목 제거
    }

    public void setShopInfo(int pos, String shopname, String location, String chatUid){
        shopPos = pos;
        shopName = shopname;
        shopLocation = location;
        chatUid = chatUid;

        Log.d(TAG, "setShopInfo : " + shopPos + " / " + shopName + " / " + shopLocation + " / " + chatUid);

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

    }
}