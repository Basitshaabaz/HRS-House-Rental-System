package com.example.hrshouserentalsystem.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.hrshouserentalsystem.Common.Common;
import com.example.hrshouserentalsystem.Login;
import com.example.hrshouserentalsystem.Model.User;
import com.example.hrshouserentalsystem.Resources.CustomProgressDialogue;
import com.example.hrshouserentalsystem.databinding.FragmentUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

//************************************************************
public class UserProfile extends Fragment
//************************************************************
{
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FragmentUserProfileBinding mBinding;
    CustomProgressDialogue mCustomProgressDialogue;
    Boolean check = false;

    //************************************************************
    public UserProfile()
    //************************************************************
    {
        // Required empty public constructor
    }

    //************************************************************
    public static UserProfile newInstance()
    //************************************************************
    {

        return new UserProfile();
    }

    @Override
    //************************************************************
    public void onCreate(Bundle savedInstanceState)
    //************************************************************
    {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    //************************************************************
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    //************************************************************
    {

        mBinding = FragmentUserProfileBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Users");
        mReference.orderByChild(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {
                    if (Objects.requireNonNull(i.getKey()).equals(Common.currentUser)) {
                        User user = i.getValue(User.class);
                        Glide.with(getContext()).load(Objects.requireNonNull(user).getProfile_url()).into(mBinding.ivProfile);
                        mBinding.etUsername.setText(user.getUserName());
                        mBinding.etUserEmail.setText(user.getEmail());

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mBinding.btnEdit.setOnClickListener(v -> {

            if (!check) {

                mBinding.etUsername.setClickable(true);
                mBinding.etUserEmail.setClickable(true);
                mBinding.etUsername.setCursorVisible(true);
                mBinding.etUserEmail.setCursorVisible(true);
                mBinding.etUsername.setFocusable(true);
                mBinding.etUserEmail.setFocusable(true);
                mBinding.etUsername.setFocusableInTouchMode(true);
                mBinding.etUserEmail.setFocusableInTouchMode(true);
                mBinding.btnEdit.setText("Update");
                check = true;
            } else {
                mBinding.etUsername.setClickable(false);
                mBinding.etUserEmail.setClickable(false);
                mBinding.etUsername.setCursorVisible(false);
                mBinding.etUserEmail.setCursorVisible(false);
                mBinding.etUsername.setFocusable(false);
                mBinding.etUserEmail.setFocusable(false);
                mBinding.etUsername.setFocusableInTouchMode(false);
                mBinding.etUserEmail.setFocusableInTouchMode(false);
                mBinding.btnEdit.setText("Edit");
                check = false;
            }


        });
        mBinding.btnLogOut.setOnClickListener(view -> {
            mCustomProgressDialogue=new CustomProgressDialogue(getContext(),"Signing Out...");
            mCustomProgressDialogue.show();

            if (mAuth.getCurrentUser().getUid() != null) {
                mAuth.signOut();
                if (mCustomProgressDialogue.isShowing())
                    mCustomProgressDialogue.dismiss();
                startActivity(new Intent(getActivity(), Login.class));
                Toast.makeText(getContext(), "Signed Out", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        return mBinding.getRoot();
    }
}