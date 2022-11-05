package com.example.nailzip;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nailzip.model.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoReviewFragment extends Fragment {

    private String TAG = "InfoReviewFragment";

    private FloatingActionButton btn_fab;
    private TextView tv_score, tv_reviewCount;
    private ImageView btn_star1, btn_star2, btn_star3, btn_star4, btn_star5;
    private RecyclerView review_recyclerview;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList = new ArrayList<>();

    private static String shopName;
    private static String shopLocation;
    private static String chatUid;
    private static int shopPos;
    private static String shopUid;
    private static String myUid;

    private int reviewcount = 0;
    private float totalScore = 0;
    private float reviewScore = 0;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    private CreateReviewActivity createReviewActivity = new CreateReviewActivity();

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfoReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoReviewFragment newInstance(String param1, String param2) {
        InfoReviewFragment fragment = new InfoReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_review, container, false);

        init(view);

        Log.d(TAG, "mainShopInfo : " + shopPos + " / " + shopName + " / " + shopLocation);

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

                                readPosts();

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

        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createReviewActivity.setShopInfo(shopPos, shopName, shopLocation, chatUid);

                Intent startCreateReviewActivity = new Intent(getContext(), CreateReviewActivity.class);
                startActivity(startCreateReviewActivity);
            }
        });

        review_recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        review_recyclerview.setLayoutManager(linearLayoutManager);
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(getContext(), reviewList);
        review_recyclerview.setAdapter(reviewAdapter);

        return view;
    }

    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Review").child(shopUid).child("reviewers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewList.clear();
                totalScore = 0;
                reviewcount = 0;
                reviewScore = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Review review = dataSnapshot.getValue(Review.class);
                    reviewList.add(review);

                    Log.d(TAG, "Review 저장 성공 : " + reviewList.get(0));

                }
                Log.d(TAG, "리스트 크기 : " + reviewList.size());

                reviewcount = reviewList.size();

                for (int i = 0; i<reviewcount; i++){
                    totalScore = totalScore + reviewList.get(i).reviewPoint;
                }

                tv_reviewCount.setText("( " + String.valueOf(reviewcount).toString() + " )");
                if (reviewcount != 0){
                    reviewScore = totalScore / reviewcount;
                }
                else {
                    reviewScore = 0;
                }
                tv_score.setText(String.valueOf(reviewScore).toString());

                Log.d(TAG, "총 리뷰 점수 : " + reviewcount + " / " + totalScore + " / " + reviewScore);

                if (reviewScore <= 5.0 && reviewScore > 4.5) {
                    btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star4.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star5.setImageResource(R.drawable.ic_baseline_star_24);
                } else if (reviewScore <= 4.5 && reviewScore > 4.0) {
                    btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star4.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star5.setImageResource(R.drawable.ic_baseline_star_half_24);
                } else if (reviewScore <= 4.0 && reviewScore > 3.5) {
                    btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star4.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                } else if (reviewScore <= 3.5 && reviewScore > 3.0) {
                    btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star4.setImageResource(R.drawable.ic_baseline_star_half_24);
                    btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                } else if (reviewScore <= 3.0 && reviewScore > 2.5) {
                    btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                } else if (reviewScore <= 2.5 && reviewScore > 2.0) {
                    btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star3.setImageResource(R.drawable.ic_baseline_star_half_24);
                    btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                } else if (reviewScore <= 2.0 && reviewScore > 1.5) {
                    btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                } else if (reviewScore <= 1.5 && reviewScore > 1.0) {
                    btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star2.setImageResource(R.drawable.ic_baseline_star_half_24);
                    btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                } else if (reviewScore <= 1.0 && reviewScore > 0.5) {
                    btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_star2.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                } else if (reviewScore <= 0.5 && reviewScore > 0.0) {
                    btn_star1.setImageResource(R.drawable.ic_baseline_star_half_24);
                    btn_star2.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                } else if (reviewScore == 0.0) {
                    btn_star1.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star2.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                }


                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public void init(View view){
        btn_fab = view.findViewById(R.id.btn_fab);
        review_recyclerview = view.findViewById(R.id.review_recyclerview);
        tv_score = view.findViewById(R.id.tv_score);
        tv_reviewCount = view.findViewById(R.id.tv_reviewCount);
        btn_star1 = view.findViewById(R.id.btn_star1);
        btn_star2 = view.findViewById(R.id.btn_star2);
        btn_star3 = view.findViewById(R.id.btn_star3);
        btn_star4 = view.findViewById(R.id.btn_star4);
        btn_star5 = view.findViewById(R.id.btn_star5);

    }
}