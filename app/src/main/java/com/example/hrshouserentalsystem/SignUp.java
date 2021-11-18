package com.example.hrshouserentalsystem;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hrshouserentalsystem.Model.User;
import com.example.hrshouserentalsystem.Resources.CustomProgressDialogue;
import com.example.hrshouserentalsystem.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Objects;


//************************************************************
public class SignUp extends AppCompatActivity implements View.OnClickListener
//************************************************************
{
    ActivitySignUpBinding mBinding;
    FirebaseAuth mAuth;
    StorageReference mStorageRef;
    ActivityResultLauncher<String> mGetContent;
    ActivityResultLauncher<Intent> mGetPermission;
    CustomProgressDialogue mCustomProgressDialogue;
    private Uri imageUri;

    //************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState)
    //************************************************************
    {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        mBinding.btnSignUp.setOnClickListener(this);
        mBinding.ivLogo.setOnClickListener(this);
        mBinding.ivProfile.setOnClickListener(this);
        mBinding.tvLogInBack.setOnClickListener(this);

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            imageUri = result;
            if (imageUri!=null) {
                Glide.with(this)
                        .load(result)
                        .into(mBinding.ivProfile);

            }
            else
                Glide.with(this).load(R.drawable.ic_profile).into(mBinding.ivProfile);
        });
        mGetPermission = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == SignUp.RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //************************************************************
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v)
    //************************************************************
    {
        switch (v.getId()) {

            case R.id.btn_sign_up:
                registerUser();
                break;

            case R.id.iv_logo:
            case R.id.tv_log_in_back:
                finish();
                break;

            case R.id.iv_profile:
                imageChoose(v);
                break;
        }
    }

    //************************************************************
    private void imageChoose(View v)
    //************************************************************
    {

        takePermissions(v);
        mGetContent.launch("image/*");

    }

    //************************************************************
    private void takePermission(View v)
    //************************************************************
    {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                mGetPermission.launch(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(this,
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
            int readExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return readExternalStoragePermission == PackageManager.PERMISSION_GRANTED;
        }
    }


    //************************************************************
    private void takePermissions(View v)
    //************************************************************
    {
        if (isPermissionGranted())
            Toast.makeText(getApplicationContext(), "Permission Already Granted", Toast.LENGTH_SHORT).show();

        else {
            takePermission(v);
        }
    }

    //************************************************************
    private void registerUser()
    //************************************************************
    {

        String email = Objects.requireNonNull(mBinding.etUserEmail.getText()).toString().trim();
        String userName = Objects.requireNonNull(mBinding.etUserName.getText()).toString().trim();
        String password = Objects.requireNonNull(mBinding.etUserPassword.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(mBinding.etUserConfirmPassword.getText()).toString().trim();

        if (userName.isEmpty()) {
            mBinding.etUserName.setError("UserName is Required!");
            mBinding.etUserName.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mBinding.etUserPassword.setError("Password is Required!");
            mBinding.etUserPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mBinding.etUserPassword.setError("Minimum Password Length 6 Characters");
            mBinding.etUserPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            mBinding.etUserConfirmPassword.setError("Password Does Not Match!");
            mBinding.etUserConfirmPassword.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            mBinding.etUserEmail.setError("Email Required!");
            mBinding.etUserEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mBinding.etUserEmail.setError("Please Provide Valid Email!");
            mBinding.etUserEmail.requestFocus();
            return;
        }

        uploadImage(email,password,userName);
    }

    //************************************************************
    private boolean writeToFireBase(String email, String password, String userName)
    //************************************************************
    {
        final Boolean[] check = new Boolean[1];
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification()
                                .addOnCompleteListener(task2 -> {

                                    if (task2.isSuccessful()) {
                                        check[0] =true;

                                    } else {
                                        check[0]=false;
                                        Toast.makeText(SignUp.this,
                                                Objects.requireNonNull(task2.getException()).getMessage()
                                                , Toast.LENGTH_SHORT).show();
                                    }

                                });
                    } else {
                        check[0]=false;
                        Toast.makeText(SignUp.this,
                                Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_SHORT)
                                .show();
                        if (mCustomProgressDialogue.isShowing())
                            mCustomProgressDialogue.dismiss();
                    }
                });
        return  check[0];
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //************************************************************
    private void uploadImage(String email, String password,String userName )
    //************************************************************
    {

        //=========================================
        mCustomProgressDialogue=new CustomProgressDialogue(SignUp.this,"Signing Up...");
        mCustomProgressDialogue.show();

        //================
        if (imageUri != null) {
            StorageReference ref = mStorageRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + "." + getExtension(imageUri));
            ref.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(uri -> {
                                    if (!writeToFireBase(email, password, userName))
                                        return;
                                    String imageUrl = uri.toString();
                                    User user = new User(userName, email, imageUrl);
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                            .setValue(user).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            if (mCustomProgressDialogue.isShowing())
                                                mCustomProgressDialogue.dismiss();
                                            Toast.makeText(SignUp.this,
                                                    "User registered Successfully\n Please Check Email for Verification Link",
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                            finish();

                                        } else {
                                            Toast.makeText(SignUp.this,
                                                    Objects.requireNonNull(task1.getException()).getMessage(),
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });
                                });
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        }
                            else
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_LONG).show();
                            if (mCustomProgressDialogue.isShowing())
                                mCustomProgressDialogue.dismiss();

    }

    //************************************************************
    private String getExtension(Uri uri)
    //************************************************************
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

}