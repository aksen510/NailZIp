package com.example.nailzip.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nailzip.R;

public class FoundIDActivity extends AppCompatActivity {

    private TextView txt_foundId;
    private Button btn_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_idactivity);

        init();

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startloginActivity = new Intent(FoundIDActivity.this, StartActivity.class);
                startActivity(startloginActivity);
            }
        });
    }

    public void init(){
        txt_foundId = findViewById(R.id.txt_foundID);
        btn_complete = (Button) findViewById(R.id.btn_complete);
    }
}