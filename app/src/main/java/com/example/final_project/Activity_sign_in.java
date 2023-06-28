package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class Activity_sign_in extends AppCompatActivity {

    private EditText txt_email, txt_password;//email=correo, password=contraseña
    private Button btn_sign_in, btn_sign_up;//btn_sign_up=registrarse, btn_sign_in=iniciar sesion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txt_email=(EditText) findViewById(R.id.txt_account_number);
        txt_password=(EditText) findViewById(R.id.txt_password);
        btn_sign_in=(Button) findViewById(R.id.btn_sign_in2);
        btn_sign_up=(Button) findViewById(R.id.btn_sign_up2);
        TextView text_view1 = (TextView) findViewById(R.id.link_recover_password);
        TextView text_view2 = (TextView) findViewById(R.id.link_create_account);

        aesthetics_textView(text_view1, "¿Se te olvido la contraseña?");
        aesthetics_textView(text_view2, "¿No tienes una cuenta?");

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_window=new Intent(getApplicationContext(), Activity_main.class);//new_window=nueva ventana
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

    public void aesthetics_textView(TextView text_view, String text){//aesthetics_textView=estetica de textView
        SpannableString mitextoU = new SpannableString(text);
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        text_view.setText(mitextoU);
    }
}