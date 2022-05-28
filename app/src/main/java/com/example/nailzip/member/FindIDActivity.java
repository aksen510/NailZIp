package com.example.nailzip.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nailzip.R;

public class FindIDActivity extends AppCompatActivity {

    private EditText edt_username, edt_phonenum;
    private Button btn_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_idactivity);

        init();

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startfoundActivity = new Intent(FindIDActivity.this, FoundIDActivity.class);
                startActivity(startfoundActivity);
            }
        });
    }

    public void init(){
        edt_username = findViewById(R.id.edt_username);
        edt_phonenum = findViewById(R.id.edt_phonenum);
        btn_complete = (Button) findViewById(R.id.btn_complete);
    }
}