package com.example.hrshouserentalsystem.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hrshouserentalsystem.Interface.OnItemClickListener;
import com.example.hrshouserentalsystem.Model.Selling;
import com.example.hrshouserentalsystem.R;
import com.example.hrshouserentalsystem.Buying;
import com.example.hrshouserentalsystem.ViewHolder.CategoryDetailViewHolder;

import org.parceler.Parcels;

import java.util.ArrayList;

//************************************************************
public class CategoryTwoAdapter extends RecyclerView.Adapter<CategoryDetailViewHolder>
//************************************************************
{

    private Context mContext;
    private ArrayList<Selling> mSellingList=new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    //************************************************************
    public CategoryTwoAdapter(Context mContext, ArrayList<Selling> mSellingList,OnItemClickListener onItemClickListener)
    //************************************************************
    {
        this.mContext = mContext;
        this.mSellingList=mSellingList;
        this.onItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    //************************************************************
    public CategoryDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    //************************************************************
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_two_layout,parent,false);
        return new CategoryDetailViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    //************************************************************
    public void onBindViewHolder(@NonNull CategoryDetailViewHolder holder, @SuppressLint("RecyclerView") int position)
    //************************************************************
    {
        Glide.with(mContext)
                .load(mSellingList.get(position).getImg_url())
                .into(holder.iv_Category);
        holder.tvMainTitle.setText(mSellingList.get(position).getMainTitle());
        holder.tvSubTitle.setText(mSellingList.get(position).getSubTitle());
        holder.tvReviews.setText(Double.toString(mSellingList.get(position).getRating()));
        holder.tvRoomCount.setText(Integer.toString(mSellingList.get(position).getRoomCount()));
        holder.tvBathroomCount.setText(Integer.toString(mSellingList.get(position).getBathroomCount()));
        holder.tvWashingRoomCount.setText(Integer.toString(mSellingList.get(position).getWashingCount()));
        holder.tvPrice.setText(Double.toString(mSellingList.get(position).getSelling_price()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Parcelable parcelable= Parcels.wrap(mSellingList.get(position));
                Intent intent=new Intent(mContext, Buying.class);
                intent.putExtra("Selling_Details",parcelable);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    //************************************************************
    public int getItemCount()
    //************************************************************
    {
        return mSellingList.size();
    }
}
