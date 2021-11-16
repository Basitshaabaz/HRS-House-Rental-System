package com.example.hrshouserentalsystem.ViewHolder;


import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hrshouserentalsystem.Interface.OnItemClickListener;
import com.example.hrshouserentalsystem.R;
import com.google.android.material.imageview.ShapeableImageView;


public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       public CardView cardView;
       public ShapeableImageView iv_Category;
       public TextView tv_Category;
       public RelativeLayout rl_cardView;
       public OnItemClickListener mOnItemClickListener;


    public CategoryViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
        super(itemView);
                iv_Category= itemView.findViewById(R.id.iv_category);
                tv_Category= itemView.findViewById(R.id.tv_category);
                rl_cardView= itemView.findViewById(R.id.rl_car_view);
                cardView = itemView.findViewById(R.id.main);
                this.mOnItemClickListener=onItemClickListener;

    }

    @Override
    public void onClick(View view) {


    }
}
