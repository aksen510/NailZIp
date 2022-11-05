package com.example.nailzip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nailzip.model.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    private String TAG = "ReviewAdapter";
    public Context mContext;
    public List<Review> reviewList = new ArrayList<>();

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    public ReviewAdapter(Context mContext, List<Review> reviewList) {
        this.mContext = mContext;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_review, parent, false);

        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {

        Review review = reviewList.get(position);

        holder.txt_nickname.setText(review.username);
        holder.txt_review.setText(review.content);

        long unixTime = (long) review.timestamp;
        Date date = new Date(unixTime);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String time = simpleDateFormat.format(date);
        holder.txt_reviewtime.setText(time);

        switch (review.starPoint) {

            case 1:
                holder.btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star4.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star5.setImageResource(R.drawable.ic_baseline_star_24);
                break;
            case 2:
                holder.btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                holder. btn_star4.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star5.setImageResource(R.drawable.ic_baseline_star_half_24);
                break;
            case 3:
                holder.btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star4.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                break;
            case 4:
                holder.btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star4.setImageResource(R.drawable.ic_baseline_star_half_24);
                holder.btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                break;
            case 5:
                holder.btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star3.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                break;
            case 6:
                holder.btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star3.setImageResource(R.drawable.ic_baseline_star_half_24);
                holder.btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                break;
            case 7:
                holder.btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star2.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                break;
            case 8:
                holder.btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star2.setImageResource(R.drawable.ic_baseline_star_half_24);
                holder.btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                break;
            case 9:
                holder.btn_star1.setImageResource(R.drawable.ic_baseline_star_24);
                holder.btn_star2.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                break;
            case 10:
                holder.btn_star1.setImageResource(R.drawable.ic_baseline_star_half_24);
                holder.btn_star2.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                break;
            case 11:
                holder.btn_star1.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star2.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                holder.btn_star5.setImageResource(R.drawable.ic_baseline_star_border_24);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txt_nickname, txt_reviewtime, txt_review;
        public ImageView btn_star1, btn_star2, btn_star3, btn_star4, btn_star5;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            txt_nickname = itemView.findViewById(R.id.txt_nickname);
            txt_review = itemView.findViewById(R.id.txt_review);
            txt_reviewtime = itemView.findViewById(R.id.txt_reviewtime);
            btn_star1 = itemView.findViewById(R.id.btn_star1);
            btn_star2 = itemView.findViewById(R.id.btn_star2);
            btn_star3 = itemView.findViewById(R.id.btn_star3);
            btn_star4 = itemView.findViewById(R.id.btn_star4);
            btn_star5 = itemView.findViewById(R.id.btn_star5);
        }
    }

}
