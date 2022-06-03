package com.example.nailzip.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.nailzip.R;

import java.util.Objects;

public class ChangepwActivity extends AppCompatActivity {

    private EditText edt_newpw, edt_pw_check;
    private Button btn_complete;
    private Toolbar tb_changepw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepw);

        init();


        // Toolbar 활성화
        setSupportActionBar(tb_changepw);
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
        tb_changepw = findViewById(R.id.tb_changepw);
        edt_newpw = findViewById(R.id.edt_newpw);
        edt_pw_check = findViewById(R.id.edt_pw_check);
        btn_complete = findViewById(R.id.btn_complete);
    }
}