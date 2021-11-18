package com.example.hrshouserentalsystem.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.hrshouserentalsystem.Common.Common;
import com.example.hrshouserentalsystem.Login;
import com.example.hrshouserentalsystem.Model.User;
import com.example.hrshouserentalsystem.R;
import com.example.hrshouserentalsystem.Resources.CustomProgressDialogue;
import com.example.hrshouserentalsystem.databinding.FragmentUserProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    ViewGroup mContainer;
    LayoutInflater mInflater;
    User mUser;
    String userEmail,userName;

    //************************************************************
    public UserProfile()
    //************************************************************
    {
        userName=userEmail=null;

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
        this.mContainer = container;
        this.mInflater = inflater;
        mBinding = FragmentUserProfileBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Users");
        if (mAuth != null)
        {
            mReference.orderByChild(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot i : snapshot.getChildren()) {
                        if (Objects.requireNonNull(i.getKey()).equals(Common.currentUser)) {
                            mUser = i.getValue(User.class);
                            Glide.with(getContext()).load(Objects.requireNonNull(mUser).getProfile_url()).into(mBinding.ivProfile);
                            mBinding.etUsername.setText(mUser.getUserName());
                            mBinding.etUserEmail.setText(mUser.getEmail());
                            userEmail = mUser.getEmail();
                            userName = mUser.getUserName();

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
        else
        {
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }
        mBinding.btnEdit.setOnClickListener(v -> {

            if (mAuth != null)
            {
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

                    if (!userEmail.equals(mBinding.etUserEmail.getText().toString()) ||
                            !userName.equals(mBinding.etUsername.getText().toString())
                    )
                    {
                        validateUser();
                        return;
                    }
                    else
                        {
                        Toast.makeText(getContext(), "No Data Changed", Toast.LENGTH_SHORT).show();
                    }

                    changeToEdit();
                }
        }

            else
                Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();

        });
        mBinding.btnLogOut.setOnClickListener(view -> {
            showLogOutDialog();

        });

        return mBinding.getRoot();
    }


    //************************************************************
    private void showLogOutDialog()
    //************************************************************
    {
        AlertDialog alertDialog=new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Do You want to Log out?");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //=======================================

                mCustomProgressDialogue=new CustomProgressDialogue(getContext(),"Signing Out...");
                mCustomProgressDialogue.show();

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    logOut();

                    }
                }, 1000);

                //=======================
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void logOut() {
        if (mAuth.getCurrentUser()!=null)
        {
            mAuth.signOut();
            if (mCustomProgressDialogue.isShowing())
                mCustomProgressDialogue.dismiss();
            startActivity(new Intent(getActivity(), Login.class));
            Toast.makeText(getContext(), "Signed Out", Toast.LENGTH_SHORT).show();
            getActivity().finish();

        }
        else
        {
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
            if (mCustomProgressDialogue.isShowing())
                mCustomProgressDialogue.dismiss();
        }


    }


    //************************************************************
    private void changeToEdit()
    //************************************************************
    {
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

    //************************************************************
    private void showDialog(String e,String u)
    //************************************************************
    {
        final View view=LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_password,mContainer,false);
        AlertDialog alertDialog=new AlertDialog.Builder(view.getContext()).create();
        alertDialog.setTitle("Enter Password Again");
        alertDialog.setCancelable(false);
        final EditText etPassword =(EditText)view.findViewById(R.id.et_user_password);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                        if (!etPassword.getText().toString().isEmpty())
                        {
                            updateFirebaseData(e,etPassword.getText().toString(),u);
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                        }

            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();


    }

    //************************************************************
    private void updateFirebaseData(String new_user_email,String user_password,String new_user_name)
    //************************************************************
    {

        mAuth.signInWithEmailAndPassword(userEmail,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful())
               {
                   FirebaseUser user=mAuth.getCurrentUser();
                   AuthCredential credential = EmailAuthProvider
                           .getCredential(userEmail, user_password);
                   if (user != null) {
                       if (credential != null) {
                           if (!new_user_email.equals(userEmail) && !new_user_name.equals(userName)) {
                               updateData(new_user_email, new_user_name, user, credential);
                               return;
                           }
                           if (!new_user_email.equals(userEmail)) {
                               updateData(new_user_email, mUser.getUserName(), user, credential);
                               return;
                           }
                           if (!new_user_name.equals(userName)) {
                               mUser = new User(new_user_name, mUser.getEmail(), mUser.getProfile_url());
                               FirebaseDatabase.getInstance().getReference("Users")
                                       .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                       .setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       Toast.makeText(getContext(), "User Name Changed", Toast.LENGTH_SHORT).show();
                                        changeToEdit();
                                   }
                               });
                           }
                           return;

                       }
                       else
                           Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();

                       return;

                   }
               }
               else
                   Toast.makeText(getContext(),"Incorrect Password",Toast.LENGTH_SHORT).show();
            }
        });
        // Get auth credentials from the user for re-authentication

    }

    //************************************************************
    private void updateData(String new_user_email, String new_user_name,FirebaseUser user, AuthCredential credential)
    //************************************************************
    {
         // Current Login Credentials \\
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //Now change your email address \\
                        //----------------Code for Changing Email Address----------\\
                        FirebaseUser user = mAuth.getCurrentUser();
                        user.updateEmail(new_user_email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mUser=new User(new_user_name,new_user_email,mUser.getProfile_url());
                                            FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                                    .setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    mAuth.getCurrentUser().sendEmailVerification();
                                                    mAuth.signOut();
                                                    Toast.makeText(getContext(), "Email and UserName Changed\n check Email for Verfication", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getActivity(),Login.class));
                                                    getActivity().finish();
                                                }
                                            });

                                        }
                                    }
                                });
                        //----------------------------------------------------------\\
                    }
                });
    }


    //************************************************************
    private void validateUser()
    //************************************************************
    {

        String email = Objects.requireNonNull(mBinding.etUserEmail.getText()).toString().trim();
        String userName = Objects.requireNonNull(mBinding.etUsername.getText()).toString().trim();

        if (userName.isEmpty()) {
            mBinding.etUsername.setError("UserName is Required!");
            mBinding.etUsername.requestFocus();
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
        showDialog(email,userName);
    }

}