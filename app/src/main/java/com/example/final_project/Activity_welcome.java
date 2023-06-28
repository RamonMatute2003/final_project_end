package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity_welcome extends AppCompatActivity {

    private Button btn_sign_in, btn_sign_up;//btn_sign_in=iniciar sesion, btn_sign_up=registrarse

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_sign_in=(Button) findViewById(R.id.btn_sign_in1);
        btn_sign_up=(Button) findViewById(R.id.btn_sign_up1);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_window= new Intent(getApplicationContext(), Activity_sign_in.class);//new_window=nueva ventana
                startActivity(new_window);
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_window= new Intent(getApplicationContext(), Activity_sign_up.class);//new_window=nueva ventana
                startActivity(new_window);
            }
        });
    }
}