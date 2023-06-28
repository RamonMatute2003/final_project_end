package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Activity_verification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        TextView text_view=findViewById(R.id.link_resend_code);
        Activity_sign_in activity_sign_in=new Activity_sign_in();
        activity_sign_in.aesthetics_textView(text_view, "Volver a enviar codigo");
    }
}