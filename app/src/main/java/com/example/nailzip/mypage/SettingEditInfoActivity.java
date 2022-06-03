package com.example.nailzip.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nailzip.R;
import com.example.nailzip.member.StartActivity;

import java.util.Objects;

public class SettingEditInfoActivity extends AppCompatActivity {

    private Toolbar tb_setting;
    private ListView lv_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_edit_info);

        init();

        final String[] mid = {"비밀번호 변경", "로그아웃"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SettingEditInfoActivity.this, android.R.layout.simple_list_item_1, mid);
        lv_setting.setAdapter(adapter);

        lv_setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //switch ()
                switch (i){
                    case 0:
                        Intent startEditInfoActivity = new Intent(SettingEditInfoActivity.this, ChangepwActivity.class);
                        startActivity(startEditInfoActivity);
                        break;

                    case 1:
                        new AlertDialog.Builder(SettingEditInfoActivity.this)
                                .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //firebaseAuth.signOut();
                                        // requireActivity().finish();
                                        Intent startInitialActivity = new Intent(SettingEditInfoActivity.this, StartActivity.class);
                                        startActivity(startInitialActivity);
                                        finish();

                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                        break;

                }
            }
        });

        // Toolbar 활성화
        setSupportActionBar(tb_setting);
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
        tb_setting = findViewById(R.id.tb_setting);
        lv_setting = findViewById(R.id.listview);
    }
}