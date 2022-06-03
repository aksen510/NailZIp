package com.example.nailzip.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nailzip.MainActivity;
import com.example.nailzip.R;
import com.google.android.material.button.MaterialButton;

public class RegisterShopActivity extends AppCompatActivity {

    private EditText edt_shopname, edt_ceoname, edt_email, edt_pw, edt_checkPw, edt_phonenum, edt_address, edt_shopphonenum;
    private Button btn_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop);

        init();

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startMainActivity = new Intent(RegisterShopActivity.this, MainActivity.class);
                startActivity(startMainActivity);
            }
        });
    }

    public void init(){
        edt_shopname = findViewById(R.id.edt_shopname);
        edt_ceoname = findViewById(R.id.edt_ceoname);
        edt_email = findViewById(R.id.edt_email);
        edt_pw = findViewById(R.id.edt_pw);
        edt_checkPw = findViewById(R.id.edt_pw_check);
        edt_phonenum = findViewById(R.id.edt_phonenum);
        edt_shopphonenum = findViewById(R.id.edt_shopphonenum);
        edt_address = findViewById(R.id.edt_address);
        btn_complete = (Button) findViewById(R.id.btn_complete);
    }
}