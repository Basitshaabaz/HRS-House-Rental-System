package com.example.hrshouserentalsystem.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hrshouserentalsystem.Adapters.CategoryAdapter;
import com.example.hrshouserentalsystem.Adapters.CategoryTwoAdapter;
import com.example.hrshouserentalsystem.Common.Common;
import com.example.hrshouserentalsystem.Constants.Constant;
import com.example.hrshouserentalsystem.Interface.OnItemClickListener;
import com.example.hrshouserentalsystem.Model.Category;
import com.example.hrshouserentalsystem.Model.Selling;
import com.example.hrshouserentalsystem.Model.User;
import com.example.hrshouserentalsystem.Resources.CustomProgressDialogue;
import com.example.hrshouserentalsystem.databinding.FragmentNewsFeedBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//************************************************************
public class NewsFeed extends Fragment implements OnItemClickListener
//************************************************************
{
    FragmentNewsFeedBinding mBinding;
    FirebaseAuth mAuth;
    FirebaseDatabase mUserData;
    DatabaseReference mUserReference;
    FirebaseFirestore dbFireSore;
    CategoryTwoAdapter adapter1;
    CustomProgressDialogue mCustomProgressDialogue;
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Selling> mSellingData = new ArrayList<>();
    public static Context CONTEXT;


    //************************************************************
    public NewsFeed()
    //************************************************************
    {
        // Required empty public constructor
    }

    @Override
    //************************************************************
    public void onCreate(Bundle savedInstanceState)
    //************************************************************
    {
        super.onCreate(savedInstanceState);
        CONTEXT = getContext();

    }

    @Override
    //************************************************************
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    //************************************************************
    {
        mCustomProgressDialogue=new CustomProgressDialogue(getContext(),"Loading...");
        mCustomProgressDialogue.show();
        mBinding = FragmentNewsFeedBinding.inflate(inflater, container, false);
        dbFireSore = FirebaseFirestore.getInstance();
        initImageBitmaps();
        showProfileData();
        return mBinding.getRoot();
    }


    //************************************************************
    private void initImageBitmaps()
    //************************************************************
    {
        if (categories.isEmpty()) {
            categories.add(new Category(0, Constant.IMG_URL.get(0), Constant.CATEGORY_NAME.get(0)));
            categories.add(new Category(1, Constant.IMG_URL.get(1), Constant.CATEGORY_NAME.get(1)));
            categories.add(new Category(2, Constant.IMG_URL.get(2), Constant.CATEGORY_NAME.get(2)));
            categories.add(new Category(3, Constant.IMG_URL.get(3), Constant.CATEGORY_NAME.get(3)));
            categories.add(new Category(4, Constant.IMG_URL.get(4), Constant.CATEGORY_NAME.get(4)));
        }
        initRecyclerView();
    }

    //************************************************************
    private void initRecyclerView()
    //************************************************************
    {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mBinding.rvCategory.setLayoutManager(layoutManager);
        CategoryAdapter adapter = new CategoryAdapter(getContext(), categories, this);
        mBinding.rvCategory.setAdapter(adapter);


        mBinding.rvCategory.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                int action = e.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    rv.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mBinding.rvCategory2.setLayoutManager(layoutManager);
        adapter1 = new CategoryTwoAdapter(getContext(), mSellingData, this);
        mBinding.rvCategory2.setAdapter(adapter1);
        eventChangeListener("House");
    }

    private void eventChangeListener(String type) {
        dbFireSore.collection("Selling").whereEqualTo("sellingType", type)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (mCustomProgressDialogue.isShowing())
                                mCustomProgressDialogue.dismiss();
                            Log.e("************************ Firestore ************************* ", "onEvent: " + error.getMessage());
                        }

                        if (value.isEmpty()) {
                            mSellingData.clear();
                            Toast.makeText(CONTEXT, "Empty", Toast.LENGTH_SHORT).show();
                            if (mCustomProgressDialogue.isShowing())
                                mCustomProgressDialogue.dismiss();
                            adapter1.notifyDataSetChanged();
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Selling s = dc.getDocument().toObject(Selling.class);
                                if (!s.getUser_id().equals(Common.currentUser))
                                    mSellingData.add(s);

                                else {
                                    mSellingData.clear();
                                }
                            }

                            adapter1.notifyDataSetChanged();
                            if (mCustomProgressDialogue.isShowing())
                                mCustomProgressDialogue.dismiss();
                        }
                        if (mSellingData.isEmpty()) {
                            Toast.makeText(CONTEXT, "Empty", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    //************************************************************
    public void onItemClick(int position)
    //************************************************************
    {

//        Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
        mCustomProgressDialogue=new CustomProgressDialogue(CONTEXT,"Loading...");
        mCustomProgressDialogue.show();
        switch (position) {
            case 0:
                eventChangeListener("House");
                mSellingData.clear();
                break;
            case 1:
                eventChangeListener("Room");
                mSellingData.clear();
                break;
            case 2:
                eventChangeListener("Shop");
                mSellingData.clear();
                break;
            case 3:
                eventChangeListener("Plot");
                mSellingData.clear();
                break;
            case 4:
                eventChangeListener("Hotel");
                mSellingData.clear();
                break;
            default:
                eventChangeListener("House");
                mSellingData.clear();
                break;
        }


    }


    //************************************************************
    private void showProfileData()
    //************************************************************
    {
        mAuth = FirebaseAuth.getInstance();
        mUserData = FirebaseDatabase.getInstance();
        mUserReference = mUserData.getReference("Users");


        mUserReference.orderByChild(Common.currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {

                    if (i.getKey().equals(Common.currentUser)) {
                        User user = i.getValue(User.class);
                        mBinding.tvUsername.setText(user.getUserName());
                        Glide.with(CONTEXT)
                                .load(user.getProfile_url()).into(mBinding.ivProfile);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}