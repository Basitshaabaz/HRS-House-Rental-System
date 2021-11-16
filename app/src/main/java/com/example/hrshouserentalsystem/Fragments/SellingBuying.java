package com.example.hrshouserentalsystem.Fragments;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hrshouserentalsystem.Common.Common;
import com.example.hrshouserentalsystem.Home;
import com.example.hrshouserentalsystem.Model.Selling;
import com.example.hrshouserentalsystem.Model.User;
import com.example.hrshouserentalsystem.R;
import com.example.hrshouserentalsystem.Resources.CustomProgressDialogue;
import com.example.hrshouserentalsystem.SignUp;
import com.example.hrshouserentalsystem.databinding.ActivityHomeBinding;
import com.example.hrshouserentalsystem.databinding.FragmentSellingBuyingBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;


//************************************************************
public class SellingBuying extends Fragment
//************************************************************
{
    FragmentSellingBuyingBinding mBinding;
    FirebaseAuth mAuth;
    StorageReference mStorageRef, mProfileStorage;
    FirebaseDatabase mUserData;
    FirebaseFirestore mFireStore;
    DatabaseReference mUserReference, mSellingReference;
    ActivityResultLauncher<String> mGetContent;
    ActivityResultLauncher<Intent> mGetPermission;
    CustomProgressDialogue mCustomProgressDialogue;
    String mSpinner, image_url = null;
    Uri img_uri;
    public static Context CONTEXT;

    //************************************************************
    public SellingBuying()
    //************************************************************
    {
        // Required empty public constructor
    }

    //************************************************************
    public static SellingBuying newInstance()
    //************************************************************
    {
        return new SellingBuying();
    }

    @Override
    //************************************************************
    public void onCreate(Bundle savedInstanceState)
    //************************************************************
    {
        super.onCreate(savedInstanceState);
        CONTEXT = getContext();
        registerForActivityResult();


    }


    @Override
    //************************************************************
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    //************************************************************
    {
        mBinding = FragmentSellingBuyingBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();
        mUserData = FirebaseDatabase.getInstance();
        mUserReference = mUserData.getReference("Users");
        mSellingReference = mUserData.getReference("Selling");
        mStorageRef = FirebaseStorage.getInstance().getReference("Selling");

        mBinding.btnSell.setOnClickListener(v -> validateEditText());
        initializeSpinner();
        showProfileData();
        mBinding.ivImage.setOnClickListener(v -> imageChoose(v));
        mBinding.ivBack.setOnClickListener(v -> goBack());
        return mBinding.getRoot();
    }

    //************************************************************
    private void goBack()
    //************************************************************
    {
        startActivity(new Intent(getActivity(),Home.class));
        getActivity().finish();
    }

    //************************************************************
    private void initializeSpinner()
    //************************************************************
    {
        String[] items = new String[]{"House", "Room", "Shop", "Plot", "Hotel" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.typeSpinner.setAdapter(adapter);
        mBinding.typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSpinner = mBinding.typeSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mSpinner = "House";
            }
        });

    }

    //************************************************************
    private void showProfileData()
    //************************************************************
    {
        if (!isAdded())
            return;
        else if (getActivity() != null) {
            mProfileStorage = FirebaseStorage.getInstance().getReference();
            mProfileStorage.child("Images/" + mAuth.getCurrentUser().getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(CONTEXT)
                            .load(uri).into(mBinding.ivProfile);
                }
            });

            mUserReference.orderByChild(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot i : snapshot.getChildren()) {

                        if (i.getKey().equals(Common.currentUser)) {
                            User user = i.getValue(User.class);
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


    //************************************************************
    private void validateEditText()
    //************************************************************
    {
        if (TextUtils.isEmpty(mBinding.etMainTitle.getText().toString()) ||
                TextUtils.isEmpty(mBinding.etSubTitle.getText().toString()) ||
                TextUtils.isEmpty(mBinding.etRoomCount.getText().toString()) ||
                TextUtils.isEmpty(mBinding.etBathroomCount.getText().toString()) ||
                TextUtils.isEmpty(mBinding.etWashingCount.getText().toString()) ||
                TextUtils.isEmpty(mBinding.etRating.getText().toString()) ||
                TextUtils.isEmpty(mBinding.etPrice.getText().toString()) ||
                TextUtils.isEmpty(mBinding.etDescription1.getText().toString())) {
            if (TextUtils.isEmpty(mBinding.etMainTitle.getText().toString())) {
                mBinding.etMainTitle.setError("Required");
                mBinding.etMainTitle.requestFocus();
                return;

            }
            if (TextUtils.isEmpty(mBinding.etSubTitle.getText().toString())) {
                mBinding.etSubTitle.setError("Required");
                mBinding.etSubTitle.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(mBinding.etRoomCount.getText().toString())) {
                mBinding.etRoomCount.setError("Required");
                mBinding.etRoomCount.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(mBinding.etBathroomCount.getText().toString())) {
                mBinding.etBathroomCount.setError("Required");
                mBinding.etBathroomCount.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(mBinding.etWashingCount.getText().toString())) {
                mBinding.etWashingCount.setError("Required");
                mBinding.etWashingCount.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(mBinding.etPrice.getText().toString())) {
                mBinding.etPrice.setError("Required");
                mBinding.etPrice.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(mBinding.etDescription1.getText().toString())) {
                mBinding.etDescription1.setError("Required");
                mBinding.etDescription1.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(mBinding.etRating.getText().toString())) {
                mBinding.etRating.setError("Required");
                mBinding.etRating.requestFocus();
                return;
            }
        }

        uploadImage();
        uploadSellingData();

    }


    //************************************************************
    private void uploadSellingData()
    //************************************************************
    {


    }


    //************************************************************
    private void uploadImage()
    //************************************************************
    {

        mCustomProgressDialogue=new CustomProgressDialogue(CONTEXT,"Uploading...");
        mCustomProgressDialogue.show();
        StorageReference ref = mStorageRef.child(System.currentTimeMillis() + ".jpg");
        if (img_uri != null) {
            UploadTask uploadTask = ref.putFile(img_uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    Selling selling = new Selling(
                                            Objects.requireNonNull(mAuth.getCurrentUser()).getUid(),
                                            imageUrl,
                                            mBinding.etMainTitle.getText().toString(),
                                            mBinding.etSubTitle.getText().toString(),
                                            mBinding.etDescription1.getText().toString(),
                                            mSpinner,
                                            Integer.parseInt(mBinding.etRoomCount.getText().toString()),
                                            Integer.parseInt(mBinding.etBathroomCount.getText().toString()),
                                            Integer.parseInt(mBinding.etWashingCount.getText().toString()),
                                            Double.parseDouble(mBinding.etPrice.getText().toString()),
                                            Double.parseDouble(mBinding.etRating.getText().toString())
                                    );


                                    CollectionReference dbSelling = mFireStore.collection("Selling");
                                    dbSelling.add(selling);
                                    if (mCustomProgressDialogue.isShowing())
                                        mCustomProgressDialogue.dismiss();
                                    Toast.makeText(getContext(), "Data Uploaded", Toast.LENGTH_SHORT).show();
                                    startNewActivity();
                                }
                            });
                        }
                    }
                }
            });
        }
        else {
            if (mCustomProgressDialogue.isShowing())
                mCustomProgressDialogue.dismiss();
            Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
        }

    }

    //************************************************************
    private void startNewActivity()
    //************************************************************
    {
        startActivity(new Intent(getActivity(), Home.class));
        getActivity().finish();
    }

    //************************************************************
    private void saveImageURL(String imageUrl)
    //************************************************************
    {
        this.image_url = imageUrl;
    }


    //************************************************************
    private void imageChoose(View v)
    //************************************************************
    {


        takePermissions(v);
        mGetContent.launch("image/*");


    }


    //************************************************************
    private void takePermissions(View v)
    //************************************************************
    {
        if (isPermissionGranted())
            Toast.makeText(getContext(), "Permission Already Granted", Toast.LENGTH_SHORT).show();

        else {
            takePermission(v);
        }
    }

    //************************************************************
    private void takePermission(View v)
    //************************************************************
    {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getContext().getPackageName())));
                mGetPermission.launch(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , 101);
        }
    }

    //************************************************************
    private boolean isPermissionGranted()
    //************************************************************
    {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R)
            return Environment.isExternalStorageManager();

        else {
            int readExternalStoragePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return readExternalStoragePermission == PackageManager.PERMISSION_GRANTED;
        }
    }

    //************************************************************
    private void registerForActivityResult()
    //************************************************************
    {
        if (getActivity() != null) {
            mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
                mBinding.ivImage.setImageURI(null);
                Glide.with(CONTEXT)
                        .load(result)
                        .into(mBinding.ivImage);
                img_uri = result;
            });
            mGetPermission = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == SignUp.RESULT_OK) {
                    Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            img_uri=null;
    }

}