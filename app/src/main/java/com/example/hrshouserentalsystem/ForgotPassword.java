package com.example.hrshouserentalsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.hrshouserentalsystem.Resources.CustomProgressDialogue;
import com.example.hrshouserentalsystem.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

//************************************************************
public class ForgotPassword extends AppCompatActivity implements View.OnClickListener
//************************************************************
{

    ActivityForgotPasswordBinding mBinding;
    FirebaseAuth mAuth;
    CustomProgressDialogue mCustomProgressDialogue;

    //************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState)
    //************************************************************
    {
        super.onCreate(savedInstanceState);
        mBinding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.btnSendEmail.setOnClickListener(this);
        mBinding.btnCancel.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();
    }

    //************************************************************
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view)
    //************************************************************
    {
        switch (view.getId()) {
            case R.id.btn_send_email:
                sendResetEmail(Objects.requireNonNull(mBinding.etUserEmail.getText()).toString());
                break;

            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    //************************************************************
    private void sendResetEmail(String email)
    //************************************************************
    {
            if (email.isEmpty())
                { mBinding.etUserEmail.setError("Email Required!");
                    mBinding.etUserEmail.requestFocus();
                    return;}

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                { mBinding.etUserEmail.setError("Please Provide Valid Email!");
                    mBinding.etUserEmail.requestFocus();
                    return;}
            sendResetEmailFromFirebase(email);
    }


    //************************************************************
    private void sendResetEmailFromFirebase(String email)
    //************************************************************
    {
        mCustomProgressDialogue=new CustomProgressDialogue(ForgotPassword.this,"Sending Reset Link...");
        mCustomProgressDialogue.show();
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    {       Toast.makeText(ForgotPassword.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                        if (mCustomProgressDialogue.isShowing())
                            mCustomProgressDialogue.dismiss();
                    finish();   }
                else
                    {
                        Toast.makeText(ForgotPassword.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        if (mCustomProgressDialogue.isShowing())
                            mCustomProgressDialogue.dismiss();
                    }

            }); }


}