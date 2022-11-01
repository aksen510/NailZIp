package com.example.nailzip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.utils.widget.ImageFilterButton;

import java.util.Objects;

public class CreateReviewActivity extends AppCompatActivity {

    private static final String TAG = "CreateReviewActivity";

    private Toolbar tb_back;
    private Spinner spinner_score;
    private ImageView btn_star1, btn_star2, btn_star3, btn_star4, btn_star5;
    private EditText edt_reviewContent;
    private String score_review;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);

        init();

        String[] str = getResources().getStringArray(R.array.spinnerScore);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateReviewActivity.this,R.layout.spinner_item, str);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_score.setAdapter(adapter);
        spinner_score.setSelection(10);

        spinner_score.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner_score.getSelectedItemPosition() > 0){

                    //선택된 항목

                    Log.d("알림",spinner_score.getSelectedItem().toString()+ "is selected");
                    Log.d("알림2 : ", String.valueOf(spinner_score.getSelectedItemPosition()) + "is selected");
                    score_review = spinner_score.getSelectedItem().toString();

                    switch (spinner_score.getSelectedItemPosition()) {
                        case 0:

                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_24);
                            break;
                        case 1:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_half_24);
                            break;
                        case 2:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 3:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_half_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 4:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 5:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_half_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 6:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 7:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_half_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 8:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 9:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_half_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 10:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        spinner_score = findViewById(R.id.spinner_score);
        btn_star1 = findViewById(R.id.btn_star1);
        btn_star2 = findViewById(R.id.btn_star2);
        btn_star3 = findViewById(R.id.btn_star3);
        btn_star4 = findViewById(R.id.btn_star4);
        btn_star5 = findViewById(R.id.btn_star5);
        edt_reviewContent = findViewById(R.id.edt_reviewcontent);

    }
}