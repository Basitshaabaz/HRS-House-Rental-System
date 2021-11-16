package com.example.hrshouserentalsystem.ViewHolder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hrshouserentalsystem.R;
import com.google.android.material.imageview.ShapeableImageView;

//************************************************************
public class CategoryDetailViewHolder extends RecyclerView.ViewHolder
//************************************************************
{
    public ShapeableImageView iv_Category;
    public TextView tvMainTitle, tvSubTitle, tvReviews, tvRoomCount, tvBathroomCount, tvWashingRoomCount, tvPrice;
    public CardView cardView;

    //************************************************************
    public CategoryDetailViewHolder(@NonNull View itemView)
    //************************************************************
    {
        super(itemView);
        tvMainTitle = itemView.findViewById(R.id.tv_main_title);
        tvSubTitle = itemView.findViewById(R.id.tv_sub_title);
        tvReviews = itemView.findViewById(R.id.tv_reviews);
        tvRoomCount = itemView.findViewById(R.id.tv_room_count);
        tvBathroomCount = itemView.findViewById(R.id.tv_bathroom_count);
        tvWashingRoomCount = itemView.findViewById(R.id.tv_washing_room_count);
        tvPrice = itemView.findViewById(R.id.tv_price);
        iv_Category = itemView.findViewById(R.id.iv_category2);
        cardView=itemView.findViewById(R.id.card_view_details);

    }
}
