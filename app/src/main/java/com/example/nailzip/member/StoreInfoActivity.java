package com.example.nailzip.member;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.nailzip.MainActivity;
import com.example.nailzip.R;
import com.example.nailzip.model.NailshopData;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class StoreInfoActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 10;
    //Todo: 가격 추가
    private EditText edt_shopname, edt_opentime, edt_shopphonenum, edt_address, edt_price_nail, edt_price_pedi, edt_memocontent, edt_closed;
    private Button btn_next;
    private ImageView img_shop;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    Uri imageUri;
    String shopUri="";
    StorageTask uploadTask;
    private MemberViewModel memberViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        init();

        img_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        edt_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 주소 검색 웹뷰 화면으로 이동
                Intent startSearchAddress = new Intent(StoreInfoActivity.this, SearchAddressActivity.class);
                getSearchResult.launch(startSearchAddress);
                //startActivity(startSearchAddress);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopname = edt_shopname.getText().toString().trim();
                String opentime = edt_opentime.getText().toString().trim();
                String shopphonenum = edt_shopphonenum.getText().toString().trim();
                String address = edt_address.getText().toString().trim();
                String price_nail = edt_price_nail.getText().toString().trim();
                String price_pedi = edt_price_pedi.getText().toString().trim();
                String memocontent = edt_memocontent.getText().toString().trim();
                String closed = edt_closed.getText().toString().trim();

//                FirebaseStorage.getInstance().getReference().child("shopImages").child(firebaseAuth.getCurrentUser().getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                        @SuppressWarnings("VisibleForTests")
//                        task.continueWithTask()
//                    }
//                });
                storageReference = FirebaseStorage.getInstance().getReference("shops");

                if (shopname.isEmpty() || opentime.isEmpty() || shopphonenum.isEmpty() || address.isEmpty() || price_nail.isEmpty() || price_pedi.isEmpty() || memocontent.isEmpty() || closed.isEmpty() || imageUri == null) {
                    Toast.makeText(StoreInfoActivity.this, "빈 칸을 채워주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    StorageReference filereference = storageReference.child("shopImages").child(firebaseAuth.getCurrentUser().getUid());

                    uploadTask = filereference.putFile(imageUri);
                    uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }

                            return filereference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Uri downloadUri = (Uri) task.getResult();
                                shopUri = downloadUri.toString();

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                                String shopid = reference.push().getKey();

                                NailshopData shopAccount = new NailshopData();
                                shopAccount.setShopname(shopname);
                                shopAccount.setTime(opentime);
                                shopAccount.setClosed(closed);
                                shopAccount.setShopphone(shopphonenum);
                                shopAccount.setLocation(address);
                                shopAccount.setPrice_nail(price_nail);
                                shopAccount.setPrice_pedi(price_pedi);
                                shopAccount.setMemo(memocontent);
                                shopAccount.setImg_shop(String.valueOf(shopUri));
                                shopAccount.setUid(shopid);

                                //Todo: 추후 삽입
//                    shopAccount.setImg_scrab(R.drawable.ic_baseline_bookmark_white);
                                shopAccount.setRating("0");
                                shopAccount.setRatingcnt("0");
//                    shopAccount.setPrice_nail("0");
//                    shopAccount.setPrice_pedi("0");

                                reference.child("ShopList").child(shopid).setValue(shopAccount);


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
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(StoreInfoActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

//                if (shopname.isEmpty() || opentime.isEmpty() || shopphonenum.isEmpty() || address.isEmpty() || price_nail.isEmpty() || price_pedi.isEmpty() || memocontent.isEmpty() || closed.isEmpty()) {
//                    Toast.makeText(StoreInfoActivity.this, "빈 칸을 채워주세요", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                else {
//
//                    NailshopData shopAccount = new NailshopData();
//                    shopAccount.setShopname(shopname);
//                    shopAccount.setTime(opentime);
//                    shopAccount.setClosed(closed);
//                    shopAccount.setShopphone(shopphonenum);
//                    shopAccount.setLocation(address);
//                    shopAccount.setPrice_nail(price_nail);
//                    shopAccount.setPrice_pedi(price_pedi);
//                    shopAccount.setMemo(memocontent);
//                    shopAccount.setImg_shop(String.valueOf(imageUri));
//                    shopAccount.setUid(firebaseAuth.getCurrentUser().getUid());
//
//                    //Todo: 추후 삽입
////                    shopAccount.setImg_scrab(R.drawable.ic_baseline_bookmark_white);
//                    shopAccount.setRating("0");
//                    shopAccount.setRatingcnt("0");
////                    shopAccount.setPrice_nail("0");
////                    shopAccount.setPrice_pedi("0");
//
//                    final ProgressDialog mDialog = new ProgressDialog(StoreInfoActivity.this);
//
//                    memberViewModel.storeInfo(shopAccount);
//                    mDialog.setMessage("등록중입니다");
//                    mDialog.show();
//
//                    mDialog.dismiss();
//                    Intent startMain = new Intent(StoreInfoActivity.this, MainActivity.class);
//                    startActivity(startMain);
//                    finish();
//                }
            }
        });

    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // SearchAddressActivity로부터의 결과 값이 이곳으로 전달됨 (setResult에 의해)
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        String data = result.getData().getStringExtra("data");
                        edt_address.setText(data);
                    }
                }
            }
    );



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            img_shop.setImageURI(data.getData());   // 가운데 뷰를 바꿈
            imageUri = data.getData();  //이미지 경로 원본
        }
    }

    public void init(){
        img_shop = findViewById(R.id.img_shop);
        edt_shopname = findViewById(R.id.edt_shopname);
        edt_opentime = findViewById(R.id.edt_opentime);
        edt_shopphonenum = findViewById(R.id.edt_shopphonenum);
        edt_address = findViewById(R.id.edt_address);
        edt_price_nail = findViewById(R.id.edt_price_nail);
        edt_price_pedi = findViewById(R.id.edt_price_pedi);
        edt_memocontent = findViewById(R.id.edt_memocontent);
        edt_closed = findViewById(R.id.edt_closed);
        btn_next = findViewById(R.id.btn_next);

    }
}