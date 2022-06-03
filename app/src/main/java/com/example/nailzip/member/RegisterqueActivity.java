package com.example.nailzip.member;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.nailzip.R;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class RegisterqueActivity extends AppCompatActivity {

    private Toolbar tb_back;
    private Button btn_shop, btn_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerque);

        init();

        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startRegisterShopActivity = new Intent(RegisterqueActivity.this, RegisterShopActivity.class);
                startActivity(startRegisterShopActivity);
            }
        });

        btn_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startRegisterCustomerActivity = new Intent(RegisterqueActivity.this, RegisterClientActivity.class);
                startActivity(startRegisterCustomerActivity);
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
        tb_back = findViewById(R.id.tb_back);
        btn_shop = (Button) findViewById(R.id.btn_shop);
        btn_customer = (Button) findViewById(R.id.btn_customer);
    }
}