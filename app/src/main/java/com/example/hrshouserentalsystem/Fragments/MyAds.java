package com.example.hrshouserentalsystem.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hrshouserentalsystem.Adapters.CategoryTwoAdapter;
import com.example.hrshouserentalsystem.Common.Common;
import com.example.hrshouserentalsystem.Interface.OnItemClickListener;
import com.example.hrshouserentalsystem.Model.Selling;
import com.example.hrshouserentalsystem.Resources.CustomProgressDialogue;
import com.example.hrshouserentalsystem.databinding.FragmentMyAdsBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


//************************************************************
public class MyAds extends Fragment implements OnItemClickListener
//************************************************************
{
   FragmentMyAdsBinding mBinding;
    private ArrayList<Selling> mSellingData = new ArrayList<>();
    private FirebaseFirestore dbFireSore;
    private CategoryTwoAdapter adapter;
    CustomProgressDialogue mCustomProgressDialogue;


    //************************************************************
    public MyAds()
//************************************************************
    {
        // Required empty public constructor
    }

    //************************************************************
    public static MyAds newInstance()
    //************************************************************
    {
        MyAds fragment = new MyAds();
        return fragment;
    }

    @Override
    //************************************************************
    public void onCreate(Bundle savedInstanceState)
    //************************************************************
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    //************************************************************
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    //************************************************************
    {
        mBinding=FragmentMyAdsBinding.inflate(inflater,container,false);
        dbFireSore = FirebaseFirestore.getInstance();
        populateRecyclerView();
        return mBinding.getRoot();
    }

    //************************************************************
    private void populateRecyclerView()
    //************************************************************
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mBinding.rvMyAds.setLayoutManager(layoutManager);
        adapter=new CategoryTwoAdapter(getContext(),mSellingData,this);
        mBinding.rvMyAds.setAdapter(adapter);
        getDataFromFireStore();
    }

    //************************************************************
    private void getDataFromFireStore()
    //************************************************************
    {
        mCustomProgressDialogue=new CustomProgressDialogue(getContext(),"Loading...");
        mCustomProgressDialogue.show();
        dbFireSore.collection("Selling")
                .whereEqualTo("user_id",Common.currentUser)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (mCustomProgressDialogue.isShowing())
                                mCustomProgressDialogue.dismiss();
                            Log.e("************************ Firestore ************************* ", "onEvent: " + error.getMessage());
                        }
                        if (value.isEmpty()) {
                            if (mCustomProgressDialogue.isShowing())
                                mCustomProgressDialogue.dismiss();
                            mSellingData.clear();
                            Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }

                        for (DocumentChange dc:value.getDocumentChanges())
                        {
                           if (dc.getType()== DocumentChange.Type.ADDED)
                           {Selling s = dc.getDocument().toObject(Selling.class);
                            mSellingData.add(s);}
                            if (mCustomProgressDialogue.isShowing())
                                mCustomProgressDialogue.dismiss();
                           adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    //************************************************************
    public void onItemClick(int position)
    //************************************************************
    {

    }
}