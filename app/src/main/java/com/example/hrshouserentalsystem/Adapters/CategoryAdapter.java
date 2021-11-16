package com.example.hrshouserentalsystem.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hrshouserentalsystem.Common.Common;
import com.example.hrshouserentalsystem.Interface.OnItemClickListener;
import com.example.hrshouserentalsystem.Model.Category;
import com.example.hrshouserentalsystem.R;
import com.example.hrshouserentalsystem.ViewHolder.CategoryViewHolder;

import java.util.ArrayList;

//************************************************************
public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder>
//************************************************************
{
    private final Context mContext;
    private ArrayList<Category> categories = new ArrayList<>();
    int pos = 0;
    private OnItemClickListener mOnItemClickListener;


    //************************************************************
    public CategoryAdapter(Context mContext, ArrayList<Category> categories, OnItemClickListener onItemClickListener)
    //************************************************************
    {
        this.mContext = mContext;
        this.categories = categories;
        this.mOnItemClickListener = onItemClickListener;
        Common.checkList = categories;
    }

    @NonNull
    @Override
    //************************************************************
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    //************************************************************
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);
        return new CategoryViewHolder(view, mOnItemClickListener);
    }


    @Override
    //************************************************************
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position)
    //************************************************************
    {

        Glide.with(mContext)
                .load(categories.get(position).getImg_url())
                .into(holder.iv_Category);
        holder.tv_Category.setText(categories.get(position).getCategory_Name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            //************************************************************
            public void onClick(View view)
            //************************************************************
            {
                mOnItemClickListener.onItemClick(position);
                pos = position;
                notifyDataSetChanged();
            }
        });

        if (pos == position) {
            holder.rl_cardView.setBackgroundColor(Color.parseColor("#1976D2"));
            holder.iv_Category.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.tv_Category.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));

        } else {
            holder.rl_cardView.setBackgroundColor(Color.parseColor("#EEF2FC"));
            holder.iv_Category.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.blue_700));
            holder.tv_Category.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.blue_700));
        }

    }

    @Override
    //************************************************************
    public int getItemCount()
    //************************************************************
    {
        return categories.size();
    }


}
