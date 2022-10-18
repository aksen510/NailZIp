package com.example.nailzip;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nailzip.model.NailshopData;
import com.example.nailzip.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private String TAG = "PostAdapter";
    public Context mContext;
    public List<Post> mPost = new ArrayList<>();

    private FirebaseUser firebaseUser;
    private String saveShopname;

    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post_detail, parent, false);

        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = mPost.get(position);

        Glide.with(mContext).load(post.getPostimage()).into(holder.img_design);

        if(post.getDescription().equals("")){
            holder.txt_description.setVisibility(View.GONE);
        }
        else{
            holder.txt_description.setVisibility(View.VISIBLE);
            holder.txt_description.setText(post.getDescription());
        }

        Log.d(TAG, "게시글 작성자 정보 : " + post.getPublisher());
        shopInfo(holder.img_profile, holder.txt_shopname, holder.txt_shopname_inPost, post.getPublisher());


        // 팔로우 버튼
        // TODO : 스크랩버튼, 예약버튼 추가

//        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot item : snapshot.getChildren()){
////                    saveFollowingUid.add(item.getValue().toString());
//                    String saveFollowingUid = item.getValue().toString();
//                    Log.d(TAG, "네일샵 이름 : " + saveFollowingUid);
//
//                    if (holder.txt_shopname.equals(saveFollowingUid)){
//                        btn_scrab.setBackgroundResource(R.drawable.bookmark_fill);
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d(TAG, "error");
//
//            }
//        });
//

//        holder.btn_post_scrab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(btn_scrab.getText().toString().equals("follow")){
//                    firebaseDatabase.getReference().child("Follow").child(firebaseAuth.getUid())
//                            .child("following").child(follow_shop_id).setValue(follow_shop_name);
//                    firebaseDatabase.getReference().child("Follow").child(follow_shop_id)
//                            .child("followers").child(firebaseAuth.getUid()).setValue(true);
//                    btn_scrab.setBackgroundResource(R.drawable.bookmark_fill);
//                }
//                else{
//                    firebaseDatabase.getReference().child("Follow").child(firebaseAuth.getUid())
//                            .child("following").child(follow_shop_id).removeValue();
//                    firebaseDatabase.getReference().child("Follow").child(follow_shop_id)
//                            .child("followers").child(firebaseAuth.getUid()).removeValue();
//                    btn_scrab.setBackgroundResource(R.drawable.bookmark_border);
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView img_profile, img_design;
        public Button btn_post_scrab, btn_reservation;
        public TextView txt_shopname, txt_shopname_inPost, txt_description;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            img_profile = itemView.findViewById(R.id.post_img_profile);
            img_design = itemView.findViewById(R.id.img_design);
            btn_post_scrab = itemView.findViewById(R.id.btn_post_scrab);
            btn_reservation = itemView.findViewById(R.id.btn_reservation);
            txt_shopname = itemView.findViewById(R.id.txt_shopname);
            txt_shopname_inPost = itemView.findViewById(R.id.txt_shopname_inPost);
            txt_description = itemView.findViewById(R.id.txt_description);

        }
    }

    private void shopInfo(ImageView img_profile, TextView shopname, TextView shopname_inPost, String shopId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("users")
                        .document(shopId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        NailshopData nailshopData = value.toObject(NailshopData.class);
//                Glide.with(mContext).load(nailshopData.getImg_shop()).into(img_profile);
                        shopname.setText(nailshopData.getShopname());
                        shopname_inPost.setText(nailshopData.getShopname());
                    }
                });

//        reference.child("ShopList").child(shopId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                NailshopData nailshopData = snapshot.getValue(NailshopData.class);
////                Glide.with(mContext).load(nailshopData.getImg_shop()).into(img_profile);
//                shopname.setText(nailshopData.getShopname());
//                shopname_inPost.setText(nailshopData.getShopname());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}
