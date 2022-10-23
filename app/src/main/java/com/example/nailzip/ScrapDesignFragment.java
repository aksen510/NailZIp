package com.example.nailzip;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nailzip.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

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

    private RecyclerView recyclerView;
    private ScrapAdapter scrapAdapter;
    private List<Post> postList = new ArrayList<>();
    private List<String> scrapList = new ArrayList<>();
    private String scrapingShopID;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseRemoteConfig mfirebaseRemoteConfig;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

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

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        scrapAdapter = new ScrapAdapter(getContext(), postList);
        recyclerView.setAdapter(scrapAdapter);

        checkFollowing();

        return view;
    }

    private void checkFollowing(){
        scrapList = new ArrayList<>();

        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference();

        reference.child("ScrapDesign")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("scraping")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        scrapList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                            scrapingShopID = dataSnapshot.getKey();
                            Log.d(TAG, "스크랩 아이디 : " + scrapingShopID);

                           scrapList.add(scrapingShopID);

                        }
                        Log.d(TAG, "TEST : " + scrapList);

                        readPosts();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, "TEST2 : " + scrapList);


        reference.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);

                    Log.d(TAG, "Post 저장 성공 : " + scrapList.get(0));

                    for(String id : scrapList){
                        if (post.getPostid().equals(id)){
                            postList.add(post);
                        }
                    }

//                    Log.d(TAG, "Post 저장 성공 : " + postList.get(0));

                }

                scrapAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void init(View view) {
        recyclerView = view.findViewById(R.id.scrapingrecyclerview);

    }
}