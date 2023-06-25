package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_sign_in extends AppCompatActivity {

    EditText txt_email, txt_password;//email=correo, password=contraseña
    Button btn_sign_in, btn_login;//sign_in=registrarse, login=iniciar sesion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txt_email=(EditText) findViewById(R.id.txt_email);
        txt_password=(EditText) findViewById(R.id.txt_password);
        btn_login=(Button) findViewById(R.id.btn_login);
        //btn_sign_in=(Button) findViewById(R.id.btn_sign_in);

        if(txt_email.getText().toString().isEmpty() || txt_password.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "No dejar campos vacios", Toast.LENGTH_LONG).show();
        }else{

        }
    }
}