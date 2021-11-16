package com.example.hrshouserentalsystem;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.hrshouserentalsystem.Common.Common;
import com.example.hrshouserentalsystem.Resources.CustomProgressDialogue;
import com.example.hrshouserentalsystem.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


//************************************************************
public class Login extends AppCompatActivity implements View.OnClickListener
//************************************************************
{

    ActivityLoginBinding mBinding;
    FirebaseAuth mAuth;
    CustomProgressDialogue mCustomProgressDialogue;

    //************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState)
    //************************************************************
    {
        super.onCreate(savedInstanceState);
        mBinding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mAuth=FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser()!=null)
        {
            if (mAuth.getCurrentUser().isEmailVerified())
            {
            Common.currentUser=mAuth.getCurrentUser().getUid();
            startActivity(new Intent(Login.this,Home.class));
            finish();}
            else
            {
                Toast.makeText(Login.this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
            }

        }
            mBinding.tvRegisterNow.setOnClickListener(this);
            mBinding.btnLogIn.setOnClickListener(this);
            mBinding.tvForgotPassword.setOnClickListener(this);


    }

    //************************************************************
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view)
    //************************************************************
    {   switch (view.getId())
            {       case R.id.tv_register_now:
                    startActivity(new Intent(Login.this,SignUp.class));
                    break;

                    case R.id.btn_Log_in:
                        loginUser();
                    break;

                case R.id.tv_forgot_password:
                        startActivity(new Intent(Login.this,ForgotPassword.class));
                    break;
            } }


    //************************************************************
    private void loginUser()
    //************************************************************
    {   String email= Objects.requireNonNull(mBinding.etUserEmail.getText()).toString().trim();
        String password= Objects.requireNonNull(mBinding.etUserPassword.getText()).toString().trim();

        if (email.isEmpty())
        { mBinding.etUserEmail.setError("Email Required!");
            mBinding.etUserEmail.requestFocus();
            return;}

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        { mBinding.etUserEmail.setError("Please Provide Valid Email!");
            mBinding.etUserEmail.requestFocus();
            return;}

        if(password.isEmpty())
        { mBinding.etUserPassword.setError("Password is Required!");
            mBinding.etUserPassword.requestFocus();
            return; }

        if (password.length() <6)
        { mBinding.etUserPassword.setError("Minimum Password Length 6 Characters");
            mBinding.etUserPassword.requestFocus();
            return;}


        logInToFirebase(email,password);

    }

    //************************************************************
    private void logInToFirebase(String email,String password)
    //************************************************************
    {

        mCustomProgressDialogue=new CustomProgressDialogue(Login.this,"Logging In...");
        mCustomProgressDialogue.show();
        mAuth.signInWithEmailAndPassword(email,password)
        .addOnCompleteListener(task -> {
           if (task.isSuccessful())
           {
               if (mAuth.getCurrentUser()!=null && mAuth.getCurrentUser().isEmailVerified())
               {

                   Common.currentUser=mAuth.getCurrentUser().getUid();
                   if (mCustomProgressDialogue.isShowing())
                       mCustomProgressDialogue.dismiss();
                   Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this,Home.class));
                    finish(); }
               else
               {
                   if (mCustomProgressDialogue.isShowing())
                       mCustomProgressDialogue.dismiss();
                   Toast.makeText(Login.this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
               }
           }
           else
           {
               if (mCustomProgressDialogue.isShowing())
                   mCustomProgressDialogue.dismiss();
               Toast.makeText(Login.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
           }

        });}

}