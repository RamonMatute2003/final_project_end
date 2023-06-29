package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.final_project.Settings.Urderlined;

public class Activity_verification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        TextView text_view=findViewById(R.id.link_resend_code);
        Urderlined urderlined=new Urderlined();//urderline=subrayado
        urderlined.aesthetics_textView(text_view, "Volver a enviar codigo");
    }
}