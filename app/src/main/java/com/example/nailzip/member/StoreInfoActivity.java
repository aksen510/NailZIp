package com.example.nailzip.member;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nailzip.MainActivity;
import com.example.nailzip.model.NailshopData;
import com.example.nailzip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class StoreInfoActivity extends AppCompatActivity {

    //Todo: 가격 추가
    private EditText edt_shopname, edt_opentime, edt_shopphonenum, edt_address, edt_memocontent, edt_closed;
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
                String closed = edt_closed.getText().toString().trim();

                if (shopname.isEmpty() || opentime.isEmpty() || shopphonenum.isEmpty() || address.isEmpty() || memocontent.isEmpty() || closed.isEmpty()) {
                    Toast.makeText(StoreInfoActivity.this, "빈 칸을 채워주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                    NailshopData shopAccount = new NailshopData();
                    shopAccount.setShopname(shopname);
                    shopAccount.setTime(opentime);
                    shopAccount.setClosed(closed);
                    shopAccount.setShopphone(shopphonenum);
                    shopAccount.setLocation(address);
                    shopAccount.setMemo(memocontent);

                    //Todo: 추후 삽입
                    shopAccount.setImg_shop(R.drawable.edge);
                    shopAccount.setImg_scrab(R.drawable.ic_baseline_bookmark_white);
                    shopAccount.setRating("0");
                    shopAccount.setRatingcnt("0");
                    shopAccount.setPrice_nail("0");
                    shopAccount.setPrice_pedi("0");

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
        edt_closed = findViewById(R.id.edt_closed);
        btn_next = findViewById(R.id.btn_next);

    }
}