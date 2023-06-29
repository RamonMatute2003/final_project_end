package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.final_project.Settings.Urderlined;

public class Activity_sign_up extends AppCompatActivity {
    private Button btn_sign_up, btn_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView link_have_account=findViewById(R.id.link_have_account);
        Urderlined urderlined=new Urderlined();//urderline=subrayado
        btn_sign_up=(Button) findViewById(R.id.btn_sign_up3);
        btn_sign_in=(Button) findViewById(R.id.btn_sign_in3);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_window=new Intent(getApplicationContext(), Activity_sign_in.class);//new_window=nueva ventana
                startActivity(new_window);
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_window= new Intent(getApplicationContext(), Activity_main.class);//new_window=nueva ventana
                startActivity(new_window);
            }
        });

        urderlined.aesthetics_textView(link_have_account, "¿Ya tienes una cuenta?");
    }
}