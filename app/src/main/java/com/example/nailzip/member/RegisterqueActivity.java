package com.example.nailzip.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nailzip.R;
import com.google.android.material.button.MaterialButton;

public class RegisterqueActivity extends AppCompatActivity {

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
    }

    public void init(){
        btn_shop = (Button) findViewById(R.id.btn_shop);
        btn_customer = (Button) findViewById(R.id.btn_customer);
    }
}