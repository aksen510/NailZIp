package com.example.nailzip;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nailzip.model.NailshopData;
import com.example.nailzip.model.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NailshopAdapter extends RecyclerView.Adapter<NailshopAdapter.CustomViewHolder> {
    private ArrayList<NailshopData> arrayList;
    private ArrayList<NailshopData> filteredList;
    private String TAG = "NailshopAdapter";
    private Context mContext;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentStateAdapter fragmentStateAdapter;
    private InfoHomeFragment infoHomeFragment = new InfoHomeFragment();
    private InfoMenuFragment infoMenuFragment = new InfoMenuFragment();
    private InfoDesignFragment infoDesignFragment = new InfoDesignFragment();
    private InfoReviewFragment infoReviewFragment = new InfoReviewFragment();
    private ShopInfoActivity shopInfoActivity = new ShopInfoActivity();
    private ChattingroomActivity chattingroomActivity = new ChattingroomActivity();
    private String location = " ";
    private String shopname = " ";
    private String chatUid = " ";
    private int pos = 0;

    private String txtShopName = " ";
    private String shopUid;
    private List<Review> reviewList = new ArrayList<>();
    private int reviewcount = 0;
    private float totalScore = 0;
    private float reviewScore = 0;
    private String score;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public NailshopAdapter(ArrayList<NailshopData> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;


//        this.infoHomeFragment = infoHomeFragment;
    }

    @NonNull
    @Override
    public NailshopAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_shop, parent, false);
        NailshopAdapter.CustomViewHolder holder = new NailshopAdapter.CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NailshopAdapter.CustomViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(arrayList.get(position).img_shop)
                .apply(new RequestOptions().centerInside())
                .into(holder.img_shop);
//        holder.linear_bg.setBackgroundResource(arrayList.get(position).getImg_shop());
//        holder.img_scrab.setImageResource(arrayList.get(position).getImg_scrab());
        holder.tv_shopname.setText(arrayList.get(position).getShopname());
        txtShopName = arrayList.get(position).getShopname();
        holder.tv_rating.setText(arrayList.get(position).getRating());
        holder.tv_ratingcnt.setText(arrayList.get(position).getRatingcnt());
        holder.tv_time.setText(arrayList.get(position).getTime());
        holder.tv_closed.setText(arrayList.get(position).getClosed());
        holder.tv_location.setText(arrayList.get(position).getLocation());

//        findShopUid();

        firestore.collection("users")
                .whereEqualTo("position", 1)
                .whereEqualTo("shopname", txtShopName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                shopUid = document.getId();
                                Log.d(TAG, "가져온 아이디" + shopUid);

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

                                        holder.tv_ratingcnt.setText(String.valueOf(reviewcount).toString());
                                        if (reviewcount != 0){
                                            reviewScore = (float) (Math.round(totalScore / reviewcount * 10) / 10.0);
                                        }
                                        else {
                                            reviewScore = 0;
                                        }
                                        holder.tv_rating.setText(String.valueOf(reviewScore).toString());

                                        Log.d(TAG, "총 리뷰 점수 : " + reviewcount + " / " + totalScore + " / " + reviewScore);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

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


        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = holder.getLayoutPosition();
                shopname = arrayList.get(pos).getShopname();
                location = arrayList.get(pos).getLocation();
                chatUid = arrayList.get(pos).getUid();
                Log.d(TAG, "position and id: " + pos + " / " + shopname + " / " + location);

                if (shopname.isEmpty() == false){
                    Log.d(TAG, "position2 and id2: " + pos + " / " + shopname + " / " + location);
                    infoHomeFragment.setShopInfo(pos, shopname, location, chatUid);
                    shopInfoActivity.setShopInfo(pos, shopname, location, chatUid);
                    chattingroomActivity.setShopInfo(pos, shopname, location, chatUid);
                    infoMenuFragment.setShopInfo(pos, shopname, location, chatUid);
                    infoDesignFragment.setShopInfo(pos, shopname, location, chatUid);
                    infoReviewFragment.setShopInfo(pos, shopname, location, chatUid);

                    Intent startInformation = new Intent(v.getContext(), ShopInfoActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    v.getContext().startActivity(startInformation);

                    Toast.makeText(v.getContext(), "네일 상세 정보 확인", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

//    private void findShopUid(){

//        firestore.collection("users")
//                .whereEqualTo("position", 1)
//                .whereEqualTo("shopname", txtShopName)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for (QueryDocumentSnapshot document : task.getResult()){
//                                shopUid = document.getId();
//                                Log.d(TAG, "가져온 아이디" + shopUid);
//
//                            }
//                        }
//                        else{
//                            Log.d(TAG, "아이디 가져오기 실패");
//
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });


//    }


    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position){
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

//    //검색기능
//    public Filter getFilter(){
//        return exampleFilter;
//    }
//
//    private Filter exampleFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            filteredList.clear();
//
//            String filterPattern = constraint.toString().toLowerCase().trim();
//            Log.d(TAG, "검색 0 : " + filterPattern);
//
//            for (NailshopData item : arrayList){
//                if(item.getShopname().toLowerCase().contains(filterPattern)){
//                    filteredList.add(item);
//                    Log.d(TAG, "검색 1 : " + item);
//                    Log.d(TAG, "검색 2 : " + filteredList.toString());
//
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//
//            arrayList.clear();
//            arrayList.addAll((ArrayList) results.values);
//            notifyDataSetChanged();
//        }
//    };


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout linear_bg;
        private ImageView img_shop, img_scrap;
        private TextView tv_shopname, tv_rating, tv_ratingcnt, tv_time, tv_closed, tv_location;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

//            linear_bg = itemView.findViewById(R.id.linear_bg);
//            img_scrab = itemView.findViewById(R.id.img_scrab);
            img_shop = itemView.findViewById(R.id.img_shop);
            tv_shopname = itemView.findViewById(R.id.tv_shopname);
            tv_rating = itemView.findViewById(R.id.tv_rating);
            tv_ratingcnt = itemView.findViewById(R.id.tv_ratingcnt);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_closed = itemView.findViewById(R.id.tv_closed);
            tv_location = itemView.findViewById(R.id.tv_location);


        }
    }
}
