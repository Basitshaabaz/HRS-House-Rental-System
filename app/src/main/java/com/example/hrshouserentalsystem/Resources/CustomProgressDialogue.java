package com.example.hrshouserentalsystem.Resources;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.hrshouserentalsystem.R;


public class CustomProgressDialogue extends Dialog {
    public CustomProgressDialogue(Context context,String Title) {
        super(context);

        WindowManager.LayoutParams wlmp = getWindow().getAttributes();

        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(
                R.layout.custom_progress, null);
        TextView textView=view.findViewById(R.id.progress_title);
        textView.setText(Title);
        setContentView(view);
    }
}