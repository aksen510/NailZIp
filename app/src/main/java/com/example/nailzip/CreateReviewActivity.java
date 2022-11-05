package com.example.nailzip;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nailzip.model.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class CreateReviewActivity extends AppCompatActivity {

    private static final String TAG = "CreateReviewActivity";

    private Toolbar tb_back;
    private Spinner spinner_score;
    private ImageView btn_star1, btn_star2, btn_star3, btn_star4, btn_star5;
    private EditText edt_reviewContent;
    private Button btn_complete;
    private float score_review;
    private int starPoint;

    private static String shopName;
    private static String shopLocation;
    private static String chatUid;
    private static int shopPos;
    private static String shopUid;
    private static String myUid;
    private static String userName;
//    private List<String> followingList = new ArrayList<>();

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);

        init();

        Log.d(TAG, "mainShopInfo : " + shopPos + " / " + shopName + " / " + shopLocation);

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userUid = user.getUid();
        String uid = user.getEmail();

        firestore.collection("users")
                .whereEqualTo("email",uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.get("username") + " / " + document.get("position"));
                                userName = document.get("username").toString();
//                                tv_nickname.setText(nickname);
                            }
                            if (userName == null) {
                                Log.d(TAG, "일치하는 회원정보가 없습니다.");
                                Toast.makeText(CreateReviewActivity.this, "일치하는 회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                Log.d(TAG, "사용자 이름 : " + userName);
                            }
                        } else {
                            Log.d(TAG, "일치하는 회원정보가 없습니다.");
                            Toast.makeText(CreateReviewActivity.this, "일치하는 회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "일치하는 회원정보가 없습니다.");
                        Toast.makeText(CreateReviewActivity.this, "일치하는 회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

//        firebaseDatabase.getReference().child("chatUsers").child(myUid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot item : snapshot.getChildren()){
//                    Chat chat = item.getValue(Chat.class);
//
//                    userName = chat.userName;
//                    Log.d(TAG, "리뷰 작성자 이름 : " + userName);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        firestore.collection("users")
                .whereEqualTo("position", 1)
                .whereEqualTo("shopname", shopName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                shopUid = document.getId();
                                Log.d(TAG, "가져온 아이디" + shopUid);
                            }
                        }
                        else{
                            Log.d(TAG, "아이디 가져오기 실패");

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        String[] str = getResources().getStringArray(R.array.spinnerScore);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateReviewActivity.this,R.layout.spinner_item, str);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_score.setAdapter(adapter);
        spinner_score.setSelection(11);

        spinner_score.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner_score.getSelectedItemPosition() > 0){

                    //선택된 항목

                    Log.d("알림",spinner_score.getSelectedItem().toString()+ "is selected");
                    Log.d("알림2 : ", String.valueOf(spinner_score.getSelectedItemPosition()) + "is selected");
                    score_review = Float.parseFloat(spinner_score.getSelectedItem().toString());
                    starPoint = spinner_score.getSelectedItemPosition();

                    switch (spinner_score.getSelectedItemPosition()) {

                        case 1:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_24);
                            break;
                        case 2:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_half_24);
                            break;
                        case 3:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 4:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_half_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 5:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 6:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_half_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 7:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 8:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_half_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 9:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 10:
                            btn_star1.setImageResource(R.drawable.ic_baseline_star_half_24);
                            btn_star2.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                            btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                            break;
                        case 11:
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

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_reviewContent.getContext().toString().equals("")){
                    Toast.makeText(CreateReviewActivity.this, "리뷰 내용을 채워주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    addReview();
                }

            }
        });


        // Toolbar 활성화
        setSupportActionBar(tb_back);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
        getSupportActionBar().setTitle(null); // Toolbar 제목 제거

    }

    private void addReview(){
        Review review = new Review();
        review.uid = myUid;
        review.username = userName;
        review.content = edt_reviewContent.getText().toString();
        review.starPoint = starPoint;
        review.reviewPoint = score_review;
        review.timestamp = ServerValue.TIMESTAMP;

        firebaseDatabase.getReference().child("Review").child(shopUid).child("reviewers").push().setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CreateReviewActivity.this, "리뷰 등록 성공!", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }

    public void setShopInfo(int pos, String shopname, String location, String chatUid){
        shopPos = pos;
        shopName = shopname;
        shopLocation = location;
        chatUid = chatUid;

        Log.d(TAG, "setShopInfo : " + shopPos + " / " + shopName + " / " + shopLocation + " / " + chatUid);

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
        btn_complete = findViewById(R.id.btn_complete);

    }
}