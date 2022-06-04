package com.example.nailzip.member;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nailzip.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FoundIDActivity extends AppCompatActivity {

    private TextView txt_foundId;
    private Button btn_complete;
    private Toolbar tb_back;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_idactivity);

        init();

        Intent intent = getIntent();
        txt_foundId.setText(intent.getStringExtra("email"));

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startloginActivity = new Intent(FoundIDActivity.this, StartActivity.class);
                startActivity(startloginActivity);
                finish();
            }
        });

        // Toolbar 활성화
        setSupportActionBar(tb_back);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
        getSupportActionBar().setTitle(null); // Toolbar 제목 제거
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
        txt_foundId = findViewById(R.id.txt_foundID);
        btn_complete = (Button) findViewById(R.id.btn_complete);
        tb_back = findViewById(R.id.tb_back);
    }
}