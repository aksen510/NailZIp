package com.example.nailzip.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.nailzip.MainActivity;
import com.example.nailzip.R;
import com.google.android.material.button.MaterialButton;

public class StartActivity extends AppCompatActivity {

    private EditText edt_id, edt_pw;
    private Button btn_login, btn_register;
    private TextView txt_findID, txt_findPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        init();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent starMainActivity = new Intent(StartActivity.this, MainActivity.class);
                startActivity(starMainActivity);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startRegisterqueActivity = new Intent(StartActivity.this, RegisterqueActivity.class);
                startActivity(startRegisterqueActivity);
            }
        });

        txt_findID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startFindIDActivity = new Intent(StartActivity.this, FindIDActivity.class);
                startActivity(startFindIDActivity);
            }
        });

        txt_findPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startFindPWActivity = new Intent(StartActivity.this, FindPwActivity.class);
                startActivity(startFindPWActivity);
            }
        });
    }

    public void init(){
        edt_id = findViewById(R.id.edt_id);
        edt_pw = findViewById(R.id.edt_pw);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_register = (Button)findViewById(R.id.btn_register);
        txt_findID = findViewById(R.id.txt_findID);
        txt_findPW = findViewById(R.id.txt_findPW);
    }
}