package com.example.nailzip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nailzip.model.NailshopData;
import com.example.nailzip.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScrapDesignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScrapDesignFragment extends Fragment {

    private String TAG = "ScrapDesignFragment";

    private Toolbar tb_back;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseRemoteConfig mfirebaseRemoteConfig;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    private ArrayList<Post> posts = new ArrayList<>();
    private ArrayList<String> scrapLists = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScrapDesignFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScrapDesignFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScrapDesignFragment newInstance(String param1, String param2) {
        ScrapDesignFragment fragment = new ScrapDesignFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scrap_design, container, false);

        init(view);

        RecyclerView recyclerView = view.findViewById(R.id.scrapingrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new ScrapDesignFragmentRecyclerViewAdapter());

        mfirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();


        return view;
    }


    class ScrapDesignFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public ScrapDesignFragmentRecyclerViewAdapter() {
            scrapLists = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("ScrapDesign").child(firebaseUser.getUid()).child("scraping").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    scrapLists.clear();

                    for (DataSnapshot item : snapshot.getChildren()){
                        scrapLists.add(item.getKey());
//                        saveFollowingUid = item.getValue().toString();
                        Log.d(TAG, "스크랩 아이디 : " + item.getKey());

                    }

                    notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "error1");

                }
            });


        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            CustomViewHolder customViewHolder = (CustomViewHolder) holder;
            posts = new ArrayList<>();

            Log.d(TAG, "scrapLists: " + scrapLists.get(position));

            DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("Posts");
            posts.clear();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    posts.clear();

                    for (DataSnapshot item : snapshot.getChildren()){
//                        posts.add(item.getValue(Post.class));
                        Post postList = item.getValue(Post.class);

                        for (String id : scrapLists){
                            if(postList.getPostid().equals(id)){
                                posts.add(postList);
                            }
                        }
//                        posts.add(postList);

                        Log.d(TAG, "테스트 : " + postList);

                        Log.d(TAG, "포스트 아이디 : " + item.getKey());


                    }

                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

//            Glide.with(customViewHolder.itemView.getContext())
//                    .load(posts.get(position).getPostimage())
//                    .apply(new RequestOptions().centerInside())
//                    .into(customViewHolder.img_design);
//            Log.d(TAG, "이미지 링크 : " + posts.get(position).getPostimage());
//
//            if(posts.get(position).getDescription().equals("")){
//                customViewHolder.txt_description.setVisibility(View.GONE);
//            }
//            else{
//                customViewHolder.txt_description.setVisibility(View.VISIBLE);
//                customViewHolder.txt_description.setText(posts.get(position).getDescription());
//            }
//
//            shopInfo(customViewHolder.img_profile, customViewHolder.txt_shopname, customViewHolder.txt_shopname_inPost, posts.get(position).getPublisher());
//
//            isScrap(posts.get(position).getPostid(), customViewHolder.btn_post_scrap);
//
//
//
//            // 팔로우 버튼
//            // TODO : 스크랩버튼, 예약버튼 추가
//
//            FirebaseDatabase.getInstance().getReference().child("ScrapDesign").child(firebaseUser.getUid()).child("scraping").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot item : snapshot.getChildren()){
////                    saveFollowingUid.add(item.getValue().toString());
//                        String saveScrapPostUid = item.getKey();
//                        Log.d(TAG, "포스트 아이디 : " + saveScrapPostUid);
//
//                        if (posts.get(position).getPostid().equals(saveScrapPostUid)){
//                            customViewHolder.btn_post_scrap.setBackgroundResource(R.drawable.bookmark_fill);
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Log.d(TAG, "error");
//
//                }
//            });
//
//            customViewHolder.btn_post_scrap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(customViewHolder.btn_post_scrap.getText().toString().equals("scrap")){
//                        firebaseDatabase.getReference().child("ScrapDesign").child(firebaseAuth.getUid())
//                                .child("scraping").child(posts.get(position).getPostid()).setValue(posts.get(position).getPublisher());
////                    firebaseDatabase.getReference().child("Follow").child(follow_shop_id)
////                            .child("followers").child(firebaseAuth.getUid()).setValue(true);
//                        customViewHolder.btn_post_scrap.setBackgroundResource(R.drawable.bookmark_fill);
//                    }
//                    else{
//                        firebaseDatabase.getReference().child("ScrapDesign").child(firebaseAuth.getUid())
//                                .child("scraping").child(posts.get(position).getPostid()).removeValue();
////                    firebaseDatabase.getReference().child("Follow").child(follow_shop_id)
////                            .child("followers").child(firebaseAuth.getUid()).removeValue();
//                        customViewHolder.btn_post_scrap.setBackgroundResource(R.drawable.bookmark_border);
//                    }
//                }
//            });

