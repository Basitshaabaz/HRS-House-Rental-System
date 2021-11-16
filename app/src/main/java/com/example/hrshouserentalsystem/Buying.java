package com.example.hrshouserentalsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import com.bumptech.glide.Glide;
import com.example.hrshouserentalsystem.Model.Selling;
import com.example.hrshouserentalsystem.Model.User;
import com.example.hrshouserentalsystem.databinding.ActivityBuyingBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.parceler.Parcels;

import java.util.Objects;


public class Buying extends AppCompatActivity {
    ActivityBuyingBinding mBinding;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivityBuyingBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        Parcelable parcelable=getIntent().getParcelableExtra("Selling_Details");
        Selling selling= Parcels.unwrap(parcelable);
        Glide.with(this).load(selling.getImg_url()).into(mBinding.ivImage);
        mBinding.tvMainTitle.setText(selling.getMainTitle());
        mBinding.tvSubTitle.setText(selling.getSubTitle());
        mBinding.tvRoomCount.setText(Integer.toString(selling.getRoomCount()));
        mBinding.tvBathroomCount.setText(Integer.toString(selling.getBathroomCount()));
        mBinding.tvRating.setText(Double.toString(selling.getRating()));
        mBinding.tvType.setText(selling.getSellingType());
        mBinding.tvPrice.setText(Double.toString(selling.getSelling_price()));
        mBinding.tvDescription1.setText(selling.getSellingDescription());
        mBinding.ivBack.setOnClickListener(v->onBackPressed());

        mDatabase= FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference("Users");
        mReference.orderByChild(selling.getUser_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i:snapshot.getChildren())
                {
                    if (Objects.requireNonNull(i.getKey()).equals(selling.getUser_id()))
                    {
                        User user=i.getValue(User.class);
                        Glide.with(Buying.this).load(Objects.requireNonNull(user).getProfile_url()).into(mBinding.ivProfile);
                        mBinding.tvUsername.setText(user.getUserName());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}