package com.example.nailzip.member;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nailzip.MainActivity;
import com.example.nailzip.R;
import com.example.nailzip.model.Nailshop;
import com.example.nailzip.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class StoreInfoActivity extends AppCompatActivity {

    //Todo: 가격 추가
    private EditText edt_shopname, edt_opentime, edt_shopphonenum, edt_address, edt_memocontent;
    private Button btn_next;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private MemberViewModel memberViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        init();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopname = edt_shopname.getText().toString().trim();
                String opentime = edt_opentime.getText().toString().trim();
                String shopphonenum = edt_shopphonenum.getText().toString().trim();
                String address = edt_address.getText().toString().trim();
                String memocontent = edt_memocontent.getText().toString().trim();

                if (shopname.isEmpty() || opentime.isEmpty() || shopphonenum.isEmpty() || address.isEmpty() || memocontent.isEmpty()) {
                    Toast.makeText(StoreInfoActivity.this, "빈 칸을 채워주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Nailshop shopAccount = new Nailshop();
                    shopAccount.setShopname(shopname);
                    shopAccount.setOpentime(opentime);
                    shopAccount.setShopphone(shopphonenum);
                    shopAccount.setAddress(address);
                    shopAccount.setMemo(memocontent);

                    final ProgressDialog mDialog = new ProgressDialog(StoreInfoActivity.this);

                    memberViewModel.storeInfo(shopAccount);
                    mDialog.setMessage("등록중입니다");
                    mDialog.show();

                    mDialog.dismiss();
                    Intent startMain = new Intent(StoreInfoActivity.this, MainActivity.class);
                    startActivity(startMain);
                    finish();
                }
            }
        });

    }

    public void init(){
        edt_shopname = findViewById(R.id.edt_shopname);
        edt_opentime = findViewById(R.id.edt_opentime);
        edt_shopphonenum = findViewById(R.id.edt_shopphonenum);
        edt_address = findViewById(R.id.edt_address);
        edt_memocontent = findViewById(R.id.edt_memocontent);
        btn_next = findViewById(R.id.btn_next);

    }
}