//            reference.child("Posts").child(scrapLists.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    posts.clear();
//
//                    for (DataSnapshot item : snapshot.getChildren()){
//                        posts.add(item.getValue(Post.class));
//
//                        Log.d(TAG, "포스트 아이디 : " + item.getKey());
//
//                        Glide.with(customViewHolder.itemView.getContext())
//                                .load(posts.get(position).getPostimage())
//                                .apply(new RequestOptions().centerInside())
//                                .into(customViewHolder.img_design);
//                        Log.d(TAG, "이미지 링크 : " + posts.get(position).getPostimage());
//
//                        if(posts.get(position).getDescription().equals("")){
//                            customViewHolder.txt_description.setVisibility(View.GONE);
//                        }
//                        else{
//                            customViewHolder.txt_description.setVisibility(View.VISIBLE);
//                            customViewHolder.txt_description.setText(posts.get(position).getDescription());
//                        }
//
//                        shopInfo(customViewHolder.img_profile, customViewHolder.txt_shopname, customViewHolder.txt_shopname_inPost, posts.get(position).getPublisher());
//
//                        isScrap(posts.get(position).getPostid(), customViewHolder.btn_post_scrap);
//
//
//
//                        // 팔로우 버튼
//                        // TODO : 스크랩버튼, 예약버튼 추가
//
//                        FirebaseDatabase.getInstance().getReference().child("ScrapDesign").child(firebaseUser.getUid()).child("scraping").addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for (DataSnapshot item : snapshot.getChildren()){
////                    saveFollowingUid.add(item.getValue().toString());
//                                    String saveScrapPostUid = item.getKey();
//                                    Log.d(TAG, "포스트 아이디 : " + saveScrapPostUid);
//
//                                    if (posts.get(position).getPostid().equals(saveScrapPostUid)){
//                                        customViewHolder.btn_post_scrap.setBackgroundResource(R.drawable.bookmark_fill);
//                                    }
//
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                Log.d(TAG, "error");
//
//                            }
//                        });
//
//                        customViewHolder.btn_post_scrap.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if(customViewHolder.btn_post_scrap.getText().toString().equals("scrap")){
//                                    firebaseDatabase.getReference().child("ScrapDesign").child(firebaseAuth.getUid())
//                                            .child("scraping").child(posts.get(position).getPostid()).setValue(posts.get(position).getPublisher());
////                    firebaseDatabase.getReference().child("Follow").child(follow_shop_id)
////                            .child("followers").child(firebaseAuth.getUid()).setValue(true);
//                                    customViewHolder.btn_post_scrap.setBackgroundResource(R.drawable.bookmark_fill);
//                                }
//                                else{
//                                    firebaseDatabase.getReference().child("ScrapDesign").child(firebaseAuth.getUid())
//                                            .child("scraping").child(posts.get(position).getPostid()).removeValue();
////                    firebaseDatabase.getReference().child("Follow").child(follow_shop_id)
////                            .child("followers").child(firebaseAuth.getUid()).removeValue();
//                                    customViewHolder.btn_post_scrap.setBackgroundResource(R.drawable.bookmark_border);
//                                }
//                            }
//                        });
//
//                    }
//
//                    notifyDataSetChanged();
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Log.d(TAG, "error1");
//
//                }
//            });




        }

        private void isScrap (final String postid, final Button button){


            DatabaseReference reference = firebaseDatabase.getReference()
                    .child("ScrapDesign").child(firebaseAuth.getCurrentUser().getUid()).child("scraping");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(postid).exists()){
                        button.setText("scraping");
                    }
                    else{
                        button.setText("scrap");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        @Override
        public int getItemCount() {
            Log.d(TAG, "테스트 4 : " + scrapLists.size());
            return (null != scrapLists ? scrapLists.size() : 0);
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView img_profile, img_design;
            public Button btn_post_scrap, btn_reservation;
            public TextView txt_shopname, txt_shopname_inPost, txt_description;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);

                img_profile = itemView.findViewById(R.id.post_img_profile);
                img_design = itemView.findViewById(R.id.img_design);
                btn_post_scrap = itemView.findViewById(R.id.btn_post_scrap);
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

        }

    }


    public void init(View view) {

    }
